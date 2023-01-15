package com.example.screen_resources.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.screen_resources.R
import com.example.screen_resources.databinding.BottomNavigationViewBinding

class BottomNavigationView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var binding =
        BottomNavigationViewBinding.inflate(LayoutInflater.from(context), this, true)

    companion object {
        const val HOME_VIEW_INDEX = 0
        const val NEW_VIEW_INDEX = 1
        const val CATEGORY_VIEW_INDEX = 2
        const val SEARCH_VIEW_INDEX = 3
    }

    var bottomViewPaddingHorizontal: Float = 0f
        set(value) {
            field = value
            binding.bottomView.setPadding(value.toInt(), paddingTop, value.toInt(), paddingBottom)
        }

    var bottomBackgroundColor: Int = ContextCompat.getColor(context, R.color.black)
        set(value) {
            field = value
            binding.bottomView.setBackgroundColor(value)
        }

    var index: Int = 0
        set(value) {
            field = value
            selectItemCallback(index)
            setSelectedItem()
        }

    private var selectedView: NavigationItem = binding.homeView

    private var selectItemCallback: (Int)-> Unit = {}

    var fragmentContainer = binding.fragmentContainer

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationView)
        bottomViewPaddingHorizontal =
            typedArray.getDimension(
                R.styleable.BottomNavigationView_bottomViewPaddingHorizontal,
                0f
            )
        bottomBackgroundColor = typedArray.getColor(
            R.styleable.BottomNavigationView_bottomBackgroundColor,
            ContextCompat.getColor(context, R.color.black)
        )
        typedArray.recycle()
        setItemClick()
    }

    private fun setItemClick() {
        binding.apply {
            homeView.setOnClickListener {
                index = HOME_VIEW_INDEX
            }
            newView.setOnClickListener {
                index = NEW_VIEW_INDEX
            }
            categoryView.setOnClickListener {
                index = CATEGORY_VIEW_INDEX
            }
            searchView.setOnClickListener {
                index = SEARCH_VIEW_INDEX
            }
        }
    }

    fun setSelectItemCallBack(callBack: (Int) -> Unit) {
        selectItemCallback = callBack
    }

    private fun setSelectedItem() {
        setUnSelectedNavigationItemColor(selectedView)
        binding.apply {
            selectedView = when (index) {
                NEW_VIEW_INDEX -> newView
                CATEGORY_VIEW_INDEX -> categoryView
                SEARCH_VIEW_INDEX -> searchView
                else -> homeView
            }
        }
        setSelectedNavigationItemColor(selectedView)
    }

    private fun setSelectedNavigationItemColor(view: NavigationItem) {
        view.textColor = ContextCompat.getColor(context, R.color.primary_color)
        view.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_color))
        view.textIsGone = false
    }

    private fun setUnSelectedNavigationItemColor(view: NavigationItem) {
        view.textColor = ContextCompat.getColor(context, R.color.white)
        view.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        view.textIsGone = true
    }
}