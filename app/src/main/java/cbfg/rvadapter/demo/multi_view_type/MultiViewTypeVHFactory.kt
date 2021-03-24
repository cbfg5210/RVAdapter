package cbfg.rvadapter.demo.multi_view_type

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.ItemHeaderBinding
import cbfg.rvadapter.demo.databinding.ItemPersonBinding
import cbfg.rvadapter.entity.Header
import cbfg.rvadapter.entity.Person

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/27 14:59
 * 功能描述:
 */
class MultiViewTypeVHFactory : RVHolderFactory() {
    override fun createViewHolder(
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> {
        return if (item is Header) HeaderVH(inflate(R.layout.item_header, parent))
        else PersonVH(inflate(R.layout.item_person, parent))
    }

    private class HeaderVH(itemView: View) : RVHolder<Header>(itemView) {
        private val binding = ItemHeaderBinding.bind(itemView)
        override fun setContent(item: Header, isSelected: Boolean, payload: Any?) {
            binding.tvHeader.text = item.txt
        }
    }

    private class PersonVH(itemView: View) : RVHolder<Person>(itemView) {
        private val binding = ItemPersonBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        override fun setContent(item: Person, isSelected: Boolean, payload: Any?) {
            binding.ivAvatar.setImageResource(item.avatar)
            binding.tvName.text = "${item.name} - $adapterPosition"
        }
    }
}