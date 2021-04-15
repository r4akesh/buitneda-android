package com.webkul.mobikul.activities

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.MyWishListRvAdapter
import com.webkul.mobikul.databinding.ActivityMyWishlistBinding
import com.webkul.mobikul.fragments.EmptyFragment
import com.webkul.mobikul.handlers.MyWishListActivityHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.WishListResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

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

class MyWishListActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityMyWishlistBinding
    var mPageNumber = 1
    private var mShareCart: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_wishlist)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        mShareCart = menu.findItem(R.id.menu_item_share)
        menu.findItem(R.id.menu_item_search).isVisible = false
        menu.findItem(R.id.menu_item_notification).isVisible = false
        mShareCart?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_share -> {
                mContentViewBinding.handler?.onClickShareWishList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
        checkAndLoadLocalData()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.activity_title_my_wish_list)
    }

    fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getWishList" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + AppSharedPref.getCurrencyCode(this) + mPageNumber)
        ApiConnection.getWishList(this, mDataBaseHandler.getETagFromDatabase(mHashIdentifier), mPageNumber++)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<WishListResponseModel>(this, false) {
                    override fun onNext(wishlistResponseModel: WishListResponseModel) {
                        super.onNext(wishlistResponseModel)
                        mContentViewBinding.loading = false
                        if (wishlistResponseModel.success) {
                            onSuccessfulResponse(wishlistResponseModel)
                        } else {
                            onFailureResponse(wishlistResponseModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun checkAndLoadLocalData() {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if (response.isNotBlank()) {
            val wishListResponseModel: WishListResponseModel = mGson.fromJson(response, WishListResponseModel::class.java)
            if (wishListResponseModel.totalCount > 0) {
                onSuccessfulResponse(wishListResponseModel)
            }
        }
    }

    private fun onSuccessfulResponse(wishListResponseModel: WishListResponseModel) {
        if (mPageNumber == 2) {
            Handler().postDelayed({
                mShareCart?.isVisible = wishListResponseModel.totalCount != 0
            }, 500)

            mContentViewBinding.data = wishListResponseModel
            if (mContentViewBinding.data!!.wishList.isEmpty()) {
                addEmptyLayout()
            } else {
                removeEmptyLayout()
                setupWishListRv()
            }
            mContentViewBinding.handler = MyWishListActivityHandler(this)
        } else {
            mContentViewBinding.data!!.wishList.addAll(wishListResponseModel.wishList)
            mContentViewBinding.wishListRv.adapter?.notifyItemRangeChanged(mContentViewBinding.data!!.wishList.size - wishListResponseModel.wishList.size, wishListResponseModel.wishList.size)
        }
    }

    private fun addEmptyLayout() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content
                , EmptyFragment.newInstance("empty_wish_list.json", getString(R.string.empty_wishlist), getString(R.string.there_is_not_item_in_your_wishlist), false)
                , EmptyFragment::class.java.simpleName)
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun removeEmptyLayout() {
        val emptyFragment = supportFragmentManager.findFragmentByTag(EmptyFragment::class.java.simpleName)
        if (emptyFragment != null)
            supportFragmentManager.beginTransaction().remove(emptyFragment).commitAllowingStateLoss()
    }

    private fun setupWishListRv() {
        mContentViewBinding.wishListRv.isNestedScrollingEnabled = false
        mContentViewBinding.wishListRv.layoutManager = GridLayoutManager(this, 2)
        mContentViewBinding.wishListRv.adapter = MyWishListRvAdapter(this, mContentViewBinding.data!!.wishList)

        mContentViewBinding.wishListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!mContentViewBinding.loading!! && mContentViewBinding.data!!.wishList.size < mContentViewBinding.data!!.totalCount
                        && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.wishList.size - 3) {
                    callApi()
                }
            }
        })
    }

    override fun onFailureResponse(response: Any) {
        super.onFailureResponse(response)
        when ((response as BaseModel).otherError) {
            ConstantsHelper.CUSTOMER_NOT_EXIST -> {
                // Do nothing as it will be handled from the super.
            }
            else -> {
                AlertDialogHelper.showNewCustomDialog(
                        this,
                        getString(R.string.error),
                        response.message,
                        false,
                        getString(R.string.ok),
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()
                            mPageNumber--
                            callApi()
                        }
                        , ""
                        , null)
            }
        }
    }

    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && mContentViewBinding.data != null) {
            // Do Nothing as the data is already loaded
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mPageNumber--
                        callApi()
                    }
                    , getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            })
        }
    }
}