package com.estay.e_message

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerbutton_button_register.setOnClickListener {
            performRegister()
        }

        alreeadyhaveaccount_textview.setOnClickListener {
            Log.d("RegisterActivity","Try to show login Activity")
           val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)}
        selectphoto_button_register.setOnClickListener {
            Log.d("RegisterActivity", "try to select a photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
    var selectedPhotoUri:Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
      Log.d("RegisterActivity","photo was selected ")
            selectedPhotoUri =data.data
        val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
//            val bitmapDrawable=BitmapDrawable(bitmap)
//            selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
            selectphoto_imageview_register.setImageBitmap(bitmap)
                selectphoto_button_register.alpha=0f

        }}

    private fun performRegister (){

        val username=username_edittext_register.text.toString()
        val email=email_edittext_reegister.text.toString()
        val password=password_edittext_register.text.toString()
        if (email.isEmpty()||password.isEmpty()) {
            return }
        Log.d("RegisterActivity","Username is" +username)
        Log.d("RegisterActivity","Email is "+email)

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (!it.isSuccessful)  return@addOnCompleteListener
                Log.d("RegisterMainActivity","Successfuly  create user  with uid ${it.result?.user?.uid}")
               uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d("Main","failed to create user :${it.message}")
                Toast.makeText(this,"failed to create user :${it.message}",Toast.LENGTH_LONG).show() }}


    private fun uploadImageToFirebaseStorage(){
    if (selectedPhotoUri==null)return
        val filename=UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","SuccessFuly upload image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {

                Log.d("RegisterActivity","File Location:$it")
                saveUserToFirebaseDatabase(it.toString())


            }}
    }

private  fun saveUserToFirebaseDatabase(proFileImageUrl:String){
    val uid=FirebaseAuth.getInstance().uid?:""
    val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")

    val user=User(uid,username_edittext_register.text.toString(),proFileImageUrl)
    ref.setValue(user)
        .addOnSuccessListener {
            Log.d("RaegisterActivity","We saveed the Image in Data...")
        val intent=Intent(this,LatestMessagesActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or (Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
}
}
class User(val uid:String, val username:String,val proFileImageUrl :String){
    constructor():this("","","")
}
