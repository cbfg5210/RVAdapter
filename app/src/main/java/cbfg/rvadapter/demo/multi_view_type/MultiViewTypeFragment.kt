package cbfg.rvadapter.demo.multi_view_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cbfg.rvadapter.DataHelper
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.entity.Header
import cbfg.rvadapter.entity.Person
import kotlinx.android.synthetic.main.fragment_list_common.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/27 14:58
 * 功能描述:
 */
class MultiViewTypeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_common, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RVAdapter<Any>(view.context, MultiViewTypeVHFactory())
            .bind(rvList)
            .setItems(DataHelper.getClassifiedPeople())
            .setItemClickListener { _, item, position ->
                if (item is Header) {
                    Toast.makeText(
                        view.context,
                        "click ${item.txt} - $position",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    item as Person
                    Toast.makeText(
                        view.context,
                        "click ${item.name} - $position",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}