package com.akangcupez.imagemachine.model.repository

import androidx.annotation.WorkerThread
import com.akangcupez.imagemachine.model.dao.MachineDao
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.model.entity.Machine
import kotlinx.coroutines.flow.Flow

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 17:08
 */
class MachineRepository(private val dao: MachineDao) {

    fun getMachineList(sort: String?) : Flow<List<Machine>?> {
        return when (sort) {
            "name" -> dao.getMachineListSortByName()
            "type" -> dao.getMachineListSortByType()
            "name,type" -> dao.getMachineListSortByNameAndType()
            else -> dao.getMachineList()
        }
    }

    fun getMachineByCode(code: Long) : Flow<Machine> {
        return dao.getMachineByCode(code)
    }

    @WorkerThread
    suspend fun update(machine: Machine) {
        dao.updateMachine(machine)
    }

    @WorkerThread
    suspend fun delete(id: Long) {
        dao.deleteMachineById(id)
    }

    @WorkerThread
    suspend fun insert(machine: Machine) : Long {
        return dao.insert(machine)
    }

    fun getImageList(machineId: Long) : Flow<List<Image>> {
        return dao.getImageList(machineId)
    }

    @WorkerThread
    suspend fun insertImages(images: List<Image>) {
        dao.insertImages(*images.toTypedArray())
    }

    @WorkerThread
    suspend fun deleteImage(id: Long) {
        dao.deleteImageById(id)
    }

    @WorkerThread
    suspend fun deleteAllImages(machineId: Long) {
        dao.deleteAllImages(machineId)
    }

}