package cbfg.rvadapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/27 10:21
 * 功能描述: [RecyclerView.Adapter]内部的一些事件处理
 */
abstract class RVLifecycleHandler {
    /**
     * 对应：[RecyclerView.Adapter.onViewRecycled]
     */
    open fun onViewRecycled(holder: ViewHolder) {}

    /**
     * 对应：[RecyclerView.Adapter.onFailedToRecycleView]
     */
    open fun onFailedToRecycleView(holder: ViewHolder) = false

    /**
     * 对应：[RecyclerView.Adapter.onViewAttachedToWindow]
     */
    open fun onViewAttachedToWindow(holder: ViewHolder) {}

    /**
     * 对应：[RecyclerView.Adapter.onViewDetachedFromWindow]
     */
    open fun onViewDetachedFromWindow(holder: ViewHolder) {}
}