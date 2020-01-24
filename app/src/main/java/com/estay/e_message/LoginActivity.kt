package com.estay.e_message

import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
           val email=email_edittext_login.text.toString()
            val password=password_edittext_login.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {}
                .addOnFailureListener  {}
            }
back_to_register_textview.setOnClickListener {
    finish()
}
    }
}