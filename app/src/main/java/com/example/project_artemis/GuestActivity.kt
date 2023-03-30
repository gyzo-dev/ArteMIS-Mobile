package com.example.project_artemis

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.project_artemis.databinding.ActivityGuestBinding
import kotlinx.android.synthetic.main.activity_home.bottomNav
import kotlinx.android.synthetic.main.activity_home.settings

class GuestActivity : AppCompatActivity() {

    var backPressedTime: Long = 0
    private lateinit var binding : ActivityGuestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        settings.setOnClickListener {
            val intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }

        val connectionManager: ConnectivityManager = this.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("No Internet Connection")
            builder.setMessage("Please connect to the Internet to proceed")
            builder.setPositiveButton("Retry") {dialog, which ->
                val intent = Intent(this, this::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            val dialog = builder.create()
            dialog.show()
            //Toast.makeText(this,"You are not connected to the Internet", Toast.LENGTH_LONG).show()
            //finishAffinity()
            //finish()
        }
        bottomNav.selectedItemId = R.id.home
        binding.bottomNav.setOnItemSelectedListener {

            when(it.itemId){
                R.id.status -> replaceFragment(DataFragment())
                R.id.home -> replaceFragment(HomeFragment())
                R.id.maps -> replaceFragment(LocationFragment())

                else ->{
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }


    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
            finishAffinity()
            finish()
        }else{
            Toast.makeText(this, "Press back again to exit the app.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}