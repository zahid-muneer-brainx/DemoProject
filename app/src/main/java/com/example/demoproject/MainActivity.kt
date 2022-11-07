package com.example.demoproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demoproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var loginFrag: LoginFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val settings = getSharedPreferences("login", 0)
        val hasLoggedIn = settings.getBoolean("hasLoggedIn", false)

        if (hasLoggedIn) {
            startActivity(Intent(this, MainActivity2::class.java))
        } else {
            val fragmentmanager = supportFragmentManager
            val transaction = fragmentmanager.beginTransaction()
            transaction.replace(binding.loginfragmentholder.id, loginFrag)
            transaction.commit()
        }
    }
}