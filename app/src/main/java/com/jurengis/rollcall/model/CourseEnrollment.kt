package com.jurengis.rollcall.model

data class CourseEnrollment(
    var id: String = "",
    var courseName: String = "",
    var enrollment: List<Enrollment> = mutableListOf(),
    var instructor: String = ""
)