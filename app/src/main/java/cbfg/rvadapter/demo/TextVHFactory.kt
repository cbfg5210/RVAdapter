package cbfg.rvadapter.demo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import cbfg.rvadapter.R
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import kotlinx.android.synthetic.main.item_text.view.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 16:18
 * 功能描述:
 */
@Suppress("UNCHECKED_CAST")
class TextVHFactory : RVHolderFactory() {
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<Any> {
        return object : RVHolder<String>(inflater, parent, R.layout.item_text) {
            private val tvText = itemView.tvText

            @SuppressLint("SetTextI18n")
            override fun setContent(item: String, isSelected: Boolean, payload: Any?) {
                tvText.text = "$item - $adapterPosition"
            }
        } as RVHolder<Any>
    }
}