package com.estay.e_message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import java.nio.file.attribute.GroupPrincipal

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.setTitle("Select User")
//        val adapter = GroupAdapter<GroupieViewHolder>()
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//        recyclerview_NewMessage.adapter=adapter
        fetchUsers()
    }
 private fun fetchUsers(){
    val ref =FirebaseDatabase.getInstance().getReference("/users")
     ref.addListenerForSingleValueEvent(object :ValueEventListener{
         override fun onDataChange(p0: DataSnapshot) {
             val adapter=GroupAdapter<GroupieViewHolder>()
             p0.children.forEach{
                 val user=it.getValue(User::class.java)
                 if (user!=null){
                     adapter.add(UserItem(user))
                 }
             }
             recyclerview_NewMessage.adapter=adapter
         }

         override fun onCancelled(p0: DatabaseError) {

         }
     })
    }
}


class UserItem(val user: User):Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text=user.username
        Picasso.get().load(user.proFileImageUrl).into(viewHolder.itemView.imageview_new_message)
    }
    override fun getLayout(): Int {
    return  R.layout.user_row_new_message
    }
}