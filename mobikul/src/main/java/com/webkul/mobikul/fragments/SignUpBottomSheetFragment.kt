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

package com.webkul.mobikul.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.LoginAndSignUpActivity
import com.webkul.mobikul.activities.OrderPlacedActivity
import com.webkul.mobikul.databinding.FragmentSignupBottomSheetBinding
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.ApplicationConstants.ENABLE_KEYBOARD_OBSERVER
import com.webkul.mobikul.models.checkout.SaveOrderResponseModel
import com.webkul.mobikul.models.user.SignUpFormModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.*


class SignUpBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    lateinit var mContentViewBinding: FragmentSignupBottomSheetBinding

    companion object {
        fun newInstance(saveOrderResponseModel: SaveOrderResponseModel): SignUpBottomSheetFragment {
            val signUpBottomSheetFragment = SignUpBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(BundleKeysHelper.BUNDLE_KEY_SAVE_ORDER_RESPONSE, saveOrderResponseModel)
            signUpBottomSheetFragment.arguments = args
            return signUpBottomSheetFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup_bottom_sheet, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callCreateFormApi()
        // Remove Space FromShop URL #556
        mContentViewBinding.shopUrlEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                val textEntered =  mContentViewBinding.shopUrlEt.text.toString()

                if (textEntered.isNotEmpty() && textEntered.contains(" ")) {
                    mContentViewBinding.shopUrlEt.setText( mContentViewBinding.shopUrlEt.text.toString().replace(" ", ""));
                    mContentViewBinding.shopUrlEt.setSelection( mContentViewBinding.shopUrlEt.text!!.length);
                }
            }})
    }

    private fun callCreateFormApi() {
        mContentViewBinding.loading = true
        (context as BaseActivity).mHashIdentifier = Utils.getMd5String("createAccountFormData" + AppSharedPref.getStoreId(context!!))
        ApiConnection.createAccountFormData(context!!, BaseActivity.mDataBaseHandler.getETagFromDatabase((context as BaseActivity).mHashIdentifier))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SignUpFormModel>(context!!, false) {
                    override fun onNext(signUpFormModel: SignUpFormModel) {
                        super.onNext(signUpFormModel)
                        mContentViewBinding.loading = false
                        if (signUpFormModel.success) {
                            signUpFormModel.isMobileRequired = true
                            signUpFormModel.isMobileVisible = true
                            onSuccessfulResponse(signUpFormModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(signUpFormModel: SignUpFormModel) {
        setupFormData(signUpFormModel)
    }

    private fun setupFormData(signUpFormModel: SignUpFormModel) {
        mContentViewBinding.data = signUpFormModel
        checkForPreSetData()
        setUpPrefix()
        setUpSuffix()
        setUpGender()
        setupPasswordTextWatcher()
        mContentViewBinding.handler = (context?.applicationContext as MobikulApplication).getSignUpBottomSheetHandler(this)

        if (ENABLE_KEYBOARD_OBSERVER)
            attachKeyboardListeners()
    }

    private fun checkForPreSetData() {
        if (arguments != null) {
            val saveOrderResponseModel = arguments?.getParcelable<SaveOrderResponseModel>(BundleKeysHelper.BUNDLE_KEY_SAVE_ORDER_RESPONSE)?:SaveOrderResponseModel()
            mContentViewBinding.data!!.prefix = saveOrderResponseModel.customerDetails?.prefix
            mContentViewBinding.data!!.firstName = saveOrderResponseModel.customerDetails?.firstname
            mContentViewBinding.data!!.lastName = saveOrderResponseModel.customerDetails?.lastname
            mContentViewBinding.data!!.suffix = saveOrderResponseModel.customerDetails?.suffix
            mContentViewBinding.data!!.emailAddr = saveOrderResponseModel.customerDetails?.email
            mContentViewBinding.data!!.orderId=saveOrderResponseModel.orderId
            mContentViewBinding.gotoSignInLayout.visibility = View.GONE
        }
    }

    private fun setUpPrefix() {
        if (mContentViewBinding.data!!.isPrefixVisible && mContentViewBinding.data!!.prefixHasOptions) {
            val prefixSpinnerData = ArrayList<String>()
            prefixSpinnerData.add("")
            for (prefixIterator in 0 until mContentViewBinding.data!!.prefixOptions.size) {
                prefixSpinnerData.add(mContentViewBinding.data!!.prefixOptions[prefixIterator])
            }

            mContentViewBinding.prefixSp.adapter = context?.let { ArrayAdapter<String>(it, R.layout.custom_spinner_item, prefixSpinnerData) }
            mContentViewBinding.prefixSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    mContentViewBinding.data!!.prefix = parent.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

            mContentViewBinding.prefixSp.setSelection(mContentViewBinding.data!!.getPrefixPosition())
        }
    }

    private fun setUpSuffix() {
        if (mContentViewBinding.data!!.isSuffixVisible && mContentViewBinding.data!!.suffixHasOptions) {
            val suffixSpinnerData = ArrayList<String>()
            suffixSpinnerData.add("")
            for (prefixIterator in 0 until mContentViewBinding.data!!.suffixOptions.size) {
                suffixSpinnerData.add(mContentViewBinding.data!!.suffixOptions[prefixIterator])
            }

            mContentViewBinding.suffixSp.adapter = context?.let { ArrayAdapter<String>(it, R.layout.custom_spinner_item, suffixSpinnerData) }
            mContentViewBinding.suffixSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    mContentViewBinding.data!!.suffix = parent.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

            mContentViewBinding.suffixSp.setSelection(mContentViewBinding.data!!.getSuffixPosition())
        }
    }

    private fun setUpGender() {
        if (mContentViewBinding.data!!.isGenderVisible) {
            val genderSpinnerData = ArrayList<String>()
            genderSpinnerData.add("")
            genderSpinnerData.add(resources.getString(R.string.male))
            genderSpinnerData.add(resources.getString(R.string.female))

            mContentViewBinding.genderSp.adapter = context?.let { ArrayAdapter<String>(it, R.layout.custom_spinner_item, genderSpinnerData) }
            mContentViewBinding.genderSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    mContentViewBinding.data!!.gender = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
    }

    private fun setupPasswordTextWatcher() {

        mContentViewBinding.password.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                if (charSequence.toString().length < 8) {
                    mContentViewBinding.passValidImage.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_dot_grey))
                    mContentViewBinding.passLengthValidText.setTextColor(ContextCompat.getColor(context!!, R.color.text_color_secondary))
                } else {
                    mContentViewBinding.passValidImage.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_dot_green))
                    mContentViewBinding.passLengthValidText.setTextColor(ContextCompat.getColor(context!!, R.color.five_star_color))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun onErrorResponse(error: Throwable) {
        if (!NetworkHelper.isNetworkAvailable(context!!) || (error is HttpException && error.code() == 304)) {
            loadOfflineData()
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    context as BaseActivity,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(context!!, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContentViewBinding.loading = true
                        callCreateFormApi()
                    }
                    , getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    private fun loadOfflineData() {
        val response = BaseActivity.mDataBaseHandler.getResponseFromDatabase((context as BaseActivity).mHashIdentifier)
        if (response.isNotEmpty()) {
            onSuccessfulResponse(BaseActivity.mGson.fromJson(response, SignUpFormModel::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (keyboardListenersAttached) {
            mContentViewBinding.signUpBottomSheet.viewTreeObserver.removeGlobalOnLayoutListener(keyboardLayoutListener)
        }
    }

    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val heightDiff: Int
        if (context is LoginAndSignUpActivity)
            heightDiff = Utils.screenHeight - (context as LoginAndSignUpActivity).mContentViewBinding.root.height
        else
            heightDiff = Utils.screenHeight - (context as OrderPlacedActivity).mContentViewBinding.root.height
        if (heightDiff < 200) {
            onHideKeyboard()
        } else {
            onShowKeyboard(heightDiff)
        }
    }

    private var keyboardListenersAttached = false

    protected fun onShowKeyboard(keyboardHeight: Int) {
        val layoutParams = mContentViewBinding.keyboardHeightLayout.layoutParams
        layoutParams.height = keyboardHeight
        mContentViewBinding.keyboardHeightLayout.layoutParams = layoutParams
    }

    protected fun onHideKeyboard() {
        val layoutParams = mContentViewBinding.keyboardHeightLayout.layoutParams
        layoutParams.height = 0
        mContentViewBinding.keyboardHeightLayout.layoutParams = layoutParams
    }

    protected fun attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return
        }

        mContentViewBinding.signUpBottomSheet.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)

        keyboardListenersAttached = true
    }
}