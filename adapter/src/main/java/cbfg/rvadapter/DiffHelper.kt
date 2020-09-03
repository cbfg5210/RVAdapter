package cbfg.rvadapter

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.DiffUtil

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/9/2 14:04
 * 功能描述: DiffUtil 刷新数据方法封装
 */
class DiffHelper<T : Any>(
    private val adapter: RVAdapter<T>,
    private val bDiffCallback: BDiffCallback<T>
) {
    private lateinit var newItems: List<T>

    private val diffCallback: DiffUtil.Callback by lazy {
        object : DiffUtil.Callback() {
            override fun getOldListSize() = adapter.getRealItemCount()
            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return bDiffCallback.areItemsTheSame(
                    adapter.getItems()[oldItemPosition],
                    newItems[newItemPosition]
                )
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return bDiffCallback.areContentsTheSame(
                    adapter.getItems()[oldItemPosition],
                    newItems[newItemPosition]
                )
            }

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                return bDiffCallback.getChangePayload(
                    adapter.getItems()[oldItemPosition],
                    newItems[newItemPosition]
                )
            }
        }
    }

    /**
     * @see [DiffUtil.calculateDiff]
     */
    @WorkerThread
    fun calculateDiff(newItems: List<T>): DiffUtil.DiffResult {
        this.newItems = newItems
        return DiffUtil.calculateDiff(diffCallback)
    }

    /**
     * @see [DiffUtil.DiffResult.dispatchUpdatesTo]
     * @param clearSelections true：清空选中项
     */
    @UiThread
    fun dispatchUpdates(diffResult: DiffUtil.DiffResult, clearSelections: Boolean = true) {
        adapter.setItems(newItems, clearSelections, needNotify = false)
        diffResult.dispatchUpdatesTo(adapter)
    }

    /**
     * @see [DiffUtil.Callback]
     */
    interface BDiffCallback<T : Any> {
        /**
         * @see [DiffUtil.Callback.areItemsTheSame]
         *
         * 如果数据类型重写了 equals 方法的话不用再覆写
         */
        fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        /**
         * @see [DiffUtil.Callback.areContentsTheSame]
         */
        fun areContentsTheSame(oldItem: T, newItem: T): Boolean

        /**
         * @see [DiffUtil.Callback.getChangePayload]
         */
        fun getChangePayload(oldItem: T, newItem: T): Any?
    }
}