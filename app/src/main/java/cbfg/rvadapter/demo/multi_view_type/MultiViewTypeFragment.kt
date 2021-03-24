package cbfg.rvadapter.demo.multi_view_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cbfg.rvadapter.DataHelper
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.demo.databinding.FragmentListCommonBinding
import cbfg.rvadapter.entity.Header
import cbfg.rvadapter.entity.Person

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/27 14:58
 * 功能描述:
 */
class MultiViewTypeFragment : Fragment() {
    private var _binding: FragmentListCommonBinding? = null
    private val binding: FragmentListCommonBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCommonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RVAdapter<Any>(view.context, MultiViewTypeVHFactory())
            .bind(binding.rvList)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}