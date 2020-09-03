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
#### Step 1.继承 RVHolderFactory 实现抽象方法 createViewHolder
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
#### Step 2.页面使用
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