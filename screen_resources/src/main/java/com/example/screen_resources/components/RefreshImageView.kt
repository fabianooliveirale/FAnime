package com.example.screen_resources.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.screen_resources.R
import com.example.screen_resources.databinding.RefreshImageViewBinding

class RefreshImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var binding =
        RefreshImageViewBinding.inflate(LayoutInflater.from(context), this, true)

    override fun setOnClickListener(l: OnClickListener?) {
        binding.mainContainer.setOnClickListener(l)
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshImageView)
        typedArray.recycle()
    }
}