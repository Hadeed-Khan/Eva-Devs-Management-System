package com.techypie.evadevsmanagementsystem.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterChooseCourse
import com.techypie.evadevsmanagementsystem.Adapters.AdapterChooseStudent
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityAddEnrollmentBinding

class ActivityAddEnrollment : AppCompatActivity(), AdapterChooseStudent.StudentInterface,
    AdapterChooseCourse.CourseInterface {

    lateinit var binding: ActivityAddEnrollmentBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var studentList: MutableList<Student>
    lateinit var chooseStudentAdapter: AdapterChooseStudent
    lateinit var courseList: MutableList<Course>
    lateinit var chooseCourseAdapter: AdapterChooseCourse
    lateinit var studentSearchList: MutableList<Student>
    lateinit var courseSearchList : MutableList<Course>
    var isStudentFound = false
    var isCourseFound = false
    var selectedStudentId = ""
    var selectedCourseId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeEveryThing()



    }

    fun initializeEveryThing() {
        databaseHelper = DatabaseHelper(this@ActivityAddEnrollment)
        studentSearchList = ArrayList()
        courseSearchList = ArrayList()
        // For Student Container
        binding.chooseStdBT.setOnClickListener {
            if (binding.containerStd.visibility == View.VISIBLE)
                binding.containerStd.visibility = View.GONE
            else
                binding.containerStd.visibility = View.VISIBLE
        }

        // For Course Container
        binding.chooseCourseBT.setOnClickListener {

            if (binding.containerCourse.visibility == View.VISIBLE)
                binding.containerCourse.visibility = View.GONE
            else
                binding.containerCourse.visibility = View.VISIBLE
        }

        // For Date Container
        binding.showDateBT.setOnClickListener {
            if (binding.dateDP.visibility == View.VISIBLE)
                binding.dateDP.visibility = View.GONE
            else
                binding.dateDP.visibility = View.VISIBLE
        }

        // for Enroll
        binding.enrollBT.setOnClickListener {
            enrollStudent()
        }
// function Calling
        showStudentRecycler()
        showCourseRecycler()
        searchStudent()
        searchCourse()

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }

    }

    fun searchCourse() {
        binding.searchCourseET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                courseSearchList.clear()
                var searchText = binding.searchCourseET.text.toString()

                if (searchText.length == 0)
                {
                    showCourseRecycler()
                    binding.noDataFound2.visibility = View.GONE
                    return
                }

                for (course in courseList)
                {
                    if (course.courseName.contains(searchText,true) || course.courseFee.contains(searchText,true))
                    {
                        courseSearchList.add(course)
                        isCourseFound = true
                    }
                }

                if (isCourseFound)
                {
                    binding.noDataFound2.visibility = View.GONE
                }
                else{
                    binding.noDataFound2.visibility = View.VISIBLE
                }
                isCourseFound = false

                binding.recyclerCourse.adapter = null
                chooseCourseAdapter = AdapterChooseCourse(this@ActivityAddEnrollment,courseSearchList)
                binding.recyclerCourse.adapter = chooseCourseAdapter
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
    }
    fun searchStudent() {

        binding.searchStdET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                studentSearchList.clear()
                var searchText = binding.searchStdET.text.toString()
                if (searchText.length < 2) {
                    binding.noDataFound.visibility = View.GONE
                    showStudentRecycler()
                    return
                }

                for (student in studentList) {
                    if (student.stdName.contains(searchText, true) || student.stdFatherName.contains(searchText, true)) {
                        studentSearchList.add(student)
                        isStudentFound = true
                    }
                }

                if (isStudentFound) {
                    binding.noDataFound.visibility = View.GONE
                    Toast.makeText(this@ActivityAddEnrollment, "Hello", Toast.LENGTH_SHORT).show()
                }
                else {
                    binding.noDataFound.visibility = View.VISIBLE
                }


                binding.recyclerStd.adapter = null
                chooseStudentAdapter = AdapterChooseStudent(this@ActivityAddEnrollment,studentSearchList)
                binding.recyclerStd.adapter = chooseStudentAdapter
                isStudentFound = false
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
    fun showStudentRecycler() {
        // for Students
        binding.recyclerStd.adapter = null
        studentList = databaseHelper.getAllStudents()
        chooseStudentAdapter = AdapterChooseStudent(this@ActivityAddEnrollment, studentList)
        binding.recyclerStd.adapter = chooseStudentAdapter
        binding.recyclerStd.layoutManager = LinearLayoutManager(this@ActivityAddEnrollment)

    }
    fun showCourseRecycler() {
        // For Course
        binding.recyclerCourse.adapter = null
        courseList = databaseHelper.getAllCourses()
        chooseCourseAdapter = AdapterChooseCourse(this@ActivityAddEnrollment, courseList)
        binding.recyclerCourse.adapter = chooseCourseAdapter
        binding.recyclerCourse.layoutManager = LinearLayoutManager(this@ActivityAddEnrollment)
    }

    override fun onSelectedStudent(student: Student) {
        binding.containerStd.visibility = View.GONE
        selectedStudentId = student.stdId
        binding.stdIdTV.text = "${student.stdId}"
        binding.stdNameTV.text = "${student.stdName}"
        binding.stdFNameTV.text = "${student.stdFatherName}"
    }

    override fun courseInterface(course: Course) {
        binding.containerCourse.visibility = View.GONE
        selectedCourseId = course.courseId
        binding.courseNameTV.text = "${course.courseName}"
    }

    fun enrollStudent() {
        // For Select Date
        var day = binding.dateDP.dayOfMonth
        var month = binding.dateDP.month + 1
        var year = binding.dateDP.year
        var date = "$day/$month/$year"

        if (selectedStudentId.isEmpty() || selectedCourseId.isEmpty()) {
            Toast.makeText(
                this@ActivityAddEnrollment,
                "Select both student and course",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        var enrollmentList = databaseHelper.getAllEnrollments()

        for (enrollment in enrollmentList){
            if (enrollment.studentId.equals(selectedStudentId) && enrollment.courseId.equals(selectedCourseId))
            {
                Toast.makeText(this@ActivityAddEnrollment, "Student Already enroll in this Course", Toast.LENGTH_SHORT).show()
                return
            }
        }
        var isEnrollmentInsert = databaseHelper.addEnrollment(selectedStudentId, selectedCourseId, date, "Active")
        Toast.makeText(this@ActivityAddEnrollment, "$isEnrollmentInsert", Toast.LENGTH_SHORT).show()
        finish()


    }

}

