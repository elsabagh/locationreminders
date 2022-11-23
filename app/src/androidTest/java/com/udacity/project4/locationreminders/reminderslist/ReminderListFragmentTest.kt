package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.core.content.MimeTypeFilter.matches
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import net.bytebuddy.implementation.bind.annotation.Argument
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

import org.mockito.Mockito.*
import org.w3c.dom.Text

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.core.IsNot.not


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest() {

        lateinit var repository: ReminderDataSource
        private lateinit var appContext: Application

        @Before
        fun init() {
            stopKoin()//stop the original app koin
            appContext = getApplicationContext()
            val myModule = module {
                viewModel {
                    RemindersListViewModel(
                        appContext,
                        get() as ReminderDataSource
                    )
                }
                single {
                    SaveReminderViewModel(
                        appContext,
                        get() as ReminderDataSource
                    )
                }
                single { RemindersLocalRepository(get()) as ReminderDataSource }
                single { LocalDB.createRemindersDao(appContext) }
            }
            //declare a new koin module
            startKoin {
                modules(listOf(myModule))
            }
            //Get our real repository
            repository= get()

            //clear the data to start fresh
            runBlocking {
                repository.deleteAllReminders()
            }
        }

        @Test
        fun testNoDataSaved() {
            launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

            onView(withId(R.id.noDataTextView))
                .check(ViewAssertions.matches(isDisplayed()))
        }
        @Test
        fun displayData() = runBlockingTest {

            val item =ReminderDTO("title", "description", "location", 0.0, 0.0)
            runBlocking {
                repository.saveReminder(item)
            }
            launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

            onView(withId(R.id.title)).check(ViewAssertions.matches(isDisplayed()))
            onView(withId(R.id.description)).check(matches(isDisplayed()))
            onView(withId(R.id.location)).check(matches(isDisplayed()))
            onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))

            onView(withId(R.id.title)).check( matches(withText( item.title.toString())))
            onView(withId(R.id.description)).check( matches(withText( item.description.toString())))
            onView(withId(R.id.location)).check( matches(withText( item.location.toString())))



        }
        @Test
        fun clickTask_navigate() = runBlockingTest {

            /*repository.saveReminder(ReminderDTO("title", "description", "location", 0.0, 0.0))
            repository.saveReminder(ReminderDTO("title", "description", "location", 0.0, 0.0))
            repository.saveReminder(ReminderDTO("title", "description", "location", 0.0, 0.0))*/

            // GIVEN - On the home screen
            val scenario =
                launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

            val navController = mock(NavController::class.java)
            scenario.onFragment {
                Navigation.setViewNavController(it.view!!, navController)
            }

            // WHEN - Click on the first list item
            onView(withId(R.id.addReminderFAB))
                .perform(click())


            // THEN - Verify that we navigate to the first detail screen
            verify(navController).navigate(
                ReminderListFragmentDirections.toSaveReminder()
            )
        }
}
