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

package com.webkul.mobikul.helpers

import android.content.Context
import android.os.AsyncTask
import com.webkul.mobikul.activities.HomeActivity
import org.jsoup.Jsoup

class VersionChecker(private val context: Context) : AsyncTask<String, String, String>() {

    override fun doInBackground(vararg params: String?): String {
        var newVersion = "1.00"
        try {
            val document = Jsoup.connect("https://play.google.com/store/apps/details?id=${context.packageName}")
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
            if (document != null) {
                val element = document.getElementsContainingOwnText("Current Version")
                for (ele in element) {
                    if (ele.siblingElements() != null) {
                        val sibElements = ele.siblingElements()
                        for (sibElement in sibElements) {
                            newVersion = sibElement.text()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return newVersion
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        (context as HomeActivity).onLatestVersionResponse(result)
    }
}