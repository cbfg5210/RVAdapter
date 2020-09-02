package cbfg.rvadapter.demo.diff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.RankItem
import kotlinx.android.synthetic.main.item_diff.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class DiffVHFactory : RVHolderFactory() {

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<out Any> {
        return RankItemVH(inflater.inflate(R.layout.item_diff, parent, false))
    }

    private class RankItemVH(itemView: View) : RVHolder<RankItem>(itemView) {
        private val tvRank = itemView.tvRank
        private val ivIcon = itemView.ivIcon
        override fun setContent(item: RankItem, isSelected: Boolean, payload: Any?) {
            tvRank.text = item.rank.toString()
            if (payload == null) {
                ivIcon.setImageResource(R.mipmap.ic_launcher)
            }
        }
    }
}