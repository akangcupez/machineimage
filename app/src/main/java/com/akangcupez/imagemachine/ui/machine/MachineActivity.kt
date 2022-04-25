package com.akangcupez.imagemachine.ui.machine

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.akangcupez.imagemachine.R
import com.akangcupez.imagemachine.databinding.ActivityMachineBinding
import com.akangcupez.imagemachine.model.entity.Machine
import com.akangcupez.imagemachine.viewmodel.MachineViewModel
import androidx.activity.viewModels
import com.akangcupez.imagemachine.App
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.ui.BaseActivity
import com.akangcupez.imagemachine.utils.*
import com.akangcupez.imagemachine.viewmodel.MachineViewModelFactory

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 18:35
 */
class MachineActivity : BaseActivity() {

    private val mViewModel: MachineViewModel by viewModels {
        MachineViewModelFactory((application as App).repository)
    }

    private lateinit var mBinding: ActivityMachineBinding
    private lateinit var mode: String
    private var machine: Machine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMachineBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.appbarDefault.toolbar)
        supportActionBar?.let {
            it.setTitle(R.string.app_name)
            it.setDisplayHomeAsUpEnabled(true)
        }

        intent?.let {
            mode = it.getStringExtra(Const.Global.ACTION_MODE) ?: Const.Global.MODE_ADD
            machine = it.getParcelableExtra(Const.Global.EXTRA_DATA)

            populateUI()
        }
    }

    private fun populateUI() {
        if (mode == Const.Global.MODE_VIEW) {
            viewMachineInfo(machine!!)
        } else {
            openEditableMachineScreen(mode, machine)
        }
    }

    override fun viewMachineInfo(machine: Machine) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content_activity_machine, MachineViewFragment.newInstance(machine))
        ft.commit()
    }

    override fun openEditableMachineScreen(mode: String, machine: Machine?) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(
            R.id.content_activity_machine,
            MachineEditableFragment.newInstance(mode, machine)
        )
        ft.commit()
    }

    override fun saveMachine(machine: Machine, mode: String) {
        if (mode == Const.Global.MODE_ADD) {
            val insertLiveData = mViewModel.insert(machine)
            insertLiveData.observe(this@MachineActivity) {
                it?.let {
                    insertLiveData.removeObservers(this@MachineActivity)
                    machine.id = it
                }
            }
        } else if (mode == Const.Global.MODE_EDIT) {
            mViewModel.update(machine)
        }

        finish()
    }

    override fun saveImage(images: List<Image>?) {
        images?.let { list ->
            if (list.isNotEmpty()) {
                mViewModel.insertImages(images)
            }
        }
    }

    override fun deleteMachine(machine: Machine) {
        machine.id?.let {
            mViewModel.deleteMachine(it)
            finish()
        }
    }

}