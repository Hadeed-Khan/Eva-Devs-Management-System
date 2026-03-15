package com.techypie.evadevsmanagementsystem.Activities

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Fee
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityDeleteFeeBinding

class ActivityDeleteFee : AppCompatActivity() {

    lateinit var binding : ActivityDeleteFeeBinding
    lateinit var fee: Fee
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeleteFeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fee = intent.getSerializableExtra("Fee") as Fee
        databaseHelper = DatabaseHelper(this)
       intializeEveryThing()

    }

    fun intializeEveryThing()
    {

        binding.showDeleteDialogBT.setOnClickListener {
            showDeleteDialog()
        }
        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
    }

    fun showDeleteDialog()
    {
        var builder = AlertDialog.Builder(this@ActivityDeleteFee)
        var layout = layoutInflater.inflate(R.layout.dialog_delete_fee,null)
        builder.setView(layout)
        var dialogBox = builder.create()
        dialogBox.show()
        dialogBox.setCancelable(false)
        dialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Dialog View Declear

        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogBT)
        var deleteFee = layout.findViewById<Button>(R.id.deleteFeeBT)

        cancelDialog.setOnClickListener {
            dialogBox.dismiss()
        }

        deleteFee.setOnClickListener{
            var isFeeDeleted = databaseHelper.feeSoftDelete(fee.feeId)
            Toast.makeText(this@ActivityDeleteFee, "$isFeeDeleted", Toast.LENGTH_SHORT).show()
            finish()


        }
    }
}
