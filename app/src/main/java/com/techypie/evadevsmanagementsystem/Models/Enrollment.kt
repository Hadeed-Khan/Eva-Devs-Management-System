package com.techypie.evadevsmanagementsystem.Models

import java.io.Serializable

data class Enrollment(
    var enrollmentId : String = "",
    var enrollmentDate : String = "",
    var studentId : String = "",
    var studentName : String = "",
    var studentFName : String = "",
    var courseId : String = "",
    var courseName : String = "",
    var courseDuration : String = "",
    var isActive : String = "",
) : Serializable
