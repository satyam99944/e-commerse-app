package com.myshoppal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.myshoppal.FireStore.FireStoreClass
import com.myshoppal.R
import com.myshoppal.models.Users
import kotlinx.android.synthetic.main.activity_register.*

// TODO Step 1: Create an RegisterActivity of the application.
// START
@Suppress("DEPRECATION")
class RegisterActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_register)

        // TODO Step 3: Hide the status bar for the LoginActivity to make it full screen activity.
        // START
        // This is used to hide the status bar and make the splash screen as a full screen activity.
        // It is deprecated in the API level 30. I will update you with the alternate solution soon.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // END
        tv_login.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        btn_register.setOnClickListener{
           // validateRegisterDetails()
            register()
        }
    }
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {

                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
               // showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }
    private fun register(){
        if(validateRegisterDetails()){
            showDialog("please wait...")
            val email=et_email.text.toString().trim{it<=' '}
            val pasword=et_password.text.toString().trim{it<=' '}
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pasword)
                    .addOnCompleteListener (
                            OnCompleteListener{task->
//                                dismisDialog()
                                if(task.isSuccessful){

                                    val firebaseUser: FirebaseUser = task.result!!.user!!

//                                    showErrorSnackBar(
//                                            "You are registered successfully. Your user id is ${firebaseUser.uid}",
//                                            false
//                                    )
                                    // START
                                    /**
                                     * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                                     * and send him to Login Screen.
                                     */
                                    val user = Users(
                                            firebaseUser.uid,
                                            et_first_name.text.toString().trim { it <= ' ' },
                                            et_last_name.text.toString().trim { it <= ' ' },
                                            et_email.text.toString().trim { it <= ' ' }
                                    )
                                    FireStoreClass().registerUser(this@RegisterActivity,user)
//                                    FirebaseAuth.getInstance().signOut()
//                                    // Finish the Register Screen
//                                    finish()
                                    // END
                                } else {
                                    // If the registering is not successful then show error message.
                                    dismisDialog()
                                    showErrorSnackBar(task.exception!!.message.toString(), true)
                                }
                            }
                    )
        }
    }
    fun userRegistrationSuccess(){
        dismisDialog()
        Toast.makeText(this@RegisterActivity,"You are registered successfully!",Toast.LENGTH_SHORT).show()
    }
}
// END