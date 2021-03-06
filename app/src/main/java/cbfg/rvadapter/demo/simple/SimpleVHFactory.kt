package cbfg.rvadapter.demo.simple

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.ItemPersonBinding
import cbfg.rvadapter.entity.Person

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 16:18
 * 功能描述:
 */
class SimpleVHFactory : RVHolderFactory() {
    override fun createViewHolder(
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> {
        return PersonVH(inflate(R.layout.item_person, parent))
    }

    private class PersonVH(itemView: View) : RVHolder<Person>(itemView) {
        private val binding = ItemPersonBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        override fun setContent(item: Person, isSelected: Boolean, payload: Any?) {
            binding.ivAvatar.setImageResource(item.avatar)
            binding.tvName.text = "${item.name} - $adapterPosition"
        }

        override fun setListeners(
            itemClickListener: View.OnClickListener?,
            itemLongClickListener: View.OnLongClickListener?
        ) {
            /**
             * 这里除了 item 设置点击/长按事件外，也给头像设置点击事件
             */
            super.setListeners(itemClickListener, itemLongClickListener)
            binding.ivAvatar.setOnClickListener(itemClickListener)
        }
    }
}