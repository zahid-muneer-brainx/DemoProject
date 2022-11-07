package com.example.demoproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.demoproject.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment @Inject constructor() : Fragment() {


    lateinit var binding: FragmentLoginBinding
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var loginbtn: Button
    private val Model: LoginViewModel by viewModels()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        ResponseObserver(Model)
        loginbtn.setOnClickListener {


            if (validateEmailPassword(email.text.toString(), password.text.toString())) {

                Model.login(email.text.toString(), password.text.toString())

            } else {
                Toast.makeText(
                    requireContext(),
                    "Invalid Email or Password",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        email = binding.useremail
        password = binding.userpassword
        loginbtn = binding.loginbtn

        return binding.root
    }

    private fun ResponseObserver(model: LoginViewModel) {
        model.serverresponse.observe(viewLifecycleOwner) { serverResponse ->
            if (serverResponse == null) {
                Toast.makeText(requireContext(), "Logged in Failure", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(
                    requireContext(),
                    "Logged in Successful" + serverResponse,
                    Toast.LENGTH_SHORT
                ).show()
                val settings: SharedPreferences =
                    this.requireActivity().getSharedPreferences("login", 0) // 0 - for private mode
                val editor = settings.edit()
                editor.putBoolean("hasLoggedIn", true)
                editor.apply()
                activity?.let {
                    val intent = Intent(it, MainActivity2::class.java)
                    it.startActivity(intent)
                }
            }
        }
    }

    private fun validateEmailPassword(email: String, password: String): Boolean {
        if (!isEmailValid(email))
            return false
        else return password.length >= 6

    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }


}