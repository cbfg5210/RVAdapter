package cbfg.rvadapter.demo.select_complex

import android.view.LayoutInflater
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.CommodityItem
import cbfg.rvadapter.entity.ShopItem
import kotlinx.android.synthetic.main.item_complex_commodity.view.*
import kotlinx.android.synthetic.main.item_complex_shop.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class ComplexVHFactory : RVHolderFactory() {

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<out Any> {
        return if (item is ShopItem) ShopItemVH(inflater, parent)
        else CommodityItemVH(inflater, parent)
    }

    /**
     * 商店 ViewHolder
     */
    private class ShopItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
        RVHolder<ShopItem>(inflater, parent, R.layout.item_complex_shop) {

        private val cbToggleSelectAll = itemView.cbToggleSelectAll
        private val ivShopImage = itemView.ivShopImage
        private val tvShopName = itemView.tvShopName

        override fun setContent(item: ShopItem, isSelected: Boolean, payload: Any?) {
            cbToggleSelectAll.isChecked = isSelected
            if (payload == null) {
                ivShopImage.setImageResource(item.image)
                tvShopName.text = item.name
            }
        }
    }

    /**
     * 商品 ViewHolder
     */
    private class CommodityItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
        RVHolder<CommodityItem>(inflater, parent, R.layout.item_complex_commodity) {

        private val cbSelect = itemView.cbSelect
        private val ivImage = itemView.ivImage
        private val tvName = itemView.tvName
        private val tvPrice = itemView.tvPrice

        override fun setContent(item: CommodityItem, isSelected: Boolean, payload: Any?) {
            itemView.isSelected = isSelected
            cbSelect.isChecked = isSelected
            if (payload == null) {
                ivImage.setImageResource(item.image)
                tvName.text = item.name
                tvPrice.text = "¥ ${String.format("%.2f", item.price)}"
            }
        }
    }
}