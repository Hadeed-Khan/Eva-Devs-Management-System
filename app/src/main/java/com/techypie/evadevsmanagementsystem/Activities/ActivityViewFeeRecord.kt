
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.contains
import androidx.recyclerview.widget.LinearLayoutManager
import com.techypie.evadevsmanagementsystem.Adapters.AdapterFee
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Fee
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityViewFeeRecordBinding

class ActivityViewFeeRecord : AppCompatActivity() {

    lateinit var binding : ActivityViewFeeRecordBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var feeList : MutableList<Fee>
    lateinit var feeAdapter : AdapterFee
    lateinit var feeSearchList : MutableList<Fee>
     lateinit var filterList : MutableList<Fee>
    var feeMonth = ""
   var searchFound = false
    var filterPaymentDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewFeeRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intializeEveryThing()

    }

    fun checkFeeType()
    {
        binding.feeTypeSP.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                var postion = binding.feeTypeSP.selectedItemPosition

                if (postion==1){
                    binding.feeMonthText.visibility = View.GONE
                    binding.showDateDialogIV.visibility = View.GONE
                    binding.montlyFeeTV.visibility = View.GONE
                }
                else{
                    binding.feeMonthText.visibility = View.VISIBLE
                    binding.showDateDialogIV.visibility = View.VISIBLE
                    binding.montlyFeeTV.visibility = View.VISIBLE
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    fun intializeEveryThing()
    {
        databaseHelper = DatabaseHelper(this@ActivityViewFeeRecord)
        feeSearchList = ArrayList()
        filterList = ArrayList()
        binding.newFeeBT.setOnClickListener {
            startActivity(Intent(this@ActivityViewFeeRecord,ActivityAddFee::class.java))
        }

        showFeeRecyclerView()
        searchFee()
        filterFee()

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
        checkFeeType()
    }

    fun filterFee()
    {

        binding.showFilterBoxIV.setOnClickListener {
            if (binding.filterBox.visibility == View.GONE){
                binding.filterBox.visibility = View.VISIBLE
            }
            else{
                binding.filterBox.visibility = View.GONE
            }
        }

        // For Clear Filter
        binding.clearFilterBT.setOnClickListener {
            clearFilter()
        }

            // Add Spinner List
        var feeTypeList = resources.getStringArray(R.array.fee_type).toMutableList()
        feeTypeList.add(0,"ALL")

        var feeTypeSpinnerAdapter = ArrayAdapter(this@ActivityViewFeeRecord,android.R.layout.simple_list_item_1,feeTypeList)
        binding.feeTypeSP.adapter = feeTypeSpinnerAdapter


        // Show Dialog
        binding.showDateDialogIV.setOnClickListener {
            showDateDialogForFeeMonth()
        }

        binding.showPaymentDateDialog.setOnClickListener {
            showPaymentDateDialog()
        }


        // For Apply Filter
        binding.applyFilterBT.setOnClickListener {
            filterList.clear()
            var selectedFeeType = binding.feeTypeSP.selectedItem.toString()
            var selectedFeeStatus = binding.statusSP.selectedItem.toString()

            if (binding.statusSP.selectedItemPosition == 0 && binding.feeTypeSP.selectedItemPosition == 0 && feeMonth.isEmpty() && filterPaymentDate.isEmpty()){
                showFeeRecyclerView()
                return@setOnClickListener
            }
            var monthlyFee = ""
            for (fee in feeList){

                if (fee.feeType != "Admission Fee"){
                    var parts = fee.feeType.split("//").toMutableList()
                    var month = parts[0].toInt()
                    var year = parts[1].toInt()
                   monthlyFee = "$month/$year"
                }

                if (fee.feeType.contains(selectedFeeType) && binding.statusSP.selectedItemPosition == 0 && feeMonth.isEmpty() && filterPaymentDate.isEmpty()){
                    filterList.add(fee)
                }

               else if (selectedFeeStatus == "Full Paid" && binding.feeTypeSP.selectedItemPosition == 0 && feeMonth.isEmpty() && fee.paidFee.equals(fee.totalFee) && filterPaymentDate.isEmpty()){
                       filterList.add(fee)
                }

                else if (selectedFeeStatus == "UnPaid" && binding.feeTypeSP.selectedItemPosition == 0 && feeMonth.isEmpty() && fee.paidFee!=fee.totalFee && filterPaymentDate.isEmpty()){
                    filterList.add(fee)
                }

                else if (binding.statusSP.selectedItemPosition == 0 && binding.feeTypeSP.selectedItemPosition == 0 && monthlyFee.equals(feeMonth) && filterPaymentDate.isEmpty() && !(fee.feeType.equals("Admission Fee"))){
                        filterList.add(fee)
                }

                else if (binding.statusSP.selectedItemPosition == 0 && binding.feeTypeSP.selectedItemPosition == 0  && fee.paymentData.equals(filterPaymentDate) && feeMonth.isEmpty()){
                    filterList.add(fee)

                }

                // For Multiple Filter
                else if (selectedFeeStatus == "Full Paid" && fee.feeType.contains(selectedFeeStatus) && fee.paidFee.equals(fee.totalFee) && feeMonth.isEmpty() && filterPaymentDate.isEmpty()){
                    filterList.add(fee)
                }

                else if (selectedFeeStatus == "UnPaid" && fee.feeType.contains(selectedFeeType) && (fee.paidFee!=fee.totalFee) && feeMonth.isEmpty() && filterPaymentDate.isEmpty()) {
                    filterList.add(fee)
                }

                else if (fee.feeType.contains(selectedFeeType) && binding.statusSP.selectedItemPosition == 0 && fee.paymentData.contains(filterPaymentDate) && feeMonth.isEmpty()){
                    filterList.add(fee)
                }

                else if (binding.feeTypeSP.selectedItemPosition == 0 && selectedFeeStatus == "Full Paid" && (fee.paidFee==fee.totalFee) && fee.paymentData.contains(filterPaymentDate) && feeMonth.isEmpty()){
                    filterList.add(fee)

                }

                else if (binding.feeTypeSP.selectedItemPosition == 0 && selectedFeeStatus == "UnPaid" && (fee.paidFee!=fee.totalFee) && fee.paymentData.contains(filterPaymentDate) && feeMonth.isEmpty()){
                    filterList.add(fee)
                }
                else if (fee.feeType.equals(selectedFeeType) && selectedFeeStatus == "Full Paid" && (fee.paidFee==fee.totalFee) && fee.paymentData.contains(filterPaymentDate) && feeMonth.isEmpty()){
                    filterList.add(fee)
                }
                else if (fee.feeType.equals(selectedFeeType) && selectedFeeStatus == "UnPaid" && (fee.paidFee!=fee.totalFee) && fee.paymentData.contains(filterPaymentDate) && feeMonth.isEmpty()){
                    filterList.add(fee)
                }

                else if (binding.feeTypeSP.selectedItemPosition == 0 && selectedFeeStatus == "Full Paid" && (fee.paidFee==fee.totalFee) && monthlyFee.contains(feeMonth) && filterPaymentDate.isEmpty()){
                    filterList.add(fee)
                }

                else if (binding.feeTypeSP.selectedItemPosition == 0 && selectedFeeStatus == "UnPaid" && (fee.paidFee!=fee.totalFee) && monthlyFee.contains(feeMonth) && filterPaymentDate.isEmpty()){
                    filterList.add(fee)
                }

                else if (binding.feeTypeSP.selectedItemPosition == 0 && binding.statusSP.selectedItemPosition == 0 && monthlyFee.equals(feeMonth) && fee.paymentData.equals(filterPaymentDate)){
                    filterList.add(fee)
                }
                else if(binding.feeTypeSP.selectedItemPosition == 0 && selectedFeeStatus == "Full Paid" && (fee.paidFee==fee.totalFee) && monthlyFee.equals(feeMonth) && fee.paymentData.equals(filterPaymentDate)){
                    filterList.add(fee)
                }
                else if(binding.feeTypeSP.selectedItemPosition == 0 && selectedFeeStatus == "UnPaid" && (fee.paidFee!=fee.totalFee) && monthlyFee.equals(feeMonth) && fee.paymentData.equals(filterPaymentDate)){
                    filterList.add(fee)
                }

                else if (selectedFeeType!= "Admission Fee"  && selectedFeeStatus == "Full Paid" && (fee.paidFee==fee.totalFee) && monthlyFee.equals(feeMonth) && filterPaymentDate.isEmpty()){
                    filterList.add(fee)
                }
                else if (binding.feeTypeSP.selectedItemPosition==2 && selectedFeeStatus == "UnPaid" && (fee.paidFee!=fee.totalFee) && monthlyFee.equals(feeMonth) && filterPaymentDate.isEmpty()){
                    filterList.add(fee)
                }

                else if (binding.feeTypeSP.selectedItemPosition==2 && selectedFeeStatus == "Full Paid" && (fee.paidFee==fee.totalFee) && monthlyFee.equals(feeMonth) && fee.paymentData.equals(filterPaymentDate)){
                    filterList.add(fee)
                }
                else if (binding.feeTypeSP.selectedItemPosition==2 && selectedFeeStatus == "UnPaid" && (fee.paidFee!=fee.totalFee) && monthlyFee.equals(feeMonth) && fee.paymentData.equals(filterPaymentDate)){
                    filterList.add(fee)
                }
                else if(binding.feeTypeSP.selectedItemPosition == 2 && selectedFeeStatus == "Full Paid" && fee.paidFee==fee.totalFee && feeMonth.isEmpty() && filterPaymentDate.isEmpty()){
                    filterList.add(fee)
                }
            }



            feeMonth = ""
            filterPaymentDate = ""

            binding.feeRecycler.adapter = null
            feeAdapter = AdapterFee(this@ActivityViewFeeRecord,filterList)
            binding.feeRecycler.adapter = feeAdapter
            binding.applyFilterBT.isEnabled = false
            binding.applyFilterBT.background.alpha= 60

        }

    }

    fun clearFilter()
    {
        binding.statusSP.setSelection(0)
        binding.feeTypeSP.setSelection(0)
        feeMonth = ""
        filterPaymentDate = ""
        binding.noDataFound.visibility = View.GONE
        showFeeRecyclerView()
        binding.montlyFeeTV.text = ""
        binding.paymentDateTV.text = ""
        binding.applyFilterBT.isEnabled = true
        binding.applyFilterBT.background.alpha= 255
    }

    fun showPaymentDateDialog()
    {
        var builder = AlertDialog.Builder(this@ActivityViewFeeRecord)
        var layout = layoutInflater.inflate(R.layout.dialog_date_picker,null)
        builder.setView(layout)
        var alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Declear View
        var dateLebel = layout.findViewById<TextView>(R.id.dateLebel)
        var datePicker = layout.findViewById<DatePicker>(R.id.filterDateDP)
        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogBT)
        var selectButton = layout.findViewById<Button>(R.id.selectDateBT)

        dateLebel.text = "Select Payment Date"
        selectButton.text = "Select"

        cancelDialog.setOnClickListener {
            alertDialog.dismiss()
        }

        selectButton.setOnClickListener {
            var day = datePicker.dayOfMonth
            var month = datePicker.month+1
            var year = datePicker.year
            filterPaymentDate = "$day/$month/$year"

            binding.paymentDateTV.text = "Payment Date: $filterPaymentDate"
            alertDialog.dismiss()
        }



    }

    fun showDateDialogForFeeMonth()
    {
        var builder = AlertDialog.Builder(this@ActivityViewFeeRecord)
        var layout = layoutInflater.inflate(R.layout.dialog_date_picker,null)
        builder.setView(layout)
        var alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Declear View
        var dateLebel = layout.findViewById<TextView>(R.id.dateLebel)
        var datePicker = layout.findViewById<DatePicker>(R.id.filterDateDP)
        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogBT)
        var selectButton = layout.findViewById<Button>(R.id.selectDateBT)

        dateLebel.text = "Select Fee Month"
        selectButton.text = "Select"

        cancelDialog.setOnClickListener {
            alertDialog.dismiss()
        }

        selectButton.setOnClickListener {
            var month = datePicker.month+1
            var year = datePicker.year
            feeMonth = "$month/$year"

            binding.montlyFeeTV.text = "Selected Month: $feeMonth"
            alertDialog.dismiss()
        }



    }
    fun searchFee() {
        binding.searchFeeET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                feeSearchList.clear()
                var searchText = binding.searchFeeET.text.toString()

                if (searchText.length  ==0)
                {
                    showFeeRecyclerView()
                    binding.noDataFound.visibility = View.GONE
                    return
                }

                for (fee in feeList){
                   if (fee.stdName.contains(searchText,true) || fee.courseName.contains(searchText,true)){
                       feeSearchList.add(fee)
                       searchFound = true
                   }
                }

                if (searchFound){
                    binding.noDataFound.visibility = View.GONE
                }
                else{
                    binding.noDataFound.visibility = View.VISIBLE
                }

                binding.feeRecycler.adapter = null
                feeAdapter = AdapterFee(this@ActivityViewFeeRecord,feeSearchList)
                binding.feeRecycler.adapter = feeAdapter

                searchFound = false
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
    fun showFeeRecyclerView()
    {
        binding.feeRecycler.adapter = null
        feeList = databaseHelper.getAllFee()
        feeAdapter = AdapterFee(this@ActivityViewFeeRecord,feeList)
        binding.feeRecycler.adapter = feeAdapter
        binding.feeRecycler.layoutManager = LinearLayoutManager(this@ActivityViewFeeRecord)
    }

    override fun onResume() {
        super.onResume()
        clearFilter()
        showFeeRecyclerView()

    }

}