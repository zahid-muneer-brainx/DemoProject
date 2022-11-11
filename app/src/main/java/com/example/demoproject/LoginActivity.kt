package com.example.demoproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.demoproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore
    lateinit var binding: ActivityMainBinding
    var observer = MutableLiveData<Boolean>(false)

    @Inject
    lateinit var loginFrag: LoginFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        var status: Boolean = false
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        observer.observe(this) {
            if (it) {
                if (status) {
                    startActivity(Intent(this@LoginActivity, BottomNavigationActivity::class.java))
                } else {
                    val fragmentManager = supportFragmentManager
                    fragmentManager.commit {
                        setReorderingAllowed(true)
                        // Replace whatever is in the fragment_container view with this fragment
                        replace(binding.loginfragmentholder.id, loginFrag)
                    }
                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            preferenceDataStore.getLoginStatus().collect {

                status = it
                observer.postValue(true)
                println("Status inside: $status")
            }
        }
        println("Status: $status")

    }
}