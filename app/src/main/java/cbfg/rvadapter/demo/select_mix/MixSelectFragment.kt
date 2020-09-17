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
            .bind(rvTest)
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

        rgOptions.check(R.id.rbNoSelection)
        rgOptions.setOnCheckedChangeListener { _, checkedId ->
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
        cbToggleSelectAll.isChecked = false
        cbToggleSelectAll.text = normalText

        cbToggleSelectAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cbToggleSelectAll.text = "全不选"
                adapter.selectAll()
            } else {
                cbToggleSelectAll.text = normalText
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
