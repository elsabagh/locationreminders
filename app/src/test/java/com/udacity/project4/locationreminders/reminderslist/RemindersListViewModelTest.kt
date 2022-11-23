package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.savereminder.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.ERROR_MSG
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RemindersListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var firstReminder = ReminderDTO("location_title", "description","selected_location",(-50..50).random().toDouble(),(-50..50).random().toDouble())
    private var remindersList = mutableListOf(firstReminder,firstReminder,firstReminder,firstReminder)
    private lateinit var fakeDataSource: FakeDataSource
    private  var reminderListViewModel: RemindersListViewModel?=null
    @Before
    fun setUp() {
        fakeDataSource = FakeDataSource(remindersList)
        reminderListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    @After
    fun tearDown() {
        stopKoin()
        fakeDataSource = FakeDataSource(null)
        reminderListViewModel = null
    }


    @Test
    fun `load reminders with correct reminders return success`() {
        mainCoroutineRule.pauseDispatcher()
        reminderListViewModel!!.loadReminders()
        assertThat(reminderListViewModel!!.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        assertThat(reminderListViewModel!!.showLoading.getOrAwaitValue(), `is`(false))
    }
    @Test
    fun `load reminders with null reminders return exception error`() {
        fakeDataSource = FakeDataSource(null)
        fakeDataSource.setError(true)
        reminderListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
        reminderListViewModel!!.loadReminders()
        assertThat(reminderListViewModel!!.showLoading.getOrAwaitValue(), `is`(false))
        assertThat(reminderListViewModel!!.showSnackBar.getOrAwaitValue(), Matchers.`is`("Test Exception"))

    }

    @Test
    fun `load reminders with list of reminders return number of reminders in list`() {
        reminderListViewModel!!.loadReminders()
        assertThat(reminderListViewModel!!.remindersList.getOrAwaitValue().size, `is`(remindersList.size))
    }
    @Test
    fun `load reminders check error `(){
        fakeDataSource.setError(true)
        reminderListViewModel!!.loadReminders()
        assertThat(reminderListViewModel!!.showSnackBar.value, Matchers.`is`("Test Exception"))
    }
}
