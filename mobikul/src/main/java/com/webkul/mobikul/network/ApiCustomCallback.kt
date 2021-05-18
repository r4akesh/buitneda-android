package com.webkul.mobikul.network

import android.content.Context
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.BaseActivity.Companion.mDataBaseHandler
import com.webkul.mobikul.activities.BaseActivity.Companion.mGson
import com.webkul.mobikul.helpers.ApplicationConstants.ENABLE_OFFLINE_MODE
import com.webkul.mobikul.helpers.Utils.Companion.disableUserInteraction
import com.webkul.mobikul.helpers.Utils.Companion.enableUserInteraction
import com.webkul.mobikul.models.BaseModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

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

open class ApiCustomCallback<R : BaseModel>(
    val context: Context,
    val isDisableInteraction: Boolean
) : Observer<R> {

    override fun onSubscribe(disposable: Disposable) {
        try {
            if (context is BaseActivity)
                context.mCompositeDisposable.add(disposable)
            if (isDisableInteraction)
                disableUserInteraction(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNext(responseModel: R) {
        if (context is BaseActivity) {
            onNext(responseModel, context.mHashIdentifier)
        } else {
            onNext(responseModel, "")
        }

    }


    fun onNext(responseModel: R, eTag: String) {
        try {
            enableUserInteraction(context)
            if ((responseModel as BaseModel).success) {
                if (ENABLE_OFFLINE_MODE && eTag.isNotEmpty())
                    mDataBaseHandler.addOrUpdateIntoOfflineTable(
                        eTag,
                        responseModel.eTag,
                        mGson.toJson(responseModel)
                    )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onError(e: Throwable) {
        enableUserInteraction(context)
        e.printStackTrace()
    }

    override fun onComplete() {
        enableUserInteraction(context)
    }
}