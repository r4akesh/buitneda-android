package com.webkul.mobikulmp.handlers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikulmp.R;
import com.webkul.mobikulmp.databinding.ItemIncrementRuleListBinding;
import com.webkul.mobikulmp.models.auction.IncrementalRule;

import java.util.ArrayList;

/**
 * Created by vedesh.kumar on 14/2/17. @Webkul Software Pvt. Ltd
 */
public class IncrementRuleRvAdapter extends RecyclerView.Adapter<IncrementRuleRvAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<IncrementalRule> mIncrementRuleListDatas;
    private boolean mShowEditLayout;

    public IncrementRuleRvAdapter(Context context, ArrayList<IncrementalRule> incrementalRules, boolean showEditLayout) {
        mContext = context;
        mIncrementRuleListDatas = incrementalRules;
        mShowEditLayout = showEditLayout;
    }


    @Override
    public IncrementRuleRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_increment_rule_list, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(IncrementRuleRvAdapter.ViewHolder holder, int position) {
        final IncrementalRule incrementalRule = mIncrementRuleListDatas.get(position);
        holder.mBinding.setPosition(position);
        holder.mBinding.setShowEditLayout(mShowEditLayout);
        holder.mBinding.setData(incrementalRule);
        holder.mBinding.setHandler(new IncrementRuleRvHandler(this));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mIncrementRuleListDatas.size();
    }

    public ArrayList<IncrementalRule> getmIncrementRuleListDatas() {
        return mIncrementRuleListDatas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemIncrementRuleListBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
