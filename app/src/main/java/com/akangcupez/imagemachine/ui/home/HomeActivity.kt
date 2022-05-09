package com.akangcupez.imagemachine.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.akangcupez.imagemachine.App
import com.akangcupez.imagemachine.R
import com.akangcupez.imagemachine.databinding.ActivityHomeBinding
import com.akangcupez.imagemachine.model.entity.Machine
import com.akangcupez.imagemachine.ui.BaseActivity
import com.akangcupez.imagemachine.ui.home.adapter.MachineAdapter
import com.akangcupez.imagemachine.ui.machine.MachineActivity
import com.akangcupez.imagemachine.ui.scanner.QRScannerActivity
import com.akangcupez.imagemachine.utils.Const
import com.akangcupez.imagemachine.viewmodel.MachineViewModel
import com.akangcupez.imagemachine.viewmodel.MachineViewModelFactory
import com.akangcupez.imagemachine.widget.SortDialog
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 18:02
 */
class HomeActivity : BaseActivity() {

    private val mViewModel: MachineViewModel by viewModels {
        MachineViewModelFactory((application as App).repository)
    }

    private lateinit var mBinding: ActivityHomeBinding
    private var mLiveData: LiveData<List<Machine>?>? = null
    private lateinit var mAdapter: MachineAdapter
    private var mSort = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar?.setTitle(R.string.home)

        mAdapter = MachineAdapter()
        mAdapter.setOnItemClickListener(object : MachineAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                gotoToMachineInfoScreen(mAdapter.getItem(position))
            }
        })

        populateUI()
    }

    private fun reloadData() {
        if (mLiveData?.hasObservers() == true) {
            mLiveData?.removeObservers(this@HomeActivity)
        }

        mAdapter.submitList(null)
        mBinding.rvMain.adapter = mAdapter

        loadData()
    }

    private fun loadData() {
        mLiveData = mViewModel.getMachineList(mSort)
        mLiveData?.observe(this@HomeActivity) { machines ->
            machines?.let {
                mAdapter.submitList(it)
            }
        }
    }

    private fun populateUI() {
        mBinding.fabAdd.setOnClickListener {
            startActivity(Intent(this@HomeActivity, MachineActivity::class.java).apply {
                putExtra(Const.Global.ACTION_MODE, Const.Global.MODE_ADD)
            })
        }

        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rv = mBinding.rvMain
        rv.layoutManager = lm
        rv.isNestedScrollingEnabled = true
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = mAdapter

        loadData()
    }

    private fun gotoToMachineInfoScreen(machine: Machine) {
        startActivity(Intent(this@HomeActivity, MachineActivity::class.java).apply {
            putExtra(Const.Global.ACTION_MODE, Const.Global.MODE_VIEW)
            putExtra(Const.Global.EXTRA_DATA, machine)
        })
    }

    private val mScannerLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            processQRCode(result.contents)
        } else {
            val originalIntent = result.originalIntent
            if (originalIntent != null && originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                val err = "Cancelled due to missing camera permission"
                showToast(err)
            }
        }
    }

    private fun processQRCode(code: String) {
        val machineLiveData = mViewModel.getMachineByCode(code.toLong())
        machineLiveData.observe(this) {
            it?.let {
                gotoToMachineInfoScreen(it)
            }
        }
    }

    private fun getScanOptions(): ScanOptions {
        return ScanOptions()
            .setOrientationLocked(true)
            .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            .setBeepEnabled(true)
            .setCaptureActivity(QRScannerActivity::class.java)
    }

    private fun openSortDialog() {
        val dialog = SortDialog.newInstance(mSort)
        dialog.show(supportFragmentManager, "SortDialog")
        dialog.setCallback(object : SortDialog.Callback {
            override fun onChanged(sort: String) {
                mSort = sort
                reloadData()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_scanner -> {
                mScannerLauncher.launch(getScanOptions())
                true
            }
            R.id.menu_sort -> {
                openSortDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}