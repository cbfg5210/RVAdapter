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

    /**
     * 如果需要处理 adapter 中的一些事件则传入此对象实例
     */
    private var lifecycleHandler: RVLifecycleHandler? = null

    private val items = ArrayList<T>()

    /**
     * 临时变量，用于记录当前 [getItemViewType] 的位置
     */
    private var tempPosition = 0

    /**
     * 保存 item 类型及对应的属性信息
     */
    private var itemInfoMap = HashMap<Class<*>, ItemInfo>()

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

    fun setLifecycleHandler(lifecycleHandler: RVLifecycleHandler): RVAdapter<T> {
        this.lifecycleHandler = lifecycleHandler
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

    fun getItems(): List<T> {
        return items
    }

    fun add(item: T): Boolean {
        return items.add(item)
    }

    fun add(index: Int, item: T) {
        items.add(index, item)
    }

    fun add(list: List<T>): Boolean {
        return items.addAll(list)
    }

    fun add(index: Int, list: List<T>): Boolean {
        return items.addAll(index, list)
    }

    fun remove(item: T): Boolean {
        return items.remove(item)
    }

    fun removeAt(index: Int): T {
        return items.removeAt(index)
    }

    fun remove(list: List<T>) {
        list.forEach { items.remove(it) }
    }

    fun removeRange(fromIndex: Int, toIndex: Int) {
        items.subList(fromIndex, toIndex).clear()
    }

    /**
     * 获取 item 信息，没有则创建保存
     */
    private fun getItemInfo(clazz: Class<*>): ItemInfo {
        return itemInfoMap[clazz] ?: ItemInfo(itemInfoMap.size).also { itemInfoMap[clazz] = it }
    }

    override fun getItemViewType(position: Int): Int {
        tempPosition = position
        val item = items[position]
        val viewType = rvHolderFactory.getItemViewType(item)
        return if (viewType != -1) viewType else getItemInfo(item.javaClass).viewType
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item = items[tempPosition]
        val holder = rvHolderFactory.createViewHolder(inflater, parent, item)

        /**
         * 设置点击/长按监听器，
         * 如果外部没有传入点击/长按事件则设为 null
         */
        holder.setListeners(
            itemClickListener?.run { View.OnClickListener { onItemClick(holder, it, this) } },
            itemLongClickListener?.run {
                View.OnLongClickListener {
                    onItemClick(holder, it, this)
                    true
                }
            })

        return holder
    }

    private fun onItemClick(
        holder: RVHolder<out Any>,
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
        bindRVHolder(holder, position, null)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        bindRVHolder(holder, position, payloads)
    }

    private fun bindRVHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        holder as RVHolder<T>
        val item = items[position]
        if (payloads.isNullOrEmpty()) {
            holder.setContent(item, false)
        } else {
            holder.setContent(item, false, payloads[0])
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        lifecycleHandler?.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        lifecycleHandler?.run { return this.onFailedToRecycleView(holder) }
        return super.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        lifecycleHandler?.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        lifecycleHandler?.onViewDetachedFromWindow(holder)
    }

    /**
     * 存储 item 属性
     */
    private data class ItemInfo(
        val viewType: Int = 0,
        var selectable: Boolean = false,
        var multiSelectable: Boolean = false
    )
}