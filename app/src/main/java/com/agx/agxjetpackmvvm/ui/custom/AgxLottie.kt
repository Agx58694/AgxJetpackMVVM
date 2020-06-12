package com.agx.agxjetpackmvvm.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.agx.agxjetpackmvvm.R
import com.airbnb.lottie.LottieAnimationView
import me.hgj.jetpackmvvm.ext.util.ifTrue

class AgxLottie(context: Context, attrs: AttributeSet) : LottieAnimationView(context, attrs) {
    private var selected: Boolean? = null
    private var selectedJson: String? = null
    private var unselectedJson: String? = null
    private var onSelectedListener: (Boolean) -> Unit = {}
    init {
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AgxLottie)
        selectedJson = ta.getString(R.styleable.AgxLottie_agx_lottie_selected_json)
        unselectedJson = ta.getString(R.styleable.AgxLottie_agx_lottie_unselected_json)
        selected = ta.getBoolean(R.styleable.AgxLottie_agx_lottie_selected,false)
        ta.recycle()
        setJson()
        initOnClick()
    }

    private fun setJson(){
        selected!!.ifTrue {
            setAnimation(selectedJson)
            playAnimation()
            return
        }
        if(unselectedJson == null){
            //设置为默认，不开始动画
            setAnimation(selectedJson)
            progress = 0F
            return
        }
        setAnimation(unselectedJson)
        playAnimation()

    }

    private fun initOnClick(){
        setOnClickListener {
            selected = !selected!!
            setJson()
            onSelectedListener.invoke(selected!!)
        }
    }

    override fun setSelected(selected: Boolean){
        super.setSelected(selected)
        this.selected = selected
        setJson()
    }

    fun setOnSelectedListener(onSelectedListener: (Boolean) -> Unit){
        this.onSelectedListener = onSelectedListener
    }
}