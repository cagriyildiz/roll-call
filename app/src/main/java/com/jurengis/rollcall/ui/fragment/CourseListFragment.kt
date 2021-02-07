package com.jurengis.rollcall.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jurengis.rollcall.R
import com.jurengis.rollcall.adapter.CourseListAdapter
import com.jurengis.rollcall.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_course_list.*

@AndroidEntryPoint
class CourseListFragment : Fragment(R.layout.fragment_course_list) {

    lateinit var courseListAdapter: CourseListAdapter
    private val viewModel: CourseListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        bindEvents()
        requestLocationPermission()
    }

    private fun bindEvents() {
        courseListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("course", it)
            }
            findNavController().navigate(
                R.id.action_courseListFragment_to_courseEnrollmentFragment,
                bundle
            )
        }
    }

    private fun observeCourses() {
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

    private fun requestLocationPermission() {
        if (allPermissionsGranted()) {
            observeCourses()
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { permission ->
        return if (permission != null) {
            ContextCompat.checkSelfPermission(
                requireContext(), permission
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                observeCourses()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Location permission is needed for this app to be work properly.",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    companion object {
        private const val TAG = "AvailableCourses"
        private const val REQUEST_CODE_PERMISSIONS = 11
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            } else { null })
    }
}
