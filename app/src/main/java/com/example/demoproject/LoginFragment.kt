package com.example.demoproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.demoproject.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment @Inject constructor() : Fragment() {
    @Inject
    lateinit var  preferenceDataStore: PreferenceDataStore
    lateinit var binding: FragmentLoginBinding
    private val model: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        responseObserver(model)
        model.failedResponse.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.loginbtn.setOnClickListener {


            if (validateEmailPassword(binding.useremail.text.toString(), binding.userpassword.text.toString())) {

                model.login(binding.useremail.text.toString(), binding.userpassword.text.toString())

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
        return binding.root
    }

    private fun responseObserver(model: LoginViewModel) {
        model.serverResponse.observe(viewLifecycleOwner) { serverResponse ->
            if (serverResponse == null) {
                Toast.makeText(requireContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(
                    requireContext(),
                    "Logged in Successful",
                    Toast.LENGTH_SHORT
                ).show()
                lifecycleScope.launch(Dispatchers.IO){
                    preferenceDataStore.saveLoginStatus(true)
                }
                activity?.let {
                    val intent = Intent(it, BottomNavigationActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }
    }

    private fun validateEmailPassword(email: String, password: String): Boolean {
        return if (!isEmailValid(email))
            false
        else password.length >= 6

    }

    private fun isEmailValid(email: String): Boolean {
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