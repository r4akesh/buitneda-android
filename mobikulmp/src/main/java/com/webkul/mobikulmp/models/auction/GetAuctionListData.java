package com.webkul.mobikulmp.models.auction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.models.BaseModel;

import java.util.ArrayList;

public class GetAuctionListData extends BaseModel {

    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    @SerializedName("productList")
    @Expose
    private ArrayList<AuctionList> productList = null;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<AuctionList> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<AuctionList> productList) {
        this.productList = productList;
    }

}