package com.webkul.mobikulmp.models.seller

import androidx.databinding.BaseObservable
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.ContactSellerActivity


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/7/19
 */

class ContactSellerRequestData : BaseObservable() {

    var name: String = ""
    var email: String = ""
    var subject: String = ""
    var query: String = ""


    fun isFormValidated(mContext: ContactSellerActivity): Boolean {
        var isFormValidated = true

        if (name.trim().isEmpty()) {
            isFormValidated = false
            mContext.mContentViewBinding.nameTil.error = mContext.getString(R.string.name) + " " + mContext.getString(R.string.is_required)
            mContext.mContentViewBinding.nameTil.requestFocus()
        } else {
            mContext.mContentViewBinding.nameTil.error = null
        }

        if (email.trim().isEmpty()) {
            isFormValidated = false
            mContext.mContentViewBinding.emailTil.error = mContext.getString(R.string.email) + " " + mContext.getString(R.string.is_required)
            mContext.mContentViewBinding.emailTil.requestFocus()
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mContext.mContentViewBinding.emailTil.error = mContext.getString(R.string.enter_a_valid) + " " + mContext.getString(R.string.email)
            mContext.mContentViewBinding.emailTil.requestFocus()
            isFormValidated = false
        } else {
            mContext.mContentViewBinding.emailTil.error = null
        }

        if (subject.trim().isEmpty()) {
            isFormValidated = false
            mContext.mContentViewBinding.subjectTil.error = mContext.getString(R.string.short_description) + " " + mContext.getString(R.string.is_required)
            mContext.mContentViewBinding.subjectTil.requestFocus()
        } else {
            mContext.mContentViewBinding.subjectTil.error = null
        }

        if (query.trim().isEmpty()) {
            isFormValidated = false
            mContext.mContentViewBinding.queryTil.error = mContext.getString(R.string.description) + " " + mContext.getString(R.string.is_required)
            mContext.mContentViewBinding.queryTil.requestFocus()
        } else {
            mContext.mContentViewBinding.queryTil.error = null
        }

        return isFormValidated
    }

}
