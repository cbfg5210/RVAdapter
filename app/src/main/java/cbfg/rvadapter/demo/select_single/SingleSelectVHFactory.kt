package cbfg.rvadapter.demo.select_single

import android.view.LayoutInflater
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.RankItem
import kotlinx.android.synthetic.main.item_select_single.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class SingleSelectVHFactory : RVHolderFactory() {

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<out Any> {
        return RankItemVH(inflater, parent)
    }

    private inner class RankItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
        RVHolder<RankItem>(inflater, parent, R.layout.item_select_single) {

        override fun setContent(item: RankItem, isSelected: Boolean, payload: Any?) {
            //Log.e("*****", "payload = $payload")
            itemView.rbSelect.isChecked = isSelected
            if (payload == null) {
                itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
                itemView.tvRank.text = item.rank.toString()
            }
        }
    }
}