package com.techypie.evadevsmanagementsystem.Models

import java.io.Serializable


data class Course(
    var courseId : String = "",
    var courseName : String = "",
    var courseDuration : String = "",
    var courseFee : String = "",
    var isActive : String = "",
) : Serializable
