package cbfg.rvadapter.demo.drag

import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.ItemDiffBinding
import cbfg.rvadapter.entity.RankItem

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class DragVHFactory : RVHolderFactory() {

    override fun createViewHolder(
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> {
        return RankItemVH(inflate(R.layout.item_diff, parent))
    }

    private class RankItemVH(itemView: View) : RVHolder<RankItem>(itemView) {
        private val binding = ItemDiffBinding.bind(itemView)
        override fun setContent(item: RankItem, isSelected: Boolean, payload: Any?) {
            binding.tvRank.text = item.rank.toString()
            binding.ivIcon.setImageResource(R.mipmap.ic_launcher)
        }
    }
}