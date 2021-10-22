package com.webkul.mobikul.helpers

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


object AppPreference {
    private var preferenceName: String = "com.buitanda.android"
    var KEY_CUSTOMER_NAME = "customerName"
    var KEY_CUSTOMER_EMAIL = "customerEmail"


    fun savePreference(mContext: Context, key: String, value: String) {
        val sharedPreferences: SharedPreferences =
            mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
        editor.commit()
    }

    fun getPreferenceValueByKey(mContext: Context, key: String): String? {
        val sharedPreferences: SharedPreferences =
            mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }

    fun saveIntPreference(mContext: Context, key: String, value: Int) {
        val sharedPreferences: SharedPreferences =
            mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
        editor.commit()
    }

    fun getIntPreferenceValueByKey(mContext: Context, key: String): Int? {
        val sharedPreferences: SharedPreferences =
            mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, 0)
    }


    fun logout(mContext: Activity) {
        val sharedPreferences: SharedPreferences =
            mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        editor.commit()
    }


}