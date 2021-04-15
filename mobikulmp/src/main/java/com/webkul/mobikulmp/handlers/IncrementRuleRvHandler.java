package com.webkul.mobikulmp.handlers;


/**
 * Created by vedesh.kumar on 19/4/18. @Webkul Software Private limited
 */

public class IncrementRuleRvHandler {
    private IncrementRuleRvAdapter mIncrementRuleRvAdapter;

    public IncrementRuleRvHandler(IncrementRuleRvAdapter incrementRuleRvAdapter) {

        mIncrementRuleRvAdapter = incrementRuleRvAdapter;
    }

    public void onClickDeleteItem(int position) {
        mIncrementRuleRvAdapter.getmIncrementRuleListDatas().remove(position);
        mIncrementRuleRvAdapter.notifyDataSetChanged();
    }
}
