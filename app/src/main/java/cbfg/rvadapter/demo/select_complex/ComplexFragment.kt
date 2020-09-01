package cbfg.rvadapter.demo.select_complex

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.SelectStrategy
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.entity.CommodityItem
import cbfg.rvadapter.entity.ShopItem
import kotlinx.android.synthetic.main.fragment_complex.*

class ComplexFragment : Fragment(R.layout.fragment_complex) {
    private lateinit var adapter: RVAdapter<Any>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RVAdapter<Any>(view.context, ComplexVHFactory())
            .bindRecyclerView(rvTest)
            .setSelectable(
                ShopItem::class.java,
                SelectStrategy.MULTI_SELECTABLE,
                clearItsSelections = false,
                needNotify = false
            )
            .setSelectable(
                CommodityItem::class.java,
                SelectStrategy.MULTI_SELECTABLE,
                clearItsSelections = false,
                needNotify = false
            )
            .setItems(getItems())
            .setItemClickListener { _, item, position ->
                if (item is ShopItem) {
                    onShopClick(item, position)
                } else {
                    onCommodityClick(position)
                }
                count()
            }
    }

    /**
     * 点击了店铺，全选/取消全选 该店铺商品
     */
    private fun onShopClick(shopItem: ShopItem, position: Int) {
        val items = adapter.getItems()
        //全选
        if (adapter.getSelections().contains(shopItem)) {
            for (j in position + 1 until adapter.itemCount) {
                if (items[j] is CommodityItem) {
                    adapter.selectAt(j)
                } else {
                    break
                }
            }
        } else {
            for (j in position + 1 until adapter.itemCount) {
                if (items[j] is CommodityItem) {
                    adapter.deselectAt(j)
                } else {
                    break
                }
            }
        }
    }

    /**
     * 点击了商品，如果该店铺下不是所有的商品都选中，要取消全选；如果该店铺下所有的商品都选中了，要设为全选
     */
    private fun onCommodityClick(position: Int) {
        val items = adapter.getItems()
        var shopItem: ShopItem? = null

        for (i in (position - 1) downTo 0) {
            val item = items[i]
            if (item is ShopItem) {
                shopItem = item
                break
            }
        }

        shopItem ?: return

        val selections = adapter.getSelections()
        val shopIndex = items.indexOf(shopItem)
        var isAllSelected = true

        //判断该商店的商品是否全部被选中
        for (i in (shopIndex + 1) until items.size) {
            val item = items[i]
            if (item !is CommodityItem) {
                break
            }
            if (!selections.contains(item)) {
                isAllSelected = false
                break
            }
        }

        //商店目前是选中状态
        if (selections.contains(shopItem)) {
            //如果该商店的商品不是全部被选中，要取消商店的选中
            if (!isAllSelected) {
                adapter.deselect(shopItem)
            }
        } else if (isAllSelected) {
            //商店目前不是选中状态
            //如果该商店的商品被全部选中，要设置商店的选中
            adapter.select(shopItem)
        }
    }

    /**
     * 计算金额
     */
    private fun count() {
        var count = 0F
        adapter.getSelections().forEach { selection ->
            if (selection is CommodityItem) {
                count += selection.price
            }
        }
        tvCount.text = String.format("总计：¥ %.2f", count)
    }

    private fun getItems(): List<Any> {
        return arrayListOf(
            ShopItem(R.mipmap.ic_launcher_round, "依依得衣服装店"),
            CommodityItem(R.mipmap.ic_launcher, "长袖连衣裙", 95F),
            CommodityItem(R.mipmap.ic_launcher, "衬衫裙", 85F),
            CommodityItem(R.mipmap.ic_launcher, "背带裤", 40F),
            ShopItem(R.mipmap.ic_launcher_round, "小米官方商店"),
            CommodityItem(R.mipmap.ic_launcher, "小米手机", 899F),
            CommodityItem(R.mipmap.ic_launcher, "充电宝", 50F),
            ShopItem(R.mipmap.ic_launcher_round, "乐淘百货"),
            CommodityItem(R.mipmap.ic_launcher, "32包竹浆本色抽纸", 27.9F),
            CommodityItem(R.mipmap.ic_launcher, "毛毛虫童鞋", 79.9F),
            CommodityItem(R.mipmap.ic_launcher, "花生酥516g", 32.8F)
        )
    }
}
