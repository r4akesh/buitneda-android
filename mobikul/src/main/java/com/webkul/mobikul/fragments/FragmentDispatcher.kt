package com.webkul.mobikul.fragments

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.webkul.mobikul.R

class FragmentDispatcher(fragmentActivity: FragmentActivity) : LifecycleObserver {
    private val hostActivity: FragmentActivity? = fragmentActivity
    private val lifeCycle: Lifecycle? = fragmentActivity.lifecycle
    private val profilePendingList = mutableListOf<BottomSheetDialogFragment>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        if (profilePendingList.isNotEmpty()) {
            showFragment(profilePendingList.last())
        }
    }

    fun dispatcherFragment(frag: BottomSheetDialogFragment) {
        if (lifeCycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) == true) {
            showFragment(frag)
        } else {
            profilePendingList.clear()
            profilePendingList.add(frag)
        }
    }

     fun showFragment(frag: BottomSheetDialogFragment) {
        hostActivity?.let {
            it.supportFragmentManager.beginTransaction().add(R.id.main_frame,frag).commit()
           /* Transaction.begin(it, R.id.main_frame)
                .show(frag)
                .commit()*/
        }
    }
}