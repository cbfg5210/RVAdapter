package cbfg.rvadapter.demo.select_mix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.*
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.RankItem
import kotlinx.android.synthetic.main.item_select_mix.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class MixSelectVHFactory(
    private var selectable: Boolean,
    private var multiSelectable: Boolean
) : RVHolderFactory() {

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): RVHolder<out Any> {
        return RankItemVH(inflater.inflate(R.layout.item_select_mix, parent, false))
    }

    private inner class RankItemVH(itemView: View) : RVHolder<RankItem>(itemView) {

        override fun setContent(item: RankItem, isSelected: Boolean, payload: Any?) {
            if (payload != null) {
                handlePayload(isSelected, payload)
            } else {
                itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
                itemView.tvRank.text = item.rank.toString()

                updateSelectableState(selectable, multiSelectable)

                if (selectable) {
                    if (multiSelectable) {
                        itemView.cbSelect.isChecked = isSelected
                    } else {
                        itemView.rbSelect.isChecked = isSelected
                    }
                }
            }
        }

        private fun handlePayload(isSelected: Boolean, payload: Any) {
            when (payload) {
                RVAdapter.FLAG_UNSELECTABLE -> {
                    updateSelectableState(false, multiSelectable)
                }
                RVAdapter.FLAG_SELECTABLE -> {
                    updateSelectableState(mSelectable = true, mMultiSelectable = false)
                    itemView.rbSelect.isChecked = isSelected
                }
                is ItemEvent -> {
                    when (payload.obj) {
                        SelectStrategy.UNSELECTABLE -> {
                            updateSelectableState(false, multiSelectable)
                        }
                        SelectStrategy.SINGLE_SELECTABLE -> {
                            updateSelectableState(mSelectable = true, mMultiSelectable = false)
                            itemView.rbSelect.isChecked = isSelected
                        }
                        else -> {
                            updateSelectableState(mSelectable = true, mMultiSelectable = true)
                            itemView.cbSelect.isChecked = isSelected
                        }
                    }
                }
                RVAdapter.FLAG_SELECTED -> {
                    if (multiSelectable) {
                        itemView.cbSelect.isChecked = true
                    } else {
                        itemView.rbSelect.isChecked = true
                    }
                }
                RVAdapter.FLAG_DESELECTED -> {
                    if (multiSelectable) {
                        itemView.cbSelect.isChecked = false
                    } else {
                        itemView.rbSelect.isChecked = false
                    }
                }
            }
        }

        /**
         * 更新 不可选/单选/多选 状态:
         */
        private fun updateSelectableState(mSelectable: Boolean, mMultiSelectable: Boolean) {
            selectable = mSelectable
            multiSelectable = mMultiSelectable
            if (!selectable) {
                itemView.rbSelect.visibility = View.GONE
                itemView.cbSelect.visibility = View.GONE
            } else if (multiSelectable) {
                itemView.rbSelect.visibility = View.GONE
                itemView.cbSelect.visibility = View.VISIBLE
            } else {
                itemView.rbSelect.visibility = View.VISIBLE
                itemView.cbSelect.visibility = View.GONE
            }
        }
    }
}