package com.past.music.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.pastmusic.R

/**
 * Creator：gaojin
 * date：2017/11/6 下午8:44
 */
class KtDownLoadFragment : KtUiBaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.activity_download
    }

    val names = arrayOf("已下载", "正在下载")

    internal var mAdapter: FragmentAdapter? = null

    var mTabLayout: TabLayout? = null

    var mViewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        setTitle("下载歌曲")
        mTabLayout = view.findViewById(R.id.local_tab_layout)
        mViewPager = view.findViewById(R.id.local_view_pager)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mTabLayout!!.tabGravity = TabLayout.GRAVITY_CENTER
        val fm = activity.supportFragmentManager
        mAdapter = FragmentAdapter(fm)
        mViewPager!!.adapter = mAdapter
        mTabLayout!!.setupWithViewPager(mViewPager)
    }

    internal inner class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                DownMusicFragment.newInstance("/storage/emulated/0/pastmusic", false, null)
            } else {
                DownFragment.newInstance()
            }

        }

        override fun getCount(): Int {
            return 2
        }

        //此方法用来显示tab上的名字
        override fun getPageTitle(position: Int): CharSequence {
            return names[position % 2]
        }
    }

    companion object {
        fun newInstance(): KtDownLoadFragment {
            return KtDownLoadFragment()
        }
    }
}