package com.jurengis.rollcall.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jurengis.rollcall.model.Course
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Region

class CourseListViewModel @ViewModelInject constructor(
    private val activity: Activity,
    private val beaconManager: BeaconManager
) : ViewModel(), BeaconConsumer {

    val beacons: MutableLiveData<MutableList<Course>> = MutableLiveData()

    init {
        beaconManager.bind(this)
    }

    override fun onCleared() {
        super.onCleared()
        beaconManager.unbind(this)
    }

    private fun getCourseDetail(uuid: Int): Course {
        return Course(1, "Course Name", "B4", "Instructor Name")
    }

    override fun onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers()
        beaconManager.addRangeNotifier { detectedBeacons, _ ->
            if (detectedBeacons.isNotEmpty()) {
                Log.i(TAG, "Detected ${detectedBeacons.count()} beacons:")
                val courseList: MutableList<Course> = mutableListOf()
                for (beacon in detectedBeacons) {
                    Log.i(TAG, beacon.serviceUuid.toString())
                    val course = getCourseDetail(beacon.serviceUuid)
                    courseList.add(course)
                }
                beacons.postValue(courseList)
                beaconManager.removeAllRangeNotifiers()
            }
        }
        beaconManager.startRangingBeaconsInRegion(
            Region("wildcard-region", null, null, null)
        )
    }

    override fun getApplicationContext(): Context {
        return activity.applicationContext
    }

    override fun unbindService(serviceConnection: ServiceConnection?) {
        if (serviceConnection != null) {
            activity.unbindService(serviceConnection)
        }
    }

    override fun bindService(intent: Intent?, serviceConnection: ServiceConnection?, i: Int): Boolean {
        if (serviceConnection != null) {
            return activity.bindService(intent, serviceConnection, i)
        }
        return false
    }

    companion object {
        private const val TAG = "AvailableCoursesVM"
    }
}