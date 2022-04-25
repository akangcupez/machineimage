package com.akangcupez.imagemachine.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.akangcupez.imagemachine.R
import com.akangcupez.imagemachine.ui.home.HomeActivity

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 18:02
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //simulate splash screen delay
        Handler(Looper.getMainLooper()).postDelayed({
            gotoHomeActivity()
        }, 3000)
    }

    private fun gotoHomeActivity() {
        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        finish()
    }

}