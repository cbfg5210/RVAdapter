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
    context: Context,
    private val rvHolderFactory: RVHolderFactory
) : RecyclerView.Adapter<RVHolder<Any>>() {

    private val items = ArrayList<T>()
    private val selections = HashSet<T>()
    private var itemClickListener: ((view: View, item: T, position: Int) -> Unit)? = null
    private var itemLongClickListener: ((view: View, item: T, position: Int) -> Unit)? = null

    /**
     * 是否可选总开关
     */
    private var selectable = false

    /**
     * 保存 item 类型及对应的属性信息
     */
    private var itemInfoMap = HashMap<Class<*>, ItemInfo>()

    /**
     * 临时变量，用于记录当前 [getItemViewType] 的位置
     */
    private var tempPosition = 0

    /**
     * 如果需要处理 adapter 中的一些事件则传入此对象实例
     */
    private var lifecycleHandler: RVLifecycleHandler? = null

    /**
     * 状态页相关
     * [autoShowEmptyState] true：如果 [emptyState] 已经赋值过的话，后续数据为空会自动显示空白页
     */
    private var normalState: Any? = null
    private var emptyState: Any? = null
    private var autoShowEmptyState = true
    private lateinit var stateHolderFactory: RVHolderFactory
    private var stateClickListener: ((view: View, item: Any, position: Int) -> Unit)? = null
    private var stateLongClickListener: ((view: View, item: Any, position: Int) -> Unit)? = null

    init {
        rvHolderFactory.inflater = LayoutInflater.from(context)
    }

    fun bind(rv: RecyclerView): RVAdapter<T> {
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

    fun setStateClickListener(listener: ((view: View, item: Any, position: Int) -> Unit)): RVAdapter<T> {
        this.stateClickListener = listener
        return this
    }

    fun setStateLongClickListener(listener: ((view: View, item: Any, position: Int) -> Unit)): RVAdapter<T> {
        this.stateLongClickListener = listener
        return this
    }

    fun setLifecycleHandler(lifecycleHandler: RVLifecycleHandler): RVAdapter<T> {
        this.lifecycleHandler = lifecycleHandler
        return this
    }

    /**
     * @param autoShowEmptyState true：如果 [emptyState] 已经赋值过的话，后续数据为空会自动显示空白页
     */
    fun setStateHolderFactory(
        stateHolderFactory: RVHolderFactory,
        autoShowEmptyState: Boolean = true
    ): RVAdapter<T> {
        this.stateHolderFactory = stateHolderFactory
        this.stateHolderFactory.inflater = rvHolderFactory.inflater
        this.autoShowEmptyState = autoShowEmptyState
        return this
    }

    fun getItems(): List<T> = items

    fun getSelections(): Set<T> = selections

    fun isSelectable() = selectable

    fun isSelectable(clazz: Class<*>) = selectable && getItemInfo(clazz).selectable

    /**
     * 是否可选总开关
     * @param clearSelections 是否清空所有选中项
     * @param needNotify true：刷新
     */
    fun setSelectable(
        selectable: Boolean,
        clearSelections: Boolean = true,
        needNotify: Boolean = true
    ): RVAdapter<T> {
        this.selectable = selectable
        if (clearSelections) {
            selections.clear()
        }
        if (needNotify) {
            notifyItemRangeChanged(
                0,
                items.size,
                if (selectable) FLAG_SELECTABLE else FLAG_UNSELECTABLE
            )
        }
        return this
    }

    /**
     * 某数据类型 item 是否可选开关
     * @param clearItsSelections true：清空该数据类型的选中项
     * @param needNotify true：刷新
     */
    inline fun <reified B> setSelectable(
        strategy: SelectStrategy,
        clearItsSelections: Boolean = true,
        needNotify: Boolean = true
    ) = setSelectable(B::class.java, strategy, clearItsSelections, needNotify)

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
    ): RVAdapter<T> {
        val itemInfo = getItemInfo(clazz)
        if (strategy == SelectStrategy.UNSELECTABLE) {
            //更新 item 可选状态
            itemInfo.selectable = false
            itemInfo.multiSelectable = false
            //更新可选总开关
            for ((_, it) in itemInfoMap) {
                if (it.selectable) {
                    this@RVAdapter.selectable = true
                    break
                }
            }
        } else {
            //更新 item 可选状态
            itemInfo.selectable = true
            itemInfo.multiSelectable = strategy == SelectStrategy.MULTI_SELECTABLE
            //更新可选总开关
            this@RVAdapter.selectable = true
        }

        /**
         * 是否移除指定类型 item 选中项
         * 是否刷新数据
         */
        if (clearItsSelections) {
            deselect(clazz, false)
        }
        if (needNotify) {
            notifyItemRangeChanged(0, items.size, ItemEvent(clazz, strategy))
        }
        return this
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

    fun add(item: T) {
        add(items.size, item)
    }

    fun add(index: Int, item: T) {
        items.add(index, item)
        notifyItemInserted(index)
    }

    fun addList(list: List<T>) {
        addList(items.size, list)
    }

    fun addList(index: Int, list: List<T>) {
        items.addAll(index, list)
        notifyItemRangeInserted(index, list.size)
    }

    fun remove(item: T) {
        selections.remove(item)
        val index = items.indexOf(item)
        if (index != -1) {
            items.remove(item)
            notifyItemRemoved(index)
        }
    }

    fun removeAt(index: Int) {
        selections.remove(items.removeAt(index))
        notifyItemRemoved(index)
    }

    fun removeList(list: Collection<T>) {
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

    fun select(item: T) {
        select(item, items.indexOf(item))
    }

    fun selectAt(index: Int) {
        select(items[index], index)
    }

    /**
     * 选中 item
     * 该类型允许选择才可以选中
     * 该类型单选的话要判断移除先前选中项
     */
    private fun select(item: T, index: Int) {
        require(isSelectable(item.javaClass)) { TIP_SELECT_DISABLED }
        if (selections.contains(item)) {
            return
        }
        if (!getItemInfo(item.javaClass).multiSelectable) {
            selections.firstOrNull { it.javaClass == item.javaClass }?.run {
                selections.remove(this)
                val preIndex = items.indexOf(this)
                if (preIndex != -1) {
                    notifyItemChanged(preIndex, FLAG_DESELECTED)
                }
            }
        }
        selections.add(item)
        if (index != -1) {
            notifyItemChanged(index, FLAG_SELECTED)
        }
    }

    /**
     * @param strictCheck true：严格检查数据类型是否单选，如果单选并且选中多项则抛出异常
     */
    fun selectList(list: Collection<T>, strictCheck: Boolean = false) {
        _selectList(list, strictCheck)
        notifyItemRangeChanged(0, items.size, FLAG_SELECTED)
    }

    /**
     * @param strictCheck true：严格检查数据类型是否单选，如果单选并且选中多项则抛出异常
     */
    fun selectRange(fromIndex: Int, toIndex: Int, strictCheck: Boolean = false) {
        _selectList(items.subList(fromIndex, toIndex), strictCheck)
        notifyItemRangeChanged(fromIndex, toIndex - fromIndex, FLAG_SELECTED)
    }

    /**
     * 多项选择
     * @param strictCheck true：严格检查是否违反单选原则，false：只检查第一项数据类型是否违反单选原则
     */
    private fun _selectList(list: Collection<T>, strictCheck: Boolean = false) {
        if (list.isEmpty()) {
            return
        }
        if (strictCheck) {
            val mapList = list.groupBy { it.javaClass }
            for ((_, mList) in mapList) {
                _selectList(mList, false)
            }
        } else {
            val firstItem = list.first()
            require(isSelectable(firstItem.javaClass)) { TIP_SELECT_DISABLED }
            if (!getItemInfo(firstItem.javaClass).multiSelectable) {
                require(list.size == 1) { "'${firstItem.javaClass}' 类型数据只能单选，不可以多选！" }
            }
            if (list.size == 1) {
                select(firstItem)
            } else {
                selections.addAll(list)
            }
        }
    }

    /**
     * 选中指定类型 item 所有数据，类型是单选的话只能选中一项
     */
    fun select(clazz: Class<*>) {
        if (items.isEmpty()) {
            return
        }
        require(isSelectable(clazz)) { TIP_SELECT_DISABLED }
        if (!getItemInfo(clazz).multiSelectable) {
            items.firstOrNull { it.javaClass == clazz }?.run { select(this) }
        } else {
            items.forEach {
                if (it.javaClass == clazz) {
                    selections.add(it)
                }
            }
            notifyItemRangeChanged(0, items.size, FLAG_SELECTED)
        }
    }

    fun selectAll(strictCheck: Boolean = false) {
        selectList(items, strictCheck)
    }

    fun deselectAt(index: Int) {
        selections.remove(items[index])
        notifyItemChanged(index, FLAG_DESELECTED)
    }

    fun deselect(item: T) {
        selections.remove(item)
        val index = items.indexOf(item)
        if (index != -1) {
            notifyItemChanged(index, FLAG_DESELECTED)
        }
    }

    fun deselectList(list: Collection<T>) {
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
        if (selections.isNotEmpty()) {
            selections.removeAll(selections.filterIsInstance(clazz))
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
     * 显示状态页，调用这个方法前要设置 [stateHolderFactory]
     * @param isEmptyState true：空白页
     */
    fun showStatePage(state: Any, isEmptyState: Boolean = false) {
        if (isEmptyState) {
            emptyState = state
            normalState = null
        } else {
            normalState = state
        }
        notifyDataSetChanged()
    }

    /**
     * 获取 item 信息，没有则创建保存
     */
    private fun getItemInfo(clazz: Class<*>): ItemInfo {
        return itemInfoMap[clazz] ?: ItemInfo(itemInfoMap.size).also { itemInfoMap[clazz] = it }
    }

    override fun getItemViewType(position: Int): Int {
        if (items.isEmpty()) {
            (normalState ?: emptyState)?.run { return stateHolderFactory.getItemViewType(this) }
        }
        tempPosition = position
        val item = items[position]
        val viewType = rvHolderFactory.getItemViewType(item)
        return if (viewType != -1) viewType else getItemInfo(item.javaClass).viewType
    }

    fun getRealItemCount() = items.size

    override fun getItemCount(): Int {
        if (items.isEmpty()) {
            return if (normalState != null || emptyState != null) 1 else 0
        }
        /**
         * 如果有数据了的话要清空状态 item
         */
        normalState = null
        if (!autoShowEmptyState) {
            emptyState = null
        }
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder<Any> {
        if (items.isEmpty()) {
            val state = normalState ?: emptyState
            if (state != null) {
                val holder = stateHolderFactory.createViewHolder(parent, viewType, this)
                holder.setListeners(
                    stateClickListener?.run {
                        View.OnClickListener { this(it, state, holder.adapterPosition) }
                    },
                    stateLongClickListener?.run {
                        View.OnLongClickListener {
                            this(it, state, holder.adapterPosition)
                            true
                        }
                    }
                )
                return holder as RVHolder<Any>
            }
        }
        val item = items[tempPosition]
        val holder = rvHolderFactory.createViewHolder(parent, viewType, item)
        holder.setListeners(
            itemClickListener?.run { View.OnClickListener { onItemClick(holder, it, this) } },
            itemLongClickListener?.run {
                View.OnLongClickListener {
                    onItemClick(holder, it, this)
                    true
                }
            })
        return holder as RVHolder<Any>
    }

    private fun onItemClick(
        holder: RVHolder<out Any>,
        view: View,
        clicker: ((view: View, item: T, position: Int) -> Unit)?
    ) {
        val position = holder.adapterPosition
        if (position in 0 until items.size) {
            clicker?.invoke(view, items[position], position)
        }
    }

    /**
     * 更新选中项状态
     *
     * 可选才处理选中事件
     *
     * 单选情况：
     * 已经选中该项的话不用再处理
     * 没有选中该项的情况下，先移除前面选中的，再添加当前的到选中列表
     *
     * 多选情况：
     * 如果已经选中则移除选中，否则选中
     */
    fun toggleSelectionState(item: T, index: Int) {
        if (isSelectable(item.javaClass)) {
            if (!getItemInfo(item.javaClass).multiSelectable) {
                select(item, index)
            } else if (selections.contains(item)) {
                deselectAt(index)
            } else {
                selectAt(index)
            }
        }
    }

    override fun onBindViewHolder(holder: RVHolder<Any>, position: Int) {
        bindRVHolder(holder, position, null)
    }

    override fun onBindViewHolder(
        holder: RVHolder<Any>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        bindRVHolder(holder, position, payloads)
    }

    private fun bindRVHolder(
        holder: RVHolder<Any>,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        if (items.isEmpty()) {
            (normalState ?: emptyState)?.run { holder.setContent(this, false) }
            return
        }
        val item = items[position]
        val isSelected = isSelectable(item.javaClass) && selections.contains(item)
        //Log.e("*****", "payloads = $payloads,position = $position")
        if (payloads.isNullOrEmpty()) {
            holder.setContent(item, isSelected)
        } else {
            holder.setContent(item, isSelected, payloads[0])
        }
    }

    override fun onViewRecycled(holder: RVHolder<Any>) {
        super.onViewRecycled(holder)
        lifecycleHandler?.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RVHolder<Any>): Boolean {
        lifecycleHandler?.run { return this.onFailedToRecycleView(holder) }
        return super.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RVHolder<Any>) {
        super.onViewAttachedToWindow(holder)
        lifecycleHandler?.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RVHolder<Any>) {
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
        private const val TIP_SELECT_DISABLED = "此类型数据当前不可选中！"
        const val FLAG_SELECTED = 10101
        const val FLAG_DESELECTED = 10102
        const val FLAG_SELECTABLE = 10103
        const val FLAG_UNSELECTABLE = 10104
    }
}