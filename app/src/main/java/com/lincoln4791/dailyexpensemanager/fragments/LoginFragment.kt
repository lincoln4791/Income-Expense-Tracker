package com.lincoln4791.dailyexpensemanager.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lincoln4791.dailyexpensemanager.AuthRepository
import com.lincoln4791.dailyexpensemanager.R
import com.lincoln4791.dailyexpensemanager.base.BaseFragment
import com.lincoln4791.dailyexpensemanager.databinding.FragmentLoginBinding
import com.lincoln4791.dailyexpensemanager.databinding.FragmentRegistrationBinding
import com.lincoln4791.dailyexpensemanager.viewModels.AuthViewModel
import com.lincoln4791.network.AuthApi

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.login2.setOnClickListener {
            login(binding.email2.text.toString(),binding.password2.text.toString())
        }

    }


    private fun login(email : String , password : String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task : Task<AuthResult> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    Log.d("Login", "signInWithEmail:success")
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    //Log.d("Login", "signInWithEmail:success -> ${user.}")
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

}