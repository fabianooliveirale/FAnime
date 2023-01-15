package com.example.screen_resources.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getColorOrThrow
import androidx.core.view.isGone
import com.example.screen_resources.R
import com.example.screen_resources.databinding.NavigationItemBinding
import com.example.screen_resources.databinding.ToolbarBinding

class NavigationItem(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var binding = NavigationItemBinding.inflate(LayoutInflater.from(context), this, true)

    var text: CharSequence? = null
        set(value) {
            field = value
            binding.textView.text = value
        }

    var textColor: Int? = null
        set(value) {
            field = value
            value?.let {
                binding.textView.setTextColor(it)
            }
        }

    var image: Drawable? = null
        set(value) {
            field = value
            binding.imageView.setImageDrawable(value)
        }

    var imageTintList: ColorStateList? = null
        set(value) {
            field = value
            binding.imageView.imageTintList = value
        }

    var textIsGone: Boolean = true
        set(value) {
            field = value
            binding.textView.isGone = value
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavigationItem)
        text = typedArray.getString(R.styleable.NavigationItem_android_text)
        image = typedArray.getDrawable(R.styleable.NavigationItem_android_src)
        textColor = typedArray.getColor(
            R.styleable.NavigationItem_android_textColor,
            ContextCompat.getColor(this.context, R.color.white)
        )
        imageTintList = typedArray.getColorStateList(
            R.styleable.NavigationItem_android_tint
        )
        typedArray.recycle()
    }
}