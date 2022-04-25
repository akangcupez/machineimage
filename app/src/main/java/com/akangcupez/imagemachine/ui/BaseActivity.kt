package com.akangcupez.imagemachine.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.model.entity.Machine
import com.akangcupez.imagemachine.ui.preview.PreviewActivity
import com.akangcupez.imagemachine.utils.*
import com.akangcupez.imagemachine.widget.CalendarDialog

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/25/2022 04:23
 */
abstract class BaseActivity : AppCompatActivity(), Event.MachineEvent,
    ActivityCompat.OnRequestPermissionsResultCallback {

    override fun viewMachineInfo(machine: Machine) {
        //do nothing
    }

    override fun openEditableMachineScreen(mode: String, machine: Machine?) {
        //do nothing
    }

    override fun saveMachine(machine: Machine, mode: String) {
        //do nothing
    }

    override fun saveImage(images: List<Image>?) {
        //do nothing
    }

    override fun deleteMachine(machine: Machine) {
        //do nothing
    }

    override fun deleteImage(image: Image) {
        //do nothing
    }

    override fun deleteImages(machine: Machine) {
        //do nothing
    }

    override fun showCalendarDialog(date: String, callback: CalendarDialog.OnDateChangedListener) {
        val dialog: CalendarDialog = CalendarDialog.newInstance(date)
        dialog.setOnDateChangedListener(callback)
        dialog.show(supportFragmentManager, "CalendarDialog")
    }

    private fun hasStoragePermissions(): Boolean {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return hasPermission(perms)
    }

    private fun hasPermission(permissions: Array<String>): Boolean {
        var result = true
        run permsLoop@{
            permissions.forEach {
                result = checkSelfPermissionCompat(it) == PackageManager.PERMISSION_GRANTED
                if (!result) return@permsLoop
            }
        }
        return result
    }

    private fun requestStoragePermission() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        var shouldRequestRationale = false
        val r = mutableListOf<String>()
        for (v in perms) {
            if (shouldShowRequestPermissionRationaleCompat(v)) {
                r.add(v)
                shouldRequestRationale = true
            }
        }

        if (shouldRequestRationale) {
            requestPermissionsCompat(r.toTypedArray(), Const.Permissions.RC_STORAGE_PERMS)
        } else {
            requestPermissionsCompat(perms, Const.Permissions.RC_STORAGE_PERMS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Const.Permissions.RC_STORAGE_PERMS -> {
                Log.d("", grantResults.toString())
                val result = (grantResults.isNotEmpty() && grantResults.size == 2) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                onStoragePermissionResult(result)
                return
            }
            else -> {
                //Ignore all other request
            }
        }
    }

    override fun onStoragePermissionResult(b: Boolean) {
        //do nothing
    }

    private val mImagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        try {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                onFileReceived(it.data!!)
            }
        } catch (e: Exception) {
            Log.e("mytag", e.message, e)
        }
    }

    override fun browseImageFile() {
        browseImageFile(mImagePickerLauncher)
    }

    override fun browseImageFile(launcher: ActivityResultLauncher<Intent>) {
        if (hasStoragePermissions()) {
            //val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            if (intent.resolveActivity(packageManager) != null) {
                launcher.launch(Intent.createChooser(intent, "Browse File(s)"))
            }
        } else {
            requestStoragePermission()
        }
    }

    override fun onFileReceived(intent: Intent) {
        //do nothing
    }

    override fun openImage(image: Image) {
        val intent = Intent(this, PreviewActivity::class.java)
        intent.putExtra(Const.Global.EXTRA_DATA, image)
        startActivity(intent)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun goBack() {
        onBackPressed()
    }

}