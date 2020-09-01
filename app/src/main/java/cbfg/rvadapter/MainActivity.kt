package cbfg.rvadapter

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cbfg.rvadapter.demo.R
import cbfg.rvadapter.demo.multi_view_type.MultiViewTypeFragment
import cbfg.rvadapter.demo.select_multi.MultiSelectFragment
import cbfg.rvadapter.demo.select_single.SingleSelectFragment
import cbfg.rvadapter.demo.simple.SimpleFragment

class MainActivity : AppCompatActivity() {
    private var currentMenuItemId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(R.id.actionSimple)
    }

    private fun showFragment(menuItemId: Int) {
        if (menuItemId == currentMenuItemId) {
            return
        }

        val fragment = when (menuItemId) {
            R.id.actionSimple -> SimpleFragment()
            R.id.actionMultiViewType -> MultiViewTypeFragment()
            R.id.actionSingleSelect -> SingleSelectFragment()
            R.id.actionMultiSelect -> MultiSelectFragment()
            else -> return
        }

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showFragment(item.itemId)
        return true
    }
}