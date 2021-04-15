package com.webkul.mobikulmp.helpers

import android.graphics.Paint
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.bumptech.glide.Glide
import jp.wasabeef.richeditor.RichEditor


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.helpers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 7/9/19
 */
class MpBindingAdapterUtils {
    companion object {
        @JvmStatic
        @BindingAdapter("underline")
        fun setUnderline(textView: AppCompatTextView, isUnderline: Boolean) {
            if (isUnderline) {
                textView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
        }

        @JvmStatic
        @BindingAdapter("blurBackgroundImageUrl")
        fun setBlurBackgroundImageUrl(view: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrBlank()) {
                Glide.with(view.context).load(imageUrl).transform(BlurTransformation(view.context)).into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("loadHtmlEditText")
        fun setLoadHtmlEditText(richEditor: RichEditor, htmlText: String?) {

//            val editor: RichEditor = (richEditor.context as BaseActivity).findViewById<View>(R.id.editor) as RichEditor
//            editor.setEditorHeight(220)
//            editor.setEditorFontSize(14)
//            editor.setEditorFontColor(Color.DKGRAY)
//            editor.setPadding(10, 10, 10, 10)
//            editor.setPlaceholder("Insert text here...")
//            editor.html = htmlText ?: ""
//
////            editor.setOnTextChangeListener { text -> changedHtmlText = text }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_undo)?.setOnClickListener { editor.undo() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_redo)?.setOnClickListener { editor.redo() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_bold)?.setOnClickListener { editor.setBold() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_italic)?.setOnClickListener { editor.setItalic() }
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_subscript)?.setOnClickListener { editor.setSubscript() }
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_superscript)?.setOnClickListener { editor.setSuperscript() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_strikethrough)?.setOnClickListener { editor.setStrikeThrough() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_underline)?.setOnClickListener { editor.setUnderline() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_heading1)?.setOnClickListener { editor.setHeading(1) }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_heading2)?.setOnClickListener { editor.setHeading(2) }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_heading3)?.setOnClickListener { editor.setHeading(3) }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_heading4)?.setOnClickListener { editor.setHeading(4) }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_heading5)?.setOnClickListener { editor.setHeading(5) }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_heading6)?.setOnClickListener { editor.setHeading(6) }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_txt_color)?.setOnClickListener(object : View.OnClickListener {
//                private var isChanged: Boolean = false
//
//                override fun onClick(v: View) {
//                    editor.setTextColor(if (isChanged) Color.BLACK else Color.RED)
//                    isChanged = !isChanged
//                }
//            })
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_bg_color)?.setOnClickListener(object : View.OnClickListener {
//                private var isChanged: Boolean = false
//
//                override fun onClick(v: View) {
//                    editor.setTextBackgroundColor(if (isChanged) Color.TRANSPARENT else Color.YELLOW)
//                    isChanged = !isChanged
//                }
//            })
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_indent)?.setOnClickListener { editor.setIndent() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_outdent)?.setOnClickListener { editor.setOutdent() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_align_left)?.setOnClickListener { editor.setAlignLeft() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_align_center)?.setOnClickListener { editor.setAlignCenter() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_align_right)?.setOnClickListener { editor.setAlignRight() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_blockquote)?.setOnClickListener { editor.setBlockquote() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_insert_bullets)?.setOnClickListener { editor.setBullets() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_insert_numbers)?.setOnClickListener { editor.setNumbers() }
//
//            (richEditor.context as BaseActivity).findViewById<View>(R.id.action_insert_checkbox)?.setOnClickListener { editor.insertTodo() }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "loadHtmlEditText")
        fun getLoadHtmlEditText(richEditor: RichEditor): String {
            return richEditor.html
        }
    }
}

