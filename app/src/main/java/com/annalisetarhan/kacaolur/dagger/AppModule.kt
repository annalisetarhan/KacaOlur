package com.annalisetarhan.kacaolur.dagger

import android.app.Application
import androidx.room.Room
import com.annalisetarhan.kacaolur.authing.FakeUserService
import com.annalisetarhan.kacaolur.database.CourierMessageDao
import com.annalisetarhan.kacaolur.database.CourierResponseDao
import com.annalisetarhan.kacaolur.database.KacaOlurDatabase
import com.annalisetarhan.kacaolur.network.CourierMessageService
import com.annalisetarhan.kacaolur.network.CourierResponseService
import com.annalisetarhan.kacaolur.network.KacaNetwork
import com.annalisetarhan.kacaolur.network.UserService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideCourierResponseService(): CourierResponseService {
        return KacaNetwork.retrofit.create(CourierResponseService::class.java)
    }

    @Singleton
    @Provides
    fun provideCourierMessageService(): CourierMessageService {
        return KacaNetwork.retrofit.create(CourierMessageService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(): UserService {
        return KacaNetwork.retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideCourierResponseDao(database: KacaOlurDatabase): CourierResponseDao {
        return database.courierResponseDao()
    }

    @Singleton
    @Provides
    fun provideCourierMessageDao(database: KacaOlurDatabase): CourierMessageDao {
        return database.courierMessageDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(): KacaOlurDatabase {
        return Room.databaseBuilder(
            com.annalisetarhan.kacaolur.Application.context,    // this is (probably) hacky bullshit, sorry
            KacaOlurDatabase::class.java,
            "Database"
        ).build()
    }
}