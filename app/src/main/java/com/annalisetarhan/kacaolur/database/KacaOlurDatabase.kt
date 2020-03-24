package com.annalisetarhan.kacaolur.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.annalisetarhan.kacaolur.utils.DateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: get rid of dummy data and implement https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/db/GithubDb.kt

@Database(entities = [DatabaseCourierResponse::class, DatabaseCourierMessage::class],
    version = 1, exportSchema = false)
abstract class KacaOlurDatabase : RoomDatabase() {
    
    abstract fun courierResponseDao(): CourierResponseDao
    abstract fun courierMessageDao(): CourierMessageDao

    // This makes the database a singleton
    companion object {
        @Volatile
        private var INSTANCE: KacaOlurDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope):
                KacaOlurDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KacaOlurDatabase::class.java,
                    "Database"
                ).addCallback(
                    CourierResponseDatabaseCallback(
                        scope
                    )
                ).addCallback(
                    CourierMessageDatabaseCallback(
                        scope
                    )
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class CourierResponseDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.courierResponseDao())
                }
            }
        }

        suspend fun populateDatabase(courierResponseDao: CourierResponseDao) {
            courierResponseDao.deleteAll()

            val bid = DatabaseCourierResponse(
                9,
                DateTime().getString(),
                "Mehmet T.",
                true,
                9.00f,
                1200,
                null,
                null,
                true
            )
            val question = DatabaseCourierResponse(
                99,
                DateTime().getString(),
                "Mehmet B.",
                false,
                null,
                null,
                "Kaç tane?",
                null,
                true
            )
            val bidAgain = DatabaseCourierResponse(
                999,
                DateTime().getString(),
                "Mehmet E.",
                true,
                5.00f,
                1800,
                null,
                null,
                true
            )
            val questionAgain = DatabaseCourierResponse(
                9999,
                DateTime().getString(),
                "Mehmet H.",
                false,
                null,
                null,
                "Hangi renk?",
                null,
                true
            )

            courierResponseDao.insert(bid)
            courierResponseDao.insert(question)
            courierResponseDao.insert(bidAgain)
            courierResponseDao.insert(questionAgain)
        }
    }



    private class CourierMessageDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            KacaOlurDatabase.INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateMessageThread(database.courierMessageDao())
                }
            }
        }

        suspend fun populateMessageThread(courierMessageDao: CourierMessageDao) {
            courierMessageDao.deleteAll()

            val mess1 = DatabaseCourierMessage(
                9,
                DateTime().getString(),
                "12:45",
                true,
                "Hello! I'm on my way",
                true
            )
            val mess2 =
                DatabaseCourierMessage(
                    99,
                    DateTime().getString(),
                    "12:50",
                    false,
                    "Tamam. Hadi.",
                    true)
            val mess3 = DatabaseCourierMessage(
                999,
                DateTime().getString(),
                "13:05",
                true,
                "Foto gönderdim. Karar ver.",
                true
            )
            val mess4 =
                DatabaseCourierMessage(
                    9999,
                    DateTime().getString(),  "13:10",
                    false,
                    "Verdim. Gel.",
                    true
                )

            courierMessageDao.sendMessage(mess1)
            courierMessageDao.sendMessage(mess2)
            courierMessageDao.sendMessage(mess3)
            courierMessageDao.sendMessage(mess4)

        }
    }
}