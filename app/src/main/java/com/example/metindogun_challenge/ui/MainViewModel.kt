package com.example.metindogun_challenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metindogun_challenge.data.repository.PhotoRepository
import com.example.metindogun_challenge.data.model.Photo
import com.example.metindogun_challenge.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val locationRepository: LocationRepository
    ): ViewModel() {

    private var _photoList = MutableStateFlow<List<Photo>>(emptyList())
    val photoList: StateFlow<List<Photo>> = _photoList

    private var _locationUpdate = MutableStateFlow<UIState>(UIState.Stopped)
    val locationUpdate: StateFlow<UIState> = _locationUpdate

    private var listenLocationJob: Job? = null
    private var timerJob: Job? = null

    fun startLocationUpdates(){

        _locationUpdate.tryEmit(UIState.Started)
        startTimer()

        listenLocationJob = viewModelScope.launch {

            locationRepository.getLocationUpdates().collect { location ->
                photoRepository.getLocationPhoto(location.latitude.toString(), location.longitude.toString())?.let { photo->
                    val list = _photoList.value.toMutableList()
                    list.add(0,photo)
                    _photoList.tryEmit(list)
                }
            }
        }
    }

    fun stopLocationUpdates(){
        listenLocationJob?.cancel()
        listenLocationJob = null
        timerJob?.cancel()
        timerJob = null
        _locationUpdate.tryEmit(UIState.Stopped)
    }

    private fun startTimer(){
        timerJob = viewModelScope.launch {
            delay(2*60*60*1000)
            stopLocationUpdates()
        }
    }

    override fun onCleared() {
        stopLocationUpdates()
        super.onCleared()
    }

}

sealed class UIState{
    object Started : UIState()
    object Stopped : UIState()
}