package com.lincoln4791.dailyexpensemanager.view

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
import com.lincoln4791.dailyexpensemanager.databinding.FragmentRegistrationBinding
import com.lincoln4791.dailyexpensemanager.viewModels.AuthViewModel
import com.lincoln4791.network.AuthApi

class RegistrationFragment : BaseFragment<AuthViewModel,FragmentRegistrationBinding,AuthRepository>() {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        binding.registration.setOnClickListener {
            createUser(binding.email.text.toString(),binding.password.text.toString())
        }


    }


    fun createUser(email:String,password:String){
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
    }




    override fun getViewModel() : Class<AuthViewModel>{
        return AuthViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater,container,false)
    }


    override fun getFragmentRepository() : AuthRepository{
        return AuthRepository(remoteDataSource.buildApi(AuthApi::class.java))
    }


}