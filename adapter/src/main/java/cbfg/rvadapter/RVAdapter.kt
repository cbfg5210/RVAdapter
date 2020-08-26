package cbfg.rvadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 13:09
 * 功能描述:
 */
class RVAdapter<T : Any>(
    private val context: Context,
    private val rvHolderFactory: RVHolderFactory
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var itemClickListener: ((view: View, item: T, position: Int) -> Unit)? = null
    private var itemLongClickListener: ((view: View, item: T, position: Int) -> Unit)? = null

    private val items = ArrayList<T>()

    /**
     * 临时变量，用于记录当前 [getItemViewType] 的位置
     */
    private var tempPosition = 0

    fun bindRecyclerView(rv: RecyclerView): RVAdapter<T> {
        rv.adapter = this
        return this
    }

    fun setItemClickListener(listener: (view: View, item: T, position: Int) -> Unit): RVAdapter<T> {
        this.itemClickListener = listener
        return this
    }

    fun setItemLongClickListener(listener: (view: View, item: T, position: Int) -> Unit): RVAdapter<T> {
        this.itemLongClickListener = listener
        return this
    }

    fun setItems(mItems: List<T>?): RVAdapter<T> {
        this.items.clear()
        if (mItems.isNullOrEmpty()) {
            return this
        }
        this.items.addAll(mItems)
        return this
    }

    override fun getItemViewType(position: Int): Int {
        tempPosition = position
        return rvHolderFactory.getItemViewType(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item = items[tempPosition]
        val holder = rvHolderFactory.onCreateViewHolder(inflater, parent, item)

        var itemClicker: View.OnClickListener? = null
        itemClickListener?.run {
            itemClicker = View.OnClickListener {
                onItemClick(holder, it, this)
            }
        }

        var itemLongClicker: View.OnLongClickListener? = null
        itemLongClickListener?.run {
            itemLongClicker = View.OnLongClickListener {
                onItemClick(holder, it, this)
                true
            }
        }

        holder.setListeners(itemClicker, itemLongClicker)

        return holder
    }

    private fun onItemClick(
        holder: RVHolder<Any>,
        view: View,
        clicker: ((view: View, item: T, position: Int) -> Unit)?
    ) {
        val position = holder.adapterPosition
        if (position < 0 || position >= items.size) {
            return
        }
        clicker?.invoke(view, items[position], position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as RVHolder<T>
        val item = items[position]
        holder.setContent(item, false)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder as RVHolder<T>
        val item = items[position]
        if (payloads.isEmpty()) {
            holder.setContent(item, false)
        } else {
            holder.setContent(item, false, payloads[0])
        }
    }
}