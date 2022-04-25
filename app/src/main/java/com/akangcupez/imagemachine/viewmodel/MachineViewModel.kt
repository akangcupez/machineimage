package com.akangcupez.imagemachine.viewmodel

import androidx.lifecycle.*
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.model.entity.Machine
import com.akangcupez.imagemachine.model.repository.MachineRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 17:29
 */
class MachineViewModel(private val repository: MachineRepository) : ViewModel() {

    private val mLastMachineId : MutableLiveData<Long?> = MutableLiveData()

    init {
        mLastMachineId.value = null
    }

    fun getMachineList(sort: String) : LiveData<List<Machine>?> {
        return repository.getMachineList(sort).asLiveData()
    }

    fun getMachineByCode(code: Long) : LiveData<Machine?> {
        return repository.getMachineByCode(code).asLiveData()
    }

    fun update(machine: Machine) = viewModelScope.launch {
        repository.update(machine)
    }

    fun insert(machine: Machine) : LiveData<Long?> {
        mLastMachineId.value = null
        insertMachineReturnId(machine)
        return mLastMachineId
    }

    private fun insertMachineReturnId(machine: Machine) = viewModelScope.launch {
        val lastId = repository.insert(machine)
        mLastMachineId.value = lastId
    }

    fun deleteMachine(id: Long) = viewModelScope.launch {
        repository.deleteAllImages(id)
        repository.delete(id)
    }

    fun insertImages(images: List<Image>) = viewModelScope.launch {
        repository.insertImages(images)
    }

    fun getImageList(machineId: Long) : LiveData<List<Image>?> {
        return repository.getImageList(machineId).asLiveData()
    }

    fun deleteImage(id: Long) = viewModelScope.launch {
        repository.deleteImage(id)
    }

}

class MachineViewModelFactory(private val repository: MachineRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MachineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MachineViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown ViewModel class")
    }
}