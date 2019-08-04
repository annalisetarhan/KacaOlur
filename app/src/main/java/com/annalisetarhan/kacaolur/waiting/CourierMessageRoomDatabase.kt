package com.annalisetarhan.kacaolur.waiting

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CourierMessage::class], version = 1, exportSchema = false)
abstract class CourierMessageRoomDatabase : RoomDatabase() {

    abstract fun courierMessageDao(): CourierMessageDao

    companion object {
        @Volatile
        private var INSTANCE: CourierMessageRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CourierMessageRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CourierMessageRoomDatabase::class.java,
                    "Courier_message_database"
                ).addCallback(CourierMessageDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }

        private class CourierMessageDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateMessageThread(database.courierMessageDao())
                    }
                }
            }

            suspend fun populateMessageThread(courierMessageDao: CourierMessageDao) {
                courierMessageDao.deleteAll()

                val mess1 = CourierMessage(0, true, "12:45", "Hello! I'm on my way")
                val mess2 = CourierMessage(0, false, "12:50", "Tamam. Hadi.")
                val mess3 = CourierMessage(0, true, "13:05", "Foto gönderdim. Karar ver.")
                val mess4 = CourierMessage(0, false, "13:10", "Verdim. Gel.")

                courierMessageDao.sendMessage(mess1)
                courierMessageDao.sendMessage(mess2)
                courierMessageDao.sendMessage(mess3)
                courierMessageDao.sendMessage(mess4)

            }
        }
    }
}