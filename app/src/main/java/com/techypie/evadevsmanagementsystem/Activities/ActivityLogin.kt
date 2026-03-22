package com.techypie.evadevsmanagementsystem.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mycompanny.stories.MyNotification
import com.techypie.evadevsmanagementsystem.databinding.ActivityLoginBinding

class ActivityLogin : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    var storedUserName = ""
    var storedPassword = ""

    lateinit var sharedPref : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupNotification()

        sharedPref = getSharedPreferences("Login", MODE_PRIVATE)


        storedUserName = sharedPref.getString("USER_NAME", "ADMIN").toString()
        storedPassword = sharedPref.getString("PASSWORD", "ADMIN").toString()



        binding.loginBT.setOnClickListener {
            var userName = binding.userNameET.text.toString().trim()
            var password = binding.passwordET.text.toString().trim()

            if (userName.isEmpty() || password.isEmpty()){
                Toast.makeText(this@ActivityLogin, "Please Fill Both Fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // for remember me Function
            if(userName == storedUserName && password == storedPassword && binding.rememberMeCB.isChecked)
            {
                sharedPref.getString("Remember_Me","Yes")
                var editor = sharedPref.edit()
                editor.putString("Remember_Me","Yes")
                editor.commit()
                startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                finish()
            }


            if(userName == storedUserName && password == storedPassword )
            {
                startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                finish()
                binding.passIncorrect.visibility = View.GONE
            }
            else{
                binding.passIncorrect.visibility = View.VISIBLE
            }




        }





    }


    override fun onResume() {
        super.onResume()

        var rememberMe = sharedPref.getString("Remember_Me","").toString()
        if (rememberMe == "Yes"){
            startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
            finish()
            return
        }
    }

    fun setupNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Please Grant Permsiion", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }else{
                var myNotification = MyNotification(this@ActivityLogin)
                myNotification.createNotificationChannel(this@ActivityLogin)
                myNotification.scheduleAt(9,0,0)
            }
        }
        else{
            var myNotification = MyNotification(this@ActivityLogin)
            myNotification.createNotificationChannel(this@ActivityLogin)
            myNotification.scheduleAt(9,0,0)
        }
    }
}