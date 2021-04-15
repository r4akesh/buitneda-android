/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.models.homepage

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.webkul.mobikul.BR
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.firebase.NOTIFICATION_CHANNEL_ABANDONED_CART
import com.webkul.mobikul.firebase.NOTIFICATION_CHANNEL_OFFERS
import com.webkul.mobikul.firebase.NOTIFICATION_CHANNEL_ORDERS
import com.webkul.mobikul.fragments.SettingsBottomSheetFragment
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref


class SettingsBottomSheetModel(val mFragmentContext: SettingsBottomSheetFragment) : BaseObservable() {

    var allNotification: Boolean = AppSharedPref.getNotificationsEnabled(mFragmentContext.context!!)
        @Bindable get() = field
        set(value) {
            field = value
            AppSharedPref.setNotificationsEnabled(mFragmentContext.context!!, field)
            notifyPropertyChanged(BR.allNotification)
        }

    var offersNotification: Boolean = AppSharedPref.getOfferNotificationsEnabled(mFragmentContext.context!!)
        set(value) {
            field = value
            if (field) {
                if (mFragmentContext.isNotificationChannelEnabled(NOTIFICATION_CHANNEL_OFFERS)) {
                    AppSharedPref.setOfferNotificationsEnabled(mFragmentContext.context!!, field)
                } else {
                    showGoToSettingsDialog()
                    mFragmentContext.mContentViewBinding.offersSwitch.isChecked = false
                }
            } else {
                AppSharedPref.setOfferNotificationsEnabled(mFragmentContext.context!!, field)
            }
        }

    var ordersNotification: Boolean = AppSharedPref.getOrderNotificationsEnabled(mFragmentContext.context!!)
        set(value) {
            field = value
            if (field) {
                if (mFragmentContext.isNotificationChannelEnabled(NOTIFICATION_CHANNEL_ORDERS)) {
                    AppSharedPref.setOrderNotificationsEnabled(mFragmentContext.context!!, field)
                } else {
                    showGoToSettingsDialog()
                    mFragmentContext.mContentViewBinding.ordersSwitch.isChecked = false
                }
            } else {
                AppSharedPref.setOrderNotificationsEnabled(mFragmentContext.context!!, field)
            }
        }

    var abandonedCartNotification: Boolean = AppSharedPref.getAbandonedCartNotificationsEnabled(mFragmentContext.context!!)
        set(value) {
            field = value
            if (field) {
                if (mFragmentContext.isNotificationChannelEnabled(NOTIFICATION_CHANNEL_ABANDONED_CART)) {
                    AppSharedPref.setAbandonedCartNotificationsEnabled(mFragmentContext.context!!, field)
                } else {
                    showGoToSettingsDialog()
                    mFragmentContext.mContentViewBinding.abandonedCartSwitch.isChecked = false
                }
            } else {
                AppSharedPref.setAbandonedCartNotificationsEnabled(mFragmentContext.context!!, field)
            }
        }

    var recentlyViewedProducts: Boolean = AppSharedPref.getRecentlyViewedProductsEnabled(mFragmentContext.context!!)
        set(value) {
            field = value
            AppSharedPref.setRecentlyViewedProductsEnabled(mFragmentContext.context!!, field)
            if (mFragmentContext.context is HomeActivity) {
//                (mFragmentContext.context as HomeActivity).setupRecentlyViewedCarouselsLayout()
            }
        }

    var searchHistory: Boolean = AppSharedPref.getSearchHistoryEnabled(mFragmentContext.context!!)
        set(value) {
            field = value
            AppSharedPref.setSearchHistoryEnabled(mFragmentContext.context!!, field)
        }

    private fun showGoToSettingsDialog() {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.context!!.getString(R.string.error),
                mFragmentContext.context!!.getString(R.string.enable_notifications_error),
                false,
                mFragmentContext.context!!.getString(R.string.go_to_settings),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    abandonedCartNotification = false
                    goToSettings()
                },
                mFragmentContext.context!!.getString(R.string.later),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    abandonedCartNotification = false
                })
    }

    fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", mFragmentContext.activity!!.packageName, null)
        mFragmentContext.startActivity(intent)
    }
}