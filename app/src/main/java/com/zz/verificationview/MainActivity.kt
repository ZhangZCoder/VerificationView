package com.zz.verificationview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zz.View.VerificationCodeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etView.setOnInputListener(object : VerificationCodeView.OnInputListener {
            override fun onInput() {
//                Toast.makeText(this@MainActivity, "", Toast.LENGTH_SHORT).show()

            }

            override fun complete(code: String?) {
                Toast.makeText(this@MainActivity, code, Toast.LENGTH_SHORT).show()
            }

        })

    }
}