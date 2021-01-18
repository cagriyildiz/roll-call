package com.jurengis.rollcall.model

data class Course(
    var id: Int? = null,
    val name: String,
    val classRoom: String,
    val instructor: String
)