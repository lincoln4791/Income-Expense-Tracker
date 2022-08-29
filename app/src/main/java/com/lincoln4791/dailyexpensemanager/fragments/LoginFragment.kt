package com.lincoln4791.dailyexpensemanager.fragments

import android.annotation.SuppressLint
import com.lincoln4791.dailyexpensemanager.R
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.mybaseproject2.base.BaseFragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.databinding.FragmentLoginBinding
import com.lincoln4791.dailyexpensemanager.view.MainActivity
import com.lincoln4791.dailyexpensemanager.viewModels.VMLogin
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val REQ_ONE_TAP = 1
    @Inject lateinit var prefManager : PrefManager
    @Inject lateinit var auth: FirebaseAuth
    @Suppress("unused")
    val vmLogin by viewModels<VMLogin>()

    private lateinit var navCon : NavController
    private lateinit var dialogLoading : Dialog

    //One Tap
    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> Login")
                    //navCon.navigateUp()
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        initOneTapClient()


        // Set the dimensions of the sign-in button.
        // Set the dimensions of the sign-in button.
        binding.signInButton.setSize(SignInButton.SIZE_WIDE)

        binding.login2.setOnClickListener {
            validate()
        }

        binding.signInButton.setOnClickListener {
            signInWithGoogle()
        }

        binding.tvRegistration.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            navCon.navigate(action)

        }

        binding.ivToolbarBack.setOnClickListener {
            goBack()
        }
    }

    @SuppressLint("InflateParams")
    private fun init(view:View) {
        navCon = Navigation.findNavController(view)
        dialogLoading = Dialog(requireContext())
        @Suppress("unused") val loadingView = layoutInflater.inflate(R.layout.dialog_content_loading,null,false)
        dialogLoading.setCancelable(false)
        dialogLoading.setContentView(loadingView)
    }

    private fun initOneTapClient() {
        oneTapClient = Identity.getSignInClient(requireActivity())
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
    }


    private fun validate(){
        binding.phone.error = null
        when {
            binding.phone.text.isNullOrEmpty() -> binding.phone.error = "Please enter phone Number"
            binding.phone.text!!.length != 11 -> binding.phone.error = "Phone number should be 11 digit"
            else -> {
                Log.d("Registration","Ready To Register")
                navigateToOtpActivity()
            }
        }
    }

    private fun navigateToOtpActivity() {
            val action = LoginFragmentDirections.actionLoginFragmentToOtpValidationFragment(
                "",
                binding.phone.text.toString())
            navCon.navigate(action)

    }


    @Suppress("unused", "DEPRECATION")
    private fun login(email : String, password : String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task : Task<AuthResult> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    Log.d("Login", "signInWithEmail:success")
                    Toast.makeText(requireContext(), "Authentication Success.",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    //Log.d("Login", "signIn :success -> ${task.result.credential.}")
                    Log.d("Login", "signIn :success -> $user")
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }


    //Google Sign in

    private fun signInWithGoogle() {
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener {result ->
                @Suppress("DEPRECATION")
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0,null)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("Login", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener {e->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                e.localizedMessage?.let { Log.d("Login", it) }
            }
    }


    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            dialogLoading.show()
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener {task->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Login", "signInWithCredential:success")
                                        val user = auth.currentUser

                                        Firebase.database.reference.child(Constants.USER_DATA).child(
                                            task.result!!.user!!.uid).addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                if(snapshot.exists()){
                                                    //User Already Exists
                                                    val profile = HashMap<String,Any>()
                                                    profile[Constants.NAME] = if(user!!.displayName.isNullOrEmpty()){""} else user.displayName!!
                                                    profile[Constants.PHONE] = if(user.phoneNumber.isNullOrEmpty()){""} else user.phoneNumber!!
                                                    profile[Constants.EMAIL] = if(user.email.isNullOrEmpty()){""} else user.email!!
                                                    profile[Constants.PROFILE_PIC_URI] = ""
                                                    if(user.photoUrl!=null){
                                                        if(user.photoUrl.toString().isEmpty()){
                                                            profile[Constants.PROFILE_PIC_URI] = user.photoUrl.toString()
                                                        }
                                                    }
                                                    Firebase.database.reference.child(Constants.USER_DATA).child(
                                                        task.result!!.user!!.uid).child(
                                                        Constants.PROFILE).updateChildren(profile).addOnCompleteListener {
                                                        updateUI(user)
                                                    }
                                                }
                                                else{
                                                    //New User, Never Registered Before
                                                    val profile = HashMap<String,String>()
                                                    profile[Constants.NAME] = if(user!!.displayName.isNullOrEmpty()){""} else user.displayName!!
                                                    profile[Constants.PHONE] = if(user.phoneNumber.isNullOrEmpty()){""} else user.phoneNumber!!
                                                    profile[Constants.EMAIL] = if(user.email.isNullOrEmpty()){""} else user.email!!
                                                    profile[Constants.PROFILE_PIC_URI] = ""
                                                    if(user.photoUrl!=null){
                                                        if(user.photoUrl.toString().isEmpty()){
                                                            profile[Constants.PROFILE_PIC_URI] = user.photoUrl.toString()
                                                        }
                                                    }
                                                    Firebase.database.reference.child(Constants.USER_DATA).child(
                                                        task.result!!.user!!.uid).child(
                                                        Constants.PROFILE).setValue(profile).addOnCompleteListener {
                                                        updateUI(user)
                                                    }

                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                updateUI(user)
                                                Log.e("Login","Database Error in check existing user -> ${error.message}:: code ->${error.code}")
                                                Toast.makeText(requireContext(),"Something went wrong, please try again later",Toast.LENGTH_LONG).show()
                                            }
                                        })

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Login", "signInWithCredential:failure", task.exception)
                                        updateUI(null)
                                    }
                                }
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d("Login", "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
    }



    private fun updateUI(account:FirebaseUser?){
        if(account!=null){
            prefManager.email = account.email.toString()
            prefManager.isLoggedIn = true
            prefManager.name = account.displayName.toString()
            prefManager.UID = account.uid

            Log.d("Login","name->${account.displayName}::uid -> ${account.uid}")

        }
        else{
            Toast.makeText(requireContext(),"Something is wrong",Toast.LENGTH_SHORT).show()
        }
        dialogLoading.dismiss()
        Toast.makeText(requireContext(),"Login Success",Toast.LENGTH_LONG).show()
        navigateToMainActivity()
    }



    private fun goBack(){
        startActivity(Intent(requireActivity(),MainActivity::class.java))
    }

    private  fun navigateToMainActivity(){
        startActivity(Intent(requireActivity(),MainActivity::class.java))
    }




}