package com.kaczmarek.bggapplication.logic.database

import androidx.room.*
import com.kaczmarek.bggapplication.entities.database.Designer

@Dao
interface DesignerDao {

    @Query("SELECT * FROM Designer")
    suspend fun getAllDesigners(): List<Designer>

    @Insert
    suspend fun addDesigner(designer: Designer)

    @Update
    suspend fun updateDesigner(designer: Designer)

    @Delete
    suspend fun deleteDesigner(designer: Designer)
}