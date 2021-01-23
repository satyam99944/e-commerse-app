package com.myshoppal.FireStore

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.myshoppal.models.Users
import com.myshoppal.ui.activities.LoginActivity
import com.myshoppal.ui.activities.RegisterActivity
import com.myshoppal.utils.Constants

class FireStoreClass {
    private val mFireStore=FirebaseFirestore.getInstance()
    fun registerUser(activity: RegisterActivity, userInfo: Users) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection(Constants.USERS)
                // Document ID for users fields. Here the document it is the User ID.
                .document(userInfo.id)
                // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {

                    // Here call a function of base activity for transferring the result to it.
                    activity.userRegistrationSuccess()
                }
                .addOnFailureListener { e ->
                    activity.dismisDialog()
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while registering the user.",
                            e
                    )
                }
    }
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
                // The document id to get the Fields of user.
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->

                    Log.i(activity.javaClass.simpleName, document.toString())

                    // Here we have received the document snapshot which is converted into the User Data model object.
                   // val user = document.toObject(Users::class.java)!!
                      val user=document.toObject(Users::class.java)
                   if(user!=null){
                       Log.d("name",user.firstName.toString())
                   }else{
                       Log.d("name","baba")
                   }
                    // TODO Step 6: Pass the result to the Login Activity.
                    // START
                    when (activity) {
                        is LoginActivity -> {
                            // Call a function of base activity for transferring the result to it.
                            if (user != null) {
                                activity.userLoggedInSuccess(user)
                            }
                        }
                    }
                    // END
                }
                .addOnFailureListener { e ->
                    // Hide the progress dialog if there is any error. And print the error in log.
                    when (activity) {
                        is LoginActivity -> {
                            activity.dismisDialog()
                        }
                    }

                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while getting user details.",
                            e
                    )
                }
    }
    // END
}