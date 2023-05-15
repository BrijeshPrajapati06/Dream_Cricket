package com.diyantech.dreamcricket

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.TooltipCompat
import androidx.viewpager2.widget.ViewPager2
import com.diyantech.dreamcricket.activity.AddMatch
import com.diyantech.dreamcricket.adapter.TabViewPagerAdapter
import com.diyantech.dreamcricket.fragment.MatchFragment
import com.diyantech.dreamcricket.fragment.PlayerFragment
import com.diyantech.dreamcricket.fragment.TeamFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        viewPager?.isSaveEnabled = false

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.red)

        // Stop The Tooltip...
        for (i in 0 until tabLayout!!.tabCount) {
            tabLayout!!.getTabAt(i)?.view?.let { tabView ->
                TooltipCompat.setTooltipText(tabView, null)
            }
        }

        // Stop The RippleEffect...
        tabLayout!!.tabRippleColor = null

        setupViewPager()
        setupTabLayout()
    }

    /* private fun showAddMatchDialog() {
         val dialog = Dialog(this)
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
         dialog.setContentView(R.layout.add_match_dialog)
         dialog.setCancelable(true)

         var date = dialog.findViewById<TextView>(R.id.txtSetDate)
         var celender = dialog.findViewById<TextView>(R.id.txtCelender)



         // create an OnDateSetListener
         val dateSetListener = object : DatePickerDialog.OnDateSetListener {
             override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                    dayOfMonth: Int) {
                 cal.set(Calendar.YEAR, year)
                 cal.set(Calendar.MONTH, monthOfYear)
                 cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                 val myFormat = "MM/dd/yyyy" // mention the format you need
                 val sdf = SimpleDateFormat(myFormat, Locale.US                      )
                 date.text = sdf.format(cal.getTime())
             }
         }

         // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
         celender!!.setOnClickListener(object : View.OnClickListener {
             override fun onClick(view: View) {
                 DatePickerDialog(this@MainActivity,
                     dateSetListener,
                     // set DatePickerDialog to point to today's date when it loads up
                     cal.get(Calendar.YEAR),
                     cal.get(Calendar.MONTH),
                     cal.get(Calendar.DAY_OF_MONTH)).show()
             }

         })

         val lp = WindowManager.LayoutParams()
         lp.copyFrom(dialog.window!!.attributes)
         lp.width = WindowManager.LayoutParams.MATCH_PARENT
         lp.height = WindowManager.LayoutParams.WRAP_CONTENT
         dialog.window!!.attributes = lp
         dialog.show()
     }*/
    @SuppressLint("NotifyDataSetChanged")
    private fun setupTabLayout() {
        val adapter = TabViewPagerAdapter(this@MainActivity, supportFragmentManager, 3, lifecycle)
        adapter.addFragment(MatchFragment(), getString(R.string.match))
        adapter.addFragment(TeamFragment(), getString(R.string.team))
        adapter.addFragment(PlayerFragment(), getString(R.string.player))
        adapter.notifyDataSetChanged()

        viewPager?.adapter = adapter
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = adapter.getPageTitle(position)
            viewPager?.setCurrentItem(tab.position, true)
        }.attach()


    }

    private fun setupViewPager() {
        val adapter = TabViewPagerAdapter(
            this@MainActivity,
            supportFragmentManager,
            tabLayout!!.tabCount,
            lifecycle
        )
        viewPager?.adapter = adapter


    }

    override fun onBackPressed() {
        val viewPager = viewPager
        if (viewPager?.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
    }

}
