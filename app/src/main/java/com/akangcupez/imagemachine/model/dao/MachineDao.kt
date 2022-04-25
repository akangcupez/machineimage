package com.akangcupez.imagemachine.model.dao

import androidx.room.*
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.model.entity.Machine
import kotlinx.coroutines.flow.Flow


/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 13:57
 */

@Dao
interface MachineDao {

    //Machine

    @Query("SELECT * FROM machine ORDER BY :sort ASC")
    fun getMachineList(sort: String): Flow<List<Machine>>

    @Query("SELECT * FROM machine WHERE code = :code LIMIT 1")
    fun getMachineByCode(code: Long) : Flow<Machine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(machine: Machine) : Long

    @Update
    suspend fun updateMachine(machine: Machine)

    @Query("DELETE FROM machine WHERE id = :id")
    suspend fun deleteMachineById(id: Long)

    //Images

    @Query("SELECT * FROM image WHERE machineId = :machineId")
    fun getImageList(machineId: Long) : Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(vararg images: Image)

    @Query("DELETE FROM image WHERE imageId = :id")
    suspend fun deleteImageById(id: Long)

    @Query("DELETE FROM image WHERE machineId = :machineId")
    suspend fun deleteAllImages(machineId: Long)

}