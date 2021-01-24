package com.jurengis.rollcall.model

data class CourseEnrollment(
    var id: String = "",
    var courseName: String = "",
    var enrollment: Map<String, Boolean> = mutableMapOf(),
    var instructor: String = ""
)