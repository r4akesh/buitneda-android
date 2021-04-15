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

import android.content.Intent
import android.view.View
import com.webkul.mobikul.activities.SplashScreenActivity
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.models.homepage.WebsiteData

class WebsitesRvHandler {

    fun onClickWebsiteItem(view: View, website: WebsiteData) {
        if (AppSharedPref.getWebsiteId(view.context) != website.id) {
            website.id?.let { AppSharedPref.setWebsiteId(view.context, it) }
            website.name?.let { AppSharedPref.setWebsiteLabel(view.context, it) }
            AppSharedPref.setStoreId(view.context, "0")

            AppSharedPref.setShowSplash(view.context, false)

            val intent = Intent(view.context, SplashScreenActivity::class.java)
            view.context.startActivity(intent)
        }
    }
}