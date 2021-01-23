package com.myshoppal.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.myshoppal.R
import kotlinx.android.synthetic.main.activity_foget_passwords.*

class FogetPasswords : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foget_passwords)
        btn_submit.setOnClickListener {
            forgotPassword()
        }
    }
    fun forgotPassword(){
        val email=et_email.text.toString().trim{it<=' '}
        if(email.isEmpty()){
            showErrorSnackBar("please enter email",true)
        }else{
            showDialog("please wait")
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {task ->
                dismisDialog()
                if (task.isSuccessful) {
                    // Show the toast message and finish the forgot password activity to go back to the login screen.
                    Toast.makeText(
                        this,
                        "Link sent to your email",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                } else {
                    showErrorSnackBar(task.exception!!.message.toString(), true)
                }
            }
        }
    }
}