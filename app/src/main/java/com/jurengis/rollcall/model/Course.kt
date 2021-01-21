package com.jurengis.rollcall.model

import java.io.Serializable

data class Course(
    var id: String = "",
    var courseName: String = "",
    var classRoom: String = "",
    var instructor: String = ""
) : Serializable