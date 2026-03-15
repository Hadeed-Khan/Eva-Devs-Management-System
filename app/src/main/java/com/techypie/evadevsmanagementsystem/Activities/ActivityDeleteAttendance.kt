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
import com.techypie.evadevsmanagementsystem.Models.Attendance
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityDeleteAttendanceBinding

class ActivityDeleteAttendance : AppCompatActivity() {

    lateinit var binding : ActivityDeleteAttendanceBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var attendance: Attendance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDeleteAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attendance = intent.getSerializableExtra("Attendance") as Attendance
        intializeEveryThing()

    }

    fun intializeEveryThing()
    {
        databaseHelper = DatabaseHelper(this@ActivityDeleteAttendance)
        binding.showDeleteDialogBT.setOnClickListener {
            showDeleteDialog()
        }

        // for Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
    }

    fun showDeleteDialog()
    {
        var builder = AlertDialog.Builder(this@ActivityDeleteAttendance)
        var layout = layoutInflater.inflate(R.layout.dialog_delete_attendance,null)
        builder.setView(layout)
        var dialogBox = builder.create()
        dialogBox.show()
        dialogBox.setCancelable(false)
        dialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Dialog View Declear

        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogBT)
        var deleteFee = layout.findViewById<Button>(R.id.deleteAttendanceBT)

        cancelDialog.setOnClickListener {
            dialogBox.dismiss()
        }

        deleteFee.setOnClickListener{

            var isAttendanceDeleted = databaseHelper.softDeleteAttendance(attendance.attendanceId)
            Toast.makeText(this@ActivityDeleteAttendance, "$isAttendanceDeleted", Toast.LENGTH_SHORT).show()
            finish()
        }


    }
}