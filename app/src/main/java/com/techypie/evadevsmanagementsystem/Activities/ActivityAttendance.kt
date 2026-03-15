package com.techypie.evadevsmanagementsystem.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterChooseCourseAttendance
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.databinding.ActivityAttendanceBinding

class ActivityAttendance : AppCompatActivity() {

    lateinit var binding : ActivityAttendanceBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var courseList : MutableList<Course>
    lateinit var adapter : AdapterChooseCourseAttendance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intializeEveryThing()

    }

    fun intializeEveryThing()
    {
        databaseHelper = DatabaseHelper(this@ActivityAttendance)
       showRecyclerView()

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
    }

    fun showRecyclerView()
    {
        courseList = databaseHelper.getAllCourses()
        adapter = AdapterChooseCourseAttendance(this@ActivityAttendance, courseList)
        binding.recyclerCourse.adapter = adapter
        binding.recyclerCourse.layoutManager = LinearLayoutManager(this@ActivityAttendance)

    }
}
