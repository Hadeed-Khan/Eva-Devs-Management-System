package com.techypie.evadevsmanagementsystem.Activities

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.techypie.evadevsmanagementsystem.Models.Enrollment
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityUpdateEnrollmentBinding

class ActivityUpdateEnrollment : AppCompatActivity() {
    // Globals Variables
    lateinit var binding: ActivityUpdateEnrollmentBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var enrollment : Enrollment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enrollment = intent.getSerializableExtra("Enrollment") as Enrollment
        intializeEveryThing()

    }

    fun intializeEveryThing()
    {
        databaseHelper = DatabaseHelper(this@ActivityUpdateEnrollment)
        // for Soft Delete
        binding.deleteDialogBT.setOnClickListener {
            showDelteDialog()
        }

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
    }

    fun showDelteDialog()
    {
        var builder = AlertDialog.Builder(this@ActivityUpdateEnrollment)
        var layout = layoutInflater.inflate(R.layout.dialog_delete_enrollment,null)
        builder.setView(layout)
        var dialogBox = builder.create()
        dialogBox.show()
        dialogBox.setCancelable(false)
        dialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Dialog View
        var canceldialog = layout.findViewById<Button>(R.id.cancelDialogBT)
        var deleteEnrollmentBT = layout.findViewById<Button>(R.id.deleteEnrollmentBT)

        canceldialog.setOnClickListener {
            dialogBox.dismiss()
        }

        deleteEnrollmentBT.setOnClickListener {
            deleteEnrollment()
        }
    }

    fun deleteEnrollment() {
        var isEnrollmentDeleted = databaseHelper.enrollmentSoftDelete(enrollment.enrollmentId)
        Toast.makeText(this@ActivityUpdateEnrollment, "$isEnrollmentDeleted", Toast.LENGTH_SHORT).show()
        finish()
    }

}