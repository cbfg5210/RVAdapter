package cbfg.rvadapter.demo.multi_view_type

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
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
        viewType: Int,
        item: Any
    ): RVHolder<out Any> {
        return if (item is Header) HeaderVH(inflater.inflate(R.layout.item_header, parent, false))
        else PersonVH(inflater.inflate(R.layout.item_person, parent, false))
    }

    private class HeaderVH(itemView: View) : RVHolder<Header>(itemView) {
        private val tvHeader = itemView.tvHeader
        override fun setContent(item: Header, isSelected: Boolean, payload: Any?) {
            tvHeader.text = item.txt
        }
    }

    private class PersonVH(itemView: View) : RVHolder<Person>(itemView) {
        private val ivAvatar = itemView.ivAvatar
        private val tvName = itemView.tvName

        @SuppressLint("SetTextI18n")
        override fun setContent(item: Person, isSelected: Boolean, payload: Any?) {
            ivAvatar.setImageResource(item.avatar)
            tvName.text = "${item.name} - $adapterPosition"
        }
    }
}