package com.webkul.mobikul.wallet.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.DialogFragmentViewTransactionBinding
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.handlers.ViewTransactionDialogHandler
import com.webkul.mobikul.wallet.models.wallet.ViewTransactionResponseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ViewTransactionDialogFragment : DialogFragment() {

    lateinit var mContentViewBinding: DialogFragmentViewTransactionBinding

    companion object {

        fun newInstance(viewId: String): ViewTransactionDialogFragment {
            val viewTransactionDialogFragment = ViewTransactionDialogFragment()
            val args = Bundle()
            args.putString("viewId", viewId)
            viewTransactionDialogFragment.arguments = args
            return viewTransactionDialogFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_view_transaction, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return mContentViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApi()
    }

    private fun callApi() {
        arguments!!.getString("viewId")?.let {
            ApiConnection.viewWalletTransaction(context!!, it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<ViewTransactionResponseData>(context!!, true) {
                        override fun onNext(viewTransactionResponseData: ViewTransactionResponseData) {
                            super.onNext(viewTransactionResponseData)
                            mContentViewBinding.loading = false
                            if (viewTransactionResponseData.success) {
                                onSuccessfulResponse(viewTransactionResponseData)
                            } else {
                                ToastHelper.showToast(context, viewTransactionResponseData.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContentViewBinding.loading = false
                            ToastHelper.showToast(context, getString(R.string.something_went_wrong))
                        }
                    })
        }
    }

    private fun onSuccessfulResponse(viewTransactionResponseData: ViewTransactionResponseData) {
        mContentViewBinding.data = viewTransactionResponseData
        mContentViewBinding.handler = ViewTransactionDialogHandler(this)
    }
}