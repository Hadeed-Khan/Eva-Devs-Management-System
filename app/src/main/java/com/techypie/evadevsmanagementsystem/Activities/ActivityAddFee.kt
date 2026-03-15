package com.techypie.evadevsmanagementsystem.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterChooseCourseForFee
import com.techypie.evadevsmanagementsystem.Adapters.AdapterChooseStudentForFee
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityAddFeeBinding
import com.techypie.evadevsmanagementsystem.databinding.ActivityViewFeeRecordBinding

class ActivityAddFee : AppCompatActivity(), AdapterChooseCourseForFee.CourseInterface,
    AdapterChooseStudentForFee.StudentInterface {

    lateinit var binding: ActivityAddFeeBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var studentList: MutableList<Student>
    lateinit var courseList: MutableList<Course>
    lateinit var chooseCourseAdapter: AdapterChooseCourseForFee
    lateinit var chooseStudentAdapter: AdapterChooseStudentForFee
    var selectedCourseId = ""
    var selectedStudentId = ""
    var courseFee = ""
    var totalFee = ""
    var feeType = ""

    // for Search
    lateinit var courseSearchList: MutableList<Course>
    lateinit var studentSearchList: MutableList<Student>

    var isCourseFound = false
    var isStudentFound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddFeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intializeEveryThing()


    }

    fun intializeEveryThing() {
        databaseHelper = DatabaseHelper(this@ActivityAddFee)
        courseSearchList = ArrayList()
        studentSearchList = ArrayList()

        // For Student Container
        binding.chooseStdBT.setOnClickListener {
            if (binding.containerStd.visibility == View.VISIBLE) {
                binding.containerStd.visibility = View.GONE
            } else
                binding.containerStd.visibility = View.VISIBLE
        }

        // For Course Container
        binding.chooseCourseBT.setOnClickListener {

            if (binding.containerCourse.visibility == View.VISIBLE)
                binding.containerCourse.visibility = View.GONE
            else
                binding.containerCourse.visibility = View.VISIBLE
        }
        // Function Recycler
        showCourseRecycler()
        showStudentRecycler()
        searchStudent()
        searchCourse()

        // fOR Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }


        // for Fee Type
        binding.feeTypeSP.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


                var selectedItem  = binding.feeTypeSP.selectedItem.toString()
                if (selectedItem == "Admission Fee"){
                    totalFee = "3000"
                    binding.monthFeeDP.visibility = View.GONE
                    feeType = selectedItem
                }
                else {
                    binding.monthFeeDP.visibility = View.VISIBLE
                    totalFee = courseFee

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        binding.addFeeBT.setOnClickListener {
            addFee()
        }
    }

    fun addFee() {
        if (selectedStudentId.isEmpty() || selectedCourseId.isEmpty() || binding.feePaidET.text.isEmpty()) {
            Toast.makeText(
                this@ActivityAddFee,
                "Please Select Student/Course and fill All Field",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        var paidAmount = binding.feePaidET.text.toString().toInt()

        if (paidAmount>totalFee.toInt())
        {
            Toast.makeText(this@ActivityAddFee, "Amount of Fee Should Less Than Actual Fee", Toast.LENGTH_SHORT).show()
            return
        }
        // For Date
        var day = binding.dateDP.dayOfMonth
        var month = binding.dateDP.month + 1
        var year = binding.dateDP.year
        var date = "$day/$month/$year"


        if (binding.feeTypeSP.selectedItemPosition == 1)
        {
            var selectedItem = binding.feeTypeSP.selectedItem.toString()
            var feeMonth = binding.monthFeeDP.month + 1
            var feeYear = binding.monthFeeDP.year
            feeType = "$feeMonth//$feeYear//$selectedItem"
        }

        var feeList = databaseHelper.getAllFee()
        for (fee in feeList){ if (fee.stdId.equals(selectedStudentId) && fee.courseId.equals(selectedCourseId) && fee.feeType.equals(feeType)){
            Toast.makeText(this@ActivityAddFee, "Your Fee is Already Submitted", Toast.LENGTH_SHORT).show()
            return
        }
        }


        var isFeeInserted = databaseHelper.addFee(selectedStudentId, selectedCourseId, feeType, totalFee, paidAmount.toString(), date, "Active")
        Toast.makeText(this@ActivityAddFee, "$isFeeInserted", Toast.LENGTH_SHORT).show()
        finish()
    }


    fun showStudentRecycler() {
        // For Student
        binding.recyclerStd.adapter = null
        studentList = databaseHelper.getAllStudents()
        chooseStudentAdapter = AdapterChooseStudentForFee(this, studentList)
        binding.recyclerStd.adapter = chooseStudentAdapter
        binding.recyclerStd.layoutManager = LinearLayoutManager(this@ActivityAddFee)
    }
    fun showCourseRecycler() {
        binding.recyclerCourse.adapter = null
        courseList = databaseHelper.getAllCourses()
        chooseCourseAdapter = AdapterChooseCourseForFee(this@ActivityAddFee, courseList)
        binding.recyclerCourse.adapter = chooseCourseAdapter
        binding.recyclerCourse.layoutManager = LinearLayoutManager(this@ActivityAddFee)

    }
    override fun courseInterface(course: Course) {
        selectedCourseId = course.courseId
        binding.containerCourse.visibility = View.GONE
        courseFee = databaseHelper.getCourseFeeById(course.courseId)
        binding.courseNameTV.text = "${course.courseName}"

    }
    override fun onSelectedStudent(student: Student) {
        selectedStudentId = student.stdId
        binding.containerStd.visibility = View.GONE
        binding.stdIdTV.text = "${student.stdId}"
        binding.stdNameTV.text = "${student.stdName}"
        binding.stdFNameTV.text = "${student.stdFatherName}"
    }
    fun searchCourse() {
        binding.searchCourseET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                courseSearchList.clear()
                var searchText = binding.searchCourseET.text.toString()

                if (searchText.length == 0) {
                    showCourseRecycler()
                    binding.noDataFound2.visibility = View.GONE
                    return
                }

                for (course in courseList) {
                    if (course.courseName.contains(searchText, true) || course.courseFee.contains(
                            searchText,
                            true
                        )
                    ) {
                        courseSearchList.add(course)
                        isCourseFound = true
                    }
                }

                if (isCourseFound) {
                    binding.noDataFound2.visibility = View.GONE
                } else {
                    binding.noDataFound2.visibility = View.VISIBLE
                }
                isCourseFound = false

                binding.recyclerCourse.adapter = null
                chooseCourseAdapter = AdapterChooseCourseForFee(this@ActivityAddFee, courseSearchList)
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
                    showStudentRecycler()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (student in studentList) {
                    if (student.stdName.contains(
                            searchText,
                            true
                        ) || student.stdFatherName.contains(searchText, true)
                    ) {
                        studentSearchList.add(student)
                        isStudentFound = true
                    }
                }

                if (isStudentFound) {
                    binding.noDataFound.visibility = View.GONE
                } else {
                    binding.noDataFound.visibility = View.VISIBLE
                }
                isStudentFound = false

                binding.recyclerStd.adapter = null
                chooseStudentAdapter =
                    AdapterChooseStudentForFee(this@ActivityAddFee, studentSearchList)
                binding.recyclerStd.adapter = chooseStudentAdapter
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
}