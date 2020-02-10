package com.estay.e_message.views

import com.estay.e_message.R
import com.estay.models.ChatMessage
import com.estay.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*


class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
    var chatPartenerUser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_textview_latest_message.text=chatMessage.text

        val chatPartenerId:String
        if (chatMessage.fromId== FirebaseAuth.getInstance().uid){
            chatPartenerId=chatMessage.toId
        }else{
            chatPartenerId=chatMessage.fromId
        }
        val ref= FirebaseDatabase.getInstance().getReference("/users/$chatPartenerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                chatPartenerUser=p0.getValue(User::class.java)
                viewHolder.itemView.username_textview_latest_message.text=chatPartenerUser?.username
                val targetImageView=viewHolder.itemView.imageview_latesst_message
                Picasso.get().load(chatPartenerUser?.profileImageUrl).into(targetImageView)
            }

        })

    }
    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}