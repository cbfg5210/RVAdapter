package cbfg.rvadapter.demo.state

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.LayoutStateLoadingBinding

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/9/2 11:37
 * 功能描述: 状态页管理
 */
class StateVHFactory : RVHolderFactory() {
    override fun getItemViewType(position: Int, item: Any): Int {
        item as RVState
        return when (item.state) {
            RVState.State.LOADING -> R.layout.layout_state_loading
            RVState.State.EMPTY -> R.layout.layout_state_empty
            RVState.State.ERROR -> R.layout.layout_state_error
        }
    }

    override fun createViewHolder(
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> {
        val itemView = inflate(viewType, parent)
        return if (viewType == R.layout.layout_state_loading) LoadingVHolder(itemView)
        else NormalVHolder(itemView)
    }

    private class LoadingVHolder(itemView: View) : RVHolder<RVState>(itemView) {
        private val binding = LayoutStateLoadingBinding.bind(itemView)

        override fun setContent(item: RVState, isSelected: Boolean, payload: Any?) {
            binding.tvTip.text = item.tip
        }

        override fun setListeners(
            itemClickListener: View.OnClickListener?,
            itemLongClickListener: View.OnLongClickListener?
        ) {
            //无需点击事件
        }
    }

    private class NormalVHolder(itemView: View) : RVHolder<RVState>(itemView) {
        private val tvTip = itemView.findViewById<TextView>(R.id.tvTip)
        private val ivImg = itemView.findViewById<ImageView>(R.id.ivImg)
        override fun setContent(item: RVState, isSelected: Boolean, payload: Any?) {
            ivImg.setImageResource(item.imgSrc)
            tvTip.text = item.tip
        }
    }
}