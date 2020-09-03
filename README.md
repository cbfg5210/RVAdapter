# RVAdapter
[![](https://jitpack.io/v/com.gitee.cbfg5210/RVAdapter.svg)](https://jitpack.io/#com.gitee.cbfg5210/RVAdapter)

之前虽然有封装过 RecyclerView.Adapter，但是在使用过程中发现了很多问题，于是就有了这次的重新封装。

### 引入依赖
#### Step 1. Add the JitPack repository to your build file
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### Step 2. Add the dependency
```gradle
dependencies {
    implementation 'com.gitee.cbfg5210:RVAdapter:$version'
}
```

### 使用
#### Step 1. 继承 RVHolderFactory 并实现抽象方法 createViewHolder
```kotlin
class SimpleVHFactory : RVHolderFactory() {
    override fun createViewHolder(
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> {
        return PersonVH(inflate(R.layout.item_person, parent))
    }

    private class PersonVH(itemView: View) : RVHolder<Person>(itemView) {
        private val ivAvatar = itemView.ivAvatar
        private val tvName = itemView.tvName

        @SuppressLint("SetTextI18n")
        override fun setContent(item: Person, isSelected: Boolean, payload: Any?) {
            ivAvatar.setImageResource(item.avatar)
            tvName.text = "${item.name} - $adapterPosition"
        }

        override fun setListeners(
            itemClickListener: View.OnClickListener?,
            itemLongClickListener: View.OnLongClickListener?
        ) {
            /**
             * 这里除了 item 设置点击/长按事件外，也给头像设置点击事件
             */
            super.setListeners(itemClickListener, itemLongClickListener)
            ivAvatar.setOnClickListener(itemClickListener)
        }
    }
}
```
#### Step 2. 页面使用
```kotlin
val adapter = RVAdapter<Person>(view.context, SimpleVHFactory())
        .bindRecyclerView(rvTest)
        .setItems(
            DataHelper.getPeople(),
            clearSelections = false,
            needNotify = false
        )
        .setItemClickListener { v, item, position ->
            /**
             * 点击头像
             */
            if (v.id == R.id.ivAvatar) {
                val tip = "click avatar ${item.name} - $position"
                Toast.makeText(view.context, tip, Toast.LENGTH_SHORT).show()
            }
            /**
             * 点击 item
             */
            val tip = "click item ${item.name} - $position"
            Toast.makeText(view.context, tip, Toast.LENGTH_SHORT).show()
        }
        .setItemLongClickListener { _, item, position ->
            val tip = "long click ${item.name} - $position"
            Toast.makeText(view.context, tip, Toast.LENGTH_SHORT).show()
        }
```

### 直达 DEMO
* [入门 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/simple/SimpleFragment.kt)
* [多 ViewType Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/multi_view_type/MultiViewTypeFragment.kt)
* [单选 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/select_single/SingleSelectFragment.kt)
* [多选 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/select_multi/MultiSelectFragment.kt)
* [混合选择 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/select_mix/MixSelectFragment.kt)
* [略复杂的多选 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/select_complex/ComplexFragment.kt)
* [状态页 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/state/StateFragment.kt)
* [Diff 更新数据 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/diff/DiffFragment.kt)
* [拖拽排序 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/drag/DragFragment.kt)
* [生命周期事件处理 Demo](https://gitee.com/cbfg5210/RVAdapter/blob/master/app/src/main/java/cbfg/rvadapter/demo/lifecycle/LifecycleFragment.kt)

### API 一览
```kotlin
/**
 * 给 RecyclerView 设置 adapter
 */
fun bindRecyclerView(rv: RecyclerView): RVAdapter<T>

/**
 * 设置点击事件
 */
fun setItemClickListener(listener: (view: View, item: T, position: Int) -> Unit): RVAdapter<T>

/**
 * 设置长按事件
 */
fun setItemLongClickListener(listener: (view: View, item: T, position: Int) -> Unit): RVAdapter<T>

/**
 * 设置状态页点击事件
 */
fun setStateClickListener(listener: (view: View, item: Any, position: Int) -> Unit): RVAdapter<T>

/**
 * 设置生命周期事件处理
 */
fun setLifecycleHandler(lifecycleHandler: RVLifecycleHandler): RVAdapter<T>

/**
 * 获取 items
 */
fun getItems(): List<T>

/**
 * 获取选中项
 */
fun getSelections(): Set<T>

/**
 * 是否可选
 */
fun isSelectable(): Boolean

/**
 * 指定数据类型是否可选
 */
fun isSelectable(clazz: Class<*>)

/**
 * 设置(不)可选，设为 false 的话表示所有数据类型都不可选，相当于总开关
 */
fun setSelectable(
        selectable: Boolean,
        clearSelections: Boolean = true,
        needNotify: Boolean = true
    ): RVAdapter<T>

/**
 * 设置指定类型数据是否可选
 */
fun setSelectable(
        clazz: Class<*>,
        strategy: SelectStrategy,
        clearItsSelections: Boolean = true,
        needNotify: Boolean = true
    ): RVAdapter<T>

/**
 * 设置数据
 * @param clearSelections true：清空选中项
 * @param needNotify true：刷新
 */
fun setItems(
        items: List<T>?,
        clearSelections: Boolean = true,
        needNotify: Boolean = true
    ): RVAdapter<T>

/**
 * 添加一项数据
 */
fun fun add(item: T)

/**
 * 向指定位置插入一项数据，注意 index 不要超出范围
 */
fun fun add(index: Int, item: T)

/**
 * 添加多项数据
 */
fun fun add(list: List<T>)

/**
 * 向指定位置插入多项数据，注意 index 不要超出范围
 */
fun fun add(index: Int, list: List<T>)

/**
 * 移除一项数据
 */
fun fun remove(item: T)

/**
 * 移除指定位置的一项数据，注意 index 不要超出范围
 */
fun fun removeAt(index: Int)

/**
 * 数据多项数据
 */
fun fun remove(list: Collection<T>)

/**
 * 移除指定范围的数据，注意 fromIndex、toIndex 不要超出范围
 */
fun fun removeRange(fromIndex: Int, toIndex: Int)

/**
 * 清空数据
 * @param alsoSelections true：清空选中项
 * @param needNotify true：刷新
 */
fun fun clear(alsoSelections: Boolean = true, needNotify: Boolean = true)

/**
 * 选中 item
 * 该类型允许选择才可以选中，否则抛出异常
 */
fun fun select(item: T)

/**
 * 选中指定位置 item，注意 index 不要超出范围
 */
fun fun selectAt(index: Int)

/**
 * 选中多项 item
 * 该类型允许选择才可以选中，否则抛出异常
 * 该类型如果是单选的话，选中多项会抛出异常
 *  @param strictCheck true：严格检查数据类型是否单选，如果单选并且选中多项则抛出异常
 */
fun fun select(list: Collection<T>, strictCheck: Boolean = false)

/**
 * 选中指定范围的多个 item，注意 fromIndex、toIndex 不要超出范围
 * 该类型允许选择才可以选中，否则抛出异常
 * 该类型如果是单选的话，选中多项会抛出异常
 *  @param strictCheck true：严格检查数据类型是否单选，如果单选并且选中多项则抛出异常
 */
fun fun selectRange(fromIndex: Int, toIndex: Int, strictCheck: Boolean = false)

/**
 * 选中指定类型 item 所有数据，类型是单选的话只会选中一项
 */
fun fun select(clazz: Class<*>)

/**
 * 选中所有 item
 * 该类型允许选择才可以选中，否则抛出异常
 * 该类型如果是单选的话，选中多项会抛出异常
 *  @param strictCheck true：严格检查数据类型是否单选，如果单选并且选中多项则抛出异常
 */
fun fun selectAll(strictCheck: Boolean = false)

/**
 * 取消选中指定位置 item，注意 index 不要超出范围
 */
fun fun deselectAt(index: Int)

/**
 * 取消选中 item
 */
fun fun deselect(item: T)

/**
 * 取消选中多个 item
 */
fun fun deselect(list: Collection<T>)

/**
 * 取消选中指定类型 item 所有数据
 * @param needNotify true：刷新
 */
fun fun deselect(clazz: Class<*>, needNotify: Boolean = true)

/**
 * 全部不选中
 */
fun fun deselectAll()

/**
 * 显示状态页，调用这个方法前要设置 [stateHolderFactory]，否则抛出异常
 * @param isEmptyState true：空白页
 */
fun fun showStatePage(state: Any, isEmptyState: Boolean = false)

/**
 * 获取数据数量，如果有状态页的话，这个可能不准确
 */
fun fun getItemCount(): Int

/**
 * 获取实际的数据数量
 */
fun fun getRealItemCount(): Int
```