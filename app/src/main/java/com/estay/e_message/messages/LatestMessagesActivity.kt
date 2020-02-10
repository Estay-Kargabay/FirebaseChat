package com.estay.messages

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.estay.e_message.R
import com.estay.e_message.views.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.estay.models.User
import com.estay.registerlogin.RegisterActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_row.view.*
import com.estay.models.ChatMessage as ChatMessage1

class LatestMessagesActivity : AppCompatActivity() {

  companion object {
    var currentUser: User? = null
    val TAG="LatestMessages"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_latest_messages)
    recyclerview_latest_mesage.adapter=adapter
    recyclerview_latest_mesage.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

    
    adapter.setOnItemClickListener { item, view ->
      Log.d(TAG,"workssss")
      val intent=Intent(this,ChatLogActivity::class.java)
      val row=item as LatestMessageRow
      intent.putExtra(NewMessageActivity.USER_KEY,row.chatPartenerUser)
      startActivity(intent)
    }
    fetchCurrentUser()
    verifyUserIsLoggedIn()
   // setupDummyRos()
   // recyclerview_latest_mesage.setBackgroundColor(Color.BLACK)
    listenForLatestMessages()
  }
  val  LatestMessagesMap=HashMap<String,ChatMessage1>()
private   fun refreshRecycleViewMessages(){
  adapter.clear()
  LatestMessagesMap.values.forEach{
    adapter.add(LatestMessageRow(it))
  }
}
  private fun listenForLatestMessages() {
    val fromId=FirebaseAuth.getInstance().uid
    val ref=FirebaseDatabase.getInstance().getReference("/Latest_messages/$fromId")
    ref.addChildEventListener(object :ChildEventListener{
      override fun onChildAdded(p0: DataSnapshot, p1: String?) {
      val chatMessage=p0.getValue(ChatMessage1::class.java) ?: return
        LatestMessagesMap[p0.key!!]=chatMessage
        refreshRecycleViewMessages()
      }
      override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        val chatMessage=p0.getValue(ChatMessage1::class.java) ?: return
        LatestMessagesMap[p0.key!!]=chatMessage
        refreshRecycleViewMessages()

      }
      override fun onCancelled(p0: DatabaseError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }

      override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }
      override fun onChildRemoved(p0: DataSnapshot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      }

    })


  }

  val adapter=GroupAdapter<ViewHolder>()

//  private fun setupDummyRos() {
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//  }

  private fun fetchCurrentUser() {
    val uid = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
    ref.addListenerForSingleValueEvent(object: ValueEventListener {

      override fun onDataChange(p0: DataSnapshot) {
        currentUser = p0.getValue(User::class.java)
        Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
      }

      override fun onCancelled(p0: DatabaseError) {

      }
    })
  }

  private fun verifyUserIsLoggedIn() {
    val uid = FirebaseAuth.getInstance().uid
    if (uid == null) {
      val intent = Intent(this, RegisterActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
      startActivity(intent)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.menu_new_message -> {
        val intent = Intent(this, NewMessageActivity::class.java)
        startActivity(intent)
      }
      R.id.menu_sign_out -> {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
      }
    }

    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.nav_menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

}
