package cbfg.rvadapter.demo.lifecycle

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.RVLifecycleHandler
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.ItemPersonBinding
import cbfg.rvadapter.entity.Person

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 16:18
 * 功能描述:
 */
@Suppress("UNCHECKED_CAST")
class LifecycleVHFactory : RVHolderFactory() {
    override fun createViewHolder(
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> {
        return PersonVH(inflate(R.layout.item_person, parent))
    }

    private class PersonVH(itemView: View) : RVHolder<Person>(itemView) {
        private val binding = ItemPersonBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        override fun setContent(item: Person, isSelected: Boolean, payload: Any?) {
            binding.ivAvatar.setImageResource(item.avatar)
            binding.tvName.text = "${item.name} - $adapterPosition"
        }

        override fun setListeners(
            itemClickListener: View.OnClickListener?,
            itemLongClickListener: View.OnLongClickListener?
        ) {
            /**
             * 这里除了 item 设置点击/长按事件外，也给头像设置点击事件
             */
            super.setListeners(itemClickListener, itemLongClickListener)
            binding.ivAvatar.setOnClickListener(itemClickListener)
        }
    }

    fun getLifecycleHandler(): RVLifecycleHandler {
        return object : RVLifecycleHandler() {
            override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
                super.onViewRecycled(holder)
                Log.e(TAG, "onViewRecycled,holder = $holder")
            }

            override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
                Log.e(TAG, "onFailedToRecycleView,holder = $holder")
                return super.onFailedToRecycleView(holder)
            }

            override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
                super.onViewAttachedToWindow(holder)
                Log.e(TAG, "onViewAttachedToWindow,holder = $holder")
            }

            override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
                super.onViewDetachedFromWindow(holder)
                Log.e(TAG, "onViewDetachedFromWindow,holder = $holder")
            }
        }
    }

    companion object {
        private const val TAG = "--Lifecycle--"
    }
}