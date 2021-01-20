package com.jurengis.rollcall.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Singleton
    @Provides
    fun provideBeaconManager(
        @ApplicationContext context: Context
    ): BeaconManager {
        val iBeaconLayout = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
        return BeaconManager.getInstanceForApplication(context).apply {
            beaconParsers.add(BeaconParser().setBeaconLayout(iBeaconLayout))
        }
    }
}