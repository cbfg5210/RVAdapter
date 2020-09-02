package cbfg.rvadapter.demo.simple

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import cbfg.rvadapter.DataHelper
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.Person
import kotlinx.android.synthetic.main.fragment_list_common.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 15:58
 * 功能描述: 单 view type，点击、长按
 */
class SimpleFragment : Fragment(R.layout.fragment_list_common) {
    private lateinit var adapter: RVAdapter<Person>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RVAdapter<Person>(view.context, SimpleVHFactory())
            .bindRecyclerView(rvList)
            .setItems(DataHelper.getPeople())
            .setItemClickListener { v, item, position ->
                /**
                 * 点击头像
                 */
                if (v.id == R.id.ivAvatar) {
                    Toast.makeText(
                        view.context,
                        "click avatar ${item.name} - $position",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                /**
                 * 点击 item
                 */
                Toast.makeText(
                    view.context,
                    "click item ${item.name} - $position",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setItemLongClickListener { _, item, position ->
                Toast.makeText(
                    view.context,
                    "long click ${item.name} - $position",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}