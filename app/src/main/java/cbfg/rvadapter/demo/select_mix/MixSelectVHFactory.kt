package cbfg.rvadapter.demo.select_mix

import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.*
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.ItemSelectMixBinding
import cbfg.rvadapter.entity.RankItem

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
        parent: ViewGroup?,
        viewType: Int,
        item: Any
    ): RVHolder<out Any> {
        return RankItemVH(inflate(R.layout.item_select_mix, parent))
    }

    private inner class RankItemVH(itemView: View) : RVHolder<RankItem>(itemView) {
        private val binding = ItemSelectMixBinding.bind(itemView)

        override fun setContent(item: RankItem, isSelected: Boolean, payload: Any?) {
            if (payload != null) {
                handlePayload(isSelected, payload)
            } else {
                binding.ivIcon.setImageResource(R.mipmap.ic_launcher)
                binding.tvRank.text = item.rank.toString()

                updateSelectableState(selectable, multiSelectable)

                if (selectable) {
                    if (multiSelectable) {
                        binding.cbSelect.isChecked = isSelected
                    } else {
                        binding.rbSelect.isChecked = isSelected
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
                    binding.rbSelect.isChecked = isSelected
                }
                is ItemEvent -> {
                    when (payload.obj) {
                        SelectStrategy.UNSELECTABLE -> {
                            updateSelectableState(false, multiSelectable)
                        }
                        SelectStrategy.SINGLE_SELECTABLE -> {
                            updateSelectableState(mSelectable = true, mMultiSelectable = false)
                            binding.rbSelect.isChecked = isSelected
                        }
                        else -> {
                            updateSelectableState(mSelectable = true, mMultiSelectable = true)
                            binding.cbSelect.isChecked = isSelected
                        }
                    }
                }
                RVAdapter.FLAG_SELECTED -> {
                    if (multiSelectable) {
                        binding.cbSelect.isChecked = true
                    } else {
                        binding.rbSelect.isChecked = true
                    }
                }
                RVAdapter.FLAG_DESELECTED -> {
                    if (multiSelectable) {
                        binding.cbSelect.isChecked = false
                    } else {
                        binding.rbSelect.isChecked = false
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
                binding.rbSelect.visibility = View.GONE
                binding.cbSelect.visibility = View.GONE
            } else if (multiSelectable) {
                binding.rbSelect.visibility = View.GONE
                binding.cbSelect.visibility = View.VISIBLE
            } else {
                binding.rbSelect.visibility = View.VISIBLE
                binding.cbSelect.visibility = View.GONE
            }
        }
    }
}