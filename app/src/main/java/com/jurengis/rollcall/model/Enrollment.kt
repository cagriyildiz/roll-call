package com.jurengis.rollcall.model

import com.google.firebase.Timestamp

data class Enrollment(
    var week: String = "",
    var date: Timestamp = Timestamp(0, 0),
    var status: Boolean = false
)