package com.example.firebaseauth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*



class MainActivity : AppCompatActivity() {

    lateinit var providers : List<AuthUI.IdpConfig>
    var MY_REQUEST_CODE: Int = 1956
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        providers = Arrays.asList<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.EmailBuilder().build(),//email
                //AuthUI.IdpConfig.FacebookBuilder().build(),//FACEBOOK
                AuthUI.IdpConfig.GoogleBuilder().build(),//GOOGLE LOGIN
                AuthUI.IdpConfig.PhoneBuilder().build(),//phone
        )
        showSignInOptions()

        //event
        val signOut = findViewById<Button>(R.id.button_signout)
        signOut.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener{
                        signOut.isEnabled = false
                        showSignInOptions()
                    }
                    .addOnFailureListener {
                        e->Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE)
        {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK)
            {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,""+user!!.email, Toast.LENGTH_SHORT).show()
            }else
            {
                Toast.makeText(this,""+response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(),MY_REQUEST_CODE)
    }
}