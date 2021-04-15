package com.webkul.mobikulmp.helpers

import android.content.Context
import android.graphics.Color
import android.view.View
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikulmp.R
import jp.wasabeef.richeditor.RichEditor

class RichTextEditorHelper {


    internal var onChangeTextEditor: OnChangeTextEditor? = null


    fun setOnChangeTextEditor(onChangeTextEditor: OnChangeTextEditor) {
        this.onChangeTextEditor = onChangeTextEditor
    }

    interface OnChangeTextEditor {
        fun onChangeTextEditor(query: String)
    }


    var query = ""

    fun setUpRichTextView(context: Context, editor: RichEditor, data: String) {
        if (!editor.tag.toString().isNullOrEmpty()) {
            query = data
            editor.setEditorHeight(220)
            editor.setEditorFontSize(14)
            editor.setEditorFontColor(Color.DKGRAY)
            editor.setPadding(10, 10, 10, 10)
            editor.setPlaceholder(context.getString(R.string.insert_text_here))
            if (query.isNotEmpty())
                editor.html = query

            editor.setOnTextChangeListener { text ->
                query = text
                onChangeTextEditor?.onChangeTextEditor(query)
            }

            (context as BaseActivity).findViewById<View>(R.id.action_undo)?.setOnClickListener { editor.undo() }

            context.findViewById<View>(R.id.action_redo)?.setOnClickListener { editor.redo() }

            context.findViewById<View>(R.id.action_bold)?.setOnClickListener { editor.setBold() }

            context.findViewById<View>(R.id.action_italic)?.setOnClickListener { editor.setItalic() }
            context.findViewById<View>(R.id.action_subscript)?.setOnClickListener { editor.setSubscript() }
            context.findViewById<View>(R.id.action_superscript)?.setOnClickListener { editor.setSuperscript() }

            context.findViewById<View>(R.id.action_strikethrough)?.setOnClickListener { editor.setStrikeThrough() }

            context.findViewById<View>(R.id.action_underline)?.setOnClickListener { editor.setUnderline() }

            context.findViewById<View>(R.id.action_heading1)?.setOnClickListener { editor.setHeading(1) }

            context.findViewById<View>(R.id.action_heading2)?.setOnClickListener { editor.setHeading(2) }

            context.findViewById<View>(R.id.action_heading3)?.setOnClickListener { editor.setHeading(3) }

            context.findViewById<View>(R.id.action_heading4)?.setOnClickListener { editor.setHeading(4) }

            context.findViewById<View>(R.id.action_heading5)?.setOnClickListener { editor.setHeading(5) }

            context.findViewById<View>(R.id.action_heading6)?.setOnClickListener { editor.setHeading(6) }

            context.findViewById<View>(R.id.action_txt_color)?.setOnClickListener(object : View.OnClickListener {
                private var isChanged: Boolean = false

                override fun onClick(v: View) {
                    editor.setTextColor(if (isChanged) Color.BLACK else Color.RED)
                    isChanged = !isChanged
                }
            })

            context.findViewById<View>(R.id.action_bg_color)?.setOnClickListener(object : View.OnClickListener {
                private var isChanged: Boolean = false

                override fun onClick(v: View) {
                    editor.setTextBackgroundColor(if (isChanged) Color.TRANSPARENT else Color.YELLOW)
                    isChanged = !isChanged
                }
            })

            context.findViewById<View>(R.id.action_indent)?.setOnClickListener { editor.setIndent() }

            context.findViewById<View>(R.id.action_outdent)?.setOnClickListener { editor.setOutdent() }

            context.findViewById<View>(R.id.action_align_left)?.setOnClickListener { editor.setAlignLeft() }

            context.findViewById<View>(R.id.action_align_center)?.setOnClickListener { editor.setAlignCenter() }

            context.findViewById<View>(R.id.action_align_right)?.setOnClickListener { editor.setAlignRight() }

            context.findViewById<View>(R.id.action_blockquote)?.setOnClickListener { editor.setBlockquote() }

            context.findViewById<View>(R.id.action_insert_bullets)?.setOnClickListener { editor.setBullets() }

            context.findViewById<View>(R.id.action_insert_numbers)?.setOnClickListener { editor.setNumbers() }


            context.findViewById<View>(R.id.action_insert_checkbox)?.setOnClickListener { editor.insertTodo() }
        }
    }

}