package com.jurengis.rollcall.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.jurengis.rollcall.model.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Region

class CourseListViewModel @ViewModelInject constructor(
    private val activity: Activity,
    private val beaconManager: BeaconManager,
    private val firestore: FirebaseFirestore
) : ViewModel(), BeaconConsumer {

    val beacons: MutableLiveData<MutableList<Course>> = MutableLiveData()

    init {
        beaconManager.bind(this)
    }

    override fun onCleared() {
        super.onCleared()
        beaconManager.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers()
        beaconManager.addRangeNotifier { detectedBeacons, _ ->
            if (detectedBeacons.isNotEmpty()) {
                Log.i(TAG, "Detected ${detectedBeacons.count()} beacons:")
                viewModelScope.launch(Dispatchers.IO) {
                    val availableCourses = mapBeaconsToCourses(detectedBeacons)
                    beacons.postValue(availableCourses)
                }
                beaconManager.removeAllRangeNotifiers()
            }
        }
        beaconManager.startRangingBeaconsInRegion(
            Region("wildcard-region", null, null, null)
        )
    }

    private suspend fun getAllCourses(): MutableList<Course> {
        val courseList = mutableListOf<Course>()
        val documents = firestore
            .collection("courses")
            .get()
            .await()
            .documents
        documents.forEach {
            val course = it.toObject(Course::class.java)
            if (course != null) {
                course.id = it.id
                courseList.add(course)
            }
        }
        return courseList
    }

    private suspend fun mapBeaconsToCourses(
        detectedBeacons: MutableCollection<Beacon>
    ): MutableList<Course> {
        val courseList = getAllCourses()
        return courseList.filter { course ->
            detectedBeacons.any { beacon ->
                beacon.id1.toString().endsWith(course.id)
            }
        }.toMutableList()
    }

    override fun getApplicationContext(): Context {
        return activity.applicationContext
    }

    override fun unbindService(serviceConnection: ServiceConnection?) {
        if (serviceConnection != null) {
            activity.unbindService(serviceConnection)
        }
    }

    override fun bindService(
        intent: Intent?,
        serviceConnection: ServiceConnection?,
        i: Int
    ): Boolean {
        if (serviceConnection != null) {
            return activity.bindService(intent, serviceConnection, i)
        }
        return false
    }

    companion object {
        private const val TAG = "AvailableCoursesVM"
    }
}