package com.webkul.mobikul.helpers

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.webkul.mobikul.models.catalog.CartItem
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

        fun logSearchEvent(searchQuery: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SEARCH, params)
            }
        }

        fun logSignUpEvent(signUpMethod: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
              /*  params.putString("customer_name", name)
                params.putString("customer_email", email)*/
                params.putString(FirebaseAnalytics.Param.METHOD, signUpMethod)
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

        fun logLoginEvent(loginMethod: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString(FirebaseAnalytics.Param.METHOD, loginMethod)
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

        fun logCheckoutBeginEvent(cartItem:ArrayList<CartItem>) {
            if (sFirebaseAnalytics != null && sContext != null) {
                val params = Bundle()
                val items:ArrayList<Bundle> = ArrayList()
                for(myItem in cartItem){
                    val creteBundle = Bundle()
                    creteBundle.putString(FirebaseAnalytics.Param.ITEM_NAME,myItem.name)
                    items.add(creteBundle)

                }
                items.forEach {
                    with(params) {
                        putParcelableArray(
                            FirebaseAnalytics.Param.ITEMS,
                            arrayOf(it)
                        )
                    }
                }


               /*
                params.putString(
                    "customer",
                    if (AppSharedPref.isLoggedIn(sContext!!)) AppSharedPref.getCustomerEmail(
                        sContext!!
                    ) else "guest"
                )*/
              //  sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, params)
            }
        }

        fun logShareEvent(productId: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                params.putString(FirebaseAnalytics.Param.ITEM_ID, productId)
                sFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SHARE, params)
            }
        }

        fun logViewItemEvent(id: String, name: String) {
            if (sFirebaseAnalytics != null) {
                val params = Bundle()
                val items = Bundle()
                items.putString(FirebaseAnalytics.Param.ITEM_ID, id)
                items.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
                params.putParcelableArray(FirebaseAnalytics.Param.ITEMS,arrayOf(items))
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