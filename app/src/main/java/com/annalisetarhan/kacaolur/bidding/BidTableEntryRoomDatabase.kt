package com.annalisetarhan.kacaolur.bidding

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [BidTableEntry::class], version = 1)
public abstract class BidTableEntryRoomDatabase : RoomDatabase() {
    
    abstract fun bidTableEntryDao(): BidTableEntryDao

    // I think this is to make the database a singleton
    companion object {
        @Volatile
        private var INSTANCE: BidTableEntryRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BidTableEntryRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BidTableEntryRoomDatabase::class.java,
                    "Bid_table_entry_database"
                ).addCallback(BidTableEntryDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class BidTableEntryDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.bidTableEntryDao())
                }
            }
        }

        suspend fun populateDatabase(bidTableEntryDao: BidTableEntryDao) {
            bidTableEntryDao.deleteAll()

            var bid = BidTableEntry(0, "Mehmet T.", true, 9.00f, 90, null, null)
            bidTableEntryDao.insert(bid)
            var question = BidTableEntry(1, "Mehmet B.", false, null, null, "Kaç tane?", null)
            bidTableEntryDao.insert(question)
            var bidAgain = BidTableEntry(2, "Mehmet E.", true, 5.00f, 50, null, null)
            bidTableEntryDao.insert(bidAgain)
            var questionAgain = BidTableEntry(3, "Mehmet H.", false, null, null, "Hangi renk?", null)
            bidTableEntryDao.insert(questionAgain)
        }
    }
}