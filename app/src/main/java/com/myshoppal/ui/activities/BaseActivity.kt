package com.myshoppal.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.myshoppal.R
import kotlinx.android.synthetic.main.progress_bar.*

open class BaseActivity:AppCompatActivity() {
    lateinit var mprogressDialog:Dialog
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }
    // END
    fun showDialog(text:String){
        mprogressDialog= Dialog(this)
        mprogressDialog.setContentView(R.layout.progress_bar)
        mprogressDialog.progress_text.text=text
        mprogressDialog.setCancelable(false)
        mprogressDialog.setCanceledOnTouchOutside(false)
        mprogressDialog.show()
    }
    fun dismisDialog(){
        mprogressDialog.dismiss()
    }
}
