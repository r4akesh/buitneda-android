package com.webkul.mobikulmp.models

import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.AskQuestionsToAdminActivity

/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.models
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 20/5/19
 *
 */

class AskQuestionsToAdminRequestData {
    var subject: String = ""
    var query: String = ""


    fun isFormValidated(mContext: AskQuestionsToAdminActivity): Boolean {
        var isFormValidated = true
        /* Checking query */
        if (query.trim().isEmpty()) {
            mContext.mContentViewBinding.query.error = mContext.getString(R.string.query) + " " + mContext.getString(R.string.is_required)
            Utils.showShakeError(mContext, mContext.mContentViewBinding.query)
            mContext.mContentViewBinding.query.requestFocus()
            isFormValidated = false
        } else {
            mContext.mContentViewBinding.query.isErrorEnabled = false
            mContext.mContentViewBinding.query.error = null
        }

        /* Checking subject */
        if (subject.trim().isEmpty()) {
            mContext.mContentViewBinding.subject.error = mContext.getString(R.string.subject) + " " + mContext.getString(R.string.is_required)
            Utils.showShakeError(mContext, mContext.mContentViewBinding.subject)
            mContext.mContentViewBinding.subject.requestFocus()
            isFormValidated = false
        } else {
            mContext.mContentViewBinding.subject.isErrorEnabled = false
            mContext.mContentViewBinding.subject.error = null
        }
        return isFormValidated
    }

}