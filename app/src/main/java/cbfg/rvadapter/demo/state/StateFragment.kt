package cbfg.rvadapter.demo.state

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import cbfg.rvadapter.DataHelper
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.simple.SimpleVHFactory
import cbfg.rvadapter.entity.Person
import kotlinx.android.synthetic.main.fragment_state.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/6/1 10:51
 * 功能描述:
 */
class StateFragment : Fragment(R.layout.fragment_state), View.OnClickListener {
    private lateinit var adapter: RVAdapter<Person>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLoading.setOnClickListener(this)
        btnEmpty.setOnClickListener(this)
        btnError.setOnClickListener(this)
        btnSetData.setOnClickListener(this)
        btnClearData.setOnClickListener(this)

        adapter = RVAdapter<Person>(view.context, SimpleVHFactory())
            .bindRecyclerView(rvList)
            .setStateHolderFactory(StateVHFactory())
            .setStateClickListener { _, item, position ->
                Toast.makeText(
                    view.context,
                    "item = $item,position = $position",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLoading -> {
                adapter.showStatePage(RVState(RVState.State.LOADING, 0, "Loading..."))
            }
            R.id.btnEmpty -> {
                adapter.showStatePage(RVState(RVState.State.EMPTY, 0, "No data!"), true)
            }
            R.id.btnError -> {
                adapter.showStatePage(RVState(RVState.State.ERROR, 0, "Error!"))
            }
            R.id.btnSetData -> {
                adapter.setItems(DataHelper.getPeople())
            }
            R.id.btnClearData -> {
                adapter.clear()
            }
        }
    }
}