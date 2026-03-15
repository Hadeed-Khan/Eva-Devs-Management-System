package com.techypie.evadevsmanagementsystem.Activities

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterCourse
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityAddCourseBinding

class ActivityAddCourse : AppCompatActivity() {
    lateinit var binding : ActivityAddCourseBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var courseList : MutableList<Course>
    lateinit var courseAdapter: AdapterCourse
    lateinit var courseSearchList : MutableList<Course>
    lateinit var filterList : MutableList<Course>
    var isDataFound = false
    var isFilterDataFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intializeEveryThing()

    }
    fun intializeEveryThing() {
        databaseHelper = DatabaseHelper(this@ActivityAddCourse)
        courseList = databaseHelper.getAllCourses()
        courseSearchList = ArrayList()
        filterList = ArrayList()

        binding.addCourseIV.setOnClickListener {
            showAddCourseDialog()
        }

        // for Show Recycler View
        showRecycler()
        courseSearch()
        courseFiltering()

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }

    }

    fun courseFiltering()
    {
        binding.feeSortingSP.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

binding.noDataFound.visibility = View.GONE
                var selectedItem = binding.feeSortingSP.selectedItem.toString()
                filterList.clear()
                if (selectedItem == "Default")
                {
                    showRecycler()
                }
                else if(selectedItem == "High To Low"){
                    var sortedList = courseList.sortedByDescending {
                        it.courseFee
                    }
                    binding.courseRecyclerView.adapter = null
                    courseAdapter = AdapterCourse(this@ActivityAddCourse,sortedList.toMutableList())
                    binding.courseRecyclerView.adapter = courseAdapter

                }
                else{
                    var sortedListByA = courseList.sortedBy {
                        it.courseFee
                    }
                    binding.courseRecyclerView.adapter = null
                    courseAdapter = AdapterCourse(this@ActivityAddCourse,sortedListByA.toMutableList())
                    binding.courseRecyclerView.adapter = courseAdapter
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        var durationList = resources.getStringArray(R.array.course_duration).toMutableList()
        durationList.add(0,"ALL")
        var durationSpAdapter = ArrayAdapter(this@ActivityAddCourse,android.R.layout.simple_list_item_1,durationList)
        binding.courseDurationSP.adapter = durationSpAdapter

        binding.courseDurationSP.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


            filterList.clear()
                var selectedItem = binding.courseDurationSP.selectedItem.toString()
                if (selectedItem == "ALL")
                {
                    showRecycler()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (course in courseList)
                {
                    if (course.courseDuration.equals(selectedItem,true)){
                        filterList.add(course)
                        isFilterDataFound = true
                    }
                }

                if (isFilterDataFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.courseRecyclerView.adapter = null
                courseAdapter = AdapterCourse(this@ActivityAddCourse,filterList)
                binding.courseRecyclerView.adapter = courseAdapter
                isFilterDataFound = false

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    fun courseSearch() {
        binding.searchCourseET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                courseSearchList.clear()
                var searchText = binding.searchCourseET.text.toString()
                if (searchText.length<2)
                {
                    binding.noDataFound.visibility = View.GONE
                    showRecycler()
                    return
                }



                for (course in courseList)
                {
                    if (course.courseName.contains(searchText,true)  || course.courseFee.contains(searchText,true)){
                        courseSearchList.add(course)
                        isDataFound = true
                    }
                }

                if (isDataFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.courseRecyclerView.adapter = null
                courseAdapter = AdapterCourse(this@ActivityAddCourse,courseSearchList)
                binding.courseRecyclerView.adapter = courseAdapter
                isDataFound = false

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }
    fun showRecycler()
    {
        binding.courseRecyclerView.adapter = null
        courseList = databaseHelper.getAllCourses()
        courseAdapter = AdapterCourse(this@ActivityAddCourse,courseList)
        binding.courseRecyclerView.adapter = courseAdapter
        binding.courseRecyclerView.layoutManager = LinearLayoutManager(this@ActivityAddCourse)
    }
    fun showAddCourseDialog() {
        var builder = AlertDialog.Builder(this@ActivityAddCourse)
        var layout = layoutInflater.inflate(R.layout.dialog_add_course,null)
        builder.setView(layout)
        var dialogBox = builder.create()
        dialogBox.show()
        dialogBox.setCancelable(false)
        dialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Declear Dialog Box View
        var courseNameET = layout.findViewById<EditText>(R.id.courseNameET)
        var courseFeeET = layout.findViewById<EditText>(R.id.courseFeeET)
        var courseDurationSP = layout.findViewById<Spinner>(R.id.courseDurationSP)
        var addCourse = layout.findViewById<Button>(R.id.addCourseBT)
        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogCBT)

        // for Remove Dialog Box
        cancelDialog.setOnClickListener {
            dialogBox.dismiss()
        }

        // For Add Course into Database
        addCourse.setOnClickListener {
            if (courseNameET.text.isEmpty() || courseFeeET.text.isEmpty())
            {
                Toast.makeText(this@ActivityAddCourse, "Please fill all field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var courseName = courseNameET.text.toString().trim()
            var courseDuration = courseDurationSP.selectedItem.toString()
            var courseFee = courseFeeET.text.toString().trim()

            for (course in courseList){
                if (course.courseName.equals(courseName,true)){
                    Toast.makeText(this@ActivityAddCourse, "This Course is Already Register", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            var isCourseAdd = databaseHelper.addCourse(courseName,courseDuration,courseFee,"Active")
            Toast.makeText(this@ActivityAddCourse, "$isCourseAdd", Toast.LENGTH_SHORT).show()
            binding.courseDurationSP.setSelection(0)
            binding.searchCourseET.text.clear()
            binding.feeSortingSP.setSelection(0)
            var lastCourse = databaseHelper.getLastCourse()
            courseList.add(lastCourse)
            courseAdapter.notifyItemInserted(courseList.size-1)

            dialogBox.dismiss()
        }

    }
    override fun onResume() {
        super.onResume()
        binding.courseDurationSP.setSelection(0)
        binding.searchCourseET.text.clear()
        binding.feeSortingSP.setSelection(0)
        showRecycler()
    }
}
