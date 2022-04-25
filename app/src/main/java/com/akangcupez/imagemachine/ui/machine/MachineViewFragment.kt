package com.akangcupez.imagemachine.ui.machine

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.akangcupez.imagemachine.App
import com.akangcupez.imagemachine.R
import com.akangcupez.imagemachine.databinding.FragmentMachineViewBinding
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.model.entity.Machine
import com.akangcupez.imagemachine.ui.machine.adapter.ImageAdapter
import com.akangcupez.imagemachine.utils.Const
import com.akangcupez.imagemachine.utils.Event
import com.akangcupez.imagemachine.utils.Helper
import com.akangcupez.imagemachine.viewmodel.MachineViewModel
import com.akangcupez.imagemachine.viewmodel.MachineViewModelFactory

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 21:32
 */
class MachineViewFragment : Fragment() {

    private lateinit var mEvent: Event.MachineEvent
    private lateinit var mBinding: FragmentMachineViewBinding
    private lateinit var mViewModel: MachineViewModel
    private lateinit var mImageAdapter: ImageAdapter
    private var machine: Machine? = null

    companion object {
        @JvmStatic
        fun newInstance(machine: Machine?) = MachineViewFragment().apply {
            arguments = bundleOf(
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
            machine = it.getParcelable(Const.Global.EXTRA_DATA)
        }
        setHasOptionsMenu(true)

        val factory = MachineViewModelFactory(App.context.repository)
        mViewModel = ViewModelProvider(requireActivity(), factory)[MachineViewModel::class.java]

        mImageAdapter = ImageAdapter(true)
        mImageAdapter.setOnItemClickListener(mAdapterClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMachineViewBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUI()
    }

    private val mImagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                    if (it.data!!.clipData != null) {
                        processImages(it.data!!)
                    } else {
                        processSingleImage(it.data!!)
                    }
                }
            } catch (e: Exception) {
                Log.e("mytag", e.message, e)
            }
        }

    private fun processSingleImage(intent: Intent) {
        val uri = intent.data
        val img = Image()
        img.uri = uri
        img.filePath = uri.toString()
        img.machineId = machine?.id

        val lst: MutableList<Image> = mutableListOf()
        lst.add(img)
        mEvent.saveImage(lst.toList())
    }

    private fun processImages(intent: Intent) {
        intent.clipData?.let { data ->
            val selectedLen = data.itemCount
            val currentLen = mImageAdapter.itemCount
            if (selectedLen > 0) {
                if ((selectedLen + currentLen) > 10) {
                    mEvent.showToast(getString(R.string.error_max_image))
                } else {
                    val lst: MutableList<Image> = mutableListOf()
                    for (i in 0 until selectedLen) {
                        val uri = data.getItemAt(i).uri
                        val img = Image()
                        img.uri = uri
                        img.filePath = uri.toString()
                        img.machineId = machine?.id
                        lst.add(img)
                    }

                    mEvent.saveImage(lst.toList())
                }
            }
        }
    }

    private fun populateUI() {
        mBinding.btnAddImage.setOnClickListener {
            val c = mImageAdapter.itemCount
            if (c < 10) {
                mEvent.browseImageFile(mImagePickerLauncher)
            } else {
                mEvent.showToast(getString(R.string.error_max_image))
            }
        }

        val glm = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        val rv = mBinding.rvMachineImages
        rv.layoutManager = glm
        rv.isNestedScrollingEnabled = true
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = mImageAdapter

        machine?.let {
            mBinding.tvMachineName.text = it.name
            mBinding.tvMachineType.text = it.type
            mBinding.tvMachineCode.text = it.code.toString()
            mBinding.tvMachineUpdated.text = Helper.dateMillisToString(it.updated, "dd-MM-yyyy")

            loadImages(it)
        }
    }

    private val mAdapterClickListener = (object : ImageAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            val image = mImageAdapter.getItem(position)
            image?.let {
                mEvent.openImage(it)
            }
        }

        override fun onDeleteItem(position: Int) {
            mImageAdapter.getItem(position)?.let {
                mViewModel.deleteImage(it.imageId!!)
            }
        }
    })

    private fun loadImages(machine: Machine) {
        val imagesLiveData = mViewModel.getImageList(machine.id!!)
        imagesLiveData.observe(requireActivity()) { images ->
            images?.let {
                mImageAdapter.submitList(it)
                updateImageCounter()
            }
        }
    }

    private fun updateImageCounter() {
        val imageCounter = mImageAdapter.itemCount.toString()
        mBinding.tvMachineImages.text =
            String.format(getString(R.string.image_counter_info), imageCounter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_machine, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mEvent.goBack()
                true
            }
            R.id.menu_edit -> {
                mEvent.openEditableMachineScreen(Const.Global.MODE_EDIT, machine)
                true
            }
            R.id.menu_delete -> {
                mEvent.deleteMachine(machine!!)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}