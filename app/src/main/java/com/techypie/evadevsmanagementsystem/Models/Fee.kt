package com.techypie.evadevsmanagementsystem.Models

import java.io.Serializable

data class Fee(
    var feeId : String = "",
    var feeType : String = "",
    var totalFee : String = "",
    var paidFee : String = "",
    var paymentData : String = "",
    var stdId : String = "",
    var stdName : String = "",
    var courseId : String = "",
    var courseName : String = "",
) : Serializable
