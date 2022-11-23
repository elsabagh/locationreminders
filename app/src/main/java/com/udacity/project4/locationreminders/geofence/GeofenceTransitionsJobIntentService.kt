package com.udacity.project4.locationreminders.geofence

import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.geofence.GeofenceBroadcastReceiver.Companion.ACTION_GEOFENCE_EVENT
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.utils.sendNotification
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }

    override fun onHandleWork(intent: Intent) {
        val geoFenceEvent = GeofencingEvent.fromIntent(intent)
        when (geoFenceEvent?.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                geoFenceEvent.triggeringGeofences?.let {
                    sendNotification(it)
                }
            }
        }
    }
//        if (geofencingEvent.hasError()) {
//            return
//        }
//
//        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
//            val fenceId = when {
//                geofencingEvent.triggeringGeofences.isNotEmpty() ->
//                    geofencingEvent.triggeringGeofences[0].requestId
//                else -> {
//                    Log.e("TAG", "No Geofence Trigger Found! Abort mission!")
//                    return
//                }
//            }
//            val foundIndex = GeofencingConstants.LANDMARK_DATA.indexOfFirst {
//                it.id == fenceId
//            }
//            if ( -1 == foundIndex ) {
//                Log.e("TAG", "Unknown Geofence: Abort Mission")
//                return
//            }
//
//            sendNotification(geofencingEvent.triggeringGeofences)
//        }

    private fun sendNotification(triggeringGeofences: List<Geofence>) {
        triggeringGeofences.forEach {
            val requestId = it.requestId
            val remindersLocalRepository: ReminderDataSource by inject()
            CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                val result = remindersLocalRepository.getReminder(requestId)
                if (result is Result.Success<ReminderDTO>) {
                    val reminderDTO = result.data
                    //send a notification to the user with the reminder details
                    sendNotification(
                        this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                            reminderDTO.title,
                            reminderDTO.description,
                            reminderDTO.location,
                            reminderDTO.latitude,
                            reminderDTO.longitude,
                            reminderDTO.id
                        )
                    )
                }
            }
        }
    }
}
