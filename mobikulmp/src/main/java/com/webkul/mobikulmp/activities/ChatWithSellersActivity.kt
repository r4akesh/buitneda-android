package com.webkul.mobikulmp.activities


import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.ChatSellerAdapter
import com.webkul.mobikulmp.databinding.ActivityChatWithSellersBinding
import com.webkul.mobikulmp.models.chat.ChatSellerData
import com.webkul.mobikulmp.models.chat.ChatSellerListResponseData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class ChatWithSellersActivity : BaseActivity() {
    private lateinit var mContentViewBinding: ActivityChatWithSellersBinding

    private var mSellerAdapter: ChatSellerAdapter? = null
    private var mChatSellerListResponseData: ChatSellerListResponseData? = null
    private val mSellerDataList = ArrayList<ChatSellerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat_with_sellers)
        initSupportActionBar()
        callApi()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.chat_with_sellers)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun callApi() {
        mContentViewBinding.loading = true

        MpApiConnection.getChatSellerList(this@ChatWithSellersActivity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ChatSellerListResponseData>(this@ChatWithSellersActivity, true) {
                    override fun onNext(t: ChatSellerListResponseData) {
                        super.onNext(t)
                        mContentViewBinding.loading = false
                        try {
                            mChatSellerListResponseData = t
                            if (mChatSellerListResponseData != null && mChatSellerListResponseData!!.success && mChatSellerListResponseData!!.sellerList != null && mChatSellerListResponseData!!.sellerList!!.size > 0) {
                                if (mChatSellerListResponseData!!.apiKey != null && !mChatSellerListResponseData!!.apiKey!!.isEmpty()) {
                                    AppSharedPref.setApiKey(this@ChatWithSellersActivity, mChatSellerListResponseData!!.apiKey!!)
                                }
                                mContentViewBinding.data = mChatSellerListResponseData
                                setupSellersRv()
                                mSellerDataList.addAll(mChatSellerListResponseData!!.sellerList!!)
                                mSellerAdapter!!.notifyDataSetChanged()
                                mContentViewBinding.showNoresultTv = false
                            } else {
                                mContentViewBinding.sellerSearchCardView.visibility = View.GONE
                                mContentViewBinding.showNoresultTv = true
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        mContentViewBinding.sellerSearchCardView.visibility = View.GONE
                        mContentViewBinding.showNoresultTv = true
                        ToastHelper.showToast(context, getString(R.string.something_went_wrong))
                    }
                })
    }

    private fun setupSellersRv() {
        mContentViewBinding.sellerSearchCardView.visibility = View.VISIBLE
        mSellerAdapter = ChatSellerAdapter(this, mSellerDataList)
        mContentViewBinding.sellerRv.adapter = mSellerAdapter
        mContentViewBinding.sellerSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchSeller(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchSeller(newText)
                return true
            }
        })
    }

    private fun searchSeller(sellerName: String?) {
        if (sellerName == null || sellerName.isEmpty() /*|| sellerName.length < 3*/) {
            mSellerDataList.clear()
            mSellerDataList.addAll(mChatSellerListResponseData!!.sellerList!!)
            mSellerAdapter!!.notifyDataSetChanged()
            mContentViewBinding.showNoresultTv = false
        } else {
            mSellerAdapter!!.filter.filter(sellerName) { count ->
                if (count > 0) {
                    mContentViewBinding.showNoresultTv = false
                } else if (count == 0) {
                    mContentViewBinding.showNoresultTv = true
                }
            }
        }
    }
}