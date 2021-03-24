package cbfg.rvadapter.demo.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cbfg.rvadapter.DataHelper
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.databinding.FragmentStateBinding
import cbfg.rvadapter.demo.simple.SimpleVHFactory
import cbfg.rvadapter.entity.Person

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/6/1 10:51
 * 功能描述:
 */
class StateFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentStateBinding? = null
    private val binding: FragmentStateBinding
        get() = _binding!!

    private lateinit var adapter: RVAdapter<Person>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoading.setOnClickListener(this)
        binding.btnEmpty.setOnClickListener(this)
        binding.btnError.setOnClickListener(this)
        binding.btnSetData.setOnClickListener(this)
        binding.btnClearData.setOnClickListener(this)

        adapter = RVAdapter<Person>(view.context, SimpleVHFactory())
            .bind(binding.rvList)
            .setStateHolderFactory(StateVHFactory())
            //.setStateHolderFactory(StateVHFactory(), autoShowEmptyState = false)
            .setStateClickListener { _, item, position ->
                Toast.makeText(
                    view.context,
                    "item = $item,position = $position",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setStateLongClickListener { _, item, position ->
                Toast.makeText(
                    view.context,
                    "long click,item = $item,position = $position",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLoading -> {
                showStatePage(RVState.State.LOADING, 0, "正在努力加载...${System.currentTimeMillis()}")
            }
            R.id.btnEmpty -> {
                showStatePage(
                    RVState.State.EMPTY,
                    R.color.colorAccent,
                    "没有数据喔~${System.currentTimeMillis()}"
                )
            }
            R.id.btnError -> {
                showStatePage(
                    RVState.State.ERROR,
                    R.color.colorPrimary,
                    "出错啦！${System.currentTimeMillis()}"
                )
            }
            R.id.btnSetData -> {
                adapter.setItems(DataHelper.getPeople())
            }
            R.id.btnClearData -> {
                adapter.clear()
            }
        }
    }

    private fun showStatePage(state: RVState.State, imgSrc: Int, tip: String) {
        adapter.showStatePage(RVState(state, imgSrc, tip), state == RVState.State.EMPTY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}