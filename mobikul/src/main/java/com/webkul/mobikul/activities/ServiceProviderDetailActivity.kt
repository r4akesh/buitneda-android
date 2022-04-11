package com.webkul.mobikul.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.ProviderReviewListAdapter
import com.webkul.mobikul.adapters.ServiceInfoAdapter
import com.webkul.mobikul.adapters.ServiceProviderGalleryAdapter
import com.webkul.mobikul.databinding.ActivityServiceProviderDetailBinding
import com.webkul.mobikul.databinding.ReviewRatingDialogLayoutBinding
import com.webkul.mobikul.helpers.AppPreference
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.service.ProviderModel
import com.webkul.mobikul.models.service.ServiceProviderReviewModel
import com.webkul.mobikul.network.Status
import com.webkul.mobikul.view_model.CommonViewModel
import kotlinx.android.synthetic.main.activity_service_provider_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ServiceProviderDetailActivity : BaseActivity() {
    private lateinit var mContentBinding: ActivityServiceProviderDetailBinding
    private lateinit var commonViewModel: CommonViewModel
    private var providerId:Int = 0
    private lateinit var provider: ProviderModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentBinding = ActivityServiceProviderDetailBinding.inflate(layoutInflater)
        setContentView(mContentBinding.root)
        init()
    }

    private fun init(){
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        if(intent!=null && intent.hasExtra("serviceId")){
            providerId = intent.getStringExtra("serviceId")!!.toInt()
        }
        makeRequestGetInfo()
        mContentBinding.backBtn.setOnClickListener{
            finish()
        }

        mContentBinding.writeReviewBtn.setOnClickListener{
            showRatingDialog()
        }

        mContentBinding.callToProviderBtn.setOnClickListener {
            makeRequestVisitor("phone")
            Utils.openDialer(this,provider.company_contact_number)
        }

        mContentBinding.chatWithProviderBtn.setOnClickListener {
            makeRequestVisitor("whatsapp")
            Utils.openChat(this,provider.whatsapp_number)
        }

    }

    private fun makeRequestGetInfo(){
        commonViewModel.getServiceProviderInfo(providerId)
        lifecycleScope.launch {
            commonViewModel.serviceInfoState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mContentBinding.loading = true
                    }
                    Status.SUCCESS -> {
                        mContentBinding.loading = false
                        makeRequestGetReviewRating()
                        it.data?.let { comment ->
                            if(comment.provider!=null){
                                mContentBinding.data = comment.provider
                                provider = comment.provider
                                if(comment.provider.review_avg!=null && comment.provider.review_avg!=""){
                                    mContentBinding.myRatingBar.rating =  comment.provider.review_avg.toFloat()
                                }

                                if(comment.provider.service_description!=null){
                                    val serviceList:List<String> = comment.provider.service_description.split("\\.")
                                    setUpDetails(serviceList)
                                }
                                if(comment.provider.business_detail!=null && comment.provider.business_detail!=""){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        mContentBinding.aboutInfo.text = Html.fromHtml(comment.provider.business_detail, Html.FROM_HTML_MODE_LEGACY).toString()

                                    } else {
                                        mContentBinding.aboutInfo.text = Html.fromHtml(intent.getStringExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE)).toString()
                                    }
                                }

                                if(comment.provider.business_images.isNotEmpty()){
                                    setUpBusinessGallery(comment.provider.business_images)
                                }





                            }
                        }
                    }

                    else -> {
                        mContentBinding.loading = false
                        //  Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun makeRequestGetReviewRating(){
        commonViewModel.getProviderReviewList(providerId)
        lifecycleScope.launch {
            commonViewModel.providerReviewListState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mContentBinding.loading = true
                    }
                    Status.SUCCESS -> {
                        mContentBinding.loading = false
                        it.data?.let { comment ->
                            if(comment.review.isNotEmpty()){
                                setUpReviewList(comment.review)
                            }
                        }
                    }
                    else -> {
                        mContentBinding.loading = false
                        //  Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun makeRequestVisitor(clickOn:String) {
        commonViewModel.makeRequestVisitor("Service Provider",clickOn,providerId)
        lifecycleScope.launch {
            commonViewModel.visitorState.collect {
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {

                    }

                    Status.ERROR->{

                    }

                }
            }
        }
    }

    private fun setUpReviewList(list:List<ServiceProviderReviewModel>){
        mContentBinding.serviceRatingReviewList.layoutManager = LinearLayoutManager(this)
        val serviceProviderReviewAdapter = ProviderReviewListAdapter(this,list as ArrayList)
        mContentBinding.serviceRatingReviewList.adapter = serviceProviderReviewAdapter
    }

    private fun setUpDetails(list:List<String>){
        mContentBinding.serviceInfoRecyclerView.layoutManager = LinearLayoutManager(this)
        val serviceInfoAdapter = ServiceInfoAdapter(this,list)
        mContentBinding.serviceInfoRecyclerView.adapter = serviceInfoAdapter
    }

    private fun setUpBusinessGallery(list:ArrayList<String>){
        mContentBinding.serviceImageRecyclerView.layoutManager = GridLayoutManager(this,2)
        val serviceProviderGalleryAdapter = ServiceProviderGalleryAdapter(this,list)
        mContentBinding.serviceImageRecyclerView.adapter = serviceProviderGalleryAdapter
    }


    private fun showRatingDialog(){
        val builder = AlertDialog.Builder(this).create()
        val dialogBinding:ReviewRatingDialogLayoutBinding =  DataBindingUtil.inflate(
            layoutInflater,
            R.layout.review_rating_dialog_layout,
            mContentBinding.mainLayout,
            false
        )

        if(AppPreference.getPreferenceValueByKey(this,AppPreference.KEY_CUSTOMER_NAME)!=""){
            dialogBinding.customerName = AppPreference.getPreferenceValueByKey(this,AppPreference.KEY_CUSTOMER_NAME)
        }else{
            dialogBinding.customerName = ""
        }

        dialogBinding.submitReviewBtn.setOnClickListener {
            if(dialogBinding.givenRating.rating==0.0f){
                Utils.showToast(this@ServiceProviderDetailActivity,"Please select the rating out of 5")
            }else if(dialogBinding.userName.text.toString()==""){
                Utils.showToast(this@ServiceProviderDetailActivity,"Please enter name")
            }else if(dialogBinding.reviewComment.text.toString()==""){
                Utils.showToast(this@ServiceProviderDetailActivity,"Please enter the review")
            }else{
                makeRequestSubmitRating(dialogBinding.givenRating.rating,dialogBinding.reviewComment.text.toString(),dialogBinding.userName.text.toString())
                builder.dismiss()
            }
        }

        dialogBinding.closeDialogButton.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(dialogBinding.root)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun makeRequestSubmitRating(rating:Float,ratingComment:String,name:String){
        commonViewModel.makeRequestSubmitReview(providerId.toString(),rating.toInt().toString(),
            name,ratingComment)
        lifecycleScope.launch {
            commonViewModel.submitReviewState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mContentBinding.loading = true
                    }
                    Status.SUCCESS -> {
                        mContentBinding.loading = false
                        it.data?.let { comment ->
                            Utils.showToast(this@ServiceProviderDetailActivity,comment.message)
                        }
                    }
                    else -> {
                        mContentBinding.loading = false
                        //  Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}