package com.jurengis.rollcall.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.jurengis.rollcall.R
import com.jurengis.rollcall.adapter.CourseEnrollmentAdapter
import com.jurengis.rollcall.model.Enrollment
import com.jurengis.rollcall.ui.CameraActivity
import com.jurengis.rollcall.viewmodel.CourseEnrollmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_course_enrollment.*

@AndroidEntryPoint
class CourseEnrollmentFragment : Fragment(R.layout.fragment_course_enrollment) {

    lateinit var courseEnrollmentAdapter: CourseEnrollmentAdapter
    private val viewModel: CourseEnrollmentViewModel by viewModels()
    private val args: CourseEnrollmentFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvents()
        setCourseProperties()
        setupRecyclerView()
        observeEnrollmentList()
    }

    private fun bindEvents() {
        btnEnroll.setOnClickListener {
            val intent = Intent(activity, CameraActivity::class.java)
            startActivity(intent)
        }
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
                updateRecyclerView(enrollmentList)
                updateCurrentWeekText(enrollmentList)
            }
        })
    }

    private fun updateRecyclerView(enrollmentList: List<Enrollment>?) {
        courseEnrollmentAdapter.differ.submitList(enrollmentList)
    }

    private fun updateCurrentWeekText(enrollmentList: List<Enrollment>) {
        val currentWeek = enrollmentList.size + 1
        val currentWeekText = String.format(getString(R.string.week), currentWeek)
        tvEnrollCurrentWeek.text = currentWeekText
    }
}