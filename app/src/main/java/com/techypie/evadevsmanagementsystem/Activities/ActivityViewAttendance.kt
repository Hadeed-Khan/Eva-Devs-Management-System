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
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterAttendance
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Attendance
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityViewAttendanceBinding
import android.text.TextWatcher as TextWatcher1

class ActivityViewAttendance : AppCompatActivity() {

    lateinit var binding : ActivityViewAttendanceBinding
    lateinit var course : Course
    lateinit var databaseHelper: DatabaseHelper
    lateinit var attendanceList: MutableList<Attendance>
    lateinit var adapter : AdapterAttendance
    lateinit var searchList : MutableList<Attendance>
    lateinit var filterList : MutableList<Attendance>
    var searchFound = false
    var filterFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        course = intent.getSerializableExtra("Course") as Course

        intializeEveryThing()


    }

    fun intializeEveryThing()
    {
        databaseHelper = DatabaseHelper(this@ActivityViewAttendance)
        searchList = ArrayList()
        filterList = ArrayList()
        binding.newAttendanceBT.setOnClickListener {
            startActivity(Intent(this@ActivityViewAttendance,ActivityMarkAttendance::class.java)
                .putExtra("Course",course))
        }
        showRecyclerView()

        searchAttendance()
        filterAttendance()

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }

    }

    fun searchAttendance() {
       binding.searchAttendanceET.addTextChangedListener(object : TextWatcher{
           override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

           }

           override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               searchList.clear()
               var searchText = binding.searchAttendanceET.text.toString()
               if (searchText.length == 0){
                   showRecyclerView()
                   binding.noDataFound.visibility = View.GONE
                   return
               }

               for (attendance in attendanceList)
               {
                   if (attendance.stdName.contains(searchText,true)|| attendance.stdId.contains(searchText,true)){
                       searchList.add(attendance)
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


               binding.attendanceRecycler.adapter = null
               adapter = AdapterAttendance(this@ActivityViewAttendance,searchList)
               binding.attendanceRecycler.adapter = adapter
               searchFound = false
           }

           override fun afterTextChanged(p0: Editable?) {

           }

       })
    }

    fun filterAttendance()
    {
        binding.showFilterBox.setOnClickListener {
            if (binding.filterBox.visibility == View.GONE)
            {
                binding.filterBox.visibility = View.VISIBLE
            }
            else{
                binding.filterBox.visibility = View.GONE
            }
        }

        // Status wise Filter
        binding.statusFilterSP.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                filterList.clear()

                var selectedItem = binding.statusFilterSP.selectedItem.toString()
                if (selectedItem == "ALL"){
                    binding.noDataFound.visibility = View.GONE
                    showRecyclerView()
                    return
                }

                for (attendance in attendanceList){
                    if (attendance.status.equals(selectedItem)){
                        filterList.add(attendance)
                        filterFound = true
                    }
                }

                if (filterFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.attendanceRecycler.adapter = null
                adapter = AdapterAttendance(this@ActivityViewAttendance,filterList)
                binding.attendanceRecycler.adapter = adapter
                filterFound = false
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        // Date Wise
        binding.showDateDialog.setOnClickListener {
            showDateDialog()
        }

        // For Clear Filter
        binding.clearFilter.setOnClickListener {
            clearFilter()
        }

    }
    fun clearFilter()
    {
        binding.statusFilterSP.setSelection(0)
        binding.filterBox.visibility = View.GONE
        binding.noDataFound.visibility = View.GONE
        showRecyclerView()
    }

    fun showDateDialog() {
        var builder = AlertDialog.Builder(this@ActivityViewAttendance)
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

            binding.statusFilterSP.setSelection(0)
            binding.attendanceRecycler.adapter = null


            for (attendance in attendanceList)
            {
                if (attendance.date.equals(date)){
                    filterList.add(attendance)
                    filterFound = true
                }
            }

            if (filterFound){
                binding.noDataFound.visibility = View.GONE
            }
            else{
                binding.noDataFound.visibility = View.VISIBLE
            }

            adapter = AdapterAttendance(this@ActivityViewAttendance,filterList)
            binding.attendanceRecycler.adapter = adapter
            alertDialog.dismiss()
            filterFound = false
        }

    }


    fun showRecyclerView() {
        binding.attendanceRecycler.adapter = null
        attendanceList = databaseHelper.getCourseAttendacne(course.courseId)
        adapter = AdapterAttendance(this@ActivityViewAttendance,attendanceList)
        binding.attendanceRecycler.adapter = adapter
        binding.attendanceRecycler.layoutManager = LinearLayoutManager(this@ActivityViewAttendance)
    }

    override fun onResume() {
        super.onResume()
        showRecyclerView()
        clearFilter()
    }

}
