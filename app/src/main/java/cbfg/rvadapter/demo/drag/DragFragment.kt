package cbfg.rvadapter.demo.drag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.RankItem
import kotlinx.android.synthetic.main.fragment_drag.*

class DragFragment : Fragment(R.layout.fragment_drag), View.OnClickListener {
    private lateinit var adapter: RVAdapter<RankItem>
    private lateinit var dragHelper: DragHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RVAdapter<RankItem>(view.context, DragVHFactory())
            .bindRecyclerView(rvTest)
            .setItems(getItems())

        dragHelper = DragHelper(adapter)
        dragHelper.bindRecyclerView(rvTest)
        dragHelper.setDragBgRes(0, R.color.lightGray)

        btnLinear.setOnClickListener(this)
        btnGrid.setOnClickListener(this)
        btnEnableDrag.setOnClickListener(this)
        btnDisableDrag.setOnClickListener(this)
        btnNotify.setOnClickListener(this)
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
                rvTest.layoutManager = LinearLayoutManager(context)
            }
            R.id.btnGrid -> {
                val gridManager = GridLayoutManager(context, RecyclerView.VERTICAL)
                gridManager.spanCount = 3
                rvTest.layoutManager = gridManager
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
}
