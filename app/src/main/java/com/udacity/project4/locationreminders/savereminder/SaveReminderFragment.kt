package com.udacity.project4.locationreminders.savereminder

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.BuildConfig
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSaveReminderBinding
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.geofence.GeofenceBroadcastReceiver
import com.udacity.project4.locationreminders.geofence.GeofenceBroadcastReceiver.Companion.ACTION_GEOFENCE_EVENT
import com.udacity.project4.locationreminders.geofence.GeofencingConstants
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.selectreminderlocation.SelectLocationFragment
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

//class SaveReminderFragment : BaseFragment() {
//    //Get the view model this time as a single to be shared with the another fragment
//    override val _viewModel: SaveReminderViewModel by inject()
//    private lateinit var binding: FragmentSaveReminderBinding
//
//    lateinit var item:ReminderDataItem
//
//    lateinit var geofencingClient: GeofencingClient
//    private val radius = 300f
//    private val BACKGROUND_REQUEST_CODE=50
//    private val ENABLE_BOTH_PERMISSIONS=70
//    private val geofencePendingIntent: PendingIntent by lazy {
//        val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
//        intent.action = ACTION_GEOFENCE_EVENT
//        PendingIntent.getBroadcast(requireContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//    }
//    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >=
//            android.os.Build.VERSION_CODES.Q
//
//    val REQUEST_TURN_DEVICE_LOCATION_ON=2
//    private val REQUEST_LOCATION_PERMISSION = 1
//
//    companion object {
//        private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
//        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
//        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
//
//    }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding =
//            DataBindingUtil.inflate(inflater, R.layout.fragment_save_reminder, container, false)
//
//        setDisplayHomeAsUpEnabled(true)
//
//        geofencingClient = LocationServices.getGeofencingClient(requireActivity())
//        binding.viewModel = _viewModel
//
//        return binding.root
//    }
//    @RequiresApi(Build.VERSION_CODES.Q)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.lifecycleOwner = this
//        binding.selectLocation.setOnClickListener {
//            //            Navigate to another fragment to get the user location
//            _viewModel.navigationCommand.value =
//                NavigationCommand.To(SaveReminderFragmentDirections.actionSaveReminderFragmentToSelectLocationFragment())
//        }
//
//
//
//
//
//        binding.saveReminder.setOnClickListener {
//            val title = _viewModel.reminderTitle.value
//            val description = _viewModel.reminderDescription.value
//            val location = _viewModel.reminderSelectedLocationStr.value
//            val latitude = _viewModel.latitude.value
//            val longitude = _viewModel.longitude.value
//            item= ReminderDataItem(title,description,location,latitude,longitude)
//
//         //   checkDeviceLocationSettingsAndStartGeofence()
//                if(_viewModel.validateAndSaveReminder(item)) {
//                    if (foregroundAndBackgroundLocationPermissionApproved()) {
//                        checkDeviceLocationSettingsAndStartGeofence()
//                    } else {
//                        requestPermissions()
//                    }
//                }
//        }
//    }
//    @RequiresApi(Build.VERSION_CODES.Q)
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    )
//    {
//        if (requestCode== ENABLE_BOTH_PERMISSIONS) {
//            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(
//                    requireContext(),
//                    "we need background and foreground permissions to add geofence",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                checkDeviceLocationSettingsAndStartGeofence()
//            }
//        }
//        else if (requestCode == BACKGROUND_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(requireContext(), "we need background permission to add geofence", Toast.LENGTH_SHORT).show()
//            }else{
//                checkDeviceLocationSettingsAndStartGeofence()
//            }
//        }
//    }
//    @RequiresApi(Build.VERSION_CODES.Q)
//    fun requestPermissions() {
//        if (foregroundAndBackgroundLocationPermissionApproved())
//            return
//        var array = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
//        val request =
//            if (runningQOrLater) {
//                array += Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
//            }
//        else
//            REQUEST_PERMISSIONS_REQUEST_CODE
//
//        requestPermissions(
//            array,
//            request
//        )
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
//            checkDeviceLocationSettingsAndStartGeofence(false)
//        }
//    }
//    @RequiresApi(Build.VERSION_CODES.Q)
//    @TargetApi(29)
//    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
//        val foregroundLocationApproved = ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//        if (foregroundLocationApproved ) {
//            val backgroundLocationApproved = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
//            if (backgroundLocationApproved)
//            {
//                return true
//            }
//            else
//            {
//                requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), BACKGROUND_REQUEST_CODE)
//            }
//        }
//        else
//        {
//            requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),ENABLE_BOTH_PERMISSIONS)
//        }
//        return false
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun checkDeviceLocationSettingsAndStartGeofence(resolve:Boolean = true) {
//        val locationRequest = LocationRequest.create().apply {
//            priority = LocationRequest.PRIORITY_LOW_POWER
//        }
//        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
//        val settingsClient = LocationServices.getSettingsClient(requireContext())
//        val locationSettingsResponseTask =
//            settingsClient.checkLocationSettings(builder.build())
//        locationSettingsResponseTask.addOnSuccessListener {
//            addGeofence(item,radius)
//        }
//        locationSettingsResponseTask.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException && resolve){
//                try {
//                    startIntentSenderForResult(exception.resolution.intentSender!!,REQUEST_TURN_DEVICE_LOCATION_ON,null,0,0,0,null)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
//                }
//            } else {
//                Log.d("check","checkDeviceLocationSettingsAndStartGeofence")
//                Snackbar.make(
//                    binding.container,
//                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
//                ).setAction(android.R.string.ok) {
//                    checkDeviceLocationSettingsAndStartGeofence()
//                }.show()
//            }
//        }
////        locationSettingsResponseTask.addOnCompleteListener {
////            if ( it.isSuccessful ) {
////                addGeofence()
////            }
////        }
//    }
//    @SuppressLint("MissingPermission")
//    fun addGeofence(item: ReminderDataItem, radius: Float)
//    {
//        val geofence = Geofence.Builder()
//            .setRequestId(item.id)
//            .setCircularRegion(item.latitude!!,
//                item.longitude!!,
//                GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
//            )
//            .setExpirationDuration(Geofence.NEVER_EXPIRE)
//            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
//            .build()
//
//        val geofencingRequest = GeofencingRequest.Builder()
//            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//            .addGeofence(geofence)
//            .build()
//
//        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
//                addOnSuccessListener {
//                    Toast.makeText(requireContext(), "geofence added", Toast.LENGTH_SHORT).show()
//                    Log.e("Add Geofence", geofence.requestId)
//                    _viewModel.saveReminder(item)
//                }
//                addOnFailureListener {
//                    Toast.makeText(requireContext(), R.string.geofences_not_added, Toast.LENGTH_SHORT).show()
//                    if ((it.message != null)) {
//                        Log.w(TAG, it.message!!)
//                    }
//                }
//            }
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        //make sure to clear the view model after destroy, as it's a single view model.
//        _viewModel.onClear()
//    }
//}



private const val FINE_AND_BACKGROUND_LOCATIONS_REQUEST_CODE = 33
private const val FINE_LOCATION_REQUEST_CODE  = 34
private const val FINE_LOCATION_PERMISSION_INDEX = 0
private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
private const val ACTION_GEOFENCE_EVENT = "SaveReminderFragment.project4.action.ACTION_GEOFENCE_EVENT"
private const val TURN_DEVICE_LOCATION_ON_REQUEST_CODE = 29
const val GEOFENCE_RADIUS_IN_METERS = 100f
class SaveReminderFragment : BaseFragment() {
    //Get the view model this time as a single to be shared with the another fragment
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSaveReminderBinding

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    private lateinit var reminderDataItem : ReminderDataItem

    private lateinit var geofencingClient: GeofencingClient

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
        intent.action = GeofenceBroadcastReceiver.ACTION_GEOFENCE_EVENT
        PendingIntent.getBroadcast(
            requireContext(),
            0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_save_reminder, container, false)

        setDisplayHomeAsUpEnabled(true)

        binding.viewModel = _viewModel

        geofencingClient = LocationServices.getGeofencingClient(requireActivity())

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.selectLocation.setOnClickListener {
            // Navigate to another fragment to get the user location
            _viewModel.navigationCommand.value = NavigationCommand.To(
                SaveReminderFragmentDirections.actionSaveReminderFragmentToSelectLocationFragment())
        }

        // save button
        binding.saveReminder.setOnClickListener {
            val title = _viewModel.reminderTitle.value
            val description = _viewModel.reminderDescription.value
            val location = _viewModel.reminderSelectedLocationStr.value
            val latitude = _viewModel.latitude.value
            val longitude = _viewModel.longitude.value


            reminderDataItem = ReminderDataItem(title, description, location, latitude, longitude)

            if(_viewModel.validateEnteredData(reminderDataItem)) {

                if (fineAndBackgroundLocationPermissionsApproved()) {
                    checkDeviceLocationSettingsAndStartGeofence()
                } else {
                    requestFineAndBackgroundLocationPermissions()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _viewModel.onClear()
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestFineAndBackgroundLocationPermissions() {
        if (fineAndBackgroundLocationPermissionsApproved())
            return
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val requestCode = when {
            runningQOrLater -> {
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                FINE_AND_BACKGROUND_LOCATIONS_REQUEST_CODE
            }
            else -> FINE_LOCATION_REQUEST_CODE
        }

        requestPermissions(permissionsArray, requestCode)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun fineAndBackgroundLocationPermissionsApproved(): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION))

        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }

        return foregroundLocationApproved && backgroundPermissionApproved
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantedResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        if (grantedResults.isEmpty() ||
            grantedResults[FINE_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == FINE_AND_BACKGROUND_LOCATIONS_REQUEST_CODE &&
                    grantedResults[BACKGROUND_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED)
        ) {

            _viewModel.showSnackBarInt.value = R.string.permission_denied_explanation

        } else {

            checkDeviceLocationSettingsAndStartGeofence()
        }
    }

    private fun checkDeviceLocationSettingsAndStartGeofence(resolve:Boolean = true) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve){
                try {
                    startIntentSenderForResult(
                        exception.resolution.intentSender,
                        TURN_DEVICE_LOCATION_ON_REQUEST_CODE,
                        null,
                        0,
                        0,
                        0,
                        null)

                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("<<1>>", "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    requireView(),
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartGeofence()
                }.show()
            }
        }

        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                startGeoFence()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startGeoFence() {
        val geofence = Geofence.Builder()
            .setRequestId(reminderDataItem.id)
            .setCircularRegion(
                reminderDataItem.latitude!!,
                reminderDataItem.longitude!!,
                GEOFENCE_RADIUS_IN_METERS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
            addOnSuccessListener {
                _viewModel.saveReminder(reminderDataItem)
            }
            addOnFailureListener {
                _viewModel.showSnackBarInt.value = R.string.error_adding_geofence
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TURN_DEVICE_LOCATION_ON_REQUEST_CODE) {
            checkDeviceLocationSettingsAndStartGeofence(false)
        }
    }
}