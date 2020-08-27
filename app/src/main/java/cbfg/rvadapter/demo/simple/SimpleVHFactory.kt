package cbfg.rvadapter.demo.simple

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.R
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.entity.Person
import kotlinx.android.synthetic.main.item_person.view.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 16:18
 * 功能描述:
 */
@Suppress("UNCHECKED_CAST")
class SimpleVHFactory : RVHolderFactory() {
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<Any> {
        return PersonVH(inflater, parent) as RVHolder<Any>
    }

    private class PersonVH(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) : RVHolder<Person>(inflater, parent, R.layout.item_person) {
        private val ivAvatar = itemView.ivAvatar
        private val tvName = itemView.tvName

        @SuppressLint("SetTextI18n")
        override fun setContent(item: Person, isSelected: Boolean, payload: Any?) {
            ivAvatar.setImageResource(item.avatar)
            tvName.text = "${item.name} - $adapterPosition"
        }

        override fun setListeners(
            itemClickListener: View.OnClickListener?,
            itemLongClickListener: View.OnLongClickListener?
        ) {
            /**
             * 这里除了 item 设置点击/长按事件外，也给头像设置点击事件
             */
            super.setListeners(itemClickListener, itemLongClickListener)
            ivAvatar.setOnClickListener(itemClickListener)
        }
    }
}