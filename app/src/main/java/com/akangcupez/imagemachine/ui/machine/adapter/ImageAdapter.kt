package com.akangcupez.imagemachine.ui.machine.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akangcupez.imagemachine.databinding.ItemImageBinding
import com.akangcupez.imagemachine.model.entity.Image

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/25/2022 06:26
 */
class ImageAdapter(var editable: Boolean) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val mList: MutableList<Image> = mutableListOf()

    companion object {
        private var sListener: OnItemClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = getItem(position)
        image?.let {
            holder.populateUI(it, editable)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(images: List<Image>) {
        mList.clear()
        if (images.isNotEmpty()) {
            mList.addAll(images)
        }
        notifyDataSetChanged()
    }

    fun getItem(position: Int) : Image? {
        return if (mList.isNotEmpty()) mList[position] else null
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        sListener = onItemClickListener
    }

    class ViewHolder(private val mBinding: ItemImageBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        init {
            mBinding.ivImage.setOnClickListener {
                sListener?.onItemClick(it, adapterPosition)
            }

            mBinding.btnDelete.setOnClickListener {
                sListener?.onDeleteItem(adapterPosition)
            }
        }

        fun populateUI(image: Image, editable: Boolean) {
            mBinding.btnDelete.visibility = if (editable) View.VISIBLE else View.GONE
            mBinding.ivImage.setImageURI(Uri.parse(image.filePath))
        }
    }

    interface OnItemClickListener {

        fun onItemClick(view: View, position: Int)

        fun onDeleteItem(position: Int)

    }

}