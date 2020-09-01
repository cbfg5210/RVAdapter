package cbfg.rvadapter.demo.multi_view_type

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.entity.Header
import cbfg.rvadapter.entity.Person
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_person.view.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/27 14:59
 * 功能描述:
 */
class MultiViewTypeVHFactory : RVHolderFactory() {
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<out Any> {
        return if (item is Header) HeaderVH(inflater, parent)
        else PersonVH(inflater, parent)
    }

    private class HeaderVH(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) : RVHolder<Header>(inflater, parent, R.layout.item_header) {
        private val tvHeader = itemView.tvHeader
        override fun setContent(item: Header, isSelected: Boolean, payload: Any?) {
            tvHeader.text = item.txt
        }
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
    }
}