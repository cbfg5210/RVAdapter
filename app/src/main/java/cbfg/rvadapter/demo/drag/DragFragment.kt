package cbfg.rvadapter.demo.drag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.FragmentDragBinding
import cbfg.rvadapter.entity.RankItem

class DragFragment : Fragment(R.layout.fragment_drag), View.OnClickListener {
    private var _binding: FragmentDragBinding? = null
    private val binding: FragmentDragBinding
        get() = _binding!!

    private lateinit var adapter: RVAdapter<RankItem>
    private lateinit var dragHelper: DragHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RVAdapter<RankItem>(view.context, DragVHFactory())
            .bind(binding.rvTest)
            .setItems(getItems())

        dragHelper = DragHelper(adapter)
        dragHelper.bindRecyclerView(binding.rvTest)
        dragHelper.setDragBgRes(0, R.color.lightGray)

        binding.btnLinear.setOnClickListener(this)
        binding.btnGrid.setOnClickListener(this)
        binding.btnEnableDrag.setOnClickListener(this)
        binding.btnDisableDrag.setOnClickListener(this)
        binding.btnNotify.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnEnableDrag -> {
                dragHelper.setDraggable(true)
            }
            R.id.btnDisableDrag -> {
                dragHelper.setDraggable(false)
            }
            R.id.btnNotify -> {
                adapter.notifyDataSetChanged()
            }
            R.id.btnLinear -> {
                binding.rvTest.layoutManager = LinearLayoutManager(context)
            }
            R.id.btnGrid -> {
                val gridManager = GridLayoutManager(context, RecyclerView.VERTICAL)
                gridManager.spanCount = 3
                binding.rvTest.layoutManager = gridManager
            }
        }
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
