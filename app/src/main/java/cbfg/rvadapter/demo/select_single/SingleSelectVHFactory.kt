package cbfg.rvadapter.demo.select_single

import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.ItemSelectSingleBinding
import cbfg.rvadapter.entity.RankItem

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
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> = RankItemVH(
        inflate(R.layout.item_select_single, parent)
    )

    private class RankItemVH(itemView: View) : RVHolder<RankItem>(itemView) {
        private val binding = ItemSelectSingleBinding.bind(itemView)

        override fun setContent(item: RankItem, isSelected: Boolean, payload: Any?) {
            //Log.e("*****", "payload = $payload")
            binding.rbSelect.isChecked = isSelected
            if (payload == null) {
                binding.ivIcon.setImageResource(R.mipmap.ic_launcher)
                binding.tvRank.text = item.rank.toString()
            }
        }
    }
}