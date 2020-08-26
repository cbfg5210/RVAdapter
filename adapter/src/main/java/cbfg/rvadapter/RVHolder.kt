package cbfg.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 13:22
 * 功能描述:
 */
abstract class RVHolder<T : Any>(
    inflater: LayoutInflater,
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
) : RecyclerView.ViewHolder(inflater.inflate(layoutRes, parent, false)) {

    /**
     * 设置内容
     */
    abstract fun setContent(item: T, isSelected: Boolean, payload: Any? = null)

    /**
     * 可以在这个方法里面给需要的 view 设置外部传入的点击/长按事件，
     * 默认会给 item view 设置，如果不需要的话可以覆写这个方法
     */
    open fun setListeners(
        itemClickListener: View.OnClickListener?,
        itemLongClickListener: View.OnLongClickListener?
    ) {
        itemView.setOnClickListener(itemClickListener)
        itemView.setOnLongClickListener(itemLongClickListener)
    }
}