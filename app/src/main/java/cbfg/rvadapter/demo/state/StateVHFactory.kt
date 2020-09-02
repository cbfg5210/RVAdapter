package cbfg.rvadapter.demo.state

import android.view.LayoutInflater
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import cbfg.rvadapter.demo.R

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
            RVState.State.LOADING -> LoadingVHolder(inflater, parent)
            RVState.State.EMPTY -> EmptyVHolder(inflater, parent)
            RVState.State.ERROR -> ErrorVHolder(inflater, parent)
        }
    }

    private class LoadingVHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) : RVHolder<RVState>(inflater, parent, R.layout.layout_state_loading) {
        override fun setContent(item: RVState, isSelected: Boolean, payload: Any?) {
        }
    }

    private class EmptyVHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) : RVHolder<RVState>(inflater, parent, R.layout.layout_state_empty) {
        override fun setContent(item: RVState, isSelected: Boolean, payload: Any?) {
        }
    }

    private class ErrorVHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) : RVHolder<RVState>(inflater, parent, R.layout.layout_state_error) {
        override fun setContent(item: RVState, isSelected: Boolean, payload: Any?) {
        }
    }
}