package com.webkul.mobikul.activities

import android.app.ActionBar
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ActivityAdvancedSearchBinding
import com.webkul.mobikul.handlers.AdvancedSearchActivityHandler
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.catalog.AdvancedSearchFormModel
import com.webkul.mobikul.models.extra.AdvanceSearchFieldList
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*


class AdvancedSearchActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityAdvancedSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_advanced_search)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.activity_title_advanced_search)
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getAdvanceSearchFormData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCurrencyCode(this))
        ApiConnection.getAdvanceSearchFormData(this, mDataBaseHandler.getETagFromDatabase(mHashIdentifier))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<AdvancedSearchFormModel>(this, false) {
                    override fun onNext(advancedSearchFormModel: AdvancedSearchFormModel) {
                        super.onNext(advancedSearchFormModel)
                        mContentViewBinding.loading = false
                        if (advancedSearchFormModel.success) {
                            onSuccessfulResponse(advancedSearchFormModel)
                        } else {
                            onFailureResponse(advancedSearchFormModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(advancedSearchFormModel: AdvancedSearchFormModel) {
        mContentViewBinding.data = advancedSearchFormModel
        mContentViewBinding.handler = AdvancedSearchActivityHandler(this)
        setupAdvanceSearchForm(advancedSearchFormModel)
        setupScrollView()
    }

    private fun setupScrollView() {
        mContentViewBinding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY - oldScrollY < 0 || scrollY > mContentViewBinding.scrollView.getChildAt(0).height - mContentViewBinding.scrollView.height - 100) {
                mContentViewBinding.searchBtn.animate().alpha(1.0f).translationY(0f).interpolator = DecelerateInterpolator(1.4f)
            } else {
                mContentViewBinding.searchBtn.animate().alpha(0f).translationY(mContentViewBinding.searchBtn.height.toFloat()).interpolator = AccelerateInterpolator(1.4f)
            }
        })
    }

    private fun setupAdvanceSearchForm(advancedSearchFormModel: AdvancedSearchFormModel) {
        if (advancedSearchFormModel.fieldList.isNotEmpty()) {
            advancedSearchFormModel.fieldList.forEachIndexed { index, eachField ->
                when (eachField.inputType) {
                    "string" -> {
                        addStringTypeAttribute(index, eachField)
                    }
                    "price" -> {
                        addPriceTypeAttribute(index)
                    }
                    "date" -> {
                        addDateTypeAttribute(index)
                    }
                    "select" -> {
                        addSelectTypeAttribute(index, eachField)
                    }
                    "yesno" -> {

                    }
                    "button" -> {

                    }
                }
            }
            for (eachField in advancedSearchFormModel.fieldList) {

            }
        }
    }

    private fun addStringTypeAttribute(index: Int, eachField: AdvanceSearchFieldList) {
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val til = TextInputLayout(ContextThemeWrapper(this, R.style.CustomTilStyle))
        til.layoutParams = layoutParams

        val editText = EditText(this)
        editText.layoutParams = layoutParams
        editText.textSize = 16f
        editText.tag = "inputType/$index"
        editText.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary))
        editText.hint = eachField.label
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.text_color_secondary))

        til.addView(editText)

        mContentViewBinding.advanceSearchFieldContainer.addView(til)
    }

    private fun addPriceTypeAttribute(index: Int) {
        val priceLinearLayout = LinearLayout(this)
        priceLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        priceLinearLayout.tag = "inputType/$index"
        priceLinearLayout.orientation = LinearLayout.HORIZONTAL
        priceLinearLayout.gravity = Gravity.CENTER_VERTICAL

        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.weight = 1f

        val priceFromTil = TextInputLayout(ContextThemeWrapper(this, R.style.CustomTilStyle))
        priceFromTil.layoutParams = layoutParams

        val priceFromEditText = EditText(this)
        priceFromEditText.id = R.id.price_from_et
        priceFromEditText.textSize = 16f
        priceFromEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        priceFromEditText.setSingleLine()
        priceFromEditText.tag = "priceFrom"
        priceFromEditText.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary))
        priceFromEditText.hint = resources.getString(R.string.price_from)
        priceFromEditText.setHintTextColor(ContextCompat.getColor(this, R.color.text_color_secondary))
        priceFromTil.addView(priceFromEditText)

        priceLinearLayout.addView(priceFromTil)

        priceLinearLayout.addView(getDashView())

        val priceToTil = TextInputLayout(ContextThemeWrapper(this, R.style.CustomTilStyle))
        priceToTil.layoutParams = layoutParams

        val priceToEditText = EditText(this)
        priceToEditText.id = R.id.price_to_et
        priceToEditText.textSize = 16f
        priceToEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        priceToEditText.setSingleLine()
        priceToEditText.tag = "priceTo"
        priceToEditText.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary))
        priceToEditText.hint = resources.getString(R.string.price_to)
        priceToEditText.setHintTextColor(ContextCompat.getColor(this, R.color.text_color_secondary))
        priceToTil.addView(priceToEditText)

        priceLinearLayout.addView(priceToTil)

        val currencyTv = TextView(this)
        currencyTv.textSize = 16f
        currencyTv.setPadding(10, 0, 10, 0)
        currencyTv.text = AppSharedPref.getCurrencyCode(this)
        currencyTv.setTextColor(ContextCompat.getColor(this, R.color.text_color_secondary))

        priceLinearLayout.addView(currencyTv)

        mContentViewBinding.advanceSearchFieldContainer.addView(priceLinearLayout)
    }

    private fun addDateTypeAttribute(index: Int) {
        val myCalendar = Calendar.getInstance()

        val dateLinearLayout = LinearLayout(this)
        dateLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dateLinearLayout.tag = "inputType/$index"
        dateLinearLayout.orientation = LinearLayout.HORIZONTAL
        dateLinearLayout.gravity = Gravity.CENTER_VERTICAL
        dateLinearLayout.setPadding(0, 30, 0, 0)

        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.weight = 1f

        val dateFromTil = TextInputLayout(ContextThemeWrapper(this, R.style.CustomTilStyle))
        dateFromTil.layoutParams = layoutParams

        val editText3 = EditText(this)
        editText3.setSingleLine()
        editText3.tag = "dateFrom"
        editText3.hint = resources.getString(R.string.date_from)
        editText3.textSize = 14f
        editText3.inputType = InputType.TYPE_NULL
        editText3.isFocusable = false
        dateFromTil.addView(editText3)

        dateLinearLayout.addView(dateFromTil)

        val date1: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "MM/dd/yy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            editText3.setText(sdf.format(myCalendar.time))
        }
        editText3.setOnClickListener { DatePickerDialog(this@AdvancedSearchActivity, R.style.AlertDialogTheme, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show() }

        dateLinearLayout.addView(getDashView())


        val dateToTil = TextInputLayout(ContextThemeWrapper(this, R.style.CustomTilStyle))
        dateToTil.layoutParams = layoutParams

        val editText4 = EditText(this)
        editText4.setSingleLine()
        editText4.tag = "dateTo"
        editText4.hint = resources.getString(R.string.date_to)
        editText4.textSize = 14f
        editText4.inputType = InputType.TYPE_NULL
        editText4.isFocusable = false
        dateToTil.addView(editText4)

        dateLinearLayout.addView(dateToTil)

        val fillGape = View(this)
        fillGape.layoutParams = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.MATCH_PARENT, 1.0f)
        fillGape.setBackgroundColor(Color.WHITE)
        dateLinearLayout.addView(fillGape)

        val resetDateBtn = Button(this)
        resetDateBtn.tag = index
        resetDateBtn.text = resources.getString(R.string.reset_date)
        resetDateBtn.textSize = 14f

        resetDateBtn.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.white))
        resetDateBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
        resetDateBtn.setOnClickListener {
            (dateLinearLayout.findViewWithTag<View>("dateFrom") as EditText).setText("")
            (dateLinearLayout.findViewWithTag<View>("dateTo") as EditText).setText("")
        }
        dateLinearLayout.addView(resetDateBtn)

        val date2: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "MM/dd/yy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            editText4.setText(sdf.format(myCalendar.time))
        }

        editText4.setOnClickListener { DatePickerDialog(this@AdvancedSearchActivity, R.style.AlertDialogTheme, date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show() }
        mContentViewBinding.advanceSearchFieldContainer.addView(dateLinearLayout)
    }

    private fun getDashView(): TextView {
        val customTextViewDash = TextView(this)
        customTextViewDash.text = "   -   "
        customTextViewDash.textSize = 14f
        return customTextViewDash
    }

    private fun addSelectTypeAttribute(index: Int, eachField: AdvanceSearchFieldList) {
        eachField.options.forEachIndexed { optionIndex, eachFieldOption ->
            val chk = CheckBox(this)
            chk.tag = "inputType/$index/check/$optionIndex"
            chk.text = eachFieldOption.label
            chk.textSize = 14f
            mContentViewBinding.advanceSearchFieldContainer.addView(chk)
        }
    }

    private fun onFailureResponse(advancedSearchFormModel: AdvancedSearchFormModel) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(R.string.error),
                advancedSearchFormModel.message,
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    callApi()
                }
                , getString(R.string.dismiss)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            finish()
        })
    }

    private fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, AdvancedSearchFormModel::class.java))
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        callApi()
                    }
                    , getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            })
        }
    }
}