package com.techypie.evadevsmanagementsystem.Activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterMarkAttendance
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.databinding.ActivityMarkAttendanceBinding

class ActivityMarkAttendance : AppCompatActivity() {

    lateinit var binding: ActivityMarkAttendanceBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var course: Course
    lateinit var enrollStudentList: MutableList<Student>
    lateinit var adapter: AdapterMarkAttendance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMarkAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        course = intent.getSerializableExtra("Course") as Course
        intializeEveryThing()

    }

    fun intializeEveryThing() {
        databaseHelper = DatabaseHelper(this@ActivityMarkAttendance)

        // For Show Date
        binding.selectDateBT.setOnClickListener {
            if (binding.dateDP.visibility == View.GONE) {
                binding.dateDP.visibility = View.VISIBLE
            } else {
                binding.dateDP.visibility = View.GONE
            }
        }

        // For Show Recycler View
        enrollStudentList = databaseHelper.getEnrollStudent(course.courseId)
        adapter =
            AdapterMarkAttendance(this@ActivityMarkAttendance, enrollStudentList, course.courseId)
        binding.recyclerStudent.adapter = adapter
        binding.recyclerStudent.layoutManager = LinearLayoutManager(this@ActivityMarkAttendance)


        binding.markAttendanceBT.setOnClickListener {
            markAttendance()
        }

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }

    }

    fun markAttendance() {
        // For Date
        var day = binding.dateDP.dayOfMonth
        var month = binding.dateDP.month + 1
        var year = binding.dateDP.year

        var date = "$day/$month/$year"
        var attendanceList = adapter.getAllAttendance()
        var isAdendanceAdd = ""

        var allAttendanceList = databaseHelper.getCourseAttendacne(course.courseId)

        if (allAttendanceList.size > 0) {

            for (x in 0..attendanceList.size - 1) {

                for (y in 0..allAttendanceList.size - 1) {
                    if (allAttendanceList[y].stdId.equals(attendanceList[x].stdId) && allAttendanceList[y].courseId.equals(
                            course.courseId
                        ) && allAttendanceList[y].date.equals(date)
                    ) {
                        Toast.makeText(
                            this@ActivityMarkAttendance,
                            "Today Attendance in Already Marked",  
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                }

            }

        }
        var isDataAdd = databaseHelper.addAttendance(attendanceList,date)
        Toast.makeText(this@ActivityMarkAttendance, "$isDataAdd", Toast.LENGTH_SHORT).show()
        finish()
    }

}

