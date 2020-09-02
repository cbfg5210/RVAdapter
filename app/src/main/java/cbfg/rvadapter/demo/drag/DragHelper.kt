package cbfg.rvadapter.demo.drag

import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cbfg.rvadapter.RVAdapter
import java.util.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/9/2 15:15
 * 功能描述: 拖拽方法封装
 */
class DragHelper<T : Any>(private val adapter: RVAdapter<T>) {
    private val itemTouchCallback = object : ItemTouchHelper.Callback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder ?: return
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY)
            }
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = if (recyclerView.layoutManager is GridLayoutManager) {
                ItemTouchHelper.UP or
                        ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or
                        ItemTouchHelper.RIGHT
            } else {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            Collections.swap(adapter.getItems(), fromPosition, toPosition)
            adapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.setBackgroundColor(0)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return draggable
        }
    }

    private val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
    private var draggable = true

    fun bindRecyclerView(rv: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(rv)
    }

    fun setDraggable(draggable: Boolean) {
        this.draggable = draggable
    }
}