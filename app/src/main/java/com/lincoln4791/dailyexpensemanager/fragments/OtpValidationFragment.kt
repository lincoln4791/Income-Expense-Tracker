package com.lincoln4791.dailyexpensemanager.fragments

import com.lincoln4791.dailyexpensemanager.R
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.common.Constants
import com.lincoln4791.dailyexpensemanager.common.PrefManager
import com.lincoln4791.dailyexpensemanager.databinding.FragmentOtpBinding
import java.util.concurrent.TimeUnit

class OtpValidationFragment : Fragment() {

    val TAG = "Otp"

    val args: OtpValidationFragmentArgs by navArgs()
    private lateinit var navCon : NavController
    private lateinit var binding: FragmentOtpBinding
    private lateinit var database: DatabaseReference
    private var mAuth = FirebaseAuth.getInstance()
    private lateinit var prefManager : PrefManager
    private lateinit var dialogLoading : Dialog

    private var name = ""
    private var phone = ""
    private var password = ""
    private var cPassword = ""
    private var isCodeSent  = false
    private var verificationCode  = ""
    private var providedCode  = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> Login")
                    //navCon.navigateUp()
                confirmGoingBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun goBack() {
        val action = OtpValidationFragmentDirections.actionOtpValidationFragment2ToHomeFragment()
        navCon.navigate(action)
        this.onDestroy()
        this.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        navCon = Navigation.findNavController(view)
        database = Firebase.database.reference


        name = args.name
        phone = args.phone


        binding.ivToolbarBack.setOnClickListener {
           confirmGoingBack()
        }


        binding.btnVerifyOtpCode.setOnClickListener {
            providedCode = binding.pinView.text.toString()
            if(verificationCode != null){
                if(!providedCode.isNullOrEmpty()){
                    binding.btnVerifyOtpCode.visibility = View.INVISIBLE
                    val credential = PhoneAuthProvider.getCredential(verificationCode!!,
                        providedCode)
                    dialogLoading.show()
                    signInWithCredential(credential)
                }
                else{
                    Log.d("Otp","Provided Code is null")
                    binding.pinView.error="Please Enter Otp Code"
                }
            }
            else{
                Log.d("Otp","Verification Code is null")
                binding.tvOptStatusOtpActivity.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
                Toast.makeText(requireContext(),"Verification Code Has Not Been Sent Yet", Toast.LENGTH_LONG).show()
            }
        }

        sendOptRequest()
        setOtpTimeOut()

        Log.d("OtpActivity","$name::$phone :: $password :: $cPassword")

    }

    private fun sendOptRequest() {
        Log.d("Otp","phone number is ${phone}")
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber("+88"+phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationCode = p0
                    binding.btnVerifyOtpCode.isClickable=true
                    isCodeSent = true
                    binding.tvOptStatusOtpActivity.text = "Please Check Your Mobile"
                    binding.tvOptStatusOtpActivity.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                    binding.tvCodeSentTo.text = "code has been sent to $phone"
                    binding.tvCodeSentTo.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                    Log.d("Otp", "Code  Sent")
                }



                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d(TAG, "On Verification Completed Method")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d(TAG, "Wrong  ${p0.message}")
                }
            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun setOtpTimeOut() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            if(!isCodeSent){
                val errorDialogue = Dialog(requireContext())
                val view : View = LayoutInflater.from(requireContext()).inflate(R.layout.dialogue_something_went_wrong,null,false)
                errorDialogue.setContentView(view)
                errorDialogue.setCancelable(false)

                try {
                    errorDialogue.show()
                }
                catch (e:Exception){
                    e.printStackTrace()
                }

                view.findViewById<Button>(R.id.btn_ok_dialogue_somethingWentWrong).setOnClickListener {
                    errorDialogue.dismiss()
                    //errorDialogue.cancel()
                    val action = OtpValidationFragmentDirections.actionOtpValidationFragment2ToHomeFragment()
                    navCon.navigate(action)
                    onDestroy()
                    onDetach()
                }
            }
        },75000)

    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    prefManager.UID = task.result.user?.uid.toString()
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.d(TAG,"UID is -> ${task.result.user!!.uid} :: name -> $name :: phone: $phone")

                    if(name.isNotEmpty()){

                        //User Comes From Registration Page
                        Log.d(TAG,"Comes From Registration Page")

                            Firebase.database.reference.child(Constants.USER_DATA).child(
                                task.result.user!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(snapshot.exists()){
                                        //User Registered Before
                                        Log.d(TAG,"Comes From Old User")
                                        prefManager.name = name
                                        val request = UserProfileChangeRequest.Builder().setDisplayName(name).build()

                                        user?.updateProfile(request)?.addOnCompleteListener(OnCompleteListener {
                                            val profile = HashMap<String,Any>()
                                            profile[Constants.NAME] = name
                                            database.child(Constants.USER_DATA).child(task.result.user!!.uid).child(Constants.PROFILE).updateChildren(profile).addOnCompleteListener {
                                                navigateToHomePage()
                                            }
                                        })

                                    }
                                    else{
                                        //New User, Never Registered Before
                                        Log.d(TAG,"New Registration")
                                        prefManager.name = name
                                        val request = UserProfileChangeRequest.Builder().setDisplayName(name).build()

                                        user?.updateProfile(request)?.addOnCompleteListener {
                                            val profile = HashMap<String, String>()
                                            profile[Constants.NAME] = name
                                            profile[Constants.PHONE] = phone
                                            profile[Constants.EMAIL] = ""
                                            profile[Constants.PROFILE_PIC_URI] = ""

                                            database.child(Constants.USER_DATA)
                                                .child(task.result.user!!.uid)
                                                .child(Constants.PROFILE).setValue(profile)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        Log.d(TAG, "User data saved ")
                                                        navigateToHomePage()
                                                    } else {
                                                        Log.d(TAG, "User data not saved ")
                                                        navigateToHomePage()
                                                    }

                                                }
                                        }

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    navigateToHomePage()
                                    Log.e(TAG,"Database Error in checki gexisting user -> ${error.message}:: code ->${error.code}")
                                    Toast.makeText(requireContext(),"Something went wrong, please try again later",Toast.LENGTH_LONG).show()
                                }
                            })

                    }
                    else{
                        //User Comes From Login Page
                        Firebase.database.reference.child(Constants.USER_DATA).child(
                            task.result.user!!.uid).addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){
                                    //User Registered Before
                                    Log.d(TAG,"Old User Login")
                                    prefManager.name = snapshot.child(Constants.PROFILE).child(Constants.NAME).getValue(String::class.java)!!
                                    prefManager.email = snapshot.child(Constants.PROFILE).child(Constants.EMAIL).getValue(String::class.java)!!
                                    navigateToHomePage()
                                }
                                else{
                                    //New User, Never Registered Before
                                    Log.d(TAG,"New Login, means new registration")

                                    val profile = HashMap<String,String>()
                                    profile[Constants.NAME] = ""
                                    profile[Constants.PHONE] = phone
                                    profile[Constants.EMAIL] = ""
                                    profile[Constants.PROFILE_PIC_URI] = ""

                                    database.child(Constants.USER_DATA).child(task.result.user!!.uid).child(Constants.PROFILE).setValue(profile).addOnCompleteListener {
                                        if(it.isSuccessful){
                                            Log.d(TAG,"User data saved ")
                                            navigateToHomePage()
                                        }
                                        else{
                                            Log.d(TAG,"User data not saved ")
                                            navigateToHomePage()
                                        }

                                    }

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                navigateToHomePage()
                                Log.e(TAG,"Database Error in checki gexisting user -> ${error.message}:: code ->${error.code}")
                                Toast.makeText(requireContext(),"Something went wrong, please try again later",Toast.LENGTH_LONG).show()
                            }
                        })

                       /* prefManager.name= user?.displayName.toString()
                        Log.d(TAG,"Name is -> ${user?.displayName.toString()} ")*/
                        //navigateToHomePage()
                    }


                } else {
                    dialogLoading.dismiss()
                    binding.tvOptStatusOtpActivity.text = "Please Enter The Correct OTP"
                    binding.tvOptStatusOtpActivity.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
                    binding.btnVerifyOtpCode.visibility = View.VISIBLE
                    binding.btnVerifyOtpCode.isClickable = true
                }
            })
    }

    private fun navigateToHomePage() {
        prefManager.isLoggedIn = true
        prefManager.phone = phone
        Toast.makeText(requireContext(),"Login Success",Toast.LENGTH_LONG).show()
        Log.d(TAG,"Login Successful")
        val action = OtpValidationFragmentDirections.actionOtpValidationFragment2ToHomeFragment()
        dialogLoading.dismiss()
        navCon.navigate(action)

        onDestroy()
        onDetach()
    }


    private fun confirmGoingBack() {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_exit, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        view.findViewById<TextView>(R.id.tv_alertImage_dialog_logout).text = "Are you sure want to exit from authentication process?"
        view.findViewById<View>(R.id.btn_yes_alertImage_dialog_delete).setOnClickListener {
            dialog.dismiss()
            goBack()
        }
        view.findViewById<View>(R.id.btn_no_alertImage_dialog_delete)
            .setOnClickListener { dialog.dismiss() }
    }


}
