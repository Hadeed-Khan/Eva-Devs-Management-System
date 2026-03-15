package com.techypie.evadevsmanagementsystem.Activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.SharedPreferencesCompat.EditorCompat
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Global Variables
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intializeEveryThing()

    }

    // My Owns Functions
    fun intializeEveryThing()
    {
        // For Add Student Screen

        binding.addStudentCard.setOnClickListener{
            startActivity(Intent(this@MainActivity,ActivityAddStudent::class.java))
        }

        // For Add Course Screen
        binding.addCourseCard.setOnClickListener {
            startActivity(Intent(this@MainActivity,ActivityAddCourse::class.java))
        }

        binding.enrollmentCard.setOnClickListener {
            startActivity(Intent(this@MainActivity, ActivityViewEnrollments::class.java))
        }

        binding.feeCard.setOnClickListener {
            startActivity(Intent(this@MainActivity,ActivityViewFeeRecord::class.java))
        }

        binding.attendanceCard.setOnClickListener {
            startActivity(Intent(this@MainActivity,ActivityAttendance::class.java))
        }

        binding.reportCard.setOnClickListener {
            startActivity(Intent(this@MainActivity,ActivityReport::class.java))
        }

        binding.inActiveCard.setOnClickListener {
            startActivity(Intent(this@MainActivity,ActivityInActiveSection::class.java))
        }

        // For Showing Setting dialog
        binding.settingIV.setOnClickListener {
showSettingDialog()
        }

    }

    fun showSettingDialog(){

        var builder = AlertDialog.Builder(this@MainActivity)
        var layout = layoutInflater.inflate(R.layout.dialog_setting, null)
        builder.setView(layout)
        var dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)


        var changePassWordBt = layout.findViewById<Button>(R.id.changePassWordBT)
        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogBT)
        var logoutBT = layout.findViewById<ImageView>(R.id.logoutIV)

        cancelDialog.setOnClickListener {
            dialog.dismiss()
        }

        logoutBT.setOnClickListener {
            forLogout()
        }

        changePassWordBt.setOnClickListener {
            showChangeDialog()
        }

    }

    fun forLogout(){
        var sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putString("Remember_Me","No")
        editor.commit()
        startActivity(Intent(this@MainActivity,ActivityLogin::class.java))
        finish()
    }


    fun showChangeDialog()
    {
        var builder = AlertDialog.Builder(this@MainActivity)
        var layout = layoutInflater.inflate(R.layout.dialog_change_credentials, null)

        builder.setView(layout)


        var dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)


        var userNameET = layout.findViewById<EditText>(R.id.userNameET)
        var passwordET = layout.findViewById<EditText>(R.id.passwordET)
        var updateBT = layout.findViewById<Button>(R.id.updateBT)
        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogBT)

        cancelDialog.setOnClickListener {
            dialog.dismiss()
        }


        updateBT.setOnClickListener {

            var newName = userNameET.text.toString().trim()
            var newPassword = passwordET.text.toString().trim()

            if (newName.isEmpty() || newPassword.isEmpty()){
                Toast.makeText(this@MainActivity, "Please Fill Both Fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var sharedPref = getSharedPreferences("Login", MODE_PRIVATE)

            var oldUserName = sharedPref.getString("USER_NAME","").toString()
            var oldPassword = sharedPref.getString("PASSWORD","").toString()

            if (oldUserName == newName && oldPassword == newPassword){
                Toast.makeText(this@MainActivity, "This is Old Credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var editor = sharedPref.edit()

            editor.putString("USER_NAME", newName)
            editor.putString("PASSWORD", newPassword)

            var success = editor.commit()

            if(success)
            {
                Toast.makeText(this@MainActivity, "Credentials Updated!", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()

        }



    }

}