package com.myshoppal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.myshoppal.FireStore.FireStoreClass
import com.myshoppal.R
import com.myshoppal.models.Users
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_login.et_password as et_password1
import kotlinx.android.synthetic.main.activity_register.et_email as et_email1

/**
 * Login Screen of the application.
 */
@Suppress("DEPRECATION")
class LoginActivity : BaseActivity(), View.OnClickListener {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_login)

        // This is used to hide the status bar and make the login screen as a full screen activity.
        // It is deprecated in the API level 30. I will update you with the alternate solution soon.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        tv_register.setOnClickListener (this)
        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        // TODO Step 7: Assign a onclick event to the register text to launch the register activity.
        // START
        tv_register.setOnClickListener {

            // Launch the register screen when the user clicks on the text.
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)

        }
        // END
    }

    override fun onClick(view: View?) {

        if (view != null) {
            when(view.id){
                R.id.tv_register->{
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login->{
                //  validateLoginDetails()
                    loginRegisteredUser()
                }
                R.id.tv_forgot_password->{
            startActivity(Intent(this,FogetPasswords::class.java))
                }

            }
        }
    }
    // TODO Step 5: Create a function to validate the login details.
    // START
    /**
     * A function to validate the login entries of a user.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
               // showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }
    // END
    fun loginRegisteredUser(){
        if(validateLoginDetails()){
            var email=et_email.text.toString().trim { it <= ' ' }
            var password=et_password.text.toString().trim { it <= ' ' }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener { task->
                    if(task.isSuccessful){
                        showErrorSnackBar("Login successful", false)
                        FireStoreClass().getUserDetails(this@LoginActivity)
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                        Log.d("lerror",""+task.exception!!.message.toString())
                    }
                }
            )


        }
    }
    fun userLoggedInSuccess(user: Users) {

        // Hide the progress dialog.
        dismisDialog()

        // Print the user details in the log as of now.
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        // Redirect the user to Main Screen after log in.
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
    // END

}