package com.jurengis.rollcall.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.jurengis.rollcall.R
import com.jurengis.rollcall.adapter.CourseEnrollmentAdapter
import com.jurengis.rollcall.ui.CourseEnrollmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_course_enroll.*

@AndroidEntryPoint
class CourseEnrollFragment : Fragment(R.layout.fragment_course_enroll) {

    lateinit var courseEnrollmentAdapter: CourseEnrollmentAdapter
    private val viewModel: CourseEnrollmentViewModel by viewModels()
    private val args: CourseEnrollFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCourseProperties()
        setupRecyclerView()
        observeEnrollmentList()
    }

    private fun setCourseProperties() {
        val course = args.course
        tvEnrollCourseName.text = course.courseName
        tvEnrollInstructorName.text = course.instructor
    }

    private fun setupRecyclerView() {
        courseEnrollmentAdapter = CourseEnrollmentAdapter()
        rvCourseEnrollment.apply {
            adapter = courseEnrollmentAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun observeEnrollmentList() {
        viewModel.enrollments.observe(viewLifecycleOwner, { enrollmentList ->
            enrollmentList?.let {
                courseEnrollmentAdapter.differ.submitList(enrollmentList)
            }
        })
    }
}