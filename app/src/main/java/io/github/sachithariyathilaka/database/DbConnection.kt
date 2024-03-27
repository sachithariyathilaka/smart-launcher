package io.github.sachithariyathilaka.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sachithariyathilaka.database.dao.AppDao
import io.github.sachithariyathilaka.model.AppPackage

@Database(version = 1, entities = [AppPackage::class])
abstract class DbConnection : RoomDatabase() {

    //User Dao Initialize
    abstract fun getAppDao(): AppDao

}