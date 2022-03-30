package com.example.freenowapp.repository

import com.example.freenowapp.api.ApiHelper
import com.example.freenowapp.model.Coordinate
import com.example.freenowapp.model.Poi
import com.example.freenowapp.model.Response
import javax.inject.Inject


interface MainRepository{
    suspend fun getVehicles(p1Lat: Double, p1Lon: Double, p2Lat: Double, p2Lon: Double): Response
}

class MainRepositoryImpl @Inject constructor(private val apiHelper: ApiHelper): MainRepository {
    override suspend fun getVehicles(p1Lat: Double, p1Lon: Double, p2Lat: Double, p2Lon: Double) = apiHelper.getVehicles(
        p1Lat, p1Lon, p2Lat, p2Lon
    )
}

class MockMainRepository : MainRepository{
    override suspend fun getVehicles(
        p1Lat: Double,
        p1Lon: Double,
        p2Lat: Double,
        p2Lon: Double
    ): Response {
        // delay(5000)
        val poi = Poi(
            Coordinate(123.0, 34.22),
            "well" , 53.3, 10)
        val pois = arrayListOf<Poi>()
        pois.add(poi)
        return Response(pois)
    }

}