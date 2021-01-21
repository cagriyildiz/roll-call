package com.jurengis.rollcall.model

import java.io.Serializable

data class Course(
    var id: String = "",
    val courseName: String = "",
    val classRoom: String = "",
    val instructor: String = ""
) : Serializable