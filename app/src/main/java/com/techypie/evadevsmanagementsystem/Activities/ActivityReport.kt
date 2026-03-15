package com.techypie.evadevsmanagementsystem.Activities

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter.AdapterCourseCourseReport
import com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter.AdapterSelectedCourseAttendanceReport
import com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter.AdapterSelectedStudentAttendanceReport
import com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter.AdapterStudentFeeHistory
import com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter.AdapterStudentFeeList
import com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter.AdapterPopularCourse
import com.techypie.evadevsmanagementsystem.DatabaseHelper
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.Models.CourseEnrollment
import com.techypie.evadevsmanagementsystem.Models.Fee
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityReportBinding
import kotlin.time.times

open class ActivityReport : AppCompatActivity(), AdapterStudentFeeList.StudentInterface, AdapterSelectedStudentAttendanceReport.StudentInterfaceAttendance, AdapterSelectedCourseAttendanceReport.CourseInterfaceAttendance,
    AdapterCourseCourseReport.CourseInterfaceCReport {
    // Global Variable
    lateinit var binding: ActivityReportBinding
    lateinit var databaseHelper: DatabaseHelper
    lateinit var studentList: MutableList<Student>
    lateinit var courseList: MutableList<Course>
    lateinit var studentAdapter: AdapterStudentFeeList
    lateinit var totalPaidFeeList: MutableList<String>
    lateinit var totalFeeList: MutableList<String>
    var selectedStudentId = ""
    var selectedStudentName = ""
    var selectedStudentIdAttendance = ""
    lateinit var feeHistoryList: MutableList<Fee>
    lateinit var feeHistoryAdapter: AdapterStudentFeeHistory
    var selectedCourseId = ""
    lateinit var courseReport : CourseReport


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intializeEveryThing()

    }

    fun intializeEveryThing() {
        databaseHelper = DatabaseHelper(this@ActivityReport)
        courseList = databaseHelper.getAllCourses()
        courseReport = CourseReport(this@ActivityReport,binding,databaseHelper,courseList)


        showBasicReport()
        showFeeReport()
        showAttendanceReport()
        courseReport.showCourseReport()

        // For Back Activity
        binding.backActivityIV.setOnClickListener {
            finish()
        }
        
    }
    fun showAttendanceReport() {
        binding.AttendanceReportBT.setOnClickListener {
            binding.basicReportCard.visibility = View.GONE
            binding.feeReportCard.visibility = View.GONE
            binding.courseReportCard.visibility = View.GONE
            binding.attendanceReportCard.visibility = View.VISIBLE

        }

        var totalAttendance = databaseHelper.getTotalAttendance()
        var totalPrsentAttendance = databaseHelper.getTotalPrsentAttendance()
        var totalAbsentAttendance = databaseHelper.getTotalAbsentAttendance()
        var totalLeaveAttendance = databaseHelper.getTotalLeaveAttendance()

        // For  Percentage
        var presentPercentage = 0.00
        var absentPercentage = 0.00
        var leavePercentage = 0.00

        if (totalAttendance > 0) {
            presentPercentage = ((totalPrsentAttendance.toFloat() / totalAttendance.toFloat()) *100).toDouble()
            absentPercentage = ((totalAbsentAttendance.toFloat()  / totalAttendance.toFloat()) *100).toDouble()
            leavePercentage = ((totalLeaveAttendance.toFloat()  / totalAttendance.toFloat()) *100).toDouble()
        }

        binding.totalAttendanceTV.text = "$totalAttendance"
        binding.totalPresentTV.text = "$totalPrsentAttendance"
        binding.totalAbsentTV.text = "$totalAbsentAttendance"
        binding.totalLeaveTV.text = "$totalLeaveAttendance"
        binding.presentPercentageTV.text = "$presentPercentage"
        binding.totalAbsentPercentageTV.text = "$absentPercentage"
        binding.leavePercentageTV.text = "$leavePercentage"

        showStudentRecyclerAttendance()
        showCourseRecyclerAttendance()
    }
    fun showFeeReport() {

        var totalPaidAmount = 0
        var totalFee = 0
        var totalPaidStudent = databaseHelper.getAllPaidStudent()
        var totalUnpaidStudent = databaseHelper.getAllUnPaidStudents()
        totalPaidFeeList = databaseHelper.getTotalPaidFee()
        totalFeeList = databaseHelper.getAllTotalFee()

        binding.feeReportBT.setOnClickListener {
            binding.basicReportCard.visibility = View.GONE
            binding.attendanceReportCard.visibility = View.GONE
            binding.courseReportCard.visibility = View.GONE
            binding.feeReportCard.visibility = View.VISIBLE

        }

        for (totalPaidFee in totalPaidFeeList) {
            totalPaidAmount += totalPaidFee.toInt()
        }


        for (singleTotalFee in totalFeeList) {
            totalFee += singleTotalFee.toInt()
        }

        binding.totalPaidStudentTV.text = "$totalPaidStudent"
        binding.totalUnpaidStudentTV.text = "$totalUnpaidStudent"
        binding.totalFeeTV.text = "Rs $totalPaidAmount"

        var remainingFee = totalFee - totalPaidAmount
        if (remainingFee < 0) {
            binding.totalRemainingTV.text = "Rs 0"
        } else {
            binding.totalRemainingTV.text = "Rs $remainingFee"
        }
        showStudentListRecycler()


    }
    fun showStudentListRecycler() {
        studentList = databaseHelper.getAllStudents()
        studentAdapter = AdapterStudentFeeList(this@ActivityReport, studentList)
        binding.feeHistoryRecycler.adapter = studentAdapter
        binding.feeHistoryRecycler.layoutManager = LinearLayoutManager(this@ActivityReport)
    }
    fun showBasicReport() {
        var totalStudentCount = 0
        var totalMaleStudent = 0
        var totalFemaleStudent = 0
        var totalCourse = 0
        var totalEnrollment = 0

        // For Show Basic Report Card
        binding.basicReportBT.setOnClickListener {
            binding.feeReportCard.visibility = View.GONE
            binding.attendanceReportCard.visibility = View.GONE
            binding.courseReportCard.visibility = View.GONE
            binding.basicReportCard.visibility = View.VISIBLE
        }


        totalStudentCount = databaseHelper.getTotalStudent()
        totalMaleStudent = databaseHelper.getMaleStudents()
        totalFemaleStudent = databaseHelper.getFemaleStudents()
        totalCourse = databaseHelper.getTotalCourse()
        totalEnrollment = databaseHelper.getTotalEnrollment()

        // For Basic Report Show
        binding.totalStudentTV.text = "$totalStudentCount"
        binding.totalMaleStudentTV.text = "$totalMaleStudent"
        binding.totalFemaleStudentTV.text = "$totalFemaleStudent"
        binding.totalCourseTV.text = "$totalCourse"
        binding.totalEnrollmentTV.text = "$totalEnrollment"
    }
    override fun onSelectedStudent(student: Student) {
        selectedStudentId = student.stdId
        showHistoryDialog()
    }
    fun showHistoryDialog() {
        var builder = AlertDialog.Builder(this@ActivityReport)
        var layout = layoutInflater.inflate(R.layout.dialog_history_fee, null)
        builder.setView(layout)
        var dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // View Declaear

        var cancelDialog = layout.findViewById<Button>(R.id.cancelDialogBT)

        var recyclerView = layout.findViewById<RecyclerView>(R.id.feeHistoryRecycler)
        cancelDialog.setOnClickListener {
            dialog.dismiss()
        }

        feeHistoryList = databaseHelper.getStudentFeeHistory(selectedStudentId.toInt())
        feeHistoryAdapter = AdapterStudentFeeHistory(this@ActivityReport, feeHistoryList)
        recyclerView.adapter = feeHistoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(this@ActivityReport)
    }
    fun showStudentRecyclerAttendance() {
        studentList = databaseHelper.getAllStudents()
        var studentAdapterAttendance =
            AdapterSelectedStudentAttendanceReport(this@ActivityReport, studentList)
        binding.studentAttendanceRecycler.adapter = studentAdapterAttendance
        binding.studentAttendanceRecycler.layoutManager = LinearLayoutManager(this@ActivityReport)
    }
    override fun onSelectedStudentAttendance(student: Student) {
        selectedStudentIdAttendance = student.stdId
        selectedStudentName = student.stdName
        showAttendanceDialog()
    }
    fun showAttendanceDialog() {
        var builder = AlertDialog.Builder(this@ActivityReport)
        var layout = layoutInflater.inflate(R.layout.dialog_student_attendance_percentage, null)
        builder.setView(layout)
        var dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Declear Dialog View
        var cancelDialogBT = layout.findViewById<Button>(R.id.cancelDialogBT)
        var studentNameAttendance = layout.findViewById<TextView>(R.id.studentNameAttendance)
        var studentTotalAttendance = layout.findViewById<TextView>(R.id.totalAttendanceTV)
        var studentPresentAttendance = layout.findViewById<TextView>(R.id.totalPresentTV)
        var studentAbsentAttendance = layout.findViewById<TextView>(R.id.totalAbsentTV)
        var studentLeaveAttendance = layout.findViewById<TextView>(R.id.totalLeaveTV)
        var studentPresentPercent = layout.findViewById<TextView>(R.id.presentPercentageTV)
        var studentAbsentPercent = layout.findViewById<TextView>(R.id.totalAbsentPercentageTV)
        var studentleavePercent = layout.findViewById<TextView>(R.id.leavePercentageTV)

        // Databasa Functions

        var stdTotalAttendance =
            databaseHelper.getStudentTotalAttendance(selectedStudentIdAttendance.toInt())
        var stdPresentAttendance =
            databaseHelper.getStudentPresentAttendance(selectedStudentIdAttendance.toInt())
        var stdAbsentAttendance =
            databaseHelper.getStudentAbsentAttendance(selectedStudentIdAttendance.toInt())
        var stdLeaveAttendance =
            databaseHelper.getStudentLeaveAttendance(selectedStudentIdAttendance.toInt())

        cancelDialogBT.setOnClickListener {
            dialog.dismiss()
        }

        // For Single Student Percentage
        var stdPresentPercentage = 0.0
        var stdAbsentPercentage = 0.0
        var stdLeavePercentage = 0.0

        if (stdTotalAttendance > 0) {
            stdPresentPercentage = ((stdPresentAttendance.toFloat() / stdTotalAttendance.toFloat())*100).toDouble()
            stdAbsentPercentage = ((stdAbsentAttendance.toFloat()  / stdTotalAttendance.toFloat())*100).toDouble()
            stdLeavePercentage = ((stdLeaveAttendance.toFloat() / stdTotalAttendance.toFloat())*100).toDouble()
        }

        // Show On Dialog
        studentNameAttendance.text = "$selectedStudentName Attendance Record"
        studentTotalAttendance.text = "$stdTotalAttendance"
        studentPresentAttendance.text = "$stdPresentAttendance"
        studentAbsentAttendance.text = "$stdAbsentAttendance"
        studentLeaveAttendance.text = "$stdLeaveAttendance"
        studentPresentPercent.text = "$stdPresentPercentage"
        studentAbsentPercent.text = "$stdAbsentPercentage"
        studentleavePercent.text = "$stdLeavePercentage"
    }
    override fun onSelectedCourseAttendance(course: Course) {
        selectedCourseId = course.courseId
        showCourseAttendanceDialog(course.courseName)
    }
    fun showCourseAttendanceDialog(courseName: String) {
        var builder = AlertDialog.Builder(this@ActivityReport)
        var layout = layoutInflater.inflate(R.layout.dialog_course_attendance_percentage, null)
        builder.setView(layout)
        var dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // Declear View For Dialog
        var cancelDialogBT = layout.findViewById<Button>(R.id.cancelDialogBT)
        var courseNameTV = layout.findViewById<TextView>(R.id.courseNameAttendance)
        var courseTotalAttendance = layout.findViewById<TextView>(R.id.totalAttendanceTV)
        var coursetPresentAttendance = layout.findViewById<TextView>(R.id.totalPresentTV)
        var courseAbsentAttendance = layout.findViewById<TextView>(R.id.totalAbsentTV)
        var courseLeaveAttendance = layout.findViewById<TextView>(R.id.totalLeaveTV)
        var coursePresentPercent = layout.findViewById<TextView>(R.id.presentPercentageTV)
        var courseAbsentPercent = layout.findViewById<TextView>(R.id.totalAbsentPercentageTV)
        var courseleavePercent = layout.findViewById<TextView>(R.id.leavePercentageTV)


        // DataBasa Function
        var totalCourseAttendance =
            databaseHelper.getTotalCourseAttendance(selectedCourseId.toInt())
        var presentCourseA = databaseHelper.getPresentCourseAttendance(selectedCourseId.toInt())
        var absentCourseA = databaseHelper.getAbsentCourseAttendance(selectedCourseId.toInt())
        var leaveCourseA = databaseHelper.getLeaveCourseAttendance(selectedCourseId.toInt())


        // For Percentage
        var presentPercentage = 0.0
        var absentPercentage = 0.0
        var leavePercentage = 0.0
        if (totalCourseAttendance > 0) {
            presentPercentage = ((presentCourseA.toFloat()  / totalCourseAttendance.toFloat())*100).toDouble()
            absentPercentage = ((absentCourseA.toFloat()  / totalCourseAttendance.toFloat())*100).toDouble()
            leavePercentage = ((leaveCourseA.toFloat() / totalCourseAttendance.toFloat())*100).toDouble()
        }

        // Show
        courseNameTV.text = "$courseName Attendance Record"
        courseTotalAttendance.text = totalCourseAttendance.toString()
        coursetPresentAttendance.text = presentCourseA.toString()
        courseAbsentAttendance.text = absentCourseA.toString()
        courseLeaveAttendance.text = leaveCourseA.toString()
        coursePresentPercent.text = presentPercentage.toString()
        courseAbsentPercent.text = absentPercentage.toString()
        courseleavePercent.text = leavePercentage.toString()

        cancelDialogBT.setOnClickListener {
            dialog.dismiss()
        }

    }

    fun showCourseRecyclerAttendance() {
        courseList = databaseHelper.getAllCourses()
        var adapter = AdapterSelectedCourseAttendanceReport(this@ActivityReport, courseList)
        binding.courseAttendanceRecycler.adapter = adapter
        binding.courseAttendanceRecycler.layoutManager = LinearLayoutManager(this@ActivityReport)
    }


    // Course Report Class
    class CourseReport(context: Context,binding: ActivityReportBinding,databaseHelper: DatabaseHelper,courseList: MutableList<Course>)  {

        var context : Context
        var binding = binding
        var databaseHelper = databaseHelper
        var courseList = courseList

      lateinit  var courseEnrollmentList : MutableList<CourseEnrollment>

        init {
            this.context = context
            this.binding = binding
            this.databaseHelper = databaseHelper
            this.courseList = courseList
            courseEnrollmentList = ArrayList()
        }

        fun showCourseReport()
        {
            binding.courseReportBT.setOnClickListener {
                binding.feeReportCard.visibility = View.GONE
                binding.basicReportCard.visibility = View.GONE
                binding.attendanceReportCard.visibility = View.GONE
                binding.courseReportCard.visibility = View.VISIBLE
            }

            // Course Recycler Show
            showCourseRecycler()
            calculatePriority()
        }

        fun showCourseRecycler()
        {
            var courseList = databaseHelper.getAllCourses()
            var adapter = AdapterCourseCourseReport(context, courseList)
            binding.courseRecycler.adapter = adapter
            binding.courseRecycler.layoutManager = LinearLayoutManager(context)
        }

        fun calculatePriority()
        {
            for(course in courseList)
            {
                var courseEnrollment = databaseHelper.getTotalCourseEnrollment(course.courseId)
                var singleCourseEnrollments = CourseEnrollment(course.courseId,course.courseName,courseEnrollment.toString())
                courseEnrollmentList.add(singleCourseEnrollments)
            }


            var sortedList = courseEnrollmentList.sortedByDescending {
                it.totalEnrollStudent
            }

            var popularCourseAdapter = AdapterPopularCourse(context,sortedList.toMutableList())
            binding.recyclerPopularCourse.adapter = popularCourseAdapter
            binding.recyclerPopularCourse.layoutManager = LinearLayoutManager(context)

        }

    }
    override fun onSelectedCourseCReport(course: Course) {
        selectedCourseId = course.courseId
        showRecord()
    }

    fun showRecord()
    {
        var courseMaleStudent = databaseHelper.getCourseMaleStudent(selectedCourseId)
        var courseFemaleStudent = databaseHelper.getCourseFemaleStudent(selectedCourseId)
        binding.totalCourseMaleStudentTV.text = "$courseMaleStudent"
        binding.totalCourseFemaleStudentTV.text = "$courseFemaleStudent"
    }




}



