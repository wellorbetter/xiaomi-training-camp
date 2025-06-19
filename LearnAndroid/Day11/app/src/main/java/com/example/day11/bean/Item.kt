package com.example.day11.bean

import android.graphics.drawable.Drawable
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author wellorbetter
 */
class Item(val img: Drawable? = null, var name: String = "", var id: Int = 0, override val itemType: Int): MultiItemEntity {
    var poetName:String = ""
    var poemName:String = ""
    var poemContent:String=""
    var like:Boolean=false
    constructor(itemType: Int):this(null, "", 0, POEM_TEXT)
    constructor(poetName:String, poemName:String, poemContent:String, itemType: Int) : this(itemType) {
        this.poetName = poetName
        this.poemName = poemName
        this.poemContent = poemContent
    }
    companion object {
        const val PERSON_CARD = 0
        const val POEM_TEXT = 1

        // 这是一个扩展函数，用于判断Item实例是否是PERSON_CARD类型
        fun Item.isPersonCard() = this.itemType == PERSON_CARD

        fun Item.isPoemText() = this.itemType == POEM_TEXT

    }

    // 这里可以添加其他Item类的方法或属性
}