package com.akangcupez.imagemachine.model.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/25/2022 02:32
 */

@Parcelize
data class MachineImages(
    @Embedded val machine: Machine,
    @Relation(
        parentColumn = "id",
        entityColumn = "machineId"
    )
    val images: List<Image>
) : Parcelable {

    override fun toString(): String {
        return "MachineImages(machine=$machine, images=$images)"
    }

}