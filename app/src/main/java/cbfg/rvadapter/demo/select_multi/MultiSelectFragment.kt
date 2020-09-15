package cbfg.rvadapter.demo.select_multi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.SelectStrategy
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.RankItem
import kotlinx.android.synthetic.main.fragment_list_select_multi.*

class MultiSelectFragment : Fragment(R.layout.fragment_list_select_multi) {
    private lateinit var adapter: RVAdapter<RankItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RVAdapter<RankItem>(view.context, MultiSelectVHFactory())
            .bindRecyclerView(rvTest)
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

        cbToggleSelectAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cbToggleSelectAll.text = "全不选"
                adapter.selectAll()
            } else {
                cbToggleSelectAll.text = "全选"
                adapter.deselectAll()
            }
            showSelectedItems()
        }
    }

    private fun showSelectedItems() {
        tvSelections.text = "选中的项: "
        adapter.getSelections().forEach { tvSelections.append("rank-${it.rank},") }
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem(i, i))
        }
        return items
    }
}
