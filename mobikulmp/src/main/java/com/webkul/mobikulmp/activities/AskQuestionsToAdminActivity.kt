package com.webkul.mobikulmp.activities

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
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ActivityAskQuestionsBinding
import com.webkul.mobikulmp.handlers.AskQuestionToAdminHandler
import com.webkul.mobikulmp.models.AskQuestionsToAdminRequestData

class AskQuestionsToAdminActivity : BaseActivity() {
    lateinit var mContentViewBinding: ActivityAskQuestionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_ask_questions)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }


    private fun startInitialization() {
        mContentViewBinding.data = AskQuestionsToAdminRequestData()
        mContentViewBinding.handler = AskQuestionToAdminHandler(this)
        initSupportActionBar()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.ask_questions)
    }
}
