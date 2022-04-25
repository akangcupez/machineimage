package com.akangcupez.imagemachine.utils

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.model.entity.Machine
import com.akangcupez.imagemachine.widget.CalendarDialog

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 21:36
 */
object Event {

    interface MachineEvent {

        fun viewMachineInfo(machine: Machine)

        fun openEditableMachineScreen(mode: String, machine: Machine?)

        fun saveMachine(machine: Machine, mode: String)

        fun saveImage(images: List<Image>?)

        fun deleteMachine(machine: Machine)

        fun deleteImage(image: Image)

        fun deleteImages(machine: Machine)

        fun showCalendarDialog(date: String, callback: CalendarDialog.OnDateChangedListener)

        fun onStoragePermissionResult(b: Boolean)

        fun browseImageFile()

        fun browseImageFile(launcher: ActivityResultLauncher<Intent>)

        fun onFileReceived(intent: Intent)

        fun openImage(image: Image)

        fun showToast(message: String)

        fun goBack()

    }

    interface ImageEvent {

        fun addImages(machine: Machine, images: MutableList<Image>)

    }

}