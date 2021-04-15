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

package com.webkul.mobikulmp.handlers

import android.content.Intent
import android.view.View
import com.webkul.mobikulmp.activities.MarketplaceLandingActivity


class NavDrawerMpRvHandler {

    fun onClickItem(view: View) {
        view.context.startActivity(Intent(view.context, MarketplaceLandingActivity::class.java))
    }
}