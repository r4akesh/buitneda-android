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
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.CheckoutActivity
import com.webkul.mobikul.adapters.PriceDetailsRvAdapter
import com.webkul.mobikul.adapters.ProductsReviewRvAdapter
import com.webkul.mobikul.databinding.FragmentPaymentInfoBinding
import com.webkul.mobikul.handlers.PaymentInfoFragmentHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CHECKOUT_ADDRESS_DATA
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_SHIPPING_METHOD
import com.webkul.mobikul.helpers.ConstantsHelper.RC_ADD_EDIT_ADDRESS
import com.webkul.mobikul.helpers.ConstantsHelper.RC_PAYMENT
import com.webkul.mobikul.models.checkout.BillingShippingAddress
import com.webkul.mobikul.models.checkout.CheckoutAddressInfoResponseModel
import com.webkul.mobikul.models.checkout.ReviewsAndPaymentsResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.models.wallet.ApplyPaymentAmountResponseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.ExecutionException

class PaymentInfoFragment : BaseFragment() {
    var appliedCoupon = false

    internal var onDetachInterface: OnDetachInterface? = null


    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
    }

    companion object {
        fun newInstance(shippingMethod: String, checkoutAddressData: CheckoutAddressInfoResponseModel?): PaymentInfoFragment {
            val paymentInfoFragment = PaymentInfoFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_SHIPPING_METHOD, shippingMethod)
            if (checkoutAddressData != null)
                args.putParcelable(BUNDLE_KEY_CHECKOUT_ADDRESS_DATA, checkoutAddressData)
            paymentInfoFragment.arguments = args
            return paymentInfoFragment
        }
    }

    lateinit var mContentViewBinding: FragmentPaymentInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_info, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
    }

    private fun initSupportActionBar() {
        (context as CheckoutActivity).supportActionBar?.title = getString(R.string.review_payment)
        (context as CheckoutActivity).mContentViewBinding.paymentPageIndicator.setBackgroundColor(ContextCompat.getColor(context as CheckoutActivity, R.color.colorAccent))
    }

    fun callApi() {
        (context as CheckoutActivity).mContentViewBinding.loading = true
        ApiConnection.getReviewsAndPaymentsData(context as CheckoutActivity, arguments!!.getString(BUNDLE_KEY_SHIPPING_METHOD))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ReviewsAndPaymentsResponseModel>(context as CheckoutActivity, true) {
                    override fun onNext(reviewsAndPaymentsResponseModel: ReviewsAndPaymentsResponseModel) {
                        super.onNext(reviewsAndPaymentsResponseModel)
                        (context as CheckoutActivity).mContentViewBinding.loading = false
                        if (reviewsAndPaymentsResponseModel.success) {
                            onSuccessfulResponse(reviewsAndPaymentsResponseModel)
                        } else {
                            onFailureResponse(reviewsAndPaymentsResponseModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        (context as CheckoutActivity).mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(reviewsAndPaymentsResponseModel: ReviewsAndPaymentsResponseModel) {
        if (mContentViewBinding.data == null) {
            reviewsAndPaymentsResponseModel.couponCode = null
            mContentViewBinding.data = reviewsAndPaymentsResponseModel
            if (arguments!!.containsKey(BUNDLE_KEY_CHECKOUT_ADDRESS_DATA)) {
                onGetAddressSuccessfulResponse(arguments?.getParcelable(BUNDLE_KEY_CHECKOUT_ADDRESS_DATA))
            } else
                getAddressData()
            setupWalletLayout()

            if (!appliedCoupon) {
                setupPaymentMethods()
            }
            setupProductsReviewRv()
            setupPriceDetails()
        } else {
            mContentViewBinding.data!!.orderReviewData.totals = reviewsAndPaymentsResponseModel.orderReviewData.totals
            mContentViewBinding.data!!.cartTotal = reviewsAndPaymentsResponseModel.cartTotal
            setupPriceDetails()
        }

    }

    private fun setupPaymentMethods() {
        var isAnyPaymentGatewayVisible = false
        mContentViewBinding.data!!.paymentMethods.forEach { eachPaymentMethod ->
            if (ApplicationConstants.AVAILABLE_PAYMENT_METHOD.contains(eachPaymentMethod.code)  && eachPaymentMethod.code != ApplicationConstants.PAYMENT_MP_WALLET) {
                isAnyPaymentGatewayVisible = true
                val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val eachPaymentMethodRb = RadioButton(context)
                eachPaymentMethodRb.layoutParams = layoutParams
                eachPaymentMethodRb.text = eachPaymentMethod.title
                eachPaymentMethodRb.textSize = 14f
                eachPaymentMethodRb.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                eachPaymentMethodRb.setTextColor(ContextCompat.getColor(context!!, R.color.text_color_primary))
                eachPaymentMethodRb.tag = eachPaymentMethod.code

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    eachPaymentMethodRb.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.colorAccent))
                }
                eachPaymentMethodRb.highlightColor = resources.getColor(R.color.colorAccent)
                if (!eachPaymentMethod.imageUrl.isEmpty()) {
                    drawableFromUrl(eachPaymentMethod.imageUrl, eachPaymentMethodRb)
                }
                mContentViewBinding.paymentMethodRg.addView(eachPaymentMethodRb)
                if (!eachPaymentMethod.extraInformation.isEmpty()) {
                    val textView = TextView(context)
                    textView.layoutParams = layoutParams
                    textView.tag = "extraInformation" + eachPaymentMethod.code
                    textView.text = eachPaymentMethod.extraInformation
                    val leftRightPadding = Utils.convertDpToPixel(resources.getDimension(R.dimen.spacing_generic), context!!).toInt()
                    textView.setPadding(mContentViewBinding.paymentMethodRg.paddingLeft + leftRightPadding, 10, mContentViewBinding.paymentMethodRg.paddingRight + leftRightPadding, 10)
                    textView.visibility = View.GONE
                    textView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_200))
                    mContentViewBinding.paymentMethodRg.addView(textView)
                }
            }
        }


        if (isAnyPaymentGatewayVisible) {
            mContentViewBinding.isPaymentMethodAvailable = true
            mContentViewBinding.paymentMethodRg.setOnCheckedChangeListener { group, checkedId ->
                val selectedRb: RadioButton? = group?.findViewById<RadioButton>(checkedId)
                mContentViewBinding.data!!.selectedPaymentMethod = selectedRb?.tag.toString()
                val views = getViewsByTag(mContentViewBinding.paymentMethodRg, "extraInformation")
                for (view in views) {
                    view.visibility = View.GONE
                }

                if (mContentViewBinding.paymentMethodRg.findViewWithTag<TextView>("extraInformation" + selectedRb?.tag.toString()) != null) {
                    val textView = mContentViewBinding.paymentMethodRg.findViewWithTag("extraInformation" + selectedRb?.tag.toString()) as TextView
                    textView.visibility = View.VISIBLE
                    textView.setBackgroundColor(resources.getColor(R.color.color_whiteBlack))
                    textView.setTextColor(resources.getColor(R.color.text_color_primary))
                }
            }
        } else {
            mContentViewBinding.isPaymentMethodAvailable = false
        }
    }

    private fun getViewsByTag(root: ViewGroup, tagKey: String): ArrayList<View> {
        val views = ArrayList<View>()
        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tagKey))
            }

            val tagObj = child.tag
            if (tagObj != null && tagObj.toString().contains(tagKey)) {
                views.add(child)
            }
        }
        return views
    }

    /*Wallet*/

    private fun setupWalletLayout() {
        if (AppSharedPref.isLoggedIn(context!!)) {
            for (paymentMethodPosition in mContentViewBinding.data!!.paymentMethods.indices) {
                if (mContentViewBinding.data!!.paymentMethods[paymentMethodPosition].code == ApplicationConstants.PAYMENT_MP_WALLET) {
                    mContentViewBinding.walletLayout.visibility = View.VISIBLE
                    mContentViewBinding.payWithWalletCb.setOnCheckedChangeListener { compoundButton, _ ->
                        if (compoundButton.isChecked) {
                            if (mContentViewBinding.walletData != null)
                                callWalletApi("set", mContentViewBinding.walletData!!.walletData.unformattedPaymentToMade.toString())
                        } else {
                            callWalletApi("reset", "0")
                        }
                    }
                    callWalletApi("reset", "0")
                    break
                }
            }
        }
    }

    private fun callWalletApi(wallet: String, amount: String) {

        (context as CheckoutActivity).mContentViewBinding.loading = true
        ApiConnection.applyWalletPaymentAmount(context as CheckoutActivity, wallet, amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ApplyPaymentAmountResponseData>(context as CheckoutActivity, true) {
                    override fun onNext(applyPaymentAmountResponseData: ApplyPaymentAmountResponseData) {
                        super.onNext(applyPaymentAmountResponseData)
                        mContentViewBinding.payWithWalletCb.visibility = View.VISIBLE
                        if (applyPaymentAmountResponseData.success) {
                            callApi()
                            mContentViewBinding.walletData = applyPaymentAmountResponseData
                            if (mContentViewBinding.payWithWalletCb.isChecked) {
                                if (applyPaymentAmountResponseData.walletData.unformattedLeftAmountToPay == 0.toDouble()) {
                                    for (i in 0 until mContentViewBinding.paymentMethodRg.childCount) {
                                        mContentViewBinding.paymentMethodRg.getChildAt(i).isEnabled = false
                                    }
                                    mContentViewBinding.paymentMethodRg.clearCheck()
                                    mContentViewBinding.data!!.selectedPaymentMethod = ""
                                }
                                mContentViewBinding.walletDetailsLayout.visibility = View.VISIBLE
                            } else {
                                for (i in 0 until mContentViewBinding.paymentMethodRg.childCount) {
                                    mContentViewBinding.paymentMethodRg.getChildAt(i).isEnabled = true
                                }
                                mContentViewBinding.walletDetailsLayout.visibility = View.GONE
                                if (applyPaymentAmountResponseData.walletData.unformattedAmountInWallet == 0.toDouble()) {
                                    mContentViewBinding.noAmountWalletMsg.visibility = View.VISIBLE
                                    mContentViewBinding.payWithWalletCb.visibility = View.GONE
                                }
                            }
                        } else {
                            (context as CheckoutActivity).mContentViewBinding.loading = false
                            ToastHelper.showToast(context, applyPaymentAmountResponseData.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        (context as CheckoutActivity).mContentViewBinding.loading = false
                        ToastHelper.showToast(context, getString(R.string.something_went_wrong))
                    }
                })
    }

    private fun setupProductsReviewRv() {
        mContentViewBinding.productsReviewRv.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        mContentViewBinding.productsReviewRv.adapter = ProductsReviewRvAdapter(context!!, mContentViewBinding.data!!.orderReviewData.items)
    }

    private fun setupPriceDetails() {
        mContentViewBinding.priceDetailsRv.adapter = PriceDetailsRvAdapter(context!!, mContentViewBinding.data!!.orderReviewData.totals)
    }

    private fun onFailureResponse(reviewsAndPaymentsResponseModel: ReviewsAndPaymentsResponseModel) {
        if (reviewsAndPaymentsResponseModel.cartCount == 0) {
            AlertDialogHelper.showNewCustomDialog(
                    (context as CheckoutActivity),
                    getString(R.string.error),
                    reviewsAndPaymentsResponseModel.message,
                    false,
                    getString(R.string.ok),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        (context as CheckoutActivity).finish()
                    })
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    (context as CheckoutActivity),
                    getString(R.string.error),
                    reviewsAndPaymentsResponseModel.message,
                    false,
                    getString(R.string.try_again),
                    { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        when (reviewsAndPaymentsResponseModel.otherError) {
                            ConstantsHelper.CUSTOMER_NOT_EXIST -> {
                                context?.let {
                                    Utils.logoutAndGoToHome(it)
                                }
                            }
                            else -> callApi()
                        }
                    }, getString(R.string.dismiss), { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                if ((context as CheckoutActivity).mIsVirtual) {
                    (context as CheckoutActivity).finish()
                } else {
                    (context as CheckoutActivity).supportFragmentManager.popBackStack()
                }
            })
        }
    }

    private fun onErrorResponse(error: Throwable) {
        AlertDialogHelper.showNewCustomDialog(
                (context as CheckoutActivity),
                getString(R.string.error),
                NetworkHelper.getErrorMessage((context as CheckoutActivity), error),
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    callApi()
                }, getString(R.string.dismiss), DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            if ((context as CheckoutActivity).mIsVirtual) {
                (context as CheckoutActivity).finish()
            } else {
                (context as CheckoutActivity).supportFragmentManager.popBackStack()
            }
        })
    }

    private fun drawableFromUrl(url: String, eachPaymentMethodRb: RadioButton) {
        try {
            object : AsyncTask<Void, Void, Drawable>() {
                override fun doInBackground(vararg params: Void): Drawable {
                    if (Looper.myLooper() == null) {
                        Looper.prepare()
                    }
                    var theBitmap: Bitmap? = null
                    try {
                        theBitmap = Glide.with(context!!)
                                .asBitmap()
                                .load(url)
                                .into(50, 50) // Width and height
                                .get()
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    return BitmapDrawable(resources, theBitmap)
                }

                override fun onPostExecute(drawableImage: Drawable) {
                    if (AppSharedPref.getStoreCode(context!!) == "ar")
                        eachPaymentMethodRb.setCompoundDrawablesWithIntrinsicBounds(drawableImage, null, null, null)
                    else
                        eachPaymentMethodRb.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableImage, null)
                }
            }.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAddressData() {
        (context as CheckoutActivity).mContentViewBinding.loading = true
        ApiConnection.getCheckoutAddressInfo(context as CheckoutActivity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<CheckoutAddressInfoResponseModel>(context as CheckoutActivity, true) {
                    override fun onNext(checkoutAddressInfoResponseModel: CheckoutAddressInfoResponseModel) {
                        super.onNext(checkoutAddressInfoResponseModel)
                        (context as CheckoutActivity).mContentViewBinding.loading = false
                        if (checkoutAddressInfoResponseModel.success) {
                            onGetAddressSuccessfulResponse(checkoutAddressInfoResponseModel)
                        } else {
                            onGetAddressFailureResponse(checkoutAddressInfoResponseModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        (context as CheckoutActivity).mContentViewBinding.loading = false
                        onGetAddressErrorResponse(e)
                    }
                })
    }

    private fun onGetAddressSuccessfulResponse(checkoutAddressInfoResponseModel: CheckoutAddressInfoResponseModel?) {
        mContentViewBinding.checkoutAddressData = checkoutAddressInfoResponseModel
        setupNewAddressLayout()

        if ((context as CheckoutActivity).mIsVirtual) {
            mContentViewBinding.checkoutAddressData!!.sameAsShipping = false
            mContentViewBinding.checkoutAddressData!!.newAddressData = AppSharedPref.getCustomerCachedNewAddress(context!!)
            if (!mContentViewBinding.checkoutAddressData!!.newAddressData?.firstname.isNullOrEmpty()) {
                mContentViewBinding.checkoutAddressData!!.hasNewAddress = true

                val newBillingShippingAddress = BillingShippingAddress()
                newBillingShippingAddress.id = "new"
                newBillingShippingAddress.value = mContentViewBinding.checkoutAddressData!!.newAddressData?.getFormattedAddress()
                mContentViewBinding.checkoutAddressData!!.addressData?.add(newBillingShippingAddress)
            }
            setupSelectedCheckoutAddress()
        }

        mContentViewBinding.handler = PaymentInfoFragmentHandler(this)
    }

    private fun setupNewAddressLayout() {
        mContentViewBinding.checkoutAddressData!!.newAddressData?.isDefaultBilling = true
        mContentViewBinding.checkoutAddressData!!.newAddressData?.isDefaultShipping = true
    }

    private fun setupSelectedCheckoutAddress() {
        if (!mContentViewBinding.checkoutAddressData?.addressData.isNullOrEmpty()) {
            setSelectedAddressData(mContentViewBinding.checkoutAddressData?.addressData?.get(0))
        }
    }

    private fun onGetAddressFailureResponse(checkoutAddressInfoResponseModel: CheckoutAddressInfoResponseModel) {
        AlertDialogHelper.showNewCustomDialog(
                (context as CheckoutActivity),
                getString(R.string.error),
                checkoutAddressInfoResponseModel.message,
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    getAddressData()
                }, getString(R.string.dismiss), DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            if ((context as CheckoutActivity).mIsVirtual) {
                (context as CheckoutActivity).finish()
            } else {
                (context as CheckoutActivity).supportFragmentManager.popBackStack()
            }
        })
    }

    private fun onGetAddressErrorResponse(error: Throwable) {
        AlertDialogHelper.showNewCustomDialog(
                (context as CheckoutActivity),
                getString(R.string.error),
                NetworkHelper.getErrorMessage((context as CheckoutActivity), error),
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    getAddressData()
                }, getString(R.string.dismiss), DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            if ((context as CheckoutActivity).mIsVirtual) {
                (context as CheckoutActivity).finish()
            } else {
                (context as CheckoutActivity).supportFragmentManager.popBackStack()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == RC_ADD_EDIT_ADDRESS) {
                mContentViewBinding.checkoutAddressData!!.newAddressData = data?.getParcelableExtra(BundleKeysHelper.BUNDLE_KEY_ADDRESS_DATA)!!

                mContentViewBinding.checkoutAddressData!!.newAddressData?.let { AppSharedPref.setCustomerCachedNewAddress(context!!, it) }

                if (!mContentViewBinding.checkoutAddressData!!.addressData.isNullOrEmpty() && mContentViewBinding.checkoutAddressData!!.addressData?.get(mContentViewBinding.checkoutAddressData!!.addressData!!.size - 1)?.id == "new") {
                    mContentViewBinding.checkoutAddressData!!.addressData?.get(mContentViewBinding.checkoutAddressData!!.addressData!!.size - 1)?.value = mContentViewBinding.checkoutAddressData!!.newAddressData?.getFormattedAddress()
                } else {
                    val newBillingShippingAddress = BillingShippingAddress()
                    newBillingShippingAddress.id = "new"
                    newBillingShippingAddress.value = mContentViewBinding.checkoutAddressData!!.newAddressData?.getFormattedAddress()
                    mContentViewBinding.checkoutAddressData!!.addressData?.add(newBillingShippingAddress)

                    mContentViewBinding.checkoutAddressData!!.hasNewAddress = true
                }

                setSelectedAddressData(mContentViewBinding.checkoutAddressData?.addressData?.get(mContentViewBinding.checkoutAddressData?.addressData!!.size - 1))
            } else if (requestCode == RC_PAYMENT) {
                mContentViewBinding.handler?.onPaymentResponse()
            }
        } else {
            if (requestCode == RC_PAYMENT) {
                activity?.finish()
            }
        }
    }

    fun setSelectedAddressData(billingAddress: BillingShippingAddress?) {
        mContentViewBinding.checkoutAddressData!!.selectedAddressData = billingAddress
        mContentViewBinding.checkoutAddressData = mContentViewBinding.checkoutAddressData!!
    }

    override fun onDestroy() {
        super.onDestroy()
        (context as CheckoutActivity).supportActionBar?.title = getString(R.string.shipping)
        (context as CheckoutActivity).mContentViewBinding.paymentPageIndicator.setBackgroundColor(ContextCompat.getColor(context as CheckoutActivity, R.color.divider_color))
    }

    override fun onDetach() {
        super.onDetach()
        if (appliedCoupon)
            onDetachInterface?.onFragmentDetached()
    }
}