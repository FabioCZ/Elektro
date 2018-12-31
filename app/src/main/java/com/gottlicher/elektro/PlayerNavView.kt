package com.gottlicher.elektro


import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.image

class PlayerNavView : LinearLayout {

    private val plIcon:ImageView
    private val plMoney:TextView


    var color:Color = Color.BLACK
        set(value){ plIcon.setColorFilter(ContextCompat.getColor(context, value.color), android.graphics.PorterDuff.Mode.SRC_IN) }

    var plSelected:Boolean = false
        set(value){ plIcon.image = context.getDrawable(if(value) R.drawable.ic_person_black_24dp else R.drawable.ic_person_outline_black_24dp)}

    var money:Int = 0
        set(value){ plMoney.text = if (value >= 0)value.toString() else "None" }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.player_nav_view, this, true)
        plIcon = v.findViewById(R.id.pl_icon)
        plMoney = v.findViewById(R.id.pl_money)

    }

}
