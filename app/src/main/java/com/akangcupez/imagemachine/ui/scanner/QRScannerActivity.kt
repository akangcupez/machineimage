package com.akangcupez.imagemachine.ui.scanner

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.akangcupez.imagemachine.R
import com.akangcupez.imagemachine.databinding.ActivityQrscannerBinding
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ViewfinderView
import java.util.*

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 19:54
 */
class QRScannerActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener {

    private lateinit var mBinding: ActivityQrscannerBinding
    private lateinit var mCaptureManager: CaptureManager
    private lateinit var mScannerView: DecoratedBarcodeView
    private lateinit var mFinderView: ViewfinderView
    private lateinit var mTorchMenu: MenuItem

    private var mTorchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.appbarDefault.toolbar)
        supportActionBar?.let {
            it.setTitle(R.string.app_name)
            it.setDisplayHomeAsUpEnabled(true)
        }

        mScannerView = mBinding.dbvActivityQr
        mScannerView.setTorchListener(this)
        mFinderView = mScannerView.viewFinder

        mCaptureManager = CaptureManager(this, mScannerView)
        mCaptureManager.initializeFromIntent(intent, savedInstanceState)
        mCaptureManager.setShowMissingCameraPermissionDialog(false)
        mCaptureManager.decode()

        changeMaskColor()
        mFinderView.setLaserVisibility(true)
    }

    private fun changeMaskColor() {
        val rnd = Random()
        val color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        mFinderView.setMaskColor(color)
    }

    private fun switchFlashlight() {
        if (!mTorchOn) {
            mScannerView.setTorchOn()
        } else {
            mScannerView.setTorchOff()
        }
    }

    private fun hasFlash(): Boolean {
        return applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mCaptureManager.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return mScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mCaptureManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (hasFlash()) {
            val inflater = menuInflater
            inflater.inflate(R.menu.menu_scanner, menu)
            mTorchMenu = menu.findItem(R.id.menu_flash)
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
        //return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_flash -> {
                switchFlashlight()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onTorchOn() {
        mTorchOn = true
        mTorchMenu.setIcon(R.drawable.ic_flash_on_white_24)
    }

    override fun onTorchOff() {
        mTorchOn = false
        mTorchMenu.setIcon(R.drawable.ic_flash_off_white_24)
    }

    override fun onResume() {
        super.onResume()
        mCaptureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        mCaptureManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCaptureManager.onDestroy()
    }

}