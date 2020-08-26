package cbfg.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 13:36
 * 功能描述:
 */
abstract class RVHolderFactory {
    /**
     * 供 [RecyclerView.Adapter.getItemViewType] 调用，
     * 多 view type 的情况下需要覆写这个方法，
     * 可以使用 item layout 作为 type 值
     */
    open fun getItemViewType(item: Any) = -1

    /**
     * 供 [RecyclerView.Adapter.onCreateViewHolder] 调用
     */
    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<Any>
}