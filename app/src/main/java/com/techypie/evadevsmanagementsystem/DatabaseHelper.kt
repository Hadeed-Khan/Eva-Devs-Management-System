package com.techypie.evadevsmanagementsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.techypie.evadevsmanagementsystem.Models.Attendance
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.Models.Enrollment
import com.techypie.evadevsmanagementsystem.Models.Fee
import com.techypie.evadevsmanagementsystem.Models.Student

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Management", null, 1) {

    init {
        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS STUDENTS(STD_ID INTEGER PRIMARY KEY AUTOINCREMENT , STD_NAME TEXT, STD_FATHER_NAME TEXT , STD_GENDER TEXT , STD_PHONE TEXT , IS_ACTIVE TEXT)")
        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS COURSES(COURSE_ID INTEGER PRIMARY KEY AUTOINCREMENT , COURSE_NAME TEXT , COURSE_DURATION TEXT , COURSE_FEE TEXT , IS_ACTIVE TEXT)")
        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS ENROLLMENTS(ENROLLMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT , STD_ID INTEGER , COURSE_ID INTEGER , ENROLLMENT_DATE TEXT , IS_ACTIVE TEXT)")
        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS FEES(FEE_ID INTEGER PRIMARY KEY AUTOINCREMENT,STD_ID TEXT , COURSE_ID TEXT , FEE_TYPE TEXT , TOTAL_FEE TEXT , PAID_AMOUNT TEXT , PAYMENT_DATE TEXT , IS_ACTIVE TEXT)")
        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS ATTENDANCES(ATTENDANCE_ID INTEGER PRIMARY KEY AUTOINCREMENT , COURSE_ID TEXT , STD_ID TEXT , DATE TEXT , STATUS TEXT , IS_ACTIVE TEXT)")
    }

    // For Students Database Operation
    // for add Student in Database
    fun addStudents(
        stdName: String,
        stdFatherName: String,
        stdGender: String,
        stdPhoneNumber: String,
        isActive: String
    ): String {
        var values = ContentValues()
        values.put("STD_NAME", stdName)
        values.put("STD_FATHER_NAME", stdFatherName)
        values.put("STD_GENDER", stdGender)
        values.put("STD_PHONE", stdPhoneNumber)
        values.put("IS_ACTIVE", isActive)

        var isStudentInserted = writableDatabase.insert("STUDENTS", null, values)
        if (isStudentInserted == -1L)
            return "Error"
        else
            return "Success"
    }

    // For Get Students From Database
    fun getAllStudents(): MutableList<Student> {
        var cursor =
            writableDatabase.rawQuery("SELECT * FROM STUDENTS WHERE IS_ACTIVE = 'Active'", null)
        var studentList = mutableListOf<Student>()
        while (cursor.moveToNext()) {
            var stdId = cursor.getString(0)
            var stdName = cursor.getString(1)
            var stdFatherName = cursor.getString(2)
            var stdGender = cursor.getString(3)
            var stdPhoneNumber = cursor.getString(4)
            var isStudentActive = cursor.getString(5)
            var singleStudent =
                Student(stdId, stdName, stdFatherName, stdGender, stdPhoneNumber, isStudentActive)
            studentList.add(singleStudent)
        }
        return studentList
    }

    // For Last Student
    fun getLastStudent(): Student {
        var cursor = writableDatabase.rawQuery("SELECT * FROM STUDENTS", null)
        cursor.moveToPosition(cursor.count - 1)
        var stdId = cursor.getString(0)
        var stdName = cursor.getString(1)
        var stdFatherName = cursor.getString(2)
        var stdGender = cursor.getString(3)
        var stdPhoneNumber = cursor.getString(4)
        var isStudentActive = cursor.getString(5)
        var singleStudent =
            Student(stdId, stdName, stdFatherName, stdGender, stdPhoneNumber, isStudentActive)
        return singleStudent
    }

    // For Update Student
    fun updateStudent(
        stdId: String,
        stdName: String,
        stdFatherName: String,
        stdGender: String,
        stdPhoneNumber: String
    ): String {
        var values = ContentValues()
        values.put("STD_NAME", stdName)
        values.put("STD_FATHER_NAME", stdFatherName)
        values.put("STD_GENDER", stdGender)
        values.put("STD_PHONE", stdPhoneNumber)
        var noOfUpdatedStudent =
            writableDatabase.update("STUDENTS", values, "STD_ID=?", arrayOf(stdId))
        if (noOfUpdatedStudent>0)
        return "Updated"
        else
            return "Error"
    }

    // For Soft Delete
    fun softDeleteStudent(stdId: String): String {
        var values = ContentValues()
        values.put("IS_ACTIVE", "Inactive")
        var noOfUpdatedActive =
            writableDatabase.update("STUDENTS", values, "STD_ID=?", arrayOf(stdId))
        var noOfUpdatedActiveE =
            writableDatabase.update("ENROLLMENTS", values, "STD_ID=?", arrayOf(stdId))
        var noOfUpdatedActiveF = writableDatabase.update("FEES", values, "STD_ID=?", arrayOf(stdId))
        var noOfUpdatedActiveA =
            writableDatabase.update("ATTENDANCES", values, "STD_ID=?", arrayOf(stdId))
        if (noOfUpdatedActive>0)
        return "Deleted"
        else
            return "Error"
    }

    // For Course database Operation
    // For add course into Database
    fun addCourse(
        courseName: String,
        courseDuration: String,
        courseFee: String,
        isActive: String
    ): String {
        var values = ContentValues()
        values.put("COURSE_NAME", courseName)
        values.put("COURSE_DURATION", courseDuration)
        values.put("COURSE_FEE", courseFee)
        values.put("IS_ACTIVE", isActive)

        var addCourse = writableDatabase.insert("COURSES", null, values)
        if (addCourse == -1L)
            return "Error"
        else
            return "Success"
    }

    // For get From Database

    fun getAllCourses(): MutableList<Course> {
        var cursor =
            writableDatabase.rawQuery("SELECT * FROM COURSES WHERE IS_ACTIVE = 'Active'", null)
        var courseList = mutableListOf<Course>()
        while (cursor.moveToNext()) {
            var courseId = cursor.getString(0)
            var courseName = cursor.getString(1)
            var courseDuration = cursor.getString(2)
            var courseFee = cursor.getString(3)
            var isActive = cursor.getString(4)
            var singleCourse = Course(courseId, courseName, courseDuration, courseFee, isActive)
            courseList.add(singleCourse)
        }
        return courseList

    }

    // Get Last Course
    fun getLastCourse(): Course {
        var cursor = writableDatabase.rawQuery("SELECT * FROM COURSES", null)
        cursor.moveToPosition(cursor.count - 1)
        var courseId = cursor.getString(0)
        var courseName = cursor.getString(1)
        var courseDuration = cursor.getString(2)
        var courseFee = cursor.getString(3)
        var isActive = cursor.getString(4)
        var singleCourse = Course(courseId, courseName, courseDuration, courseFee, isActive)
        return singleCourse
    }

    // For Update Course
    fun updateCourse(
        courseId: String,
        courseName: String,
        courseDuration: String,
        courseFee: String
    ): String {
        var values = ContentValues()
        values.put("COURSE_NAME", courseName)
        values.put("COURSE_DURATION", courseDuration)
        values.put("COURSE_FEE", courseFee)
        var noOfUpdatedCourse =
            writableDatabase.update("COURSES", values, "COURSE_ID=?", arrayOf(courseId))
        if (noOfUpdatedCourse>0)
        return "Updated"
        else
            return "Error"
    }

    // For Soft Deleted
    fun courseSoftDelete(courseId: String): String {
        var values = ContentValues()
        values.put("IS_ACTIVE", "Inactive")
        var noOfUpdatedCourse =
            writableDatabase.update("COURSES", values, "COURSE_ID=?", arrayOf(courseId))
        var noOfUpdatedEnrollement =
            writableDatabase.update("ENROLLMENTS", values, "COURSE_ID=?", arrayOf(courseId))
        var noOfUpdatedFee =
            writableDatabase.update("FEES", values, "COURSE_ID=?", arrayOf(courseId))
        var noOfUpdatedAttendance =
            writableDatabase.update("ATTENDANCES", values, "COURSE_ID=?", arrayOf(courseId))
        if (noOfUpdatedCourse>0)
            return "Deleted"
        else
            return "Error"
    }

    // For Report Section


    // For Enrollment Section
    fun addEnrollment(
        studentId: String,
        courseId: String,
        enrollDate: String,
        isActive: String
    ): String {
        var values = ContentValues()
        values.put("STD_ID", studentId)
        values.put("COURSE_ID", courseId)
        values.put("ENROLLMENT_DATE", enrollDate)
        values.put("IS_ACTIVE", isActive)
        var isDateInserted = writableDatabase.insert("ENROLLMENTS", null, values)
        if (isDateInserted == -1L)
            return "Error"
        else
            return "Success"

    }

    fun getAllEnrollments(): MutableList<Enrollment> {
        var cursor = writableDatabase.rawQuery(
            "SELECT E.ENROLLMENT_ID, E.ENROLLMENT_DATE, S.STD_ID, S.STD_NAME, S.STD_FATHER_NAME, C.COURSE_ID, C.COURSE_NAME, C.COURSE_DURATION FROM ENROLLMENTS E JOIN STUDENTS S ON E.STD_ID = S.STD_ID JOIN COURSES C ON E.COURSE_ID= C.COURSE_ID WHERE S.IS_ACTIVE = 'Active' AND E.IS_ACTIVE = 'Active' AND C.IS_ACTIVE = 'Active'",
            null
        )
        var enrollmentList = mutableListOf<Enrollment>()

        while (cursor.moveToNext()) {
            var enrollmentId = cursor.getString(0)
            var enrollmentDate = cursor.getString(1)
            var studentId = cursor.getString(2)
            var studentName = cursor.getString(3)
            var studentFName = cursor.getString(4)
            var courseId = cursor.getString(5)
            var courseName = cursor.getString(6)
            var courseDuration = cursor.getString(7)

            var singleEnrolllment = Enrollment(
                enrollmentId,
                enrollmentDate,
                studentId,
                studentName,
                studentFName,
                courseId,
                courseName,
                courseDuration,
                "Active"
            )
            enrollmentList.add(singleEnrolllment)
        }

        return enrollmentList
    }

    // For SoftDelete Enrollment
    fun enrollmentSoftDelete(enrollmentId: String): String {
        var values = ContentValues()
        values.put("IS_ACTIVE", "Inactive")
        var noOfUpdatedEnrollment =
            writableDatabase.update("ENROLLMENTS", values, "ENROLLMENT_ID=?", arrayOf(enrollmentId))
        if (noOfUpdatedEnrollment > 0)
            return "Deleted"
        else
            return "Error"

    }

// Fee Section Start

    fun addFee(
        stdId: String,
        courseId: String,
        feeType: String,
        totalFee: String,
        paidAmount: String,
        paymentDate: String,
        isActive: String
    ): String {
        var values = ContentValues()
        values.put("STD_ID", stdId)
        values.put("COURSE_ID", courseId)
        values.put("FEE_TYPE", feeType)
        values.put("TOTAL_FEE", totalFee)
        values.put("PAID_AMOUNT", paidAmount)
        values.put("PAYMENT_DATE", paymentDate)
        values.put("IS_ACTIVE", isActive)

        var result = writableDatabase.insert("FEES", null, values)
        if (result == -1L)
            return "Error"
        else
            return "Success"
    }


    fun getCourseFeeById(courseId: String): String {
        var cursor = writableDatabase.rawQuery(
            "SELECT COURSE_FEE FROM COURSES WHERE COURSE_ID=$courseId ",
            null
        )
        var courseFee = ""
        if (cursor.moveToNext()) {
            courseFee = cursor.getString(0)
        }
        return courseFee
    }

    // Get All Fee Record From Database

    fun getAllFee(): MutableList<Fee> {
        var cursor = writableDatabase.rawQuery(
            "SELECT F.FEE_ID , F.FEE_TYPE , F.TOTAL_FEE , F.PAID_AMOUNT,F.PAYMENT_DATE ,S.STD_ID , S.STD_NAME ,C.COURSE_ID, C.COURSE_NAME FROM FEES F JOIN STUDENTS S ON S.STD_ID = F.STD_ID JOIN COURSES C ON C.COURSE_ID = F.COURSE_ID  WHERE F.IS_ACTIVE = 'Active' AND S.IS_ACTIVE = 'Active' AND C.IS_ACTIVE = 'Active'",
            null
        )
        var feeList = mutableListOf<Fee>()
        while (cursor.moveToNext()) {
            var feeId = cursor.getString(0)
            var feeType = cursor.getString(1)
            var totalFee = cursor.getString(2)
            var paidAmount = cursor.getString(3)
            var paymentDate = cursor.getString(4)
            var stdId = cursor.getString(5)
            var stdName = cursor.getString(6)
            var courseId = cursor.getString(7)
            var courseName = cursor.getString(8)
            var singleFee = Fee(feeId, feeType, totalFee, paidAmount,paymentDate,stdId,stdName,courseId,courseName)
            feeList.add(singleFee)
        }
        return feeList
    }

    // For Soft Delete
    fun feeSoftDelete(feeId: String): String {
        var values = ContentValues()
        values.put("IS_ACTIVE", "Inactive")
        var noOfUpdatedFee = writableDatabase.update("FEES", values, "FEE_ID=?", arrayOf(feeId))
        if (noOfUpdatedFee > 0)
            return "Deleted"
        else
            return "Error"
    }

    // For Attendance
    fun getEnrollStudent(courseId: String): MutableList<Student> {
        var cursor = writableDatabase.rawQuery(
            "SELECT S.STD_ID , S.STD_NAME , S.STD_FATHER_NAME , S.STD_GENDER , S.STD_PHONE FROM ENROLLMENTS E JOIN STUDENTS S ON S.STD_ID= E.STD_ID WHERE E.COURSE_ID= $courseId AND E.IS_ACTIVE = 'Active' AND S.IS_ACTIVE = 'Active'",
            null
        )
        var enrollStudentList = mutableListOf<Student>()
        while (cursor.moveToNext()) {
            var stdId = cursor.getString(0)
            var stdName = cursor.getString(1)
            var stdFName = cursor.getString(2)
            var stdGender = cursor.getString(3)
            var stdPhone = cursor.getString(4)
            var singleEnrollStudent =
                Student(stdId, stdName, stdFName, stdGender, stdPhone, "Active")
            enrollStudentList.add(singleEnrollStudent)
        }

        return enrollStudentList
    }


    fun addAttendance(attendanceList : MutableList<Attendance>,date : String) : String {
        var isDataAdd = 2L
        var attendanceDate = date
        var db = writableDatabase
        db.beginTransaction()
       try {
           for (attendance in attendanceList){

               var courseId = attendance.courseId
               var stdId = attendance.stdId
               var date = attendanceDate
               var status = attendance.status
               var isActive = attendance.isActive
               var values = ContentValues()
               values.put("COURSE_ID", courseId)
               values.put("STD_ID", stdId)
               values.put("DATE", date)
               values.put("STATUS", status)
               values.put("IS_ACTIVE", isActive)
              isDataAdd =  db.insert("ATTENDANCES",null,values)
           }
           db.setTransactionSuccessful()
       }
       catch (e : Exception){
            e.printStackTrace()
       }
        finally {
            db.endTransaction()
            if (isDataAdd == -1L){
                return "Error"
            }
            else{
                return "Success"
            }
        }

    }


    fun getCourseAttendacne(courseId: String): MutableList<Attendance> {
        var cursor = writableDatabase.rawQuery(
            "SELECT S.STD_ID , S.STD_NAME , S.STD_FATHER_NAME ,A.ATTENDANCE_ID , A.DATE , A.STATUS, A.COURSE_ID FROM ATTENDANCES A JOIN STUDENTS S ON A.STD_ID = S.STD_ID  WHERE A.COURSE_ID = $courseId AND S.IS_ACTIVE = 'Active' AND A.IS_ACTIVE = 'Active'",
            null
        )
        var attendanceList = mutableListOf<Attendance>()

        while (cursor.moveToNext()) {
            var stdId = cursor.getString(0)
            var stdName = cursor.getString(1)
            var stdFName = cursor.getString(2)
            var attendanceId = cursor.getString(3)
            var date = cursor.getString(4)
            var status = cursor.getString(5)
            var courseId = cursor.getString(6)

            var singleAttendance =
                Attendance(attendanceId, date, status, stdId, stdName, stdFName, courseId, "Active")

            attendanceList.add(singleAttendance)
        }
        return attendanceList
    }

    // Soft Delete Attendance
    fun softDeleteAttendance(attendanceId: String): String {
        var values = ContentValues()
        values.put("IS_ACTIVE", "Inactive")
        var noOfupdatedAttendance =
            writableDatabase.update("ATTENDANCES", values, "ATTENDANCE_ID=?", arrayOf(attendanceId))
        if (noOfupdatedAttendance > 0)
            return "Deleted"
        else
            return "Error"
    }

    ////////////////////Report Section Start Here/////////////////////////////////////////////

    // For Basic Report Section
    fun getTotalStudent(): Int {
        var cursor =
            writableDatabase.rawQuery("SELECT * FROM STUDENTS WHERE IS_ACTIVE = 'Active'", null)
        return cursor.count
    }

    fun getMaleStudents(): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM STUDENTS WHERE IS_ACTIVE = 'Active' AND STD_GENDER = 'Male'",
            null
        )
        return cursor.count
    }

    fun getFemaleStudents(): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM STUDENTS WHERE IS_ACTIVE = 'Active' AND STD_GENDER = 'Female'",
            null
        )
        return cursor.count
    }

    fun getTotalCourse(): Int {
        var cursor =
            writableDatabase.rawQuery("SELECT * FROM COURSES WHERE IS_ACTIVE = 'Active'", null)
        return cursor.count
    }

    fun getTotalEnrollment(): Int {

        var cursor =
            writableDatabase.rawQuery("SELECT * FROM ENROLLMENTS WHERE IS_ACTIVE = 'Active'", null)
        return cursor.count
    }

    // For Fee Report Section ////////////////////////////////////////////////////////////////

    fun getAllPaidStudent(): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM FEES WHERE PAID_AMOUNT >= TOTAL_FEE AND IS_ACTIVE = 'Active'",
            null
        )
        return cursor.count
    }

    fun getAllUnPaidStudents(): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM FEES WHERE PAID_AMOUNT < TOTAL_FEE AND IS_ACTIVE = 'Active'",
            null
        )
        return cursor.count
    }

    fun getTotalPaidFee(): MutableList<String> {
        var cursor = writableDatabase.rawQuery(
            "SELECT PAID_AMOUNT FROM FEES WHERE IS_ACTIVE = 'Active'",
            null
        )
        var totalPaidList = mutableListOf<String>()
        while (cursor.moveToNext()) {
            var singlePaidAmount = cursor.getString(0)
            totalPaidList.add(singlePaidAmount)
        }
        return totalPaidList
    }

    fun getAllTotalFee(): MutableList<String> {
        var cursor =
            writableDatabase.rawQuery("SELECT TOTAL_FEE FROM FEES WHERE IS_ACTIVE = 'Active'", null)
        var totalFeeList = mutableListOf<String>()
        while (cursor.moveToNext()) {
            var totalFee = cursor.getString(0)
            totalFeeList.add(totalFee)
        }

        return totalFeeList
    }

    fun getStudentFeeHistory(studentId: Int): MutableList<Fee> {
        var cursor = writableDatabase.rawQuery(
            "SELECT F.FEE_ID , F.FEE_TYPE , F.TOTAL_FEE , F.PAID_AMOUNT,F.PAYMENT_DATE ,S.STD_ID , S.STD_NAME ,C.COURSE_ID, C.COURSE_NAME FROM FEES F JOIN STUDENTS S ON S.STD_ID = F.STD_ID AND F.STD_ID = $studentId  JOIN COURSES C ON C.COURSE_ID = F.COURSE_ID  WHERE F.IS_ACTIVE = 'Active' AND S.IS_ACTIVE = 'Active' AND C.IS_ACTIVE = 'Active'",
            null
        )
        var studentFeeList = mutableListOf<Fee>()
        while (cursor.moveToNext()) {
            var feeId = cursor.getString(0)
            var feeType = cursor.getString(1)
            var totalFee = cursor.getString(2)
            var paidAmount = cursor.getString(3)
            var paymentDate = cursor.getString(4)
            var stdId = cursor.getString(5)
            var stdName = cursor.getString(6)
            var courseId = cursor.getString(7)
            var courseName = cursor.getString(8)
            var singleFee =
                Fee(feeId, feeType, totalFee, paidAmount, paymentDate, stdId , stdName,courseId,courseName)
            studentFeeList.add(singleFee)
        }
        return studentFeeList
    }

    // For Attendance Report Section
    fun getTotalAttendance(): Int {
        var cursor =
            writableDatabase.rawQuery("SELECT * FROM ATTENDANCES WHERE IS_ACTIVE = 'Active'", null)
        return cursor.count
    }

    fun getTotalPrsentAttendance(): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE STATUS = 'Present' AND IS_ACTIVE = 'Active'",
            null
        )
        return cursor.count
    }

    fun getTotalAbsentAttendance(): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE STATUS = 'Absent' AND IS_ACTIVE = 'Active'",
            null
        )
        return cursor.count
    }

    fun getTotalLeaveAttendance(): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE STATUS = 'Leave' AND IS_ACTIVE = 'Active'",
            null
        )
        return cursor.count
    }

    // For Single Student Attendance Report
    fun getStudentTotalAttendance(studentId: Int): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE STD_ID = $studentId AND IS_ACTIVE = 'Active'",
            null
        )
        return cursor.count
    }

    fun getStudentPresentAttendance(studentId: Int): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE STD_ID = $studentId AND IS_ACTIVE = 'Active' AND STATUS = 'Present'",
            null
        )
        return cursor.count
    }

    fun getStudentAbsentAttendance(studentId: Int): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE STD_ID = $studentId AND IS_ACTIVE = 'Active' AND STATUS = 'Absent'",
            null
        )
        return cursor.count
    }

    fun getStudentLeaveAttendance(studentId: Int): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE STD_ID = $studentId AND IS_ACTIVE = 'Active' AND STATUS = 'Leave'",
            null
        )
        return cursor.count
    }

    // For Course Attendance Report
    fun getTotalCourseAttendance(courseId: Int): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE IS_ACTIVE = 'Active' AND COURSE_ID = $courseId",
            null
        )
        return cursor.count
    }

    fun getPresentCourseAttendance(courseId: Int): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE IS_ACTIVE = 'Active' AND COURSE_ID = $courseId AND STATUS = 'Present'",
            null
        )
        return cursor.count
    }

    fun getAbsentCourseAttendance(courseId: Int): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE IS_ACTIVE = 'Active' AND COURSE_ID = $courseId AND STATUS = 'Absent'",
            null
        )
        return cursor.count
    }

    fun getLeaveCourseAttendance(courseId: Int): Int {
        var cursor = writableDatabase.rawQuery(
            "SELECT * FROM ATTENDANCES WHERE IS_ACTIVE = 'Active' AND COURSE_ID = $courseId AND STATUS = 'Leave'",
            null
        )
        return cursor.count
    }


// Inactive Section Start Here

    fun getAllInactiveStudents(): MutableList<Student> {
        var cursor =
            writableDatabase.rawQuery("SELECT * FROM STUDENTS WHERE IS_ACTIVE = 'Inactive'", null)
        var studentList = mutableListOf<Student>()
        while (cursor.moveToNext()) {
            var stdId = cursor.getString(0)
            var stdName = cursor.getString(1)
            var stdFatherName = cursor.getString(2)
            var stdGender = cursor.getString(3)
            var stdPhoneNumber = cursor.getString(4)
            var isStudentActive = cursor.getString(5)
            var singleStudent =
                Student(stdId, stdName, stdFatherName, stdGender, stdPhoneNumber, isStudentActive)
            studentList.add(singleStudent)
        }
        return studentList
    }

    // For Inactive Course
    fun getAllInactiveCourses(): MutableList<Course> {
        var cursor =
            writableDatabase.rawQuery("SELECT * FROM COURSES WHERE IS_ACTIVE = 'Inactive'", null)
        var courseList = mutableListOf<Course>()
        while (cursor.moveToNext()) {
            var courseId = cursor.getString(0)
            var courseName = cursor.getString(1)
            var courseDuration = cursor.getString(2)
            var courseFee = cursor.getString(3)
            var isActive = cursor.getString(4)
            var singleCourse = Course(courseId, courseName, courseDuration, courseFee, isActive)
            courseList.add(singleCourse)
        }
        return courseList

    }

    // For Inactive Enrollments
    fun getAllInactiveEnrollments(): MutableList<Enrollment> {
        var cursor = writableDatabase.rawQuery(
            "SELECT E.ENROLLMENT_ID, E.ENROLLMENT_DATE, S.STD_ID, S.STD_NAME, S.STD_FATHER_NAME, C.COURSE_ID, C.COURSE_NAME, C.COURSE_DURATION FROM ENROLLMENTS E JOIN STUDENTS S ON E.STD_ID = S.STD_ID JOIN COURSES C ON E.COURSE_ID= C.COURSE_ID WHERE E.IS_ACTIVE = 'Inactive' ",
            null
        )
        var enrollmentList = mutableListOf<Enrollment>()

        while (cursor.moveToNext()) {
            var enrollmentId = cursor.getString(0)
            var enrollmentDate = cursor.getString(1)
            var studentId = cursor.getString(2)
            var studentName = cursor.getString(3)
            var studentFName = cursor.getString(4)
            var courseId = cursor.getString(5)
            var courseName = cursor.getString(6)
            var courseDuration = cursor.getString(7)

            var singleEnrolllment = Enrollment(
                enrollmentId,
                enrollmentDate,
                studentId,
                studentName,
                studentFName,
                courseId,
                courseName,
                courseDuration,
                "Active"
            )
            enrollmentList.add(singleEnrolllment)
        }

        return enrollmentList
    }

    // Inactive Attendance
    fun getAllInactiveAttendacne(): MutableList<Attendance> {
        var cursor = writableDatabase.rawQuery(
            "SELECT S.STD_ID , S.STD_NAME , S.STD_FATHER_NAME ,A.ATTENDANCE_ID , A.DATE , A.STATUS, A.COURSE_ID FROM ATTENDANCES A JOIN STUDENTS S ON A.STD_ID = S.STD_ID  WHERE  A.IS_ACTIVE = 'Inactive'",
            null
        )
        var attendanceList = mutableListOf<Attendance>()

        while (cursor.moveToNext()) {
            var stdId = cursor.getString(0)
            var stdName = cursor.getString(1)
            var stdFName = cursor.getString(2)
            var attendanceId = cursor.getString(3)
            var date = cursor.getString(4)
            var status = cursor.getString(5)
            var courseId = cursor.getString(6)

            var singleAttendance =
                Attendance(attendanceId, date, status, stdId, stdName, stdFName, courseId, "Active")

            attendanceList.add(singleAttendance)
        }
        return attendanceList
    }

    // For Inactive Fee
    fun getAllInactiveFee(): MutableList<Fee> {
        var cursor = writableDatabase.rawQuery(
            "SELECT F.FEE_ID , F.FEE_TYPE , F.TOTAL_FEE , F.PAID_AMOUNT,F.PAYMENT_DATE ,S.STD_ID , S.STD_NAME ,C.COURSE_ID, C.COURSE_NAME FROM FEES F JOIN STUDENTS S ON S.STD_ID = F.STD_ID JOIN COURSES C ON C.COURSE_ID = F.COURSE_ID  WHERE F.IS_ACTIVE = 'Inactive'",
            null
        )
        var feeList = mutableListOf<Fee>()
        while (cursor.moveToNext()) {
            var feeId = cursor.getString(0)
            var feeType = cursor.getString(1)
            var totalFee = cursor.getString(2)
            var paidAmount = cursor.getString(3)
            var paymentDate = cursor.getString(4)
            var stdId = cursor.getString(5)
            var stdName = cursor.getString(6)
            var courseId = cursor.getString(7)
            var courseName = cursor.getString(8)
            var singleFee = Fee(feeId, feeType, totalFee, paidAmount,paymentDate,stdId,stdName,courseId,courseName)
            feeList.add(singleFee)
        }
        return feeList
    }

    //    Course Report Section
    fun getCourseMaleStudent(courseId: String): Int {
        var cursor = writableDatabase.rawQuery("SELECT S.STD_ID , S.STD_NAME , S.STD_FATHER_NAME , S.STD_GENDER , S.STD_PHONE FROM ENROLLMENTS E JOIN STUDENTS S ON E.STD_ID= S.STD_ID WHERE E.COURSE_ID= $courseId AND E.IS_ACTIVE = 'Active' AND S.STD_GENDER = 'Male'", null)
        return cursor.count
    }

    fun getCourseFemaleStudent(courseId: String): Int {
        var cursor = writableDatabase.rawQuery("SELECT S.STD_ID , S.STD_NAME , S.STD_FATHER_NAME , S.STD_GENDER , S.STD_PHONE FROM ENROLLMENTS E JOIN STUDENTS S ON E.STD_ID= S.STD_ID WHERE E.COURSE_ID= $courseId AND E.IS_ACTIVE = 'Active' AND S.STD_GENDER = 'Female'", null)
        return cursor.count
    }


    fun getTotalCourseEnrollment(courseId : String) : Int
    {
        var cursor = writableDatabase.rawQuery("SELECT * FROM ENROLLMENTS E WHERE E.COURSE_ID = $courseId AND IS_ACTIVE = 'Active'",null)
        return cursor.count
    }




    override fun onCreate(p0: SQLiteDatabase?) {

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }


}