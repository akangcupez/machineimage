package com.akangcupez.imagemachine.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akangcupez.imagemachine.utils.Helper
import kotlinx.parcelize.Parcelize

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/22/2022 01:54
 */

@Entity(indices = [Index(value = ["code"], unique = true)])
@Parcelize
data class Machine(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String = "",
    var type: String = "",
    var code: Long = Helper.currentTimestamp(),
    var updated: Long = Helper.currentTimestamp()
) : Parcelable {

    override fun toString(): String {
        return "Machine(id=$id, name='$name', type='$type', code=$code)"
    }

}
