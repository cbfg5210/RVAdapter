package cbfg.rvadapter.demo.diff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import cbfg.rvadapter.DiffHelper
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.FragmentDiffBinding
import cbfg.rvadapter.entity.RankItem
import cbfg.rvadapter.util.AppExecutors

class DiffFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDiffBinding? = null
    private val binding: FragmentDiffBinding
        get() = _binding!!

    private var idCount: Int = 3
    private lateinit var adapter: RVAdapter<RankItem>
    private lateinit var diffHelper: DiffHelper<RankItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RVAdapter<RankItem>(view.context, DiffVHFactory())
            .bind(binding.rvTest)
            .setItems(getItems())

        diffHelper = DiffHelper(adapter, object : DiffHelper.BDiffCallback<RankItem> {
            override fun getChangePayload(oldItem: RankItem, newItem: RankItem): Any? = 1
            override fun areContentsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.rank == newItem.rank
            }
        })

        binding.btnTopAdd.setOnClickListener(this)
        binding.btnTopRemove.setOnClickListener(this)
        binding.btnUpdateAll.setOnClickListener(this)
        binding.btnShuffle.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTopAdd -> {
                val newItems = ArrayList<RankItem>()
                newItems.addAll(adapter.getItems())

                idCount++
                newItems.add(0, RankItem(idCount, idCount))
                idCount++
                newItems.add(0, RankItem(idCount, idCount))

                calculateDiffAndUpdate(newItems)
            }

            R.id.btnTopRemove -> {
                val newItems = ArrayList<RankItem>()
                newItems.addAll(adapter.getItems())

                if (newItems.size > 1) {
                    newItems.removeAt(0)
                    newItems.removeAt(0)
                } else if (newItems.size > 0) {
                    adapter.removeAt(0)
                }

                calculateDiffAndUpdate(newItems)
            }

            R.id.btnShuffle -> {
                val newItems = ArrayList<RankItem>()
                adapter.getItems().forEach { newItems.add(RankItem(it.id, it.rank)) }
                newItems.shuffle()
                calculateDiffAndUpdate(newItems)
            }

            R.id.btnUpdateAll -> {
                val newItems = ArrayList<RankItem>()
                adapter.getItems().forEach { newItems.add(RankItem(it.id, it.rank + 1)) }
                calculateDiffAndUpdate(newItems)
            }
        }
    }

    private fun calculateDiffAndUpdate(newItems: List<RankItem>) {
        AppExecutors.get()
            .diskIO()
            .execute {
                val result = diffHelper.calculateDiff(newItems)
                if (isAdded && !isRemoving) {
                    dispatchUpdates(result)
                }
            }
    }

    private fun dispatchUpdates(diffResult: DiffUtil.DiffResult) {
        AppExecutors.get()
            .mainThread()
            .execute {
                diffHelper.dispatchUpdates(diffResult, clearSelections = false)
            }
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..idCount) {
            items.add(RankItem(i, i))
        }
        return items
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
