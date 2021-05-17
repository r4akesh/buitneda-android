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

package com.webkul.mobikul.handlers

import com.webkul.mobikul.fragments.NotificationBottomSheetFragment
import com.webkul.mobikul.interfaces.OnNotificationListener

class NotificationBottomSheetHandler(var mFragmentContext: NotificationBottomSheetFragment,val notificationListener: OnNotificationListener) {

    fun onClickCancelBtn() {
        mFragmentContext.dismiss()
        notificationListener.onNotificationFragmentClose()

    }
}