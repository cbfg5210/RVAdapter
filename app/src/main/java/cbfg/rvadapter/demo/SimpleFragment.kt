package cbfg.rvadapter.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cbfg.rvadapter.DataHelper
import cbfg.rvadapter.R
import cbfg.rvadapter.RVAdapter
import kotlinx.android.synthetic.main.fragment_list_common.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 15:58
 * 功能描述: 单 view type，点击、长按
 */
class SimpleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_common, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RVAdapter<String>(view.context, TextVHFactory())
            .bindRecyclerView(rvList)
            .setItems(DataHelper.getTextList())
            .setItemClickListener { _, item, position ->
                Toast.makeText(view.context, "click $item - $position", Toast.LENGTH_SHORT).show()
            }
            .setItemLongClickListener { _, item, position ->
                Toast.makeText(view.context, "long click $item - $position", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}