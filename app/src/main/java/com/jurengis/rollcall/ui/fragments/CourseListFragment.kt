package com.jurengis.rollcall.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jurengis.rollcall.R
import com.jurengis.rollcall.adapter.CourseListAdapter
import com.jurengis.rollcall.ui.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_course_list.*

@AndroidEntryPoint
class CourseListFragment : Fragment(R.layout.fragment_course_list) {

    lateinit var courseListAdapter: CourseListAdapter
    private val viewModel: CourseListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.beacons.observe(viewLifecycleOwner, { courseList ->
            courseList?.let {
                courseListAdapter.differ.submitList(courseList)
            }
        })
    }

    private fun setupRecyclerView() {
        courseListAdapter = CourseListAdapter()
        rvAvailableCourses.apply {
            adapter = courseListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        private const val TAG = "AvailableCourses"
    }
}
