package com.akangcupez.imagemachine.ui.preview

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.akangcupez.imagemachine.R
import com.akangcupez.imagemachine.databinding.ActivityPreviewBinding
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.ui.BaseActivity
import com.akangcupez.imagemachine.utils.Const

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/25/2022 16:30
 */
class PreviewActivity : BaseActivity() {

    private lateinit var mBinding: ActivityPreviewBinding
    private var image: Image? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.appbarDefault.toolbar)
        supportActionBar?.let {
            it.title = getString(R.string.preview)
            it.setDisplayHomeAsUpEnabled(true)
        }

        intent?.let {
            image = it.getParcelableExtra(Const.Global.EXTRA_DATA)
        }

        populateUI()
    }

    private fun populateUI() {
        image?.let {
            mBinding.ivPreview.setImageURI(Uri.parse(it.filePath))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

}