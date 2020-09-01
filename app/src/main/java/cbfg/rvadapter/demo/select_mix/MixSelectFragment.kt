package cbfg.rvadapter.demo.select_mix

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.SelectStrategy
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.RankItem
import kotlinx.android.synthetic.main.fragment_list_select_mix.*

class MixSelectFragment : Fragment(R.layout.fragment_list_select_mix) {
    private lateinit var adapter: RVAdapter<RankItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vhFactory = MixSelectVHFactory(selectable = false, multiSelectable = false)
        adapter = RVAdapter<RankItem>(view.context, vhFactory)
            .bindRecyclerView(rvTest)
            .setSelectable(
                RankItem::class.java,
                SelectStrategy.UNSELECTABLE,
                clearItsSelections = false,
                needNotify = false
            )
            .setItems(getItems())
            .setItemClickListener { _, _, _ -> showSelectedItems() }

        rgOptions.check(R.id.rbNoSelection)
        rgOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                //不可选
                R.id.rbNoSelection -> {
                    adapter.setSelectable(false)
                    hideCbToggleSelectAll()
                    showSelectedItems()
                }
                //单选
                R.id.rbSingleSelection -> {
                    adapter.setSelectable(
                        RankItem::class.java,
                        SelectStrategy.SINGLE_SELECTABLE
                    )
                    hideCbToggleSelectAll()
                    showSelectedItems()
                }
                //多选
                R.id.rbMultiSelection -> {
                    adapter.setSelectable(
                        RankItem::class.java,
                        SelectStrategy.MULTI_SELECTABLE
                    )
                    showCbToggleSelectAll()
                    showSelectedItems()
                }
            }
        }
    }

    private fun hideCbToggleSelectAll() {
        cbToggleSelectAll.visibility = View.GONE
        cbToggleSelectAll.setOnCheckedChangeListener(null)
    }

    private fun showCbToggleSelectAll() {
        cbToggleSelectAll.visibility = View.VISIBLE
        cbToggleSelectAll.isChecked = false
        cbToggleSelectAll.text = "全选"

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
