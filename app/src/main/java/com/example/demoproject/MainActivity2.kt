package com.example.demoproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.demoproject.databinding.ActivityMain2Binding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity2 : AppCompatActivity() {
     lateinit var binding: ActivityMain2Binding

    @Inject lateinit var firstFragment: HomeFragment
    @Inject lateinit var secondFragment:SearchFragment
    @Inject lateinit var thirdFragment:UploadFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
         replacefragment(firstFragment)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->replacefragment(firstFragment)
                R.id.search->replacefragment(secondFragment)
                R.id.upload->replacefragment(thirdFragment)
            }
            true

        }

    }
    private fun replacefragment(fragment:Fragment) {
        val fragmentmanager=supportFragmentManager
        val transaction=fragmentmanager.beginTransaction()
        transaction.replace(binding.flFragment.id,fragment)
        transaction.commit()
    }
}