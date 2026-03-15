package com.techypie.evadevsmanagementsystem.Models

import java.io.Serializable

data class Student(
    var stdId : String = "",
    var stdName : String = "",
    var stdFatherName : String = "",
    var stdGender : String = "",
    var stdPhoneNumber : String = "",
    var isActive : String = "",
) : Serializable
