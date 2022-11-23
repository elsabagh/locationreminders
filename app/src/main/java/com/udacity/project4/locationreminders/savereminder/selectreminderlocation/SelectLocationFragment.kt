//package com.udacity.project4.locationreminders.savereminder.selectreminderlocation
//
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.annotation.TargetApi
//import android.app.Activity
//import android.app.PendingIntent
//import android.content.Intent
//import android.content.IntentSender
//import android.content.pm.PackageManager
//import android.content.res.Resources
//import android.net.Uri
//import android.os.Bundle
//import android.provider.Settings
//import android.util.Log
//import android.view.*
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.databinding.DataBindingUtil
//import androidx.navigation.fragment.findNavController
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.common.api.ResolvableApiException
//import com.google.android.gms.location.*
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.*
//import com.google.android.material.snackbar.Snackbar
//import com.udacity.project4.BuildConfig
//import com.udacity.project4.R
//import com.udacity.project4.base.BaseFragment
//import com.udacity.project4.base.NavigationCommand
//import com.udacity.project4.databinding.FragmentSelectLocationBinding
//import com.udacity.project4.locationreminders.geofence.GeofenceBroadcastReceiver
//import com.udacity.project4.locationreminders.geofence.GeofenceBroadcastReceiver.Companion.ACTION_GEOFENCE_EVENT
//import com.udacity.project4.locationreminders.geofence.GeofencingConstants
//import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
//
//import org.koin.android.ext.android.inject
//import java.util.*
//
//class SelectLocationFragment : BaseFragment(),OnMapReadyCallback {
//    override val _viewModel: SaveReminderViewModel by inject()
//    private lateinit var binding: FragmentSelectLocationBinding
//    private val TAG = SelectLocationFragment::class.java.simpleName
//    lateinit var map:GoogleMap
//    companion object { private const val REQUEST_PERMISSIONS_REQUEST_CODE = 3 }
//
//    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
//    {
//        binding =
//            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)
//
//        binding.viewModel = _viewModel
//        binding.lifecycleOwner = this
//
//        setHasOptionsMenu(true)
//  //      setDisplayHomeAsUpEnabled(true)
//
//
//        val fragment=childFragmentManager.findFragmentById(binding.mapFragment.id) as SupportMapFragment
//        fragment.getMapAsync(this)
//
//
//        binding.saveBtn.setOnClickListener {
//            findNavController().popBackStack()
//        }
//
//        return binding.root
//    }
//    override fun onMapReady(googleMap: GoogleMap)
//    {
//        map=googleMap
//
//        setMapLongClick(map)
//        setPoiClick(map)
//        setMapStyle(map)
//
//        /*   val latitude = 37.422160
//        val longitude = -122.084270
//        val zoomLevel = 15f
//
//
//        val homeLatLng = LatLng(latitude, longitude)
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
//        map.addMarker(MarkerOptions().position(homeLatLng))*/
//
//        //enableMyLocation()
//   //     foregroundAndBackgroundLocationPermissionApproved()
//
//        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)){
//            enableMapLocation()
//            //checkDeviceLocationSettingsAndStartGeofence()
//        }else{
//            requestPermissions(
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_PERMISSIONS_REQUEST_CODE
//            )
//        }
//
//
//
//    }
//    private fun setMapLongClick(map:GoogleMap) {
//        map.setOnMapLongClickListener { latLng ->
//
//            map.clear()
//            val snippet = String.format(
//                Locale.getDefault(),
//                "Lat: %1$.5f, Long: %2$.5f",
//                latLng.latitude,
//                latLng.longitude
//            )
//            map.addMarker(
//                MarkerOptions()
//                    .position(latLng)
//                    .title(snippet)
//            )
//            _viewModel.latitude.value = latLng.latitude
//            _viewModel.longitude.value = latLng.longitude
//            _viewModel.reminderSelectedLocationStr.value = snippet
//
//
//          //  onLocationSelected()
//        }
//    }
//    private fun setPoiClick(map: GoogleMap) {
//
//        map.setOnPoiClickListener { poi ->
//            map.clear()
//            val poiMarker = map.addMarker(
//                MarkerOptions()
//                    .position(poi.latLng)
//                    .title(poi.name)
//            )
//            poiMarker.showInfoWindow()
//            _viewModel.latitude.value = poi.latLng.latitude
//            _viewModel.longitude.value = poi.latLng.longitude
//            _viewModel.reminderSelectedLocationStr.value = poi.name
//        }
//    }
//
//    private fun setMapStyle(map: GoogleMap) {
//        try {
//            // Customize the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            val success = map.setMapStyle(
//                MapStyleOptions.loadRawResourceStyle(
//                    requireContext(),
//                    R.raw.map_style
//                )
//            )
//
//            if (!success) {
//                Log.e(TAG, "Style parsing failed.")
//            }
//        } catch (e: Resources.NotFoundException) {
//            Log.e(TAG, "Can't find style. Error: ", e)
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.map_options, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//
//        R.id.normal_map -> {
//            map.mapType = GoogleMap.MAP_TYPE_NORMAL
//            true
//        }
//        R.id.hybrid_map -> {
//            map.mapType = GoogleMap.MAP_TYPE_HYBRID
//            true
//        }
//        R.id.satellite_map -> {
//            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
//            true
//        }
//        R.id.terrain_map -> {
//            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
//            true
//        }
//        else -> super.onOptionsItemSelected(item)
//    }
//
//
//
//    /////////////////////////
//
//
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        Log.d(TAG, "onRequestPermissionResult")
//
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//
//            if (grantResults.isNotEmpty() &&
//                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                enableMapLocation()
//
//            } else {
//                binding.saveBtn.isEnabled=false
//                Snackbar.make(
//                    binding.container1,
//                    R.string.permission_denied_explanation,
//                    Snackbar.LENGTH_INDEFINITE
//                )
//                    .setAction(R.string.settings) {
//                        startActivity(Intent().apply {
//                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        })
//                    }.show()
//               // _viewModel.showSnackBarInt.value = R.string.permission_denied_explanation
//            }
//
//            /*
//
//            if (grantResults.isNotEmpty()
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                //checkDeviceLocationSettingsAndStartGeofence()
//                enableMapLocation()
//            } else {
//                Snackbar.make(
//                    binding.container1,
//                    R.string.permission_denied_explanation,
//                    Snackbar.LENGTH_INDEFINITE
//                )
//                    .setAction(R.string.settings) {
//                        startActivity(Intent().apply {
//                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        })
//                    }.show()
//            }*/
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun enableMapLocation() {
//        map.setMyLocationEnabled(true)
//    }
//
//
//
//}



package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject
import com.google.android.gms.location.*
import com.udacity.project4.BuildConfig


class SelectLocationFragment : BaseFragment() {
    private lateinit var googleMap: GoogleMap
    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    //latlng of user's current location
    private  var latitude : Double? = null
    private var longitude : Double? = null
    //location & latlng for the selected location
    private var selectedLocation: String? = null
    private var selectedLatitude : Double? = null
    private var selectedLongitude : Double? = null
    private val TAG = SelectLocationFragment::class.java.simpleName

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        checkForeGroundLocationPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = getChildFragmentManager()
            .findFragmentById(binding.mapFragment.id) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback(){ map ->
            googleMap = map
            checkForeGroundLocationPermission()
            getLocation()
            setMapLongClick(map)
            setPoiClick(map)
            setMapStyle(map)
            enableMyLocation()
            map.setOnMyLocationButtonClickListener{
                enableMyLocation()
                false
            }
        })
        binding.saveBtn.setOnClickListener {onLocationSelected()}
    }
    //pass selected location to viewModel
    private fun onLocationSelected() {
        _viewModel.reminderSelectedLocationStr.value = selectedLocation
        _viewModel.latitude.value = selectedLatitude
        _viewModel.longitude.value = selectedLongitude
        _viewModel.navigationCommand.value = NavigationCommand.Back
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }
    //user can change map type from menu
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.normal_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
    //When user long clicks the map add a marker and update selected location to this location
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
            selectedLocation = latLng.toString()
            selectedLatitude = latLng.latitude
            selectedLongitude = latLng.longitude
        }
    }
    //When user clicks a POI add a marker and update selected location to this location with poi name
    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            selectedLocation = poi.name
            selectedLatitude = poi.latLng.latitude
            selectedLongitude = poi.latLng.longitude
        }
    }
    //get user's location
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult !=null){
                    Log.i(TAG, "Got User's location")
                    latitude = locationResult.locations.get((locationResult.locations.size).minus(1)).latitude
                    longitude = locationResult.locations.get((locationResult.locations.size).minus(1)).longitude
                    Log.i(TAG , "location is $latitude $longitude")
                    val latLng = LatLng(latitude ?: -33.870453, longitude?:151.208755)
                    val zoom = 18f
                    googleMap.addMarker(MarkerOptions().
                    position(latLng))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
                }
            }
        }
        getFusedLocationProviderClient(requireContext())
            .requestLocationUpdates(locationRequest, locationCallback , Looper.myLooper())
    }
    //set the style of the map
    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    /*
     *  Uses the Location Client to check the current state of location settings, and gives the user
     *  the opportunity to turn on location services within our app.
     */
    @SuppressLint("MissingPermission")
    private fun checkDeviceLocationSettings(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireContext())
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    startIntentSenderForResult(exception.resolution.intentSender,
                        REQUEST_TURN_DEVICE_LOCATION_ON, null, 0, 0, 0,null)

                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    this.requireView()!!,
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettings()
                }.show()
            }
        }
        locationSettingsResponseTask.addOnSuccessListener {
            getLocation()
        }
    }
    fun checkForeGroundLocationPermission(){
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ))
        if (foregroundLocationApproved == true){
            return
        }
        val resultCode = REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE

        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        requestPermissions(
            permissionsArray,
            resultCode
        )
    }
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult")

        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED) {
            Snackbar.make(
                this.requireView()!!,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
            checkDeviceLocationSettings()
            googleMap.isMyLocationEnabled = true
        }
    }
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if ((PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION))) {
            googleMap.isMyLocationEnabled = true
            checkDeviceLocationSettings()
        }
        else {
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

}
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
private const val LOCATION_PERMISSION_INDEX = 0
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34