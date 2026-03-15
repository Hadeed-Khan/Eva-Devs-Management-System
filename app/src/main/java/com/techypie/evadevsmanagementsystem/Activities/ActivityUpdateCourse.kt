package com.techypie.evadevsmanagementsystem.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityUpdateCourseBinding

class ActivityUpdateCourse : AppCompatActivity() {
    lateinit var binding : ActivityUpdateCourseBinding
    lateinit var course: Course
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        course = intent.getSerializableExtra("Course") as Course
        intializeEveryThing()


    }
    fun intializeEveryThing()
    {
        databaseHelper = DatabaseHelper(this@ActivityUpdateCourse)

        // For Old Course Data
        forOldCourseData()

        // For Update Course
        binding.updateCourseBT.setOnClickListener {
            updateCourse()
        }
        // For Soft Delete
        binding.deleteCourseBT.setOnClickListener {
            var courseSoftDelete = databaseHelper.courseSoftDelete(course.courseId)
            Toast.makeText(this@ActivityUpdateCourse, "$courseSoftDelete", Toast.LENGTH_SHORT).show()
            finish()
        }

        // For BackActivity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
    }

    fun updateCourse() {
       var courseName = binding.courseNameET.text.toString()
        var courseFee = binding.courseFeeET.text.toString()
        var courseDuration = binding.courseDurationSP.selectedItem.toString()
        if (courseName.isEmpty() || courseFee.isEmpty())
        {
            Toast.makeText(this@ActivityUpdateCourse, "Please Fill All Field", Toast.LENGTH_SHORT).show()
            return
        }

        var isCourseUpdated = databaseHelper.updateCourse(course.courseId, courseName,courseDuration,courseFee)
        Toast.makeText(this@ActivityUpdateCourse, "$isCourseUpdated", Toast.LENGTH_SHORT).show()

        finish()
    }
    fun forOldCourseData() {
        binding.courseNameET.setText(course.courseName)
        binding.courseFeeET.setText(course.courseFee)
        var courseDuration = course.courseDuration
        // For Course Spinner
        var courseDurationList = resources.getStringArray(R.array.course_duration).toMutableList()
        var courseIndex = courseDurationList.indexOf(courseDuration)
        binding.courseDurationSP.setSelection(courseIndex)
    }
}