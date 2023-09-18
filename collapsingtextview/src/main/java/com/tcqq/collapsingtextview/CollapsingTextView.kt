package com.tcqq.collapsingtextview

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView

class CollapsingTextView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {

    private var isCharEnable: Boolean = false
    private val ellipsis = "..."
    private var actionTextColor: Int
    private var mainText: String = ""
    private var isAlreadySet: Boolean = false

    var showingLine: Int = 1
        set(value) {
            if (value != 0) {
                isCharEnable = false
                maxLines = value
                field = value
            } else {
                throw Exception("Line Number cannot be <= 0")
            }
        }
    var showingChar: Int = 0
        set(value) {
            if (value != 0) {
                isCharEnable = true
                field = value
                addShowMore()
            } else {
                throw Exception("Character length cannot be 0")
            }
        }
    var moreText: String
    var lessText: String

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CollapsingTextView, 0, 0
        )

        val moreText = a.getString(R.styleable.CollapsingTextView_collapsing_more_text) ?: DEFAULT_MORE_TEXT
        val lessText = a.getString(R.styleable.CollapsingTextView_collapsing_less_text) ?: DEFAULT_LESS_TEXT
        val actionTextColor = a.getColor(R.styleable.CollapsingTextView_collapsing_text_color, primaryColor(context))
        val showingLine = a.getInteger(R.styleable.CollapsingTextView_collapsing_showing_line, DEFAULT_SHOWING_LINE)
        a.recycle()

        this.moreText = moreText
        this.lessText = lessText
        this.actionTextColor = actionTextColor
        this.showingLine = showingLine

        addShowMore()
    }

    @ColorInt
    private fun primaryColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(android.R.attr.colorPrimary, value, true)
        return value.data
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mainText = text.toString()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        handleText()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handleText()
    }

    private fun handleText() {
        if (!isAlreadySet) {
            mainText = text.toString()
            addShowMore()
        }
    }

    private fun setShowMoreColoringAndClickable() {
        val spannableString = SpannableString(text)

        Log.d(TAG, "Text: $text")
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }

                override fun onClick(view: View) {
                    Log.d(TAG, "展开")
                    maxLines = Integer.MAX_VALUE
                    text = mainText
                    showLess()
                }
            },
            text.length - (moreText.length),
            text.length, 0
        )

        spannableString.setSpan(
            ForegroundColorSpan(actionTextColor),
            text.length - (moreText.length),
            text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, BufferType.SPANNABLE)
    }

    private fun addShowMore() {
        val vto = viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val text = text.toString()
                if (!isAlreadySet) {
                    mainText = getText().toString()
                    isAlreadySet = true
                }
                val showingText = StringBuilder()
                if (isCharEnable) {
                    if (showingChar >= text.length) {
                        try {
                            throw Exception("Character count cannot be exceed total line count")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    var newText = text.substring(0, showingChar)
                    newText += ellipsis + moreText
                    setText(newText)
                    Log.d(TAG, "Text: $newText")
                } else {
                    if (showingLine >= lineCount) {
                        try {
                            throw Exception("Line Number cannot be exceed total line count")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e(TAG, "Error: " + e.message)
                        }

                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        return
                    }
                    var start = 0
                    var end: Int
                    for (i in 0 until showingLine) {
                        end = layout.getLineEnd(i)
                        showingText.append(text.substring(start, end))
                        start = end
                    }

                    var newText = showingText.substring(
                        0,
                        showingText.length - (ellipsis.length + moreText.length + MAGIC_NUMBER)
                    )
                    Log.d(TAG, "Text: $newText")
                    Log.d(TAG, "Text: $showingText")
                    newText += ellipsis + moreText

                    setText(newText)
                }

                setShowMoreColoringAndClickable()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun showLess() {
        val text = text.toString() + lessText
        val spannableString = SpannableString(text)

        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }

                override fun onClick(view: View) {
                    Log.d(TAG, "折叠")
                    maxLines = showingLine
                    addShowMore()
                }
            },
            text.length - (lessText.length),
            text.length, 0
        )

        spannableString.setSpan(
            ForegroundColorSpan(actionTextColor),
            text.length - (lessText.length),
            text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, BufferType.SPANNABLE)
    }

    companion object {
        private const val TAG = "ShowMoreTextView"

        private const val MAGIC_NUMBER = 5

        private const val DEFAULT_MORE_TEXT = "See more"
        private const val DEFAULT_LESS_TEXT = "See less"
        private const val DEFAULT_SHOWING_LINE = 1
    }
}