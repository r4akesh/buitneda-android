package com.webkul.mobikulmp.handlers


import android.os.AsyncTask
import android.view.View
import com.google.firebase.database.ServerValue
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.ChatActivity
import com.webkul.mobikulmp.helpers.MpAppSharedPref
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */

class ChatMessageHandler(private val mContext: ChatActivity) {
    private val token: String
    private val tokenArray: Array<String>


    init {
        token = this.mContext.token
        tokenArray = token.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    fun onClickSendButton(view: View, currentMsg: String) {
        if (currentMsg.isEmpty()) {
            return
        }
        Utils.hideKeyboard(mContext)
        val map = HashMap<String, Any>()
        val temp_key = mContext.mDatabaseReference.push().key
        mContext.mDatabaseReference.updateChildren(map)

        val message_root = temp_key?.let { mContext.mDatabaseReference.child(it) }
        val map2 = HashMap<String, Any>()
        map2["name"] = mContext.name
        map2["msg"] = currentMsg
        map2["timestamp"] = ServerValue.TIMESTAMP

        message_root?.updateChildren(map2)

        if (MpAppSharedPref.isAdmin(mContext)) {
            sendNotificationToUser(currentMsg)
        } else {
            sendNotificationToServer(currentMsg)
        }
        mContext.mContentViewBinding.textMsgEt.setText("")

    }

    private fun sendNotificationToUser(currentMsg: String) {
        for (temp_token in tokenArray) {
            try {
                val url = "https://fcm.googleapis.com/fcm/send"
                val data = JSONObject()
                data.put("title", mContext.getString(R.string.new_message_from_admin))
                data.put("body", currentMsg)
                data.put("message", currentMsg)
                data.put("id", 1)
                data.put("customerToken", mContext.user_id)
                data.put("name", mContext.user_name)
                data.put("notificationType", "chatNotification")
                data.put("sound", "default")
                val notification_data = JSONObject()
                notification_data.put("data", data)
                notification_data.put("notification", data)
                notification_data.put("to", temp_token)
                notification_data.put("priority", "high")
                notification_data.put("content_available", true)
                notification_data.put("time_to_live", 30)
                notification_data.put("delay_while_idle", true)
                notifyUserTask().execute(url, notification_data.toString(), AppSharedPref.getApiKey(mContext))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun sendNotificationToServer(currentMsg: String) {
        val name = mContext.user_name
        MpApiConnection.chatNotifyAdmin(mContext, currentMsg, mContext.user_id, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                    override fun onNext(baseModel: BaseModel) {
                        super.onNext(baseModel)

                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)

                    }
                })
    }

    private inner class notifyUserTask : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String): String? {
            val url = params[0]
            val data = params[1]
            val apiKey = params[2]
            try {
                val new_URl = URL(url)
                val httpURLConnection = new_URl.openConnection() as HttpsURLConnection
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.setRequestProperty("Content-Type", "application/json")
                httpURLConnection.setRequestProperty("Authorization", "Key=$apiKey")
                try {
                    httpURLConnection.doOutput = true
                    httpURLConnection.setChunkedStreamingMode(0)

                    val outputStream = BufferedOutputStream(httpURLConnection.outputStream)
                    val streamWriter = OutputStreamWriter(outputStream)
                    streamWriter.write(data)
                    streamWriter.flush()
                    streamWriter.close()

                    // Log.d(ApplicationConstant.TAG, "notifyUserTask doInBackground : " + httpURLConnection.getResponseCode());
                    // Log.d(ApplicationConstant.TAG, "notifyUserTask doInBackground : " + httpURLConnection.getResponseMessage());
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    httpURLConnection.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }
}
