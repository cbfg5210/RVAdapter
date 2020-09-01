package cbfg.rvadapter.entity

import androidx.annotation.DrawableRes

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/9/20 14:44
 * 功能描述：商店数据类
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/9/20 14:44
 * 修改内容：
 */
data class ShopItem(@DrawableRes val image: Int, val name: String)