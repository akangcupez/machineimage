package com.akangcupez.imagemachine.ui.machine

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.akangcupez.imagemachine.R
import com.akangcupez.imagemachine.databinding.FragmentMachineEditableBinding
import com.akangcupez.imagemachine.model.entity.Machine
import com.akangcupez.imagemachine.utils.Const
import com.akangcupez.imagemachine.utils.Event
import com.akangcupez.imagemachine.utils.Helper
import com.akangcupez.imagemachine.widget.CalendarDialog

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 20:24
 */
class MachineEditableFragment : Fragment() {

    private lateinit var mEvent: Event.MachineEvent
    private lateinit var mBinding: FragmentMachineEditableBinding
    private var machine: Machine? = null
    private lateinit var mode: String

    companion object {
        @JvmStatic
        fun newInstance(mode: String, machine: Machine?) = MachineEditableFragment().apply {
            arguments = bundleOf(
                Const.Global.ACTION_MODE to mode,
                Const.Global.EXTRA_DATA to machine
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Event.MachineEvent) {
            context.also { mEvent = it }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(Const.Global.ACTION_MODE) ?: Const.Global.MODE_ADD
            machine = it.getParcelable(Const.Global.EXTRA_DATA)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMachineEditableBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (machine == null) machine = Machine()
        populateUI()
    }

    private val dateChangedListener: CalendarDialog.OnDateChangedListener = object :
        CalendarDialog.OnDateChangedListener {
        override fun onDateChanged(date: String?) {
            date?.let {
                mBinding.tvUpdated.text = it
                machine?.updated = Helper.dateStringToMillis(it, "dd-MM-yyyy")
            }
        }
    }

    private fun populateUI() {
        mBinding.llUpdated.setOnClickListener {
            val date: String = mBinding.tvUpdated.text.toString()
            mEvent.showCalendarDialog(date, dateChangedListener)
        }

        machine?.let {
            mBinding.etName.setText(it.name)
            mBinding.etType.setText(it.type)
            mBinding.tvUpdated.text = Helper.dateMillisToString(it.updated, "dd-MM-yyyy")
        }
    }

    private fun addOrUpdateMachine(machine: Machine) {
        if (machine.name.isBlank() || machine.type.isBlank()) {
            mEvent.showToast("All fields are required")
            return
        }

        mEvent.saveMachine(machine, mode)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_machine_editable, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mEvent.goBack()
                true
            }
            R.id.menu_save -> {
                machine?.name = mBinding.etName.text.toString()
                machine?.type = mBinding.etType.text.toString()
                addOrUpdateMachine(machine!!)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}