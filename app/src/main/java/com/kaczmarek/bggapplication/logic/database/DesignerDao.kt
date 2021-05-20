package com.kaczmarek.bggapplication.logic.database

import androidx.room.*
import com.kaczmarek.bggapplication.entities.database.Designer

@Dao
interface DesignerDao {

    @Query("SELECT * FROM Designer ORDER BY name")
    suspend fun getAllDesigners(): List<Designer>

    @Query("SELECT * FROM Designer WHERE name=:name")
    suspend fun getDesignerByName(name: String): Designer?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDesigner(designer: Designer): Long

    @Update
    suspend fun updateDesigner(designer: Designer)

    @Delete
    suspend fun deleteDesigner(designer: Designer)
}