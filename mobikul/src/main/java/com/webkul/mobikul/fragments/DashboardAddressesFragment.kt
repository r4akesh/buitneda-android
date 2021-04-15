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

package com.webkul.mobikul.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.databinding.FragmentDashboardAddressesBinding
import com.webkul.mobikul.handlers.DashboardAddressesFragmentHandler
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ApplicationConstants
import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.user.AddressBookResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DashboardAddressesFragment : BaseFragment() {

    lateinit var mContentViewBinding: FragmentDashboardAddressesBinding
    private var mHashIdentifier: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_addresses, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getAddressBookDataForDashboard" + AppSharedPref.getStoreId(context!!) + AppSharedPref.getCustomerToken(context!!))
        ApiConnection.getAddressBookData(context!!, BaseActivity.mDataBaseHandler.getETagFromDatabase(mHashIdentifier), true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<AddressBookResponseModel>(context!!, false) {
                    override fun onNext(addressBookResponseModel: AddressBookResponseModel) {
                        super.onNext(addressBookResponseModel)
                        mContentViewBinding.loading = false
                        if (addressBookResponseModel.success) {
                            if (ApplicationConstants.ENABLE_OFFLINE_MODE && mHashIdentifier.isNotEmpty())
                                BaseActivity.mDataBaseHandler.addOrUpdateIntoOfflineTable(mHashIdentifier, addressBookResponseModel.eTag, BaseActivity.mGson.toJson(addressBookResponseModel))
                            onSuccessfulResponse(addressBookResponseModel)
                        } else {
                            onFailureResponse(addressBookResponseModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
        checkAndLoadLocalData()
    }

    private fun checkAndLoadLocalData() {
        val response = BaseActivity.mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(BaseActivity.mGson.fromJson(response, AddressBookResponseModel::class.java))
        }
    }

    private fun onSuccessfulResponse(addressBookResponseModel: AddressBookResponseModel?) {
        mContentViewBinding.data = addressBookResponseModel
        mContentViewBinding.handler = DashboardAddressesFragmentHandler(this)
    }

    private fun onFailureResponse(addressBookResponseModel: AddressBookResponseModel) {

    }

    private fun onErrorResponse(e: Throwable) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == ConstantsHelper.RC_ADD_EDIT_ADDRESS) {
                callApi()
            }
        }
    }
}
