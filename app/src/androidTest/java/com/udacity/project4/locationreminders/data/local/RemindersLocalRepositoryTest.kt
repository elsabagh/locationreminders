package com.udacity.project4.locationreminders.data.local

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.ColumnInfo
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    /*private var reminderDTO2=ReminderDTO(  "title", "description","location", 0.0,0.0)
    private var reminderDTO3=ReminderDTO(  "title", "description","location", 0.0,0.0)
    private var reminderDTO4=ReminderDTO(  "title", "description","location", 0.0,0.0)
*/


    lateinit var database: RemindersDatabase
    lateinit var repository: RemindersLocalRepository




    @Before
    fun initRepo()
    {
        database=Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),RemindersDatabase::class.java).allowMainThreadQueries().build()

        repository=RemindersLocalRepository(database.reminderDao(),Dispatchers.Main)
    }

    @Test
    fun testRepo()=runBlockingTest {
        var item=ReminderDTO(  "title", "description","location", 0.0,0.0)


        runBlocking {
            repository.saveReminder(item)

            var result = repository.getReminder(item.id) as Result.Success

            assertThat(result.data==null, `is` (false))


            assertThat(item.id, `is`(result.data!!.id))
            assertThat(item.description, `is`(result!!.data.description))
            assertThat(item.title, `is`(result!!.data.title))
            assertThat(item.location, `is`(result!!.data.location))
            assertThat(item.latitude, `is`(result!!.data.latitude))
            assertThat(item.longitude, `is`(result!!.data.longitude))

            repository.deleteAllReminders()
            assertThat(repository.getReminder("hehehhe") is Result.Error, `is` (true))

            var result1=repository.getReminders() as Result.Success
            assertThat(result1.data.size, `is` (0))
            val value=repository.getReminder("fake") as Result.Error
            //Then
            assertThat(value.message,`is`("Reminder not found!"))


        }

    }


    @After
    fun colseDatabase()
    {
        database.close()
    }

}