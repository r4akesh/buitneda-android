package com.webkul.mobikul.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.R;
import com.webkul.mobikul.databinding.ItemBitListBinding;
import com.webkul.mobikul.models.auction.BidList;

import java.util.ArrayList;

/**
 * Created by vedesh.kumar on 15/12/16. @Webkul Software Pvt. Ltd
 */

public class BidListRvAdapter extends RecyclerView.Adapter<BidListRvAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<BidList> mBidListData;

    public BidListRvAdapter(Context context, ArrayList<BidList> bidListData) {
        mContext = context;
        mBidListData = bidListData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_bit_list, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BidList bidListData = mBidListData.get(position);
        holder.mBinding.setData(bidListData);
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mBidListData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBitListBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}