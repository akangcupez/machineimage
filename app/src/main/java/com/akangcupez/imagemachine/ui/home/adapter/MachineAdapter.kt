package com.akangcupez.imagemachine.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akangcupez.imagemachine.databinding.ItemMachineBinding
import com.akangcupez.imagemachine.model.entity.Machine

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 18:03
 */
class MachineAdapter : ListAdapter<Machine, MachineAdapter.ViewHolder>(MachineComparator()) {

    companion object {
        var sListener: OnItemClickListener? = null
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        sListener = onItemClickListener
    }

    public override fun getItem(position: Int): Machine {
        return super.getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMachineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder(private val mBinding: ItemMachineBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        init {
            mBinding.root.setOnClickListener { v ->
                sListener?.onItemClick(v, adapterPosition)
            }
        }

        fun bind(machine: Machine) {
            mBinding.tvName.text = machine.name
            mBinding.tvType.text = machine.type
        }
    }

    class MachineComparator : DiffUtil.ItemCallback<Machine>() {

        override fun areItemsTheSame(oldItem: Machine, newItem: Machine): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Machine, newItem: Machine): Boolean {
            return oldItem.updated == newItem.updated
        }

    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

}