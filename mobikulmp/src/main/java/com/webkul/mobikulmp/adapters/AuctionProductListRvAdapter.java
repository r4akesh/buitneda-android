package com.webkul.mobikulmp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikulmp.R;
import com.webkul.mobikulmp.databinding.ItemAutionProductListBinding;
import com.webkul.mobikulmp.handlers.AuctionProductListRvHandler;
import com.webkul.mobikulmp.models.auction.ProductList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vedesh.kumar on 25/2/17. @Webkul Software Pvt. Ltd
 */
public class AuctionProductListRvAdapter extends RecyclerView.Adapter<AuctionProductListRvAdapter.ViewHolder> {
    private final Context mContext;
    private final List<ProductList> mAuctionProductList;

    public AuctionProductListRvAdapter(Context context, ArrayList<ProductList> auctionProductList) {
        mContext = context;
        mAuctionProductList = auctionProductList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new ViewHolder(inflater.inflate(R.layout.item_aution_product_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductList productList = mAuctionProductList.get(position);
        holder.mBinding.setData(productList);
        holder.mBinding.setHandler(new AuctionProductListRvHandler(mContext));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mAuctionProductList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemAutionProductListBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}