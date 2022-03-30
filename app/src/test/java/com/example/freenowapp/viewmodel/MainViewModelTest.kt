package com.example.freenowapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.freenowapp.repository.MainRepository
import com.example.freenowapp.repository.MockMainRepository
import com.example.freenowapp.utils.Status
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val coroutineDispatcher = UnconfinedTestDispatcher()


    @ExperimentalCoroutinesApi
    @get:Rule val coroutineTestRule = CoroutinesTestRule()

    lateinit var mainRepository: MainRepository

    lateinit var mainViewModel : MainViewModel

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mainRepository = MockMainRepository()
        mainViewModel = MainViewModel(mainRepository, coroutineDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun gettingCoordinates() = runTest{

        mainViewModel.getVehicles(53.694865,
            9.757589 , 53.394655, 10.099891)

        val value = mainViewModel.mVehicles.value
        Assert.assertEquals(value?.status,
            Status.SUCCESS)

        assertNotNull(value?.data)
        assertNull(value?.message)

        val poiList = value?.data?.first()
        poiList?.run {
            Assert.assertEquals(coordinate.latitude, 123.0, 0.0)
            Assert.assertEquals(coordinate.longitude, 34.22, 0.0)
            Assert.assertEquals(fleetType,  "well")
            Assert.assertEquals(id, 10)
        }


    }
}