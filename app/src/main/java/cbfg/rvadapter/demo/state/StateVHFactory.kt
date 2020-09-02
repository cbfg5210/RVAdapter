package cbfg.rvadapter.demo.state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R
import kotlinx.android.synthetic.main.layout_state_empty.view.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/9/2 11:37
 * 功能描述: 状态页管理
 */
class StateVHFactory : RVHolderFactory() {
    override fun getItemViewType(item: Any): Int {
        item as RVState
        return when (item.state) {
            RVState.State.LOADING -> R.layout.layout_state_loading
            RVState.State.EMPTY -> R.layout.layout_state_empty
            RVState.State.ERROR -> R.layout.layout_state_error
        }
    }

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<out Any> {
        item as RVState
        return when (item.state) {
            RVState.State.LOADING -> LoadingVHolder(
                inflater.inflate(
                    R.layout.layout_state_loading,
                    parent,
                    false
                )
            )
            RVState.State.EMPTY -> EmptyVHolder(
                inflater.inflate(
                    R.layout.layout_state_empty,
                    parent,
                    false
                )
            )
            RVState.State.ERROR -> ErrorVHolder(
                inflater.inflate(
                    R.layout.layout_state_error,
                    parent,
                    false
                )
            )
        }
    }

    private class LoadingVHolder(itemView: View) : RVHolder<RVState>(itemView) {
        private val tvTip = itemView.tvTip
        override fun setContent(item: RVState, isSelected: Boolean, payload: Any?) {
            tvTip.text = item.tip
        }

        override fun setListeners(
            itemClickListener: View.OnClickListener?,
            itemLongClickListener: View.OnLongClickListener?
        ) {
            //无需点击事件
        }
    }

    private class EmptyVHolder(itemView: View) : RVHolder<RVState>(itemView) {
        private val tvTip = itemView.tvTip
        private val ivImg = itemView.ivImg
        override fun setContent(item: RVState, isSelected: Boolean, payload: Any?) {
            ivImg.setImageResource(item.imgSrc)
            tvTip.text = item.tip
        }
    }

    private class ErrorVHolder(itemView: View) : RVHolder<RVState>(itemView) {
        private val tvTip = itemView.tvTip
        private val ivImg = itemView.ivImg
        override fun setContent(item: RVState, isSelected: Boolean, payload: Any?) {
            ivImg.setImageResource(item.imgSrc)
            tvTip.text = item.tip
        }
    }
}