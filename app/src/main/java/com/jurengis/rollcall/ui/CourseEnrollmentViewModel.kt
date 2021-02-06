package com.jurengis.rollcall.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jurengis.rollcall.model.Course
import com.jurengis.rollcall.model.CourseEnrollment
import com.jurengis.rollcall.model.Enrollment
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CourseEnrollmentViewModel @ViewModelInject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val enrollments: MutableLiveData<List<Enrollment>> = MutableLiveData()
    val course = savedStateHandle.get<Course>("course")

    init {
        getCourseEnrollments()
    }

    private fun getCourseEnrollments() {
        viewModelScope.launch {
            val email = auth.currentUser?.email
            if (email != null && course != null) {
                val courseDocument = firestore.collection("students")
                    .document(email)
                    .collection("courses")
                    .document(course.id)
                    .get()
                    .await()
                val course = courseDocument.toObject(CourseEnrollment::class.java)
                val enrollment = setWeeks(course?.enrollment)
                enrollments.postValue(enrollment)
            }
        }
    }

    private fun setWeeks(enrollment: List<Enrollment>?): List<Enrollment>? {
        return enrollment?.mapIndexed { idx, item  ->
            Enrollment("Week ${idx+1}", item.date, item.status)
        }
    }
}