package cbfg.rvadapter.demo.select_single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.SelectStrategy
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.RankItem
import kotlinx.android.synthetic.main.fragment_list_select_single.view.*

class SingleSelectFragment : Fragment() {
    private lateinit var layout: View
    private lateinit var adapter: RVAdapter<RankItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_list_select_single, container, false)

        adapter = RVAdapter<RankItem>(layout.context, SingleSelectVHFactory())
            .bindRecyclerView(layout.rvTest)
            //设置指定类型数据可选
            .setSelectable(
                RankItem::class.java,
                SelectStrategy.SINGLE_SELECTABLE,
                clearItsSelections = false,
                needNotify = false
            )
            .setItems(getItems())
            .setItemClickListener { _, _, _ -> showSelectedItem() }
            .also {
                //这里默认选中第一项
                it.selectAt(0)
            }

        showSelectedItem()

        return layout
    }

    private fun showSelectedItem() {
        layout.tvSelections.text = "选中的项: ${adapter.getSelections()}"
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem(i, i))
        }
        return items
    }
}
