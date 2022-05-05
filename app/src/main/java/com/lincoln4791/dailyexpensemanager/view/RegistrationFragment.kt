package com.lincoln4791.dailyexpensemanager.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.AuthRepository
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.lincoln4791.dailyexpensemanager.databinding.FragmentRegistrationBinding
import com.lincoln4791.dailyexpensemanager.viewModels.AuthViewModel
import com.lincoln4791.dailyexpensemanager.network.AuthApi
import java.util.concurrent.TimeUnit

class RegistrationFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var navCon : NavController
    private lateinit var binding : FragmentRegistrationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Log.d("tag","OnBackPressCalled -> Registration")
                    //navCon.navigateUp()
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        navCon = Navigation.findNavController(view)

        binding.registrationR.setOnClickListener {
            validate()
        }

        binding.ivToolbarBack.setOnClickListener {
            goBack()
        }

    }


    private fun validate() {
        binding.phoneR.error = null
        binding.passwordR.error = null
        binding.cPasswordR.error = null

        when {
            binding.phoneR.text.isNullOrEmpty() -> binding.phoneR.error = "Please enter phone Number"
            binding.phoneR.text!!.length != 11 -> binding.phoneR.error = "Phone number should be 11 digit"
            binding.nameR.text.isNullOrEmpty() -> binding.nameR.error = "Please enter your name"
           /* binding.passwordR.text.isNullOrEmpty() -> binding.passwordR.error = "Please enter a password"
            binding.cPasswordR.text.isNullOrEmpty() -> binding.cPasswordR.error = "Please confirm password"
            binding.passwordR.text!!.length <6 -> binding.passwordR.error = "Password should be at least 6 digit"
            binding.passwordR.text.toString() != binding.cPasswordR.text.toString() -> binding.cPasswordR.error = "Password does not match"*/
            //Log.d(TAG,"password is ${password.text.} and c.password is ${confirmPassword.text}")
            else -> {
                Log.d("Registration","Ready To Register")
               navigateToOtpActivity()
            }
        }
    }

    private fun navigateToOtpActivity(){
        val action = RegistrationFragmentDirections.actionRegistrationFragmentToOtpValidationFragment23(
            binding.nameR.text.toString(),
            binding.phoneR.text.toString()
        )
        navCon.navigate(action)
        this.onDestroy()
        this.onDetach()
    }

/*    fun createUserWithEmail(email:String, password:String){
        Log.d("Register","mail -> $email ::: pass $password")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task: Task<AuthResult>->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("Register", "createUserWithEmail:success")
                val user = auth.currentUser
                Log.d("Register","Firebase USer is -> $user")
                //updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.d("Register", "createUserWithEmail:failure", task.exception)
                Toast.makeText(requireContext(), "Authentication failed -> ${task.exception?.message}",
                    Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }
        }
    }*/

    private fun goBack() {
        val action = RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment()
        navCon.navigate(action)
        onDestroy()
        onDetach()
    }




}