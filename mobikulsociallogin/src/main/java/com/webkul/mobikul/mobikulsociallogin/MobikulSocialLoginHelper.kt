/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.mobikulsociallogin

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.webkul.mobikul.sociallogin.R
import org.json.JSONObject
import java.security.SecureRandom
import java.util.*


class MobikulSocialLoginHelper : GoogleApiClient.OnConnectionFailedListener {


    val RC_GOOGLE_SIGN_IN = 102

    lateinit var mGoogleApiClient: GoogleApiClient
//    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mFacebookCallbackManager: CallbackManager


    lateinit var mActivity: Activity

    fun addSocialLoginButtons(activity: Activity, containerLayoutId: Int) {
        try {
            // FACE BOOK SDK INITIALIZATION
            FacebookSdk.sdkInitialize(activity.applicationContext)

            mActivity = activity

            val socialLoginLayout = mActivity.findViewById<LinearLayoutCompat>(containerLayoutId)

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()

//             mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);
//            mGoogleApiClient.connect();
//
            mGoogleApiClient = GoogleApiClient.Builder(mActivity).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
            val layoutParams = LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(10, 10, 10, 10)

            val googleLoginBtn = AppCompatImageView(mActivity)
            googleLoginBtn.layoutParams = layoutParams
            googleLoginBtn.setPadding(20, 20, 20, 20)
            googleLoginBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_google))
            googleLoginBtn.setOnClickListener {
                val googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
//                val googleSignInIntent = mGoogleSignInClient.getSignInIntent();
                mActivity.startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN)

            }
            socialLoginLayout.addView(googleLoginBtn)

            AppEventsLogger.activateApp(mActivity.application)
            mFacebookCallbackManager = CallbackManager.Factory.create()

            val facebookLoginBtn = AppCompatImageView(mActivity)
            facebookLoginBtn.layoutParams = layoutParams
            facebookLoginBtn.setPadding(20, 20, 20, 20)
            facebookLoginBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_facebook))
            facebookLoginBtn.setOnClickListener {
                LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "email"))
                LoginManager.getInstance().registerCallback(mFacebookCallbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val graphRequest = GraphRequest.newMeRequest(loginResult.accessToken) { facebookResponse, _ ->
                            try {
                                LoginManager.getInstance().logOut()
                                val socialLoginResponseMethod = mActivity.javaClass.getDeclaredMethod("socialLoginResponse", JSONObject::class.java)
                                socialLoginResponseMethod.invoke(mActivity, getDataFromFacebookResponse(facebookResponse))
                            } catch (e: Exception) {
                                LoginManager.getInstance().logOut()
                                e.printStackTrace()
                            }
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email,first_name,last_name,picture.type(large)")
                        graphRequest.parameters = parameters
                        graphRequest.executeAsync()
                    }

                    override fun onCancel() {}

                    override fun onError(error: FacebookException) {
                        Log.d("DEBUG", "Facebook JSON OnError: " + error.message)
                        error.printStackTrace()
                    }
                })
            }
            socialLoginLayout.addView(facebookLoginBtn)

//            initLinkedin(socialLoginLayout)
//            initTwitter(socialLoginLayout)
//            initInstagram(socialLoginLayout)

        } catch (x: ClassNotFoundException) {
            x.printStackTrace()
        } catch (x: InstantiationException) {
            x.printStackTrace()
        } catch (x: IllegalAccessException) {
            x.printStackTrace()
        }
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FacebookSdk.getCallbackRequestCodeOffset()) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        } else if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!=null ) {
                val socialLoginResponseMethod = mActivity.javaClass.getDeclaredMethod("socialLoginResponse", JSONObject::class.java)
                socialLoginResponseMethod.invoke(mActivity, getDataFromGoogleResponse(result.signInAccount))
            } else {
                Toast.makeText(mActivity, mActivity.getString(R.string.signin_error), Toast.LENGTH_LONG).show()
            }
            Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            mGoogleApiClient.disconnect()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {

            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }

    }

    private fun getDataFromGoogleResponse(googleUserData: GoogleSignInAccount?): JSONObject? {
        if (googleUserData != null) {
            try {
                val responseJson = JSONObject()
                responseJson.put("firstName", googleUserData.givenName)
                responseJson.put("lastName", googleUserData.familyName)
                responseJson.put("email", googleUserData.email)
                responseJson.put("password", generateRandomPassword())
                responseJson.put("pictureURL", googleUserData.photoUrl)
                responseJson.put("socialMethod", "google")
                return responseJson
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.signin_error), Toast.LENGTH_LONG).show()
        }
        return null
    }

    private fun getDataFromFacebookResponse(facebookResponseJson: JSONObject): JSONObject? {
        if (!facebookResponseJson.toString().isNullOrBlank()) {
            try {
                val responseJson = JSONObject()
                responseJson.put("firstName", facebookResponseJson.getString("first_name"))
                responseJson.put("lastName", facebookResponseJson.getString("last_name"))
                responseJson.put("socialMethod", "facebook")

                if(facebookResponseJson.has("email")){
                    responseJson.put("email", facebookResponseJson.getString("email"))
                }else{
                    responseJson.put("email", facebookResponseJson.getString("id") + "@gmail.com")
                }
                responseJson.put("password", generateRandomPassword())
                responseJson.put("pictureURL", facebookResponseJson.getJSONObject("picture")!!.getJSONObject("data").getString("url"))
                return responseJson
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(mActivity, mActivity.getString(R.string.signin_error), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.signin_error), Toast.LENGTH_LONG).show()
        }
        return null
    }


    private fun generateRandomPassword(): String {
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

    override fun onConnectionFailed(result: ConnectionResult) {

        if (result.errorCode == ConnectionResult.API_UNAVAILABLE) {
            // The Wearable API is unavailable
        }
        // ...
    }
}