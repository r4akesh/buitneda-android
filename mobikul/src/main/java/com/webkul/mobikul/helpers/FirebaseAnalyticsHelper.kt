package com.webkul.mobikul.helpers

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import org.json.JSONObject


class FirebaseAnalyticsHelper {

    companion object {
        private var sFirebaseAnalytics: FirebaseAnalytics? = null
        private var sContext: Context? = null
        private const val TAG = "FirebaseAnalyticsHelper"

        fun initFirebaseAnalytics(context: Context) {
            if (ApplicationConstants.ENABLE_FIREBASE_ANALYTICS) {
                if (sFirebaseAnalytics == null)
                    sFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
                sContext = context
            }
        }

        fun logAppOpenEvent() {
            if (sFirebaseAnalytics != null)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
        }


        fun logAppScreenView(screenName: String, screenClass: String) {
            if (sFirebaseAnalytics != null){
                val params = Bundle()
                params.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)

                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, null)
            }



        }

        fun logSearchEvent(searchQuery: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SEARCH, params)
            }
        }

        fun logSignUpEvent(name: String, email: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString("customer_name", name)
                params.putString("customer_email", email)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SIGN_UP, params)
            }
        }


        fun logHomeEvent(name: String, id: String?,title:String) {
            Log.d(TAG, "logHomeEvent: name: $name id: $id")
            if (sFirebaseAnalytics != null) {
                Log.d(TAG, "logHomeEvent: if name: $name id: $id")
                val params = Bundle()
                params.putString("name", name.toLowerCase().replace("-", "_"))
                id?.let {
                    params.putString("id", id.toLowerCase().replace("-", "_"))
                }
                params.putString("title",title.toLowerCase().replace("-","_"))
                println("==========================================")
                println(sFirebaseAnalytics)
                sFirebaseAnalytics?.logEvent("home_page_event", params)
            }
        }

        fun logLoginEvent(username: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString("customer_username", username)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.LOGIN, params)
            }
        }

        fun logAddToWishListEvent(
            id: String,
            name: String,
            qty: String,
            mProductParamsJSON: JSONObject
        ) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString("product_id", id)
                params.putString("product_name", name)
                params.putString("qty", qty)
                params.putString("productOptions", mProductParamsJSON.toString())
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, params)
            }
        }

        fun logAddToCartEvent(
            id: String,
            name: String,
            qty: String,
            mProductParamsJSON: JSONObject
        ) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString("product_id", id)
                params.putString("product_name", name)
                params.putString("qty", qty)
                params.putString("productOptions", mProductParamsJSON.toString())
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, params)
            }
        }

        fun logCheckoutBeginEvent() {
            if (sFirebaseAnalytics != null && sContext != null) {
                val params = Bundle()
                params.putString(
                    "customer",
                    if (AppSharedPref.isLoggedIn(sContext!!)) AppSharedPref.getCustomerEmail(
                        sContext!!
                    ) else "guest"
                )
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, params)
            }
        }

        fun logShareEvent(url: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString("product_id", url)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SHARE, params)
            }
        }

        fun logViewItemEvent(id: String, name: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString("product_id", id)
                params.putString("product_name", name)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params)
            }
        }

        fun logECommercePurchaseEvent(incrementId: String, orderId: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString("product_id", incrementId)
                params.putString("product_name", orderId)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, params)
            }
        }
    }
}