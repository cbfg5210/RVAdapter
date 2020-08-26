package cbfg.rvadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cbfg.rvadapter.demo.SimpleFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment()
    }

    private fun showFragment() {
        val fragment = SimpleFragment()

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
    }
}