package com.webkul.mlkit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mlkit.activities.CameraSearchActivity
import com.webkul.mobikul.R

/**
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class CameraSearchResultAdapter(private val context: Context, private val labelList: List<String>) : RecyclerView.Adapter<CameraSearchResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.camera_simple_spinner_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.itemView.findViewById<View>(R.id.label_tv) as TextView).text = labelList[position]
        holder.itemView.findViewById<View>(R.id.label_tv).setOnClickListener { (context as CameraSearchActivity).sendResultBack(position) }

    }

    override fun getItemCount(): Int {
        return labelList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
