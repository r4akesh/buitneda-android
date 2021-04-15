package com.webkul.mobikulmp.models

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

class AccountMpRvModel(var type: Int, var name: String = "", var drawableId: Int = 0) {

    companion object {
        const val SELLER_DASHBOARD = 108
        const val SELLER_PROFILE = 109
        const val SELLER_ORDERS = 110
        const val ATTRIBUTES = 111
        const val ADD_NEW_PRODUCT = 112
        const val SELLER_PRODUCTS = 113
        const val TRANSACTIONS = 114
        const val SELLER_PRODUCT_REVIEWS = 115
        const val INVOICE_PRINT_TEMPLATE = 116
        const val CHAT_WITH_ADMIN = 117
        const val ASK_QUESTIONS = 118
        const val TYPE_SELLER_STATUS = 119
        const val TYPE_CUSTOMERS = 121
    }
}