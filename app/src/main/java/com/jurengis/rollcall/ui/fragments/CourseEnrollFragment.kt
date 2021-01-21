package com.jurengis.rollcall.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jurengis.rollcall.R
import kotlinx.android.synthetic.main.fragment_course_enroll.*

class CourseEnrollFragment : Fragment(R.layout.fragment_course_enroll) {

    private val args: CourseEnrollFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val course = args.course
        tvEnrollCourseName.text = course.courseName
        tvEnrollInstructorName.text = course.instructor
    }
}