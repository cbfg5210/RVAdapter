package cbfg.rvadapter.entity

import androidx.annotation.DrawableRes

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/9/20 14:40
 * 功能描述：商品数据类
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/9/20 14:40
 * 修改内容：
 */
data class CommodityItem(@DrawableRes val image: Int,
                         val name: String,
                         val price: Float)