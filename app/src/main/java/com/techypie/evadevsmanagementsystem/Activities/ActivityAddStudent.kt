package com.techypie.evadevsmanagementsystem.Activities

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterStudent
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityAddStudentBinding

class ActivityAddStudent : AppCompatActivity() {
    // Global Variables
    lateinit var binding: ActivityAddStudentBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var studentList : MutableList<Student>
    lateinit var studentAdapter : AdapterStudent
    lateinit var searchList : MutableList<Student>
    var isDataFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intializeEveryThing()

    }
    fun intializeEveryThing()
    {
        // Intialization
        databaseHelper = DatabaseHelper(this@ActivityAddStudent)
        searchList = ArrayList()
        // For Show Dialog Box

        binding.addStudentIV.setOnClickListener {
            showAddStudentDialogBox()
        }

        showRecyclerView()
        forSearch()

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
    }

    fun forSearch()
    {
        binding.searchStdET.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchList.clear()
                var searchET = binding.searchStdET.text.toString().trim()
                if (searchET.length<2){
                    showRecyclerView()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (student in studentList)
                {
                    if (student.stdName.contains(searchET,true) || student.stdFatherName.contains(searchET,true) || student.stdPhoneNumber.contains(searchET,true))
                    {
                        searchList.add(student)
                        isDataFound = true
                    }

                }

                if (isDataFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.studentRecyclerView.adapter = null
                studentAdapter = AdapterStudent(this@ActivityAddStudent,searchList)
                binding.studentRecyclerView.adapter = studentAdapter
                isDataFound = false

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    fun showRecyclerView() {
        binding.studentRecyclerView.adapter  = null
        studentList = databaseHelper.getAllStudents()
        studentAdapter = AdapterStudent(this@ActivityAddStudent,studentList)
        binding.studentRecyclerView.adapter = studentAdapter
        binding.studentRecyclerView.layoutManager = LinearLayoutManager(this@ActivityAddStudent)
    }
    fun showAddStudentDialogBox() {
        var builder = AlertDialog.Builder(this@ActivityAddStudent)
        var layout = layoutInflater.inflate(R.layout.dialog_add_student,null)
        builder.setView(layout)
        var dialogBox = builder.create()
        dialogBox.show()
        dialogBox.setCancelable(false)
        dialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Dialog View Intilize
        var stdName = layout.findViewById<EditText>(R.id.studentNameET)
        var stdFatherName = layout.findViewById<EditText>(R.id.studentFatherNameET)
        var genderRadioGroup = layout.findViewById<RadioGroup>(R.id.genderRadioGroup)
        var stdPhoneN = layout.findViewById<EditText>(R.id.studentNumberET)
        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogBT)
        var addStudent = layout.findViewById<Button>(R.id.addStudentBT)
        var maleButton = layout.findViewById<RadioButton>(R.id.maleButton)
        var femaleButton = layout.findViewById<RadioButton>(R.id.femaleButton)



        // For Remove Dialog
        cancelDialog.setOnClickListener {
            dialogBox.dismiss()
        }

        // For insert Data into Database
        addStudent.setOnClickListener {
            if (stdName.text.isEmpty() || stdFatherName.text.isEmpty() || stdPhoneN.text.isEmpty() || genderRadioGroup.checkedRadioButtonId == -1){
                Toast.makeText(this@ActivityAddStudent, "Please Fill all Field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var studentName = stdName.text.toString().trim()
            var studentFatherName = stdFatherName.text.toString().trim()
           var studentPhoneNumber = stdPhoneN.text.toString().trim()

            // For Gender
            var stdGender = ""
            if (maleButton.isChecked){
                stdGender = maleButton.text.toString()
            }
            else{
                stdGender = femaleButton.text.toString()
            }

            var isStudentInserted = databaseHelper.addStudents(studentName,studentFatherName,stdGender,studentPhoneNumber,"Active")
            Toast.makeText(this@ActivityAddStudent, "$isStudentInserted", Toast.LENGTH_SHORT).show()

            // Get Last Student
            var lastStudent = databaseHelper.getLastStudent()
            studentList.add(lastStudent)
            studentAdapter.notifyItemInserted(studentList.size-1)
            dialogBox.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        showRecyclerView()
    }
}