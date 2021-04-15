package com.webkul.mobikulmp.activities


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_FROM_NOTIFICATION
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.ChatMessageAdapter
import com.webkul.mobikulmp.databinding.ActivityChatBinding
import com.webkul.mobikulmp.handlers.ChatMessageHandler
import com.webkul.mobikulmp.helpers.MpAppSharedPref
import com.webkul.mobikulmp.models.chat.ChatMessage
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityChatBinding
    lateinit var mDatabaseReference: DatabaseReference
    var user_name = ""
    var user_id = ""
    var token = ""
    var name: String = ""
    var currentMessage = ""
    private var chatMessageAdapter: ChatMessageAdapter? = null
    private val messageList = ArrayList<ChatMessage>()
    private var isFromNotification: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        val intent = intent
        user_name = intent.getStringExtra("user_name")?:""
        user_id = intent.getStringExtra("user_id")?:""
        token = if (intent.hasExtra("token")) {
            intent.getStringExtra("token")!!
        } else {
            ""
        }
        if (intent.hasExtra(BUNDLE_KEY_FROM_NOTIFICATION)) {
            isFromNotification = getIntent().getBooleanExtra(BUNDLE_KEY_FROM_NOTIFICATION, false)
        }
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child(user_id)
        mContentViewBinding.msgRv.layoutManager = LinearLayoutManager(this@ChatActivity)
        chatMessageAdapter = ChatMessageAdapter(this@ChatActivity, messageList as List<ChatMessage>)
        mContentViewBinding.msgRv.adapter = chatMessageAdapter

        mContentViewBinding.currentMessage = currentMessage
        mContentViewBinding.isLoading = false
        mContentViewBinding.handler = ChatMessageHandler(this@ChatActivity)


        if (MpAppSharedPref.isAdmin(this@ChatActivity)) {
            name = getString(R.string.admin)
            title = user_name
        } else {
            name = user_name
            title = getString(R.string.admin)
        }

        mDatabaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                loadChat(dataSnapshot)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun loadChat(dataSnapshot: DataSnapshot?) {
        try {
            if (dataSnapshot != null) {
                val i = dataSnapshot.children.iterator()
                while (i.hasNext()) {
                    mContentViewBinding.isLoading = true

                    val chat_msg = (i.next() as DataSnapshot).value as String?
                    val chat_user_name = (i.next() as DataSnapshot).value as String?
                    val timeStampValue = (i.next() as DataSnapshot).value as Long
                    val displayDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa", Locale.getDefault())
                    val displayDayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

                    val calendar = Calendar.getInstance()
                    var currentDate = displayDateFormat.format(calendar.time)
                    currentDate = currentDate.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    calendar.timeInMillis = timeStampValue

                    var displayDay = displayDayFormat.format(calendar.time)
                    val dispalyDateTimeValue = displayDateFormat.format(calendar.time)

                    val dispalyDateTimeValueArray = dispalyDateTimeValue.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val displayDate = dispalyDateTimeValueArray[0]
                    val displayTime = dispalyDateTimeValueArray[1] + " " + dispalyDateTimeValueArray[2]

                    if (currentDate.matches(displayDate.toRegex())) {
                        displayDay = ""
                    }

                    val chatMessage = ChatMessage(chat_msg, chat_user_name, displayDay, displayTime, displayDate)

                    messageList.add(chatMessage)

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mContentViewBinding.isLoading = false
        chatMessageAdapter!!.notifyDataSetChanged()

        mContentViewBinding.msgRv.scrollToPosition(chatMessageAdapter!!.itemCount - 1)

    }

    override fun onBackPressed() {
        if (isFromNotification) {
            val intent = Intent(this, (application as MobikulApplication).getHomePageClass())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
    }
}