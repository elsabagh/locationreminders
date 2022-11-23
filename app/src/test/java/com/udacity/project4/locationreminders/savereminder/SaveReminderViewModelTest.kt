package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    lateinit var saveReminderViewModel: SaveReminderViewModel
    lateinit var fakeDataSource: FakeDataSource

    @get:Rule
    val mainCoroutineRule=MainCoroutineRule()

    @Before
    fun init()
    {
        fakeDataSource= FakeDataSource()
        saveReminderViewModel= SaveReminderViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)
    }

    @Test
    fun testError()
    {
        MatcherAssert.assertThat(saveReminderViewModel.validateAndSaveReminder(ReminderDataItem("","","",0.0,0.0)),CoreMatchers.`is`(false))
        MatcherAssert.assertThat(saveReminderViewModel.validateAndSaveReminder(ReminderDataItem("a","a","a",0.0,0.0)),CoreMatchers.`is`(true))

    }

    @Test
    fun testLoading()
    {
        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(ReminderDataItem("a","a","a",0.0,0.0))
        MatcherAssert.assertThat(saveReminderViewModel.showLoading.value,CoreMatchers.`is`(true))

        mainCoroutineRule.resumeDispatcher()

        MatcherAssert.assertThat(saveReminderViewModel.showLoading.value,CoreMatchers.`is`(false))
    }


}

// https://stackoverflow.com/questions/67969753/difference-between-maincoroutinerule-and-runblocking
@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}