package cbfg.rvadapter.demo.select_multi

import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.RankItem
import kotlinx.android.synthetic.main.item_select_multi.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class MultiSelectVHFactory : RVHolderFactory() {

    override fun createViewHolder(
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> = RankItemVH(inflate(R.layout.item_select_multi, parent))

    private class RankItemVH(itemView: View) : RVHolder<RankItem>(itemView) {
        private val cbSelect = itemView.cbSelect
        private val ivIcon = itemView.ivIcon
        private val tvRank = itemView.tvRank

        override fun setContent(item: RankItem, isSelected: Boolean, payload: Any?) {
            cbSelect.isChecked = isSelected
            if (payload == null) {
                ivIcon.setImageResource(R.mipmap.ic_launcher)
                tvRank.text = item.rank.toString()
            }
        }
    }
}