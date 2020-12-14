package com.sachith.applauncher.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sachith.applauncher.model.AppPackage

@Dao
interface AppDao {

    //Insert Disable Apps
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appPackage: AppPackage)

    //Find user
    @Query("SELECT * FROM AppPackage")
    fun getData(): List<AppPackage>

}
