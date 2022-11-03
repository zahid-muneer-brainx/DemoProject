package com.example.demoproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demoproject.databinding.ActivityMainBinding
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @Inject lateinit var loginFrag:LoginFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentmanager=supportFragmentManager
        val transaction=fragmentmanager.beginTransaction()
        transaction.replace(binding.loginfragmentholder.id,loginFrag)
        transaction.commit()
    }
}