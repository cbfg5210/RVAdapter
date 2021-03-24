package cbfg.rvadapter.demo.select_mix

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.SelectStrategy
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.FragmentListSelectMixBinding
import cbfg.rvadapter.entity.RankItem

class MixSelectFragment : Fragment() {
    private var _binding: FragmentListSelectMixBinding? = null
    private val binding: FragmentListSelectMixBinding
        get() = _binding!!

    private lateinit var adapter: RVAdapter<RankItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListSelectMixBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vhFactory = MixSelectVHFactory(selectable = false, multiSelectable = false)
        adapter = RVAdapter<RankItem>(view.context, vhFactory)
            .bind(binding.rvTest)
            .setSelectable(
                RankItem::class.java,
                SelectStrategy.UNSELECTABLE,
                clearItsSelections = false,
                needNotify = false
            )
            .setItems(getItems())
            .setItemClickListener { _, item, position ->
                adapter.toggleSelectionState(item, position)
                showSelectedItems()
            }

        binding.rgOptions.check(R.id.rbNoSelection)
        binding.rgOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                //不可选
                R.id.rbNoSelection -> {
                    adapter.setSelectable(false)
                    showSelectedItems()
                }
                //单选
                R.id.rbSingleSelection -> {
                    adapter.setSelectable(
                        RankItem::class.java,
                        SelectStrategy.SINGLE_SELECTABLE
                    )
                    showSelectedItems()
                }
                //多选
                R.id.rbMultiSelection -> {
                    adapter.setSelectable(
                        RankItem::class.java,
                        SelectStrategy.MULTI_SELECTABLE
                    )
                    showSelectedItems()
                }
            }
        }

        showCbToggleSelectAll()
    }

    private fun showCbToggleSelectAll() {
        val normalText = "全选(单选类型如果多选会 crash)"
        binding.cbToggleSelectAll.isChecked = false
        binding.cbToggleSelectAll.text = normalText

        binding.cbToggleSelectAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbToggleSelectAll.text = "全不选"
                adapter.selectAll()
            } else {
                binding.cbToggleSelectAll.text = normalText
                adapter.deselectAll()
            }
            showSelectedItems()
        }
    }

    private fun showSelectedItems() {
        binding.tvSelections.text = "选中的项: "
        adapter.getSelections().forEach { binding.tvSelections.append("rank-${it.rank},") }
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem(i, i))
        }
        return items
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
