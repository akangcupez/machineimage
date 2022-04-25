package com.akangcupez.imagemachine.model.entity

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 23:54
 */

@Entity
@Parcelize
data class Image(
    @PrimaryKey(autoGenerate = true) var imageId: Long? = null,
    @Ignore var uri: Uri? = null,
    var machineId: Long? = null,
    var filePath: String? = null
) : Parcelable {

    override fun toString(): String {
        return "Image(imageId=$imageId, uri=$uri, machineId=$machineId, filePath=$filePath)"
    }
}