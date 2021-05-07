package com.webkul.mobikul.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.speech.RecognizerIntent
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Spinner
import android.widget.TextView
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.helpers.ConstantsHelper.DEFAULT_DATE_FORMAT
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
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

class Utils {

    companion object {
        @JvmStatic
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        @JvmStatic
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val screenDensity = Resources.getSystem().displayMetrics.density

        fun disableUserInteraction(context: Context) {
            try {
                (context as BaseActivity).window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } catch (e: Exception) {
            }
        }

        fun enableUserInteraction(context: Context) {
            try {
                (context as BaseActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } catch (e: Exception) {
            }
        }

        fun sendRegistrationTokenToServer(context: Context, token: String?) {
            AppSharedPref.setFcmToken(context, token)
            AuthKeyHelper.getInstance().token = token
            ApiConnection.uploadTokenData(context, token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(context, false) {})
        }

        fun showKeyboard(view: View) {
            view.requestFocus()
            if (!isHardKeyboardAvailable(view)) {
                val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(view, 0)
            }
        }

        private fun isHardKeyboardAvailable(view: View): Boolean {
            return view.context.resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS
        }

        fun hideKeyboard(activity: Activity) {
            try {
                val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (activity.currentFocus!!.windowToken != null)
                    inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
            }
        }

        fun hideKeyboard(view: View) {
            try {
                val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (view.windowToken != null)
                    inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
            }
        }

        /*fun getMd5String(stringToConvert: String): String {
            try {
                // Create MD5 Hash
                val digest = java.security.MessageDigest
//                        .getInstance("sha256")
                        .getInstance("MD5")
                digest.update(stringToConvert.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2)
                        h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }*/
        fun getMd5String(stringToConvert: String): String {
            try {
// Create MD5 Hash
                val digest = java.security.MessageDigest
                        .getInstance("sha256")
                digest.update(stringToConvert.toByteArray())
                val messageDigest = digest.digest()

// Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2)
                        h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }

        fun showShakeError(context: Context, viewToAnimate: View) {
            try {
                viewToAnimate.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error))
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
        }

        fun convertDpToPixel(dp: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun isVoiceAvailable(context: Context): Boolean {
            return context.packageManager.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0).size > 0
        }

        fun shareProduct(context: Context, url: String) {
            FirebaseAnalyticsHelper.logShareEvent(url)
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, url)
            sendIntent.type = "text/plain"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.choose_an_action), null))
            } else {
                context.startActivity(sendIntent)
            }
        }

        fun setSpinnerError(spinner: Spinner, errorString: String) {
            val selectedView = spinner.selectedView
            if (selectedView != null && selectedView is TextView) {
                spinner.requestFocus()
                selectedView.setTextColor(Color.RED)
                selectedView.text = errorString
                spinner.performClick()
            }
        }

        fun setBadgeCount(context: Context, icon: LayerDrawable, cartCount: Int) {
            val badge: BadgeDrawable
            // Reuse drawable if possible
            val reuse = icon.findDrawableByLayerId(R.id.ic_menu_badge)
            if (reuse != null && reuse is BadgeDrawable) {
                badge = reuse
            } else {
                badge = BadgeDrawable(context)
            }
            badge.setCount(cartCount.toString())
            icon.mutate()
            icon.setDrawableByLayerId(R.id.ic_menu_badge, badge)
        }

        fun logoutAndGoToHome(context: Context) {
            val customerSharedPrefEditor = AppSharedPref.getSharedPreferenceEditor(context, AppSharedPref.CUSTOMER_PREF)
            customerSharedPrefEditor.clear()
            customerSharedPrefEditor.apply()

            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun generateRandomPassword(): String {
            val random = SecureRandom()
            val letters = "abcdefghjklmnopqrstuvwxyzABCDEFGHJKMNOPQRSTUVWXYZ1234567890"
            val numbers = "1234567890"
            val specialChars = "!@#$%^&*_=+-/"
            var pw = ""
            for (i in 0..7) {
                val index = (random.nextDouble() * letters.length).toInt()
                pw += letters.substring(index, index + 1)
            }
            val indexA = (random.nextDouble() * numbers.length).toInt()
            pw += numbers.substring(indexA, indexA + 1)
            val indexB = (random.nextDouble() * specialChars.length).toInt()
            pw += specialChars.substring(indexB, indexB + 1)
            return pw
        }


        fun isValidEmailId(email: String): Boolean {
            return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches()
        }

        fun formatDate(dateFormat: String?, year: Int, month: Int, day: Int): String {
            val cal = Calendar.getInstance()
            cal.timeInMillis = 0
            cal.set(year, month, day)
            val date = cal.time
            val sdf = SimpleDateFormat(dateFormat ?: DEFAULT_DATE_FORMAT, Locale.getDefault())
            return sdf.format(date)
        }

        fun generateRandomId(): String {
            val random = SecureRandom()
            val letters = "abcdefghjklmnopqrstuvwxyzABCDEFGHJKMNOPQRSTUVWXYZ1234567890"
            val numbers = "1234567890"
            var pw = ""
            for (i in 0..7) {
                val index = (random.nextDouble() * letters.length).toInt()
                pw += letters.substring(index, index + 1)
            }
            val indexA = (random.nextDouble() * numbers.length).toInt()
            pw += numbers.substring(indexA, indexA + 1)
            return pw
        }

        fun validateUrlForSpecialCharacter(url: String): Boolean {
            if (url.contains(" "))
                return false
            return url.matches("[A-Za-z0-9^.-]*".toRegex())
        }

        fun isValidPhone(phone: String): Boolean {
            val PHONE_PATTERN = "^(91)?[6-9][0-9]{9}$"
            val pattern = Pattern.compile(PHONE_PATTERN)
            val matcher = pattern.matcher(phone)
            return matcher.matches()
        }
    }
}