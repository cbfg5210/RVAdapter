package cbfg.rvadapter.demo.select_single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.SelectStrategy
import cbfg.rvadapter.demo.databinding.FragmentListSelectSingleBinding
import cbfg.rvadapter.entity.RankItem

class SingleSelectFragment : Fragment() {
    private var _binding: FragmentListSelectSingleBinding? = null
    private val binding: FragmentListSelectSingleBinding
        get() = _binding!!

    private lateinit var adapter: RVAdapter<RankItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListSelectSingleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RVAdapter<RankItem>(view.context, SingleSelectVHFactory())
            .bind(binding.rvTest)
            //设置指定类型数据可选
            /*.setSelectable(
                RankItem::class.java,
                SelectStrategy.SINGLE_SELECTABLE,
                clearItsSelections = false,
                needNotify = false
            )*/
            .setSelectable<RankItem>(
                SelectStrategy.SINGLE_SELECTABLE,
                clearItsSelections = false,
                needNotify = false
            )
            .setItems(getItems())
            .setItemClickListener { _, item, position ->
                adapter.toggleSelectionState(item, position)
                showSelectedItem()
            }
            .also {
                //这里默认选中第一项
                it.selectAt(0)
            }

        showSelectedItem()
    }

    private fun showSelectedItem() {
        binding.tvSelections.text = "选中的项: ${adapter.getSelections()}"
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
