//package com.udacity.project4
//
//import android.app.Activity
//import android.app.Application
//import android.app.PendingIntent.getActivity
//import android.view.View
//import android.widget.Toast
//import androidx.test.core.app.ActivityScenario
//import androidx.test.core.app.ApplicationProvider.getApplicationContext
//import androidx.test.core.app.launchActivity
//import androidx.test.espresso.Espresso
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.ViewAssertion
//import androidx.test.espresso.ViewInteraction
//import androidx.test.espresso.action.ViewActions
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.assertion.ViewAssertions
//import androidx.test.espresso.matcher.RootMatchers.withDecorView
//import androidx.test.espresso.matcher.ViewMatchers
//import androidx.test.espresso.matcher.ViewMatchers.*
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.LargeTest
//import com.udacity.project4.locationreminders.RemindersActivity
//import com.udacity.project4.locationreminders.data.ReminderDataSource
//import com.udacity.project4.locationreminders.data.local.LocalDB
//import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
//import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
//import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.test.runBlockingTest
//import org.hamcrest.CoreMatchers.`is`
//import org.hamcrest.CoreMatchers.not
//import org.hamcrest.core.IsNot
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.koin.androidx.viewmodel.dsl.viewModel
//import org.koin.core.context.startKoin
//import org.koin.core.context.stopKoin
//import org.koin.dsl.module
//import org.koin.test.AutoCloseKoinTest
//import org.koin.test.get
//import org.mockito.ArgumentMatchers.matches
//import java.util.EnumSet.allOf
//
//@RunWith(AndroidJUnit4::class)
//@LargeTest
////END TO END test to black box test the app
//class RemindersActivityTest :
//    AutoCloseKoinTest() {// Extended Koin Test - embed autoclose @after method to close Koin after every test
//
//    private lateinit var repository: ReminderDataSource
//    private lateinit var appContext: Application
//
//    lateinit var scenario: ActivityScenario<RemindersActivity>
//
//    lateinit var activity: RemindersActivity
//
//    /**
//     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
//     * at this step we will initialize Koin related code to be able to use it in out testing.
//     */
//    @Before
//    fun init() {
//        scenario = ActivityScenario.launch(RemindersActivity::class.java)
//        stopKoin()//stop the original app koin
//        appContext = getApplicationContext()
//        val myModule = module {
//            viewModel {
//                RemindersListViewModel(
//                    appContext,
//                    get() as ReminderDataSource
//                )
//            }
//            single {
//                SaveReminderViewModel(
//                    appContext,
//                    get() as ReminderDataSource
//                )
//            }
//            single { RemindersLocalRepository(get()) as ReminderDataSource }
//            single { LocalDB.createRemindersDao(appContext) }
//        }
//        //declare a new koin module
//        startKoin {
//            modules(listOf(myModule))
//        }
//        //Get our real repository
//        repository = get()
//
//        //clear the data to start fresh
//        runBlocking {
//            repository.deleteAllReminders()
//        }
//
//    }
//
//
//    @Test
//    fun testSnackBar() {
//        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
//        onView(withId(R.id.addReminderFAB)).perform(click())
//        onView(withId(R.id.saveReminder)).perform(click())
//        onView(withText(R.string.err_enter_title)).check(ViewAssertions.matches(isDisplayed()))
//        activityScenario.close()
//    }
//
//    @Test
//    fun testToast()  {
//
//            onView(withId(R.id.addReminderFAB)).perform(click())
//
//            onView(withId(R.id.selectLocation)).perform(click())
//            onView(withId(R.id.map_fragment)).perform(ViewActions.longClick())
//            onView(withId(R.id.save_btn)).perform(click())
//
//            onView(withId(R.id.reminderTitle)).perform(ViewActions.replaceText("AAA"))
//            onView(withId(R.id.reminderDescription)).perform(ViewActions.replaceText("AAA"))
//
//            onView(withId(R.id.saveReminder)).perform(click())
//            //onView(withText(R.string.geofences_added)).inRoot(withDecorView(not(is(getActivity().getWindow().getDecorView())))).check(matches(isDisplayed());
//
//        //i can't handle this step app never terminate
//                scenario.onActivity {
//                    onView(withText(R.string.reminder_saved)).inRoot(
//                        withDecorView(
//                            not(
//                                `is`(
//                                    it.window.decorView
//                                )
//                            )
//                        )
//                    ).check(ViewAssertions.matches(isDisplayed()))
//                    scenario.close()
//                }
//                //  onView(withText(R.string.reminder_saved)).check(ViewAssertions.matches(isDisplayed()))
//
//
//    }
//
//
//    @After
//    fun close() {
//
//    }
//
//
////    fun activity(scenario: ActivityScenario<RemindersActivity>): Activity {
////        scenario.onActivity {
////            activity = it
////        }
////        return activity
////
////    }
//
//
//}

package com.udacity.project4
import android.app.Activity
import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorActivity

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.get
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get



@RunWith(AndroidJUnit4::class)
@LargeTest
//END TO END test to black box test the app
class RemindersActivityTest :
    AutoCloseKoinTest() {
    // Extended Koin Test - embed autoclose @after method to close Koin after every test

    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application
    private val dataBindingIdlingResource = DataBindingIdlingResource()


    // get activity context
    private fun getActivity(activityScenario: ActivityScenario<RemindersActivity>): Activity? {
        var activity: Activity? = null
        activityScenario.onActivity {
            activity = it
        }
        return activity
    }

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
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
        repository = get()

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }


    //    TODO: add End to End testing to the app
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun addNewReminder_ReminderListShowsNewReminderAndToast() = runBlocking {

// GIVEN - an empty ReminderList
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        val activity = getActivity(activityScenario)


        // WHEN - new Reminder added

        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).perform(replaceText("Title"))
        onView(withId(R.id.reminderDescription)).perform(replaceText("Description"))
        onView(withId(R.id.selectLocation)).perform(click())
        onView(withId(R.id.map_fragment)).perform(longClick())
        onView(withId(R.id.save_btn)).perform(click())
        onView(withId(R.id.saveReminder)).perform(click())

        onView(withText(R.string.reminder_saved)).inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
            .check(matches(isDisplayed()))
        onView(withText("Title")).check(matches(isDisplayed()))

        runBlocking {
            delay(19000)
        }

    }
    @Test
    fun addNewReminderWithoutLocation_ShowsSnackbar() = runBlocking {

        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // GIVEN - an SaveReminder Screen without Selected Location
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).perform(replaceText("Title"))
        onView(withId(R.id.reminderDescription)).perform(replaceText("Description"))

        //  WHEN - click saveButton
        onView(withId(R.id.saveReminder)).perform(click())

        // THEN - shows Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(isDisplayed()))

        runBlocking {
            delay(3000)
        }

    }


}
