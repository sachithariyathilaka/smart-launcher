package com.sachith.applauncher.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sachith.applauncher.database.dao.AppDao
import com.sachith.applauncher.model.AppPackage

@Database(version = 1, entities = [AppPackage::class])
abstract class DbConnection : RoomDatabase() {

    //User Dao Initialize
    abstract fun getAppDao(): AppDao

}