package com.techypie.evadevsmanagementsystem.Models

import java.io.Serializable

data class Attendance(
    var attendanceId : String = "",
    var date : String = "",
    var status : String = "",
    var stdId : String = "",
    var stdName : String = "",
    var stdFatherName : String = "",
    var courseId : String = "",
    var isActive : String = "",
):Serializable
