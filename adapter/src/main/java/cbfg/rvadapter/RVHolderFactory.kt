package cbfg.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 13:36
 * 功能描述:
 */
abstract class RVHolderFactory {
    internal lateinit var inflater: LayoutInflater

    protected fun inflate(@LayoutRes layoutRes: Int, parent: ViewGroup?): View {
        return inflater.inflate(layoutRes, parent, false)
    }

    /**
     * 供 [RecyclerView.Adapter.getItemViewType] 调用，
     * 默认支持多 view type（默认会以数据类型区分），
     * 如果默认的不满足需求，可以覆写这个方法，
     * 建议使用 item layout 作为 type 值
     */
    open fun getItemViewType(item: Any) = -1

    /**
     * 供 [RecyclerView.Adapter.onCreateViewHolder] 调用
     */
    abstract fun createViewHolder(
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any>
}