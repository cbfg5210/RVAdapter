package cbfg.rvadapter.demo.select_multi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.SelectStrategy
import cbfg.rvadapter.demo.databinding.FragmentListSelectMultiBinding
import cbfg.rvadapter.entity.RankItem

class MultiSelectFragment : Fragment() {
    private var _binding: FragmentListSelectMultiBinding? = null
    private val binding: FragmentListSelectMultiBinding
        get() = _binding!!

    private lateinit var adapter: RVAdapter<RankItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListSelectMultiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RVAdapter<RankItem>(view.context, MultiSelectVHFactory())
            .bind(binding.rvTest)
            .setSelectable(
                RankItem::class.java, SelectStrategy.MULTI_SELECTABLE,
                clearItsSelections = false,
                needNotify = false
            )
            .setItems(getItems())
            .setItemClickListener { _, item, position ->
                adapter.toggleSelectionState(item, position)
                showSelectedItems()
            }

        binding.cbToggleSelectAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbToggleSelectAll.text = "全不选"
                adapter.selectAll()
            } else {
                binding.cbToggleSelectAll.text = "全选"
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
