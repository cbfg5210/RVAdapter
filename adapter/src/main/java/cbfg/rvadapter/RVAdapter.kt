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
    private val selections = HashSet<T>()

    /**
     * 是否可选总开关
     */
    private var selectable = false

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

    /**
     * 是否可选总开关
     * @param clearSelections 是否清空所有选中项
     * @param needNotify true：刷新
     */
    fun setSelectable(
        selectable: Boolean,
        clearSelections: Boolean = true,
        needNotify: Boolean = true
    ) {
        this.selectable = selectable
        if (clearSelections) {
            selections.clear()
        }
        if (needNotify && selections.isNotEmpty()) {
            notifyItemRangeChanged(
                0,
                items.size,
                if (selectable) FLAG_SELECTABLE else FLAG_UNSELECTABLE
            )
        }
    }

    /**
     * 某数据类型 item 是否可选开关
     * @param clearItsSelections true：清空该数据类型的选中项
     * @param needNotify true：刷新
     */
    fun setSelectable(
        clazz: Class<*>,
        strategy: SelectStrategy,
        clearItsSelections: Boolean = true,
        needNotify: Boolean = true
    ) {
        /**
         * 更新 item 可选状态
         */
        getItemInfo(clazz).run {
            if (strategy == SelectStrategy.UNSELECTABLE) {
                this.selectable = false
                this.multiSelectable = false
            } else {
                this.selectable = true
                this.multiSelectable = strategy == SelectStrategy.MULTI_SELECTABLE
            }
        }
        /**
         * 更新可选总开关
         */
        var canSelect = false
        for ((_, itemInfo) in itemInfoMap) {
            if (itemInfo.selectable) {
                canSelect = true
                break
            }
        }
        this.selectable = canSelect
        /**
         * 是否移除指定类型 item 选中项
         * 是否刷新数据
         */
        if (clearItsSelections) {
            deselect(clazz, false)
        }
        if (needNotify && items.isNotEmpty()) {
            notifyItemRangeChanged(0, items.size, ItemEvent(clazz, strategy))
        }
    }

    /**
     * @param clearSelections true：清空选中项
     * @param needNotify true：刷新
     */
    fun setItems(
        items: List<T>?,
        clearSelections: Boolean = true,
        needNotify: Boolean = true
    ): RVAdapter<T> {
        clear(clearSelections, needNotify = false)
        if (!items.isNullOrEmpty()) {
            this.items.addAll(items)
        }
        if (needNotify) {
            notifyDataSetChanged()
        }
        return this
    }

    fun getItems(): List<T> {
        return items
    }

    fun add(item: T) {
        items.add(item)
        notifyItemRangeInserted(items.size - 1, 1)
    }

    fun add(index: Int, item: T) {
        items.add(index, item)
        notifyItemRangeInserted(index, 1)
    }

    fun add(list: List<T>) {
        val size = list.size
        items.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    fun add(index: Int, list: List<T>) {
        items.addAll(index, list)
        notifyItemRangeInserted(index, list.size)
    }

    fun remove(vararg list: T) {
        if (list.isNotEmpty()) {
            items.removeAll(list)
            selections.removeAll(list)
            notifyDataSetChanged()
        }
    }

    fun removeAt(index: Int) {
        val item = items.removeAt(index)
        selections.remove(item)
        notifyItemRemoved(index)
    }

    fun remove(list: List<T>) {
        if (list.isNotEmpty()) {
            items.removeAll(list)
            selections.removeAll(list)
            notifyDataSetChanged()
        }
    }

    fun removeRange(fromIndex: Int, toIndex: Int) {
        val list = items.subList(fromIndex, toIndex)
        if (list.isNotEmpty()) {
            selections.removeAll(list)
            list.clear()
            notifyItemRangeRemoved(fromIndex, toIndex - fromIndex)
        }
    }

    /**
     * @param alsoSelections true：清空选中项
     * @param needNotify true：刷新
     */
    fun clear(alsoSelections: Boolean = true, needNotify: Boolean = true) {
        items.clear()
        if (alsoSelections) {
            selections.clear()
        }
        if (needNotify) {
            notifyDataSetChanged()
        }
    }

    fun getSelections(): Set<T> = selections

    fun select(list: List<T>) {
        if (list.isNotEmpty()) {
            selections.addAll(list)
            notifyItemRangeChanged(0, items.size, FLAG_SELECTED)
        }
    }

    fun select(vararg list: T) {
        if (list.isNotEmpty()) {
            selections.addAll(list)
            notifyItemRangeChanged(0, items.size, FLAG_SELECTED)
        }
    }

    fun selectAt(index: Int) {
        selections.add(items[index])
        notifyItemChanged(index, FLAG_SELECTED)
    }

    fun selectRange(fromIndex: Int, toIndex: Int) {
        val subList = items.subList(fromIndex, toIndex)
        if (subList.isNotEmpty()) {
            select(subList)
            notifyItemRangeChanged(fromIndex, toIndex - fromIndex, FLAG_SELECTED)
        }
    }

    /**
     * 选中指定类型 item 所有数据
     * @param needNotify true：刷新
     */
    fun select(clazz: Class<*>, needNotify: Boolean = true) {
        if (items.isNotEmpty()) {
            items.forEach {
                if (it.javaClass == clazz) {
                    selections.add(it)
                }
            }
            if (needNotify) {
                notifyItemRangeChanged(0, items.size, FLAG_SELECTED)
            }
        }
    }

    fun selectAll() {
        select(items)
    }

    fun deselectAt(index: Int) {
        selections.remove(items[index])
        notifyItemChanged(index, FLAG_DESELECTED)
    }

    fun deselect(vararg list: T) {
        if (selections.isNotEmpty()) {
            selections.removeAll(list)
            notifyItemRangeChanged(0, items.size, FLAG_DESELECTED)
        }
    }

    fun deselect(list: List<T>) {
        if (selections.isNotEmpty()) {
            selections.removeAll(list)
            notifyItemRangeChanged(0, items.size, FLAG_DESELECTED)
        }
    }

    /**
     * 取消选中指定类型 item 所有数据
     * @param needNotify true：刷新
     */
    fun deselect(clazz: Class<*>, needNotify: Boolean = true) {
        if (items.isNotEmpty()) {
            selections.removeAll(items.filterIsInstance(clazz))
            if (needNotify) {
                notifyItemRangeChanged(0, items.size, FLAG_DESELECTED)
            }
        }
    }

    fun deselectAll() {
        if (selections.isNotEmpty()) {
            selections.clear()
            notifyItemRangeChanged(0, items.size, FLAG_DESELECTED)
        }
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
        holder.setListeners(
            View.OnClickListener { onItemClick(holder, it, itemClickListener) },
            View.OnLongClickListener {
                onItemClick(holder, it, itemLongClickListener)
                true
            }
        )
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
        val item = items[position]
        checkUpdateSelectionState(item, position)
        clicker?.invoke(view, item, position)
    }

    /**
     * 更新选中项状态
     */
    private fun checkUpdateSelectionState(item: T, index: Int) {
        if (!selectable) {
            return
        }
        val itemInfo = getItemInfo(item.javaClass)
        /**
         * 不可选的话不用往下处理
         */
        if (!itemInfo.selectable) {
            return
        }
        /**
         * 多选情况
         * 如果已经选中则移除选中，否则选中
         */
        if (itemInfo.multiSelectable) {
            if (selections.contains(item)) {
                deselectAt(index)
            } else {
                selectAt(index)
            }
            return
        }
        /**
         * 单选情况-已经选中该项的话不用往下处理
         * 单选情况-没有选中该项的情况下，先移除前面选中的，再添加到选中列表
         */
        if (!selections.contains(item)) {
            selections.firstOrNull { it.javaClass == item.javaClass }?.run {
                selections.remove(this)
                val itemIndex = items.indexOf(this)
                if (itemIndex != -1) {
                    notifyItemChanged(index, FLAG_DESELECTED)
                }
            }
            selections.add(item)
            notifyItemChanged(index, FLAG_SELECTED)
        }
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

    companion object {
        const val FLAG_SELECTED = 10101
        const val FLAG_DESELECTED = 10102
        const val FLAG_SELECTABLE = 10103
        const val FLAG_UNSELECTABLE = 10104
    }
}