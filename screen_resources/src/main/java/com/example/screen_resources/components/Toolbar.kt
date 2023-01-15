package com.example.screen_resources.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.screen_resources.R
import com.example.screen_resources.databinding.ToolbarBinding

class Toolbar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context,attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0 )

    private var binding = ToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    var title: CharSequence? = null
        set(value) {
            field = value
            binding.title.text = value
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Toolbar)
        title = typedArray.getString(R.styleable.Toolbar_android_text)
        typedArray.recycle()
    }

    fun setLeftIconClick(callBack: () -> Unit) {
        binding.buttonLeftArrow.setOnClickListener {
            callBack()
        }
    }
}