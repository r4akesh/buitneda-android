package com.webkul.mobikulmp.handlers

import android.content.DialogInterface
import android.view.View
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.CreateAttributeActivity


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 23/5/19
 */

class AttributeOptionItemHandler {

    fun onClickDeleteItem(v: View) {

        AlertDialogHelper.showNewCustomDialog(
                v.context as BaseActivity,
                v.context.getString(R.string.remove_item),
                v.context.getString(R.string.are_you_sure),
                false,
                v.context.getString(R.string.yes_remove_it),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    (v.context as CreateAttributeActivity).mCreateAttributeRequestData.attributeOptionList.removeAt(v.tag as Int)
                    (v.context as CreateAttributeActivity).mContentViewBinding.attributeOptionRv.getAdapter()?.notifyDataSetChanged()
                    dialogInterface.dismiss()
                },
                v.context.getString(com.webkul.mobikul.R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })


    }
}