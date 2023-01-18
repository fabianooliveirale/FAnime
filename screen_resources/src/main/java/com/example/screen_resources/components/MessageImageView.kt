package com.example.screen_resources.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.screen_resources.R
import com.example.screen_resources.databinding.MessageImageViewBinding

class MessageImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var binding =
        MessageImageViewBinding.inflate(LayoutInflater.from(context), this, true)

    var image: Drawable? = null
        set(value) {
            field = value
            binding.imageView.setImageDrawable(value)
        }

    var text: CharSequence? = null
        set(value) {
            field = value
            binding.textView.text = value
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MessageImageView)
        text = typedArray.getString(R.styleable.NavigationItem_android_text)
        image = typedArray.getDrawable(R.styleable.NavigationItem_android_src)
        typedArray.recycle()
    }

}