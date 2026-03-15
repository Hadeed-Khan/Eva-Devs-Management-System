package com.techypie.evadevsmanagementsystem.Activities

import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityUpdateStudentBinding

class ActivityUpdateStudent : AppCompatActivity() {
    lateinit var binding: ActivityUpdateStudentBinding
    lateinit var student: Student
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intialization
        databaseHelper = DatabaseHelper(this@ActivityUpdateStudent)
        student = intent.getSerializableExtra("Student") as Student

        intialzeEveryThing()
    }

    fun intialzeEveryThing() {
        forOldStudentData()

        binding.updateStudentBT.setOnClickListener {
            updateStudent()
        }
        binding.deleteStudentBT.setOnClickListener {
            var isSoftDeleted = databaseHelper.softDeleteStudent(student.stdId)
            Toast.makeText(this@ActivityUpdateStudent, "$isSoftDeleted", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.backActivityIV.setOnClickListener {
            finish()
        }

    }

    fun updateStudent() {
        var studentName = binding.studentNameET.text.toString()
        var studentFName = binding.studentFatherNameET.text.toString()
        var studentPhoneNumber = binding.studentNumberET.text.toString()

        if (studentName.isEmpty() || studentFName.isEmpty() || studentPhoneNumber.isEmpty() || binding.genderRadioGroup.checkedRadioButtonId == -1)
        {
            Toast.makeText(this@ActivityUpdateStudent, "Please Fill all Fields", Toast.LENGTH_SHORT).show()
            return
        }

        // for Gender
        var stdGender = ""
        if (binding.maleButton.isChecked)
        {
            stdGender = binding.maleButton.text.toString()
        }
        else{
            stdGender = binding.femaleButton.text.toString()
        }

        // For Updated Student
        var isStudentUpdated = databaseHelper.updateStudent(student.stdId,studentName,studentFName,stdGender,studentPhoneNumber)
        Toast.makeText(this@ActivityUpdateStudent, "$isStudentUpdated", Toast.LENGTH_SHORT).show()
        finish()
    }
    fun forOldStudentData(){

        binding.studentNameET.setText(student.stdName)
        binding.studentFatherNameET.setText(student.stdFatherName)
        binding.studentNumberET.setText(student.stdPhoneNumber)

        // For Old Student Gender
        if (student.stdGender.trim() == "Male"){
            binding.maleButton.isChecked = true
        }
        else{
            binding.femaleButton.isChecked = true
        }
    }
}