package cbfg.rvadapter.entity

import androidx.annotation.DrawableRes

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 17:21
 * 功能描述:
 */
data class Person(
    @DrawableRes val avatar: Int,
    var name: String
)