package com.annalisetarhan.kacaolur.bidding

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BidTableEntry::class), version = 1)
abstract class BidTableEntryRoomDatabase : RoomDatabase() {
    
    abstract fun bidTableEntryDao(): BidTableEntryDao

    // I think this is to make the database a singleton
    companion object {
        @Volatile
        private var INSTANCE: BidTableEntryRoomDatabase? = null

        fun getDatabase(context: Context): BidTableEntryRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BidTableEntryRoomDatabase::class.java,
                    "Bid_Table_Entry_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}