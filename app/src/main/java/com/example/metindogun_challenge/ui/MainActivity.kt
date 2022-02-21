package com.example.metindogun_challenge.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.metindogun_challenge.databinding.ActivityMainBinding
import com.example.metindogun_challenge.service.ForegroundService
import com.example.metindogun_challenge.ui.adapter.PhotoListAdapter
import com.example.metindogun_challenge.utils.isLocationEnabled
import com.example.metindogun_challenge.utils.isPermissionGranted
import com.example.metindogun_challenge.utils.shouldProvideRationale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        setUI()

    }

    private fun setToolbar(){
        setSupportActionBar(binding.toolbar)
    }

    private fun setUI() {

        val photoListAdapter = PhotoListAdapter()
        binding.photosRView.adapter = photoListAdapter
        lifecycleScope.launch {
            mainViewModel.photoList.collect {
                photoListAdapter.submitList(it.toMutableList())
                binding.photosRView.smoothScrollToPosition(0)
            }
        }

        mainViewModel.locationUpdate.onEach { uiState ->
            when(uiState){
                UIState.Started -> {
                    binding.startBtn.text = "STOP"
                }
                UIState.Stopped -> {
                    binding.startBtn.text = "START"
                    stopForegroundService()
                }
            }
        }.launchIn(lifecycleScope)

        binding.startBtn.setOnClickListener {
            startStopLocationUpdates()
        }
    }

    private fun startForegroundService(){
        ContextCompat.startForegroundService(this, Intent(this, ForegroundService::class.java))
    }

    private fun stopForegroundService(){
        stopService(Intent(this, ForegroundService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForegroundService()
    }

    private fun startStopLocationUpdates(){
        when(mainViewModel.locationUpdate.value){
            is UIState.Started -> {
                mainViewModel.stopLocationUpdates()
                stopForegroundService()
            }
            is UIState.Stopped -> {
                startLocationUpdates()
            }
        }
    }

    private fun startLocationUpdates(){

        when {
            isPermissionGranted() -> {
                if (isLocationEnabled()) {
                    startForegroundService()
                    mainViewModel.startLocationUpdates()
                } else {
                    Toast.makeText(this, "Please enable location settings", Toast.LENGTH_SHORT).show()
                }
            }

            shouldProvideRationale() -> {
                Toast.makeText(this, "Please enable permissions", Toast.LENGTH_SHORT).show()
            }

            else -> {
                requestLocationPermission()
            }
        }
    }

    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            startLocationUpdates()
        }
    }

    private fun requestLocationPermission() {

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

}