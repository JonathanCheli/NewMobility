package com.example.freenowapp.viewmodel

import androidx.lifecycle.*
import com.example.freenowapp.model.Poi
import com.example.freenowapp.repository.MainRepository
import com.example.freenowapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository,
                                        private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main): ViewModel() {


    private val _mVehicles = MutableLiveData<Resource<List<Poi>>>()
    val mVehicles : LiveData<Resource<List<Poi>>> = _mVehicles

    fun getVehicles(p1Lat: Double, p1Lon: Double, p2Lat: Double, p2Lon: Double) {
        _mVehicles.value = Resource.loading(data = null)
        viewModelScope.launch(coroutineDispatcher) {
            try {
                val result = mainRepository.getVehicles(p1Lat, p1Lon, p2Lat, p2Lon).poiList
                _mVehicles.postValue(Resource.success(result))
            }catch (exception: Exception){
                _mVehicles.postValue(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}