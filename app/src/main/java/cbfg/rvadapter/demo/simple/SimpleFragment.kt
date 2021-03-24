package cbfg.rvadapter.demo.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cbfg.rvadapter.DataHelper
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.FragmentListSimpleBinding
import cbfg.rvadapter.entity.Person

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/8/26 15:58
 * 功能描述: 单 view type，点击、长按
 */
class SimpleFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentListSimpleBinding? = null
    private val binding: FragmentListSimpleBinding
        get() = _binding!!

    private lateinit var adapter: RVAdapter<Person>
    private var idCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListSimpleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RVAdapter<Person>(view.context, SimpleVHFactory())
            .bind(binding.rvTest)
            .setItems(
                DataHelper.getPeople(),
                clearSelections = false,
                needNotify = false
            )
            .setItemClickListener { v, item, position ->
                /**
                 * 点击头像
                 */
                if (v.id == R.id.ivAvatar) {
                    val tip = "click avatar ${item.name} - $position"
                    Toast.makeText(view.context, tip, Toast.LENGTH_SHORT).show()
                }
                /**
                 * 点击 item
                 */
                val tip = "click item ${item.name} - $position"
                Toast.makeText(view.context, tip, Toast.LENGTH_SHORT).show()
            }
            .setItemLongClickListener { _, item, position ->
                val tip = "long click ${item.name} - $position"
                Toast.makeText(view.context, tip, Toast.LENGTH_SHORT).show()
            }

        binding.btnTopAdd.setOnClickListener(this)
        binding.btnTopRemove.setOnClickListener(this)
        binding.btnBottomAdd.setOnClickListener(this)
        binding.btnBottomRemove.setOnClickListener(this)

        idCount = adapter.getRealItemCount()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTopAdd -> {
                adapter.add(0, Person(R.color.lightGray, "gen-${++idCount}"))
            }
            R.id.btnTopRemove -> {
                if (adapter.getRealItemCount() > 0) {
                    adapter.removeAt(0)
                }
            }
            R.id.btnBottomAdd -> {
                adapter.add(
                    adapter.getRealItemCount(),
                    Person(R.color.lightGray, "gen-${++idCount}")
                )
            }
            R.id.btnBottomRemove -> {
                if (adapter.getRealItemCount() > 0) {
                    adapter.removeAt(adapter.getRealItemCount() - 1)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}