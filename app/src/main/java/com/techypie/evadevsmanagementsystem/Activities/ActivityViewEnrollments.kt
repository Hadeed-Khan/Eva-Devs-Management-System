package com.techypie.evadevsmanagementsystem.Activities

import android.app.AlertDialog
import android.content.Intent
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
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterEnrollment
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.Models.Enrollment
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityViewEnrollmentsBinding

class ActivityViewEnrollments : AppCompatActivity() {

    lateinit var binding: ActivityViewEnrollmentsBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var enrollmentList: MutableList<Enrollment>
    lateinit var adapterEnrollment: AdapterEnrollment
    lateinit var searchEnrollmentList: MutableList<Enrollment>
    var searchFound = false
    lateinit var courseList : MutableList<Course>
    lateinit var courseNameList : MutableList<String>
    lateinit var filterList : MutableList<Enrollment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewEnrollmentsBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        intializeEveryThing()

        binding.newEnrollmentBT.setOnClickListener {
            startActivity(Intent(this@ActivityViewEnrollments, ActivityAddEnrollment::class.java))
        }
    }

    fun intializeEveryThing() {
        databaseHelper = DatabaseHelper(this@ActivityViewEnrollments)
        courseList = databaseHelper.getAllCourses()
        searchEnrollmentList = ArrayList()
        courseNameList = ArrayList()
        filterList = ArrayList()

        showRecyclerView()

        searchEnrollments()
        filterEnrollments()

        // For Clear Filter
        binding.clearFilter.setOnClickListener {
            showRecyclerView()
            binding.courseFilterSP.setSelection(0)
            binding.filterBox.visibility = View.GONE
        }

        // For BackActivity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
    }

    fun showRecyclerView() {
        binding.recyclerEnrollment.adapter = null
        enrollmentList = databaseHelper.getAllEnrollments()
        adapterEnrollment = AdapterEnrollment(this@ActivityViewEnrollments, enrollmentList)
        binding.recyclerEnrollment.adapter = adapterEnrollment
        binding.recyclerEnrollment.layoutManager = LinearLayoutManager(this@ActivityViewEnrollments)
    }

    fun showDateDialog()
    {
        var builder = AlertDialog.Builder(this@ActivityViewEnrollments)
        var layout = layoutInflater.inflate(R.layout.dialog_date_picker,null)
        builder.setView(layout)
        var alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCancelable(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Declear Views

        var datePicker = layout.findViewById<DatePicker>(R.id.filterDateDP)
        var cancelBT = layout.findViewById<Button>(R.id.cancelDialogBT)
        var applyBT = layout.findViewById<Button>(R.id.selectDateBT)

        cancelBT.setOnClickListener {
            alertDialog.dismiss()
        }

        applyBT.setOnClickListener {

            filterList.clear()
            var day = datePicker.dayOfMonth
            var month = datePicker.month+1
            var year = datePicker.year
            var date = "$day/$month/$year"

            binding.courseFilterSP.setSelection(0)
            binding.recyclerEnrollment.adapter = null


            for (enrollment in enrollmentList)
            {
                if (enrollment.enrollmentDate.equals(date)){
                    filterList.add(enrollment)
                }
            }

            adapterEnrollment = AdapterEnrollment(this@ActivityViewEnrollments,filterList)
            binding.recyclerEnrollment.adapter = adapterEnrollment
            alertDialog.dismiss()
        }

    }

    fun filterEnrollments() {
        binding.showFilterBT.setOnClickListener {
            if (binding.filterBox.visibility == View.GONE) {
                binding.filterBox.visibility = View.VISIBLE
            } else {
                binding.filterBox.visibility = View.GONE
            }
        }

        binding.showDatePickerIV.setOnClickListener {
            showDateDialog()
        }

        for (course in courseList)
        {
            courseNameList.add(course.courseName)
        }
        courseNameList.add(0,"ALL")
        var spinnerAdapter = ArrayAdapter(this@ActivityViewEnrollments,android.R.layout.simple_list_item_1,courseNameList)
        binding.courseFilterSP.adapter = spinnerAdapter


binding.courseFilterSP.onItemSelectedListener = object : OnItemSelectedListener{
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        filterList.clear()
        var selectedItem = binding.courseFilterSP.selectedItem.toString()

        if (selectedItem.equals("ALL")){
            showRecyclerView()
            return
        }

        for (enrollment in enrollmentList){
            if (enrollment.courseName.equals(selectedItem)){
                filterList.add(enrollment)
            }
        }

        binding.recyclerEnrollment.adapter = null
        adapterEnrollment = AdapterEnrollment(this@ActivityViewEnrollments,filterList)
        binding.recyclerEnrollment.adapter = adapterEnrollment

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}



    }

    fun searchEnrollments() {
        binding.searchStudentET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchEnrollmentList.clear()
                var searchText = binding.searchStudentET.text.toString()

                if (searchText.length == 0){
                binding.noDataFound.visibility = View.GONE
                    showRecyclerView()
                    return
                }

                for (enrollment in enrollmentList)
                {
                    if (enrollment.studentName.contains(searchText,true) || enrollment.studentFName.contains(searchText,true)){
                        searchEnrollmentList.add(enrollment)
                        searchFound = true
                    }
                }
                if (searchFound)
                {
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.recyclerEnrollment.adapter = null
                adapterEnrollment = AdapterEnrollment(this@ActivityViewEnrollments,searchEnrollmentList)
                binding.recyclerEnrollment.adapter = adapterEnrollment


                searchFound = false
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    override fun onResume() {
        super.onResume()
        showRecyclerView()
        binding.courseFilterSP.setSelection(0)
        binding.searchStudentET.text.clear()
    }

}