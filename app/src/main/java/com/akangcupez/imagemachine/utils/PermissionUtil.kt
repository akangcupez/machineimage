package com.akangcupez.imagemachine.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * @author by Aji Subastian (akangcupez@gmail.com) at 4/25/2022 04:07 AM
 */

fun AppCompatActivity.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

fun AppCompatActivity.shouldShowRequestPermissionRationaleCompat(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun AppCompatActivity.requestPermissionsCompat(permissionsArray: Array<String>,
                                               requestCode: Int) {
    ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
}