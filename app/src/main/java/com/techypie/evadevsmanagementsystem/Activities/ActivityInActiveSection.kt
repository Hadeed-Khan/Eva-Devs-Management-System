package com.techypie.evadevsmanagementsystem.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterCourse
import com.techypie.evadevsmanagementsystem.Adapters.AdapterEnrollment
import com.techypie.evadevsmanagementsystem.Adapters.AdapterStudent
import com.techypie.evadevsmanagementsystem.Adapters.InactiveAdapter.AdapterInactiveAttendance
import com.techypie.evadevsmanagementsystem.Adapters.InactiveAdapter.AdapterInactiveCourse
import com.techypie.evadevsmanagementsystem.Adapters.InactiveAdapter.AdapterInactiveEnrollment
import com.techypie.evadevsmanagementsystem.Adapters.InactiveAdapter.AdapterInactiveFee
import com.techypie.evadevsmanagementsystem.Adapters.InactiveAdapter.AdapterInactiveStudent
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Attendance
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.Models.Enrollment
import com.techypie.evadevsmanagementsystem.Models.Fee
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityInActiveSectionBinding

class ActivityInActiveSection : AppCompatActivity() {

    lateinit var binding : ActivityInActiveSectionBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var inActiveStudentList : MutableList<Student>
    lateinit var inActiveCourseList : MutableList<Course>
    lateinit var inActiveEnrollmentList : MutableList<Enrollment>
    lateinit var inActiveFeeList : MutableList<Fee>
    lateinit var inActiveAttendance : MutableList<Attendance>
    // Adapter
    lateinit var adapterStudent : AdapterInactiveStudent
    lateinit var adapterCourse : AdapterInactiveCourse
    lateinit var adapterEnrollment : AdapterInactiveEnrollment
    lateinit var adapterAttendance : AdapterInactiveAttendance
    lateinit var adapterFee : AdapterInactiveFee
    var isDataFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInActiveSectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intializeEveryThing()




    }



    fun intializeEveryThing() {
        databaseHelper = DatabaseHelper(this@ActivityInActiveSection)
        binding.inactiveRecycler.layoutManager = LinearLayoutManager(this@ActivityInActiveSection)

        binding.inActiveStudentBT.setOnClickListener {
            showInactiveStudents()
        }
        binding.inActiveCourse.setOnClickListener {
            showInactiveCourse()
        }
        binding.inActiveEnrollmentBT.setOnClickListener {
            showInactiveEnrollments()
        }
        binding.inActiveAttendanceBT.setOnClickListener {
            showInactiveAttendance()
        }

        binding.inActiveFeeBT.setOnClickListener {
            showInactiveFee()
        }

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }


    }



    fun showInactiveStudents(){
        binding.inactiveRecycler.adapter = null
        binding.inactiveText.text = "Previous Students"
        inActiveStudentList = databaseHelper.getAllInactiveStudents()
        adapterStudent = AdapterInactiveStudent(this@ActivityInActiveSection,inActiveStudentList)
        binding.inactiveRecycler.adapter = adapterStudent
        binding.searchET.text.clear()
        searchStudents()

    }
    fun showInactiveCourse() {
        binding.inactiveText.text = "Previous Courses"
        binding.inactiveRecycler.adapter = null
        inActiveCourseList = databaseHelper.getAllInactiveCourses()
        adapterCourse  = AdapterInactiveCourse(this@ActivityInActiveSection,inActiveCourseList)
        binding.inactiveRecycler.adapter = adapterCourse
        searchCourse()
    }
    fun showInactiveEnrollments() {
        binding.inactiveRecycler.adapter = null
        binding.inactiveText.text = "Previous Enrollments"
       inActiveEnrollmentList = databaseHelper.getAllInactiveEnrollments()
        adapterEnrollment = AdapterInactiveEnrollment(this@ActivityInActiveSection,inActiveEnrollmentList)
        binding.inactiveRecycler.adapter = adapterEnrollment
        searchEnrollments()

    }
    fun showInactiveAttendance() {
        binding.inactiveRecycler.adapter = null
        binding.inactiveText.text = "Previous Attendance"
        inActiveAttendance = databaseHelper.getAllInactiveAttendacne()
        adapterAttendance = AdapterInactiveAttendance(this@ActivityInActiveSection,inActiveAttendance)
        binding.inactiveRecycler.adapter = adapterAttendance
        searchAttendance()
    }
    fun showInactiveFee() {
        binding.inactiveRecycler.adapter = null
        binding.inactiveText.text = "Previous Fee"
        inActiveFeeList = databaseHelper.getAllInactiveFee()
        adapterFee = AdapterInactiveFee(this@ActivityInActiveSection,inActiveFeeList)
        binding.inactiveRecycler.adapter = adapterFee
        searchFee()
    }

    // Search Section

    fun searchStudents() {
        binding.searchET.text.clear()
        binding.searchET.hint = "Search Student Name , Father Name"
        var searchStudentList : MutableList<Student>
        searchStudentList = ArrayList()

        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                searchStudentList.clear()
                var searchText = binding.searchET.text.toString()

                if (searchText.length==0)
                {
                    showInactiveStudents()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (student in inActiveStudentList){

                    if (student.stdName.contains(searchText,true) || student.stdFatherName.contains(searchText,true))
                    {
                        searchStudentList.add(student)
                        isDataFound = true
                    }
                }

                if (isDataFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.inactiveRecycler.adapter = null
                adapterStudent = AdapterInactiveStudent(this@ActivityInActiveSection,searchStudentList)
                binding.inactiveRecycler.adapter = adapterStudent

                isDataFound = false
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
    fun searchCourse() {
        binding.searchET.text.clear()
        binding.searchET.hint = "Search Course Name"

        var searchCourseList : MutableList<Course>
        searchCourseList = ArrayList()

        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                searchCourseList.clear()
                var searchText = binding.searchET.text.toString()

                if (searchText.length==0)
                {
                    showInactiveCourse()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (course in inActiveCourseList){

                    if (course.courseName.contains(searchText,true))
                    {
                        searchCourseList.add(course)
                        isDataFound = true
                    }
                }

                if (isDataFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.inactiveRecycler.adapter = null
                adapterCourse = AdapterInactiveCourse(this@ActivityInActiveSection,searchCourseList)
                binding.inactiveRecycler.adapter = adapterCourse

                isDataFound = false
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
    fun searchEnrollments() {
        binding.searchET.text.clear()
        binding.searchET.hint = "Search Student Name, Father Name"

        var searchEnrollmentsList : MutableList<Enrollment>
        searchEnrollmentsList = ArrayList()

        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                searchEnrollmentsList.clear()
                var searchText = binding.searchET.text.toString()

                if (searchText.length==0)
                {
                    showInactiveEnrollments()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (enroll in inActiveEnrollmentList){

                    if (enroll.studentName.contains(searchText,true) || enroll.studentFName.contains(searchText,true))
                    {
                        searchEnrollmentsList.add(enroll)
                        isDataFound = true
                    }
                }

                if (isDataFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.inactiveRecycler.adapter = null
                adapterEnrollment = AdapterInactiveEnrollment(this@ActivityInActiveSection,searchEnrollmentsList)
                binding.inactiveRecycler.adapter = adapterEnrollment

                isDataFound = false
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
    fun searchAttendance() {
        binding.searchET.text.clear()
        binding.searchET.hint = "Search Student Name, Father Name"

        var searchAttendanceList : MutableList<Attendance>
        searchAttendanceList = ArrayList()

        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                searchAttendanceList.clear()
                var searchText = binding.searchET.text.toString()

                if (searchText.length==0)
                {
                    showInactiveAttendance()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (attendance in inActiveAttendance){

                    if (attendance.stdName.contains(searchText,true) || attendance.stdFatherName.contains(searchText,true))
                    {
                        searchAttendanceList.add(attendance)
                        isDataFound = true
                    }
                }

                if (isDataFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.inactiveRecycler.adapter = null
                adapterAttendance = AdapterInactiveAttendance(this@ActivityInActiveSection,searchAttendanceList)
                binding.inactiveRecycler.adapter = adapterAttendance

                isDataFound = false
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
    fun searchFee() {
        binding.searchET.text.clear()
        binding.searchET.hint = "Search Student Name, course Name,fee type"

        var searchFeeList : MutableList<Fee>
        searchFeeList = ArrayList()

        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                searchFeeList.clear()
                var searchText = binding.searchET.text.toString()

                if (searchText.length==0)
                {
                    showInactiveFee()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (fee in inActiveFeeList){

                    if (fee.stdName.contains(searchText,true) || fee.courseName.contains(searchText,true) || fee.feeType.contains(searchText,true))
                    {
                        searchFeeList.add(fee)
                        isDataFound = true
                    }
                }

                if (isDataFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.inactiveRecycler.adapter = null
                adapterFee = AdapterInactiveFee(this@ActivityInActiveSection,searchFeeList)
                binding.inactiveRecycler.adapter = adapterFee

                isDataFound = false
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }


}