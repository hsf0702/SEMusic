package com.se.music.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.*
import android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP
import android.support.design.widget.TabLayout
import android.support.v4.util.Pools
import android.support.v4.view.*
import android.support.v4.view.ViewPager.*
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.ActionBar
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.TooltipCompat
import android.text.Layout
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.se.music.R
import com.se.music.utils.AnimationUtils
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.ref.WeakReference
import java.util.*


/**
 *Author: gaojin
 *Time: 2018/5/22 下午7:33
 */

class TitleZoomTabLayout : FrameLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isHorizontalScrollBarEnabled = false

        // Add the TabStrip
        mTabStrip = SlidingTabStrip(context)
        super.addView(mTabStrip, 0, LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT))

        val a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                defStyleAttr, R.style.Widget_Design_TabLayout)

        mTabStrip.setSelectedIndicatorHeight(
                a.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, 0))
        mTabStrip.setSelectedIndicatorColor(a.getColor(R.styleable.TabLayout_tabIndicatorColor, 0))

        mTabPaddingBottom = a
                .getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0)
        mTabPaddingEnd = mTabPaddingBottom
        mTabPaddingTop = mTabPaddingEnd
        mTabPaddingStart = mTabPaddingTop
        mTabPaddingStart = a.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart,
                mTabPaddingStart)
        mTabPaddingTop = a.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop,
                mTabPaddingTop)
        mTabPaddingEnd = a.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd,
                mTabPaddingEnd)
        mTabPaddingBottom = a.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom,
                mTabPaddingBottom)

        mTabTextAppearance = a.getResourceId(R.styleable.TabLayout_tabTextAppearance,
                R.style.TextAppearance_Design_Tab)

        // Text colors/sizes come from the text appearance first
        val ta = context.obtainStyledAttributes(mTabTextAppearance,
                android.support.v7.appcompat.R.styleable.TextAppearance)
        try {
            mTabTextSize = ta.getDimensionPixelSize(
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, 0).toFloat()
            mTabTextColors = ta.getColorStateList(
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)
        } finally {
            ta.recycle()
        }

        if (a.hasValue(R.styleable.TabLayout_tabTextColor)) {
            // If we have an explicit text color set, use it instead
            mTabTextColors = a.getColorStateList(R.styleable.TabLayout_tabTextColor)
        }

        if (a.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
            // We have an explicit selected text color set, so we need to make merge it with the
            // current colors. This is exposed so that developers can use theme attributes to set
            // this (theme attrs in ColorStateLists are Lollipop+)
            val selected = a.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0)
            mTabTextColors = createColorStateList(mTabTextColors!!.getDefaultColor(), selected)
        }

        mRequestedTabMinWidth = a.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth,
                INVALID_WIDTH)
        mRequestedTabMaxWidth = a.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth,
                INVALID_WIDTH)
        mTabBackgroundResId = a.getResourceId(R.styleable.TabLayout_tabBackground, 0)
        mContentInsetStart = a.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0)
        mMode = a.getInt(R.styleable.TabLayout_tabMode, MODE_FIXED)
        mTabGravity = a.getInt(R.styleable.TabLayout_tabGravity, GRAVITY_FILL)
        a.recycle()

        // TODO add attr for these
        val res = resources
        mTabTextMultiLineSize = res.getDimensionPixelSize(R.dimen.design_tab_text_size_2line).toFloat()
        mScrollableTabMinWidth = res.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width)

        // Now apply the tab mode and gravity
        applyModeAndGravity()
    }

    companion object {
        /**
         * Scrollable tabs display a subset of tabs at any given moment, and can contain longer tab
         * labels and a larger number of tabs. They are best used for browsing contexts in touch
         * interfaces when users don’t need to directly compare the tab labels.
         *
         * @see .setTabMode
         * @see .getTabMode
         */
        val MODE_SCROLLABLE = 0
        /**
         * Fixed tabs display all tabs concurrently and are best used with content that benefits from
         * quick pivots between tabs. The maximum number of tabs is limited by the view’s width.
         * Fixed tabs have equal width, based on the widest tab label.
         *
         * @see .setTabMode
         * @see .getTabMode
         */
        val MODE_FIXED = 1
        /**
         * Gravity used to fill the [TabLayout] as much as possible. This option only takes effect
         * when used with [.MODE_FIXED].
         *
         * @see .setTabGravity
         * @see .getTabGravity
         */
        val GRAVITY_FILL = 0
        /**
         * Gravity used to lay out the tabs in the center of the [TabLayout].
         *
         * @see .setTabGravity
         * @see .getTabGravity
         */
        val GRAVITY_CENTER = 1
    }

    internal val DEFAULT_GAP_TEXT_ICON = 8 // dps
    internal val FIXED_WRAP_GUTTER_MIN = 16 //dps
    internal val MOTION_NON_ADJACENT_OFFSET = 24
    private val DEFAULT_HEIGHT_WITH_TEXT_ICON = 72 // dps
    private val INVALID_WIDTH = -1
    private val DEFAULT_HEIGHT = 48 // dps
    private val TAB_MIN_WIDTH_MARGIN = 56 //dps
    private val ANIMATION_DURATION = 300
    private val sTabPool = Pools.SynchronizedPool<Tab>(16)
    internal var mTabBackgroundResId: Int
    private val mTabs = ArrayList<Tab>()
    private var mTabStrip: SlidingTabStrip
    private var mRequestedTabMinWidth: Int
    private var mRequestedTabMaxWidth: Int
    private var mScrollableTabMinWidth: Int
    private val mSelectedListeners = ArrayList<OnTabSelectedListener>()
    // Pool we use as a simple RecyclerBin
    private val mTabViewPool = Pools.SimplePool<TabView>(12)
    internal var mTabPaddingStart: Int = 0
    internal var mTabPaddingTop: Int = 0
    internal var mTabPaddingEnd: Int = 0
    internal var mTabPaddingBottom: Int = 0
    internal var mTabTextAppearance: Int = 0
    internal var mTabTextColors: ColorStateList? = null
    internal var mTabTextSize: Float = 0.toFloat()
    internal var mTabBigTextSize: Float = 0.toFloat()
    internal var mTabTextMultiLineSize: Float = 0.toFloat()
    internal var mTabMaxWidth = Integer.MAX_VALUE
    internal var mTabGravity: Int = 0
    internal var mMode: Int = 0
    internal var mViewPager: ViewPager? = null
    private var mSelectedTab: Tab? = null
    private var updateOnMeasure = true
    private var mContentInsetStart: Int
    private var mSelectedListener: OnTabSelectedListener? = null
    private var mCurrentVpSelectedListener: OnTabSelectedListener? = null
    private var mScrollAnimator: ValueAnimator? = null
    private var mPagerAdapter: PagerAdapter? = null
    private var mPagerAdapterObserver: DataSetObserver? = null
    private var mPageChangeListener: TabLayoutOnPageChangeListener? = null
    private var mAdapterChangeListener: AdapterChangeListener? = null
    private var mSetupViewPagerImplicitly: Boolean = false

    private fun createColorStateList(defaultColor: Int, selectedColor: Int): ColorStateList {
        val states = arrayOfNulls<IntArray>(2)
        val colors = IntArray(2)
        var i = 0

        states[i] = View.SELECTED_STATE_SET
        colors[i] = selectedColor
        i++

        // Default enabled state
        states[i] = View.EMPTY_STATE_SET
        colors[i] = defaultColor
        return ColorStateList(states, colors)
    }

    fun selectTextAppend(size: Float) {
        mTabBigTextSize = mTabTextSize + size
    }

    /**
     * Sets the tab indicator's color for the currently selected tab.
     *
     * @param color color to use for the indicator
     * @attr ref android.support.design.R.styleable#TabLayout_tabIndicatorColor
     */
    fun setSelectedTabIndicatorColor(@ColorInt color: Int) {
        mTabStrip.setSelectedIndicatorColor(color)
    }

    /**
     * Sets the tab indicator's height for the currently selected tab.
     *
     * @param height height to use for the indicator in pixels
     * @attr ref android.support.design.R.styleable#TabLayout_tabIndicatorHeight
     */
    fun setSelectedTabIndicatorHeight(height: Int) {
        mTabStrip.setSelectedIndicatorHeight(height)
    }

    /**
     * Set the scroll position of the tabs. This is useful for when the tabs are being displayed as
     * part of a scrolling container such as [android.support.v4.view.ViewPager].
     *
     *
     * Calling this method does not update the selected tab, it is only used for drawing purposes.
     *
     * @param position           current scroll position
     * @param positionOffset     Value from [0, 1) indicating the offset from `position`.
     * @param updateSelectedText Whether to update the text's selected state.
     */
    fun setScrollPosition(position: Int, positionOffset: Float, updateSelectedText: Boolean) {
        setScrollPosition(position, positionOffset, updateSelectedText, true)
    }

    internal fun setScrollPosition(position: Int, positionOffset: Float, updateSelectedText: Boolean,
                                   updateIndicatorPosition: Boolean) {
        val roundedPosition = Math.round(position + positionOffset)
        if (roundedPosition < 0 || roundedPosition >= mTabStrip.getChildCount()) {
            return
        }

        // Set the indicator position, if enabled
        if (updateIndicatorPosition) {
            mTabStrip.setIndicatorPositionFromTabPosition(position, positionOffset)
        }

        // Now update the scroll position, canceling any running animation
        if (mScrollAnimator != null && mScrollAnimator!!.isRunning()) {
            mScrollAnimator!!.cancel()
        }
        scrollTo(calculateScrollXForTab(position, positionOffset), 0)

        // Update the 'selected state' view as we scroll, if enabled
        if (updateSelectedText) {
            setSelectedTabView(roundedPosition)
        }
    }

    private fun getScrollPosition(): Float {
        return mTabStrip.indicatorPosition
    }

    /**
     * Add a tab to this layout. The tab will be added at the end of the list.
     * If this is the first tab to be added it will become the selected tab.
     *
     * @param tab Tab to add
     */
    fun addTab(tab: Tab) {
        addTab(tab, mTabs.isEmpty())
    }

    /**
     * Add a tab to this layout. The tab will be inserted at `position`.
     * If this is the first tab to be added it will become the selected tab.
     *
     * @param tab      The tab to add
     * @param position The new position of the tab
     */
    fun addTab(tab: Tab, position: Int) {
        addTab(tab, position, mTabs.isEmpty())
    }

    /**
     * Add a tab to this layout. The tab will be added at the end of the list.
     *
     * @param tab         Tab to add
     * @param setSelected True if the added tab should become the selected tab.
     */
    fun addTab(tab: Tab, setSelected: Boolean) {
        addTab(tab, mTabs.size, setSelected)
    }

    /**
     * Add a tab to this layout. The tab will be inserted at `position`.
     *
     * @param tab         The tab to add
     * @param position    The new position of the tab
     * @param setSelected True if the added tab should become the selected tab.
     */
    fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        if (tab.mParent !== this) {
            throw IllegalArgumentException("Tab belongs to a different TabLayout.")
        }
        configureTab(tab, position)
        addTabView(tab)

        if (setSelected) {
            tab.select()
        }
    }


    @Deprecated("Use {@link #addOnTabSelectedListener(OnTabSelectedListener)} and\n" +
            "      {@link #removeOnTabSelectedListener(OnTabSelectedListener)}.")
    fun setOnTabSelectedListener(listener: OnTabSelectedListener?) {
        // The logic in this method emulates what we had before support for multiple
        // registered listeners.
        if (mSelectedListener != null) {
            removeOnTabSelectedListener(mSelectedListener!!)
        }
        // Update the deprecated field so that we can remove the passed listener the next
        // time we're called
        mSelectedListener = listener
        if (listener != null) {
            addOnTabSelectedListener(listener)
        }
    }

    /**
     * Add a [TabLayout.OnTabSelectedListener] that will be invoked when tab selection
     * changes.
     *
     *
     *
     * Components that add a listener should take care to remove it when finished via
     * [.removeOnTabSelectedListener].
     *
     * @param listener listener to add
     */
    fun addOnTabSelectedListener(listener: OnTabSelectedListener) {
        if (!mSelectedListeners.contains(listener)) {
            mSelectedListeners.add(listener)
        }
    }

    /**
     * Remove the given [TabLayout.OnTabSelectedListener] that was previously added via
     * [.addOnTabSelectedListener].
     *
     * @param listener listener to remove
     */
    fun removeOnTabSelectedListener(listener: OnTabSelectedListener) {
        mSelectedListeners.remove(listener)
    }

    /**
     * Remove all previously added [TabLayout.OnTabSelectedListener]s.
     */
    fun clearOnTabSelectedListeners() {
        mSelectedListeners.clear()
    }

    /**
     * Create and return a new [Tab]. You need to manually add this using
     * [.addTab] or a related method.
     *
     * @return A new Tab
     * @see .addTab
     */
    fun newTab(): Tab {
        var tab = sTabPool.acquire()
        if (tab == null) {
            tab = Tab()
        }
        tab.mParent = this
        tab.mView = createTabView(tab)
        return tab
    }

    /**
     * Returns the number of tabs currently registered with the action bar.
     *
     * @return Tab count
     */
    fun getTabCount(): Int {
        return mTabs.size
    }

    /**
     * Returns the tab at the specified index.
     */
    fun getTabAt(index: Int): Tab? {
        return if (index < 0 || index >= getTabCount()) null else mTabs[index]
    }

    /**
     * Returns the position of the current selected tab.
     *
     * @return selected tab position, or `-1` if there isn't a selected tab.
     */
    fun getSelectedTabPosition(): Int {
        return if (mSelectedTab != null) mSelectedTab!!.position else -1
    }

    /**
     * Remove a tab from the layout. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param tab The tab to remove
     */
    fun removeTab(tab: Tab) {
        if (tab.mParent !== this) {
            throw IllegalArgumentException("Tab does not belong to this TabLayout.")
        }

        removeTabAt(tab.position)
    }

    /**
     * Remove a tab from the layout. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param position Position of the tab to remove
     */
    fun removeTabAt(position: Int) {
        val selectedTabPosition = if (mSelectedTab != null) mSelectedTab!!.position else 0
        removeTabViewAt(position)

        val removedTab = mTabs.removeAt(position)
        removedTab.reset()
        sTabPool.release(removedTab)

        val newTabCount = mTabs.size
        for (i in position until newTabCount) {
            mTabs[i].position = i
        }

        if (selectedTabPosition == position) {
            selectTab(if (mTabs.isEmpty()) null else mTabs[Math.max(0, position - 1)])
        }
    }

    /**
     * Remove all tabs from the action bar and deselect the current tab.
     */
    fun removeAllTabs() {
        // Remove all the views
        for (i in mTabStrip.getChildCount() - 1 downTo 0) {
            removeTabViewAt(i)
        }

        val i = mTabs.iterator()
        while (i.hasNext()) {
            val tab = i.next()
            i.remove()
            tab.reset()
            sTabPool.release(tab)
        }

        mSelectedTab = null
    }

    /**
     * Returns the current mode used by this [TabLayout].
     *
     * @see .setTabMode
     */
    @Mode
    fun getTabMode(): Int {
        return mMode
    }

    /**
     * Set the behavior mode for the Tabs in this layout. The valid input options are:
     *
     *  * [.MODE_FIXED]: Fixed tabs display all tabs concurrently and are best used
     * with content that benefits from quick pivots between tabs.
     *  * [.MODE_SCROLLABLE]: Scrollable tabs display a subset of tabs at any given moment,
     * and can contain longer tab labels and a larger number of tabs. They are best used for
     * browsing contexts in touch interfaces when users don’t need to directly compare the tab
     * labels. This mode is commonly used with a [android.support.v4.view.ViewPager].
     *
     *
     * @param mode one of [.MODE_FIXED] or [.MODE_SCROLLABLE].
     * @attr ref android.support.design.R.styleable#TabLayout_tabMode
     */
    fun setTabMode(@Mode mode: Int) {
        if (mode != mMode) {
            mMode = mode
            applyModeAndGravity()
        }
    }

    /**
     * The current gravity used for laying out tabs.
     *
     * @return one of [.GRAVITY_CENTER] or [.GRAVITY_FILL].
     */
    @TabGravity
    fun getTabGravity(): Int {
        return mTabGravity
    }

    /**
     * Set the gravity to use when laying out the tabs.
     *
     * @param gravity one of [.GRAVITY_CENTER] or [.GRAVITY_FILL].
     * @attr ref android.support.design.R.styleable#TabLayout_tabGravity
     */
    fun setTabGravity(@TabGravity gravity: Int) {
        if (mTabGravity != gravity) {
            mTabGravity = gravity
            applyModeAndGravity()
        }
    }

    /**
     * Gets the text colors for the different states (normal, selected) used for the tabs.
     */
    fun getTabTextColors(): ColorStateList? {
        return mTabTextColors
    }

    /**
     * Sets the text colors for the different states (normal, selected) used for the tabs.
     *
     * @see .getTabTextColors
     */
    fun setTabTextColors(textColor: ColorStateList?) {
        if (mTabTextColors !== textColor) {
            mTabTextColors = textColor
            updateAllTabs()
        }
    }

    /**
     * Sets the text colors for the different states (normal, selected) used for the tabs.
     *
     * @attr ref android.support.design.R.styleable#TabLayout_tabTextColor
     * @attr ref android.support.design.R.styleable#TabLayout_tabSelectedTextColor
     */
    fun setTabTextColors(normalColor: Int, selectedColor: Int) {
        setTabTextColors(createColorStateList(normalColor, selectedColor))
    }

    /**
     * The one-stop shop for setting up this [TabLayout] with a [ViewPager].
     *
     *
     *
     * This is the same as calling [.setupWithViewPager] with
     * auto-refresh enabled.
     *
     * @param viewPager the ViewPager to link to, or `null` to clear any previous link
     */
    fun setupWithViewPager(viewPager: ViewPager?) {
        setupWithViewPager(viewPager, true)
    }

    /**
     * The one-stop shop for setting up this [TabLayout] with a [ViewPager].
     *
     *
     *
     * This method will link the given ViewPager and this TabLayout together so that
     * changes in one are automatically reflected in the other. This includes scroll state changes
     * and clicks. The tabs displayed in this layout will be populated
     * from the ViewPager adapter's page titles.
     *
     *
     *
     * If `autoRefresh` is `true`, any changes in the [PagerAdapter] will
     * trigger this layout to re-populate itself from the adapter's titles.
     *
     *
     *
     * If the given ViewPager is non-null, it needs to already have a
     * [PagerAdapter] set.
     *
     * @param viewPager   the ViewPager to link to, or `null` to clear any previous link
     * @param autoRefresh whether this layout should refresh its contents if the given ViewPager's
     * content changes
     */
    fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean) {
        setupWithViewPager(viewPager, autoRefresh, false)
    }

    private fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean,
                                   implicitSetup: Boolean) {
        if (mViewPager != null) {
            // If we've already been setup with a ViewPager, remove us from it
            if (mPageChangeListener != null) {
                mViewPager!!.removeOnPageChangeListener(mPageChangeListener!!)
            }
            if (mAdapterChangeListener != null) {
                mViewPager!!.removeOnAdapterChangeListener(mAdapterChangeListener!!)
            }
        }

        if (mCurrentVpSelectedListener != null) {
            // If we already have a tab selected listener for the ViewPager, remove it
            removeOnTabSelectedListener(mCurrentVpSelectedListener!!)
            mCurrentVpSelectedListener = null
        }

        if (viewPager != null) {
            mViewPager = viewPager

            // Add our custom OnPageChangeListener to the ViewPager
            if (mPageChangeListener == null) {
                mPageChangeListener = TabLayoutOnPageChangeListener(this)
            }
            mPageChangeListener!!.reset()
            viewPager.addOnPageChangeListener(mPageChangeListener!!)

            // Now we'll add a tab selected listener to set ViewPager's current item
            mCurrentVpSelectedListener = ViewPagerOnTabSelectedListener(viewPager)
            addOnTabSelectedListener(mCurrentVpSelectedListener!!)

            val adapter = viewPager.adapter
            if (adapter != null) {
                // Now we'll populate ourselves from the pager adapter, adding an observer if
                // autoRefresh is enabled
                setPagerAdapter(adapter, autoRefresh)
            }

            // Add a listener so that we're notified of any adapter changes
            if (mAdapterChangeListener == null) {
                mAdapterChangeListener = AdapterChangeListener()
            }
            mAdapterChangeListener!!.setAutoRefresh(autoRefresh)
            viewPager.addOnAdapterChangeListener(mAdapterChangeListener!!)

            // Now update the scroll position to match the ViewPager's current item
            setScrollPosition(viewPager.currentItem, 0f, true)
        } else {
            // We've been given a null ViewPager so we need to clear out the internal state,
            // listeners and observers
            mViewPager = null
            setPagerAdapter(null, false)
        }

        mSetupViewPagerImplicitly = implicitSetup
    }


    @Deprecated("Use {@link #setupWithViewPager(ViewPager)} to link a TabLayout with a ViewPager\n" +
            "      together. When that method is used, the TabLayout will be automatically updated\n" +
            "      when the {@link PagerAdapter} is changed.")
    fun setTabsFromPagerAdapter(adapter: PagerAdapter?) {
        setPagerAdapter(adapter, false)
    }

    override fun shouldDelayChildPressedState(): Boolean {
        // Only delay the pressed state if the tabs can scroll
        return getTabScrollRange() > 0
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (mViewPager == null) {
            // If we don't have a ViewPager already, check if our parent is a ViewPager to
            // setup with it automatically
            val vp = parent
            if (vp is ViewPager) {
                // If we have a ViewPager parent and we've been added as part of its decor, let's
                // assume that we should automatically setup to display any titles
                setupWithViewPager(vp, true, true)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        if (mSetupViewPagerImplicitly) {
            // If we've been setup with a ViewPager implicitly, let's clear out any listeners, etc
            setupWithViewPager(null)
            mSetupViewPagerImplicitly = false
        }
    }

    private fun getTabScrollRange(): Int {
        return Math.max(0, mTabStrip.getWidth() - width - paddingLeft
                - paddingRight)
    }

    internal fun setPagerAdapter(adapter: PagerAdapter?, addObserver: Boolean) {
        if (mPagerAdapter != null && mPagerAdapterObserver != null) {
            // If we already have a PagerAdapter, unregister our observer
            mPagerAdapter!!.unregisterDataSetObserver(mPagerAdapterObserver!!)
        }

        mPagerAdapter = adapter

        if (addObserver && adapter != null) {
            // Register our observer on the new adapter
            if (mPagerAdapterObserver == null) {
                mPagerAdapterObserver = PagerAdapterObserver()
            }
            adapter.registerDataSetObserver(mPagerAdapterObserver!!)
        }

        // Finally make sure we reflect the new adapter
        populateFromPagerAdapter()
    }

    internal fun populateFromPagerAdapter() {
        removeAllTabs()

        if (mPagerAdapter != null) {
            val adapterCount = mPagerAdapter!!.count
            for (i in 0 until adapterCount) {
                addTab(newTab().setText(mPagerAdapter!!.getPageTitle(i)), false)
            }

            // Make sure we reflect the currently set ViewPager item
            if (mViewPager != null && adapterCount > 0) {
                val curItem = mViewPager!!.currentItem
                if (curItem != getSelectedTabPosition() && curItem < getTabCount()) {
                    selectTab(getTabAt(curItem))
                }
            }
        }
    }

    private fun updateAllTabs() {
        var i = 0
        val z = mTabs.size
        while (i < z) {
            mTabs[i].updateView()
            i++
        }
    }

    private fun createTabView(tab: Tab): TabView {
        var tabView: TabView? = mTabViewPool.acquire()
        if (tabView == null) {
            tabView = TabView(context)
        }
        tabView.tab = tab
        tabView.isFocusable = true
        tabView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        tabView.minimumWidth = getTabMinWidth()
        return tabView
    }

    private fun configureTab(tab: Tab, position: Int) {
        tab.position = position
        mTabs.add(position, tab)

        val count = mTabs.size
        for (i in position + 1 until count) {
            mTabs[i].position = i
        }
    }

    private fun addTabView(tab: Tab) {
        val tabView = tab.mView
        mTabStrip.addView(tabView, tab.position, createLayoutParamsForTabs())
    }

    private fun createLayoutParamsForTabs(): LinearLayout.LayoutParams {
        val lp = LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
        updateTabViewLayoutParams(lp)
        return lp
    }

    private fun updateTabViewLayoutParams(lp: LinearLayout.LayoutParams) {
        if (mMode == MODE_FIXED && mTabGravity == GRAVITY_FILL) {
            lp.width = 0
            lp.weight = 1f
        } else {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT
            lp.weight = 0f
        }
    }

    internal fun dpToPx(dps: Int): Int {
        return Math.round(resources.displayMetrics.density * dps)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        // If we have a MeasureSpec which allows us to decide our height, try and use the default
        // height
        val idealHeight = dpToPx(getDefaultHeight()) + paddingTop + paddingBottom
        when (View.MeasureSpec.getMode(heightMeasureSpec)) {
            View.MeasureSpec.AT_MOST -> heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    Math.min(idealHeight, View.MeasureSpec.getSize(heightMeasureSpec)),
                    View.MeasureSpec.EXACTLY)
            View.MeasureSpec.UNSPECIFIED -> heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(idealHeight, View.MeasureSpec.EXACTLY)
        }

        val specWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        if (View.MeasureSpec.getMode(widthMeasureSpec) != View.MeasureSpec.UNSPECIFIED) {
            // If we don't have an unspecified width spec, use the given size to calculate
            // the max tab width
            mTabMaxWidth = if (mRequestedTabMaxWidth > 0)
                mRequestedTabMaxWidth
            else
                specWidth - dpToPx(TAB_MIN_WIDTH_MARGIN)
        }

        // Now super measure itself using the (possibly) modified height spec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (childCount == 1) {
            // If we're in fixed mode then we need to make the tab strip is the same width as us
            // so we don't scroll
            val child = getChildAt(0)
            var remeasure = false

            when (mMode) {
                MODE_SCROLLABLE ->
                    // We only need to resize the child if it's smaller than us. This is similar
                    // to fillViewport
                    remeasure = child.measuredWidth < measuredWidth
                MODE_FIXED ->
                    // Resize the child so that it doesn't scroll
                    remeasure = child.measuredWidth != measuredWidth
            }

            if (remeasure) {
                // Re-measure the child with a widthSpec set to be exactly our measure width
                val childHeightMeasureSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, child.layoutParams.height)
                val childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        measuredWidth, View.MeasureSpec.EXACTLY)
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }
        }
    }

    private fun removeTabViewAt(position: Int) {
        val view = mTabStrip.getChildAt(position) as TabView
        mTabStrip.removeViewAt(position)
        view.reset()
        mTabViewPool.release(view)
        requestLayout()
    }

    private fun animateToTab(newPosition: Int) {
        if (newPosition == Tab.INVALID_POSITION) {
            return
        }

        if (windowToken == null || !ViewCompat.isLaidOut(this)
                || mTabStrip.childrenNeedLayout()) {
            // If we don't have a window token, or we haven't been laid out yet just draw the new
            // position now
            setScrollPosition(newPosition, 0f, true)
            return
        }

        val startScrollX = scrollX
        val targetScrollX = calculateScrollXForTab(newPosition, 0f)

        if (startScrollX != targetScrollX) {
            ensureScrollAnimator()

            mScrollAnimator!!.setIntValues(startScrollX, targetScrollX)
            mScrollAnimator!!.start()
        }

        // Now animate the indicator
        mTabStrip.animateIndicatorToPosition(newPosition, ANIMATION_DURATION)
    }

    private fun ensureScrollAnimator() {
        if (mScrollAnimator == null) {
            mScrollAnimator = ValueAnimator()
            mScrollAnimator!!.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
            mScrollAnimator!!.setDuration(ANIMATION_DURATION.toLong())
            mScrollAnimator!!.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                override fun onAnimationUpdate(animator: ValueAnimator) {
                    scrollTo(animator.getAnimatedValue() as Int, 0)
                }
            })
        }
    }

    internal fun setScrollAnimatorListener(listener: Animator.AnimatorListener) {
        ensureScrollAnimator()
        mScrollAnimator!!.addListener(listener)
    }

    private fun setSelectedTabView(position: Int) {
        val tabCount = mTabStrip.getChildCount()
        if (position < tabCount) {
            for (i in 0 until tabCount) {
                val child = mTabStrip.getChildAt(i)
                child.setSelected(i == position)
            }
        }
    }

    internal fun selectTab(tab: Tab?) {
        selectTab(tab, true)
    }

    internal fun selectTab(tab: Tab?, updateIndicator: Boolean) {
        val currentTab = mSelectedTab

        if (currentTab == tab) {
            if (currentTab != null) {
                dispatchTabReselected(tab!!)
                animateToTab(tab.position)
            }
        } else {
            val newPosition = tab?.position ?: Tab.INVALID_POSITION
            if (updateIndicator) {
                if ((currentTab == null || currentTab.position == Tab.INVALID_POSITION) && newPosition != Tab.INVALID_POSITION) {
                    // If we don't currently have a tab, just draw the indicator
                    setScrollPosition(newPosition, 0f, true)
                } else {
                    animateToTab(newPosition)
                }
                if (newPosition != Tab.INVALID_POSITION) {
                    setSelectedTabView(newPosition)
                }
            }
            if (currentTab != null) {
                dispatchTabUnselected(currentTab)
            }
            mSelectedTab = tab
            if (tab != null) {
                dispatchTabSelected(tab)
            }
        }
    }

    private fun dispatchTabSelected(tab: Tab) {
        updateOnMeasure = false
        tab.mView!!.mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabBigTextSize)
        for (i in mSelectedListeners.indices.reversed()) {
            mSelectedListeners[i].onTabSelected(tab)
        }
    }

    private fun dispatchTabUnselected(tab: Tab) {
        updateOnMeasure = false
        tab.mView!!.mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize)
        for (i in mSelectedListeners.indices.reversed()) {
            mSelectedListeners[i].onTabUnselected(tab)
        }
    }

    private fun dispatchTabReselected(tab: Tab) {
        for (i in mSelectedListeners.indices.reversed()) {
            mSelectedListeners[i].onTabReselected(tab)
        }
    }

    private fun calculateScrollXForTab(position: Int, positionOffset: Float): Int {
        if (mMode == MODE_SCROLLABLE) {
            val selectedChild = mTabStrip.getChildAt(position)
            val nextChild = if (position + 1 < mTabStrip.getChildCount())
                mTabStrip.getChildAt(position + 1)
            else
                null
            val selectedWidth = selectedChild?.width ?: 0
            val nextWidth = nextChild?.width ?: 0

            // base scroll amount: places center of tab in center of parent
            val scrollBase = selectedChild!!.getLeft() + selectedWidth / 2 - width / 2
            // offset amount: fraction of the distance between centers of tabs
            val scrollOffset = ((selectedWidth + nextWidth).toFloat() * 0.5f * positionOffset).toInt()

            return if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                scrollBase + scrollOffset
            else
                scrollBase - scrollOffset
        }
        return 0
    }

    private fun applyModeAndGravity() {
        var paddingStart = 0
        if (mMode == MODE_SCROLLABLE) {
            // If we're scrollable, or fixed at start, inset using padding
            paddingStart = Math.max(0, mContentInsetStart - mTabPaddingStart)
        }
        ViewCompat.setPaddingRelative(mTabStrip, paddingStart, 0, 0, 0)

        when (mMode) {
            MODE_FIXED -> mTabStrip.setGravity(Gravity.CENTER_HORIZONTAL)
            MODE_SCROLLABLE -> mTabStrip.setGravity(GravityCompat.START)
        }

        updateTabViews(true)
    }

    internal fun updateTabViews(requestLayout: Boolean) {
        for (i in 0 until mTabStrip.getChildCount()) {
            val child = mTabStrip.getChildAt(i)
            child.setMinimumWidth(getTabMinWidth())
            updateTabViewLayoutParams(child.getLayoutParams() as LinearLayout.LayoutParams)
            if (requestLayout) {
                child.requestLayout()
            }
        }
    }

    private fun getDefaultHeight(): Int {
        var hasIconAndText = false
        var i = 0
        val count = mTabs.size
        while (i < count) {
            val tab = mTabs[i]
            if (!(tab.getIcon() == null || TextUtils.isEmpty(tab.getText()))) {
                hasIconAndText = true
                break
            }
            i++
        }
        return if (hasIconAndText) DEFAULT_HEIGHT_WITH_TEXT_ICON else DEFAULT_HEIGHT
    }

    private fun getTabMinWidth(): Int {
        if (mRequestedTabMinWidth != INVALID_WIDTH) {
            // If we have been given a min width, use it
            return mRequestedTabMinWidth
        }
        // Else, we'll use the default value
        return if (mMode == MODE_SCROLLABLE) mScrollableTabMinWidth else 0
    }

    override fun generateLayoutParams(attrs: AttributeSet): FrameLayout.LayoutParams {
        // We don't care about the layout params of any views added to us, since we don't actually
        // add them. The only view we add is the SlidingTabStrip, which is done manually.
        // We return the default layout params so that we don't blow up if we're given a TabItem
        // without android:layout_* values.
        return generateDefaultLayoutParams()
    }

    internal fun getTabMaxWidth(): Int {
        return mTabMaxWidth
    }

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @IntDef(value = [MODE_SCROLLABLE, MODE_FIXED])
    @Retention(RetentionPolicy.SOURCE)
    annotation class Mode

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @IntDef(flag = true, value = [GRAVITY_FILL, GRAVITY_CENTER])
    @Retention(RetentionPolicy.SOURCE)
    annotation class TabGravity

    /**
     * Callback interface invoked when a tab's selection state changes.
     */
    interface OnTabSelectedListener {

        /**
         * Called when a tab enters the selected state.
         *
         * @param tab The tab that was selected
         */
        fun onTabSelected(tab: Tab)

        /**
         * Called when a tab exits the selected state.
         *
         * @param tab The tab that was unselected
         */
        fun onTabUnselected(tab: Tab)

        /**
         * Called when a tab that is already selected is chosen again by the user. Some applications
         * may use this action to return to the top level of a category.
         *
         * @param tab The tab that was reselected.
         */
        fun onTabReselected(tab: Tab)
    }

    /**
     * A tab in this layout. Instances can be created via [.newTab].
     */
    class Tab internal constructor()// Private constructor
    {
        internal var mParent: TitleZoomTabLayout? = null
        internal var mView: TabView? = null
        private var mTag: Any? = null
        private var mIcon: Drawable? = null
        private var mText: CharSequence? = null
        private var mContentDesc: CharSequence? = null
        /**
         * Return the current position of this tab in the action bar.
         *
         * @return Current position, or [.INVALID_POSITION] if this tab is not currently in
         * the action bar.
         */
        var position = INVALID_POSITION
            internal set
        private var mCustomView: View? = null

        /**
         * Returns true if this tab is currently selected.
         */
        val isSelected: Boolean
            get() {
                if (mParent == null) {
                    throw IllegalArgumentException("Tab not attached to a TabLayout")
                }
                return mParent!!.getSelectedTabPosition() == position
            }

        /**
         * @return This Tab's tag object.
         */
        fun getTag(): Any? {
            return mTag
        }

        /**
         * Give this Tab an arbitrary object to hold for later use.
         *
         * @param tag Object to store
         * @return The current instance for call chaining
         */
        fun setTag(tag: Any?): Tab {
            mTag = tag
            return this
        }


        /**
         * Returns the custom view used for this tab.
         *
         * @see .setCustomView
         * @see .setCustomView
         */
        fun getCustomView(): View? {
            return mCustomView
        }

        /**
         * Set a custom view to be used for this tab.
         *
         *
         * If the inflated layout contains a [TextView] with an ID of
         * [android.R.id.text1] then that will be updated with the value given
         * to [.setText]. Similarly, if this layout contains an
         * [ImageView] with ID [android.R.id.icon] then it will be updated with
         * the value given to [.setIcon].
         *
         *
         * @param resId A layout resource to inflate and use as a custom tab view
         * @return The current instance for call chaining
         */
        fun setCustomView(@LayoutRes resId: Int): Tab {
            val inflater = LayoutInflater.from(mView!!.getContext())
            return setCustomView(inflater.inflate(resId, mView, false))
        }

        /**
         * Set a custom view to be used for this tab.
         *
         *
         * If the provided view contains a [TextView] with an ID of
         * [android.R.id.text1] then that will be updated with the value given
         * to [.setText]. Similarly, if this layout contains an
         * [ImageView] with ID [android.R.id.icon] then it will be updated with
         * the value given to [.setIcon].
         *
         *
         * @param view Custom view to be used as a tab.
         * @return The current instance for call chaining
         */
        fun setCustomView(view: View?): Tab {
            mCustomView = view
            updateView()
            return this
        }

        /**
         * Return the icon associated with this tab.
         *
         * @return The tab's icon
         */
        fun getIcon(): Drawable? {
            return mIcon
        }

        /**
         * Set the icon displayed on this tab.
         *
         * @param resId A resource ID referring to the icon that should be displayed
         * @return The current instance for call chaining
         */
        fun setIcon(@DrawableRes resId: Int): Tab {
            if (mParent == null) {
                throw IllegalArgumentException("Tab not attached to a TabLayout")
            }
            return setIcon(AppCompatResources.getDrawable(mParent!!.context, resId))
        }

        /**
         * Return the text of this tab.
         *
         * @return The tab's text
         */
        fun getText(): CharSequence? {
            return mText
        }

        /**
         * Set the text displayed on this tab. Text may be truncated if there is not room to display
         * the entire string.
         *
         * @param resId A resource ID referring to the text that should be displayed
         * @return The current instance for call chaining
         */
        fun setText(@StringRes resId: Int): Tab {
            if (mParent == null) {
                throw IllegalArgumentException("Tab not attached to a TabLayout")
            }
            return setText(mParent!!.resources.getText(resId))
        }

        /**
         * Set the icon displayed on this tab.
         *
         * @param icon The drawable to use as an icon
         * @return The current instance for call chaining
         */
        fun setIcon(icon: Drawable?): Tab {
            mIcon = icon
            updateView()
            return this
        }

        /**
         * Set the text displayed on this tab. Text may be truncated if there is not room to display
         * the entire string.
         *
         * @param text The text to display
         * @return The current instance for call chaining
         */
        fun setText(text: CharSequence?): Tab {
            mText = text
            updateView()
            return this
        }

        /**
         * Select this tab. Only valid if the tab has been added to the action bar.
         */
        fun select() {
            if (mParent == null) {
                throw IllegalArgumentException("Tab not attached to a TabLayout")
            }
            mParent!!.selectTab(this)
        }

        /**
         * Set a description of this tab's content for use in accessibility support. If no content
         * description is provided the title will be used.
         *
         * @param resId A resource ID referring to the description text
         * @return The current instance for call chaining
         * @see .setContentDescription
         * @see .getContentDescription
         */
        fun setContentDescription(@StringRes resId: Int): Tab {
            if (mParent == null) {
                throw IllegalArgumentException("Tab not attached to a TabLayout")
            }
            return setContentDescription(mParent!!.resources.getText(resId))
        }

        /**
         * Gets a brief description of this tab's content for use in accessibility support.
         *
         * @return Description of this tab's content
         * @see .setContentDescription
         * @see .setContentDescription
         */
        fun getContentDescription(): CharSequence? {
            return mContentDesc
        }

        /**
         * Set a description of this tab's content for use in accessibility support. If no content
         * description is provided the title will be used.
         *
         * @param contentDesc Description of this tab's content
         * @return The current instance for call chaining
         * @see .setContentDescription
         * @see .getContentDescription
         */
        fun setContentDescription(contentDesc: CharSequence?): Tab {
            mContentDesc = contentDesc
            updateView()
            return this
        }

        internal fun updateView() {
            if (mView != null) {
                mView!!.update()
            }
        }

        internal fun reset() {
            mParent = null
            mView = null
            mTag = null
            mIcon = null
            mText = null
            mContentDesc = null
            position = INVALID_POSITION
            mCustomView = null
        }

        companion object {

            /**
             * An invalid position for a tab.
             *
             * @see .getPosition
             */
            val INVALID_POSITION = -1
        }
    }

    /**
     * A [ViewPager.OnPageChangeListener] class which contains the
     * necessary calls back to the provided [TabLayout] so that the tab position is
     * kept in sync.
     *
     *
     *
     * This class stores the provided TabLayout weakly, meaning that you can use
     * [ addOnPageChangeListener(OnPageChangeListener)][ViewPager.addOnPageChangeListener] without removing the listener and
     * not cause a leak.
     */
    class TabLayoutOnPageChangeListener(titleZoomTabLayout: TitleZoomTabLayout) : ViewPager.OnPageChangeListener {
        private val mTabLayoutRef: WeakReference<TitleZoomTabLayout>
        private var mPreviousScrollState: Int = 0
        private var mScrollState: Int = 0

        init {
            mTabLayoutRef = WeakReference<TitleZoomTabLayout>(titleZoomTabLayout)
        }

        override fun onPageScrollStateChanged(state: Int) {
            mPreviousScrollState = mScrollState
            mScrollState = state
        }

        override fun onPageScrolled(position: Int, positionOffset: Float,
                                    positionOffsetPixels: Int) {
            val titleZoomTabLayout = mTabLayoutRef.get()
            if (titleZoomTabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                val updateText = mScrollState != SCROLL_STATE_SETTLING || mPreviousScrollState == SCROLL_STATE_DRAGGING
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                val updateIndicator = !(mScrollState == SCROLL_STATE_SETTLING && mPreviousScrollState == SCROLL_STATE_IDLE)
                titleZoomTabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator)
            }
        }

        override fun onPageSelected(position: Int) {
            val tabLayout = mTabLayoutRef.get()
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position
                    && position < tabLayout.getTabCount()) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                val updateIndicator = mScrollState == SCROLL_STATE_IDLE || mScrollState == SCROLL_STATE_SETTLING && mPreviousScrollState == SCROLL_STATE_IDLE
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator)
            }
        }

        internal fun reset() {
            mScrollState = SCROLL_STATE_IDLE
            mPreviousScrollState = mScrollState
        }
    }

    /**
     * A [TabLayout.OnTabSelectedListener] class which contains the necessary calls back
     * to the provided [ViewPager] so that the tab position is kept in sync.
     */
    class ViewPagerOnTabSelectedListener(private val mViewPager: ViewPager) : TitleZoomTabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: Tab) {
            mViewPager.setCurrentItem(tab.position)
        }

        override fun onTabUnselected(tab: Tab) {
            // No-op
        }

        override fun onTabReselected(tab: Tab) {
            // No-op
        }
    }

    internal inner class TabView(context: Context) : LinearLayout(context) {
        var mTextView: TextView? = null
        var mIconView: ImageView? = null
        var tab: Tab? = null
            set(tab) {
                if (tab != this.tab) {
                    field = tab
                    update()
                }
            }
        private var mCustomView: View? = null
        private var mCustomTextView: TextView? = null
        private var mCustomIconView: ImageView? = null

        private var mDefaultMaxLines = 2

        init {
            if (mTabBackgroundResId != 0) {
                ViewCompat.setBackground(
                        this, AppCompatResources.getDrawable(context, mTabBackgroundResId))
            }
            ViewCompat.setPaddingRelative(this, mTabPaddingStart, mTabPaddingTop,
                    mTabPaddingEnd, mTabPaddingBottom)
            setGravity(Gravity.CENTER)
            setOrientation(LinearLayout.VERTICAL)
            isClickable = true
            ViewCompat.setPointerIcon(this,
                    PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND))
        }

        override fun performClick(): Boolean {
            val handled = super.performClick()

            if (tab != null) {
                if (!handled) {
                    playSoundEffect(SoundEffectConstants.CLICK)
                }
                tab!!.select()
                return true
            } else {
                return handled
            }
        }

        override fun setSelected(selected: Boolean) {
            val changed = isSelected != selected

            super.setSelected(selected)

            if (changed && selected && Build.VERSION.SDK_INT < 16) {
                // Pre-JB we need to manually send the TYPE_VIEW_SELECTED event
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
            }

            // Always dispatch this to the child views, regardless of whether the value has
            // changed
            if (mTextView != null) {
                mTextView!!.setSelected(selected)
            }
            if (mIconView != null) {
                mIconView!!.setSelected(selected)
            }
            if (mCustomView != null) {
                mCustomView!!.setSelected(selected)
            }
        }

        override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
            super.onInitializeAccessibilityEvent(event)
            // This view masquerades as an action bar tab.
            event.className = ActionBar.Tab::class.java.name
        }

        override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(info)
            // This view masquerades as an action bar tab.
            info.className = ActionBar.Tab::class.java.name
        }

        override fun onMeasure(origWidthMeasureSpec: Int, origHeightMeasureSpec: Int) {
            val specWidthSize = View.MeasureSpec.getSize(origWidthMeasureSpec)
            val specWidthMode = View.MeasureSpec.getMode(origWidthMeasureSpec)
            val maxWidth = getTabMaxWidth()

            val widthMeasureSpec: Int

            widthMeasureSpec = if (maxWidth > 0 && (specWidthMode == View.MeasureSpec.UNSPECIFIED || specWidthSize > maxWidth)) {
                // If we have a max width and a given spec which is either unspecified or
                // larger than the max width, update the width spec using the same mode
                View.MeasureSpec.makeMeasureSpec(mTabMaxWidth, View.MeasureSpec.AT_MOST)
            } else {
                // Else, use the original width spec
                //                int width = MeasureSpec.getSize(origWidthMeasureSpec) * 2;
                //                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(origWidthMeasureSpec));
                origWidthMeasureSpec
            }

            // Now lets measure
            super.onMeasure(widthMeasureSpec, origHeightMeasureSpec)

            // We need to switch the text size based on whether the text is spanning 2 lines or not
            if (mTextView != null) {
                var textSize = mTabTextSize
                var maxLines = mDefaultMaxLines

                if (mIconView != null && mIconView!!.getVisibility() == View.VISIBLE) {
                    // If the icon view is being displayed, we limit the text to 1 line
                    maxLines = 1
                } else if (mTextView != null && mTextView!!.getLineCount() > 1) {
                    // Otherwise when we have text which wraps we reduce the text size
                    textSize = mTabTextMultiLineSize
                }

                val curTextSize = mTextView!!.getTextSize()
                val curLineCount = mTextView!!.getLineCount()
                val curMaxLines = TextViewCompat.getMaxLines(mTextView!!)

                if (textSize != curTextSize || curMaxLines >= 0 && maxLines != curMaxLines) {
                    // We've got a new text size and/or max lines...
                    var updateTextView = true

                    if (mMode == MODE_FIXED && textSize > curTextSize && curLineCount == 1) {
                        // If we're in fixed mode, going up in text size and currently have 1 line
                        // then it's very easy to get into an infinite recursion.
                        // To combat that we check to see if the change in text size
                        // will cause a line count change. If so, abort the size change and stick
                        // to the smaller size.
                        val layout = mTextView!!.getLayout()
                        if (layout == null || approximateLineWidth(layout!!, 0, textSize) > measuredWidth - paddingLeft - paddingRight) {
                            updateTextView = false
                        }
                    }

                    if (updateTextView && updateOnMeasure) {
                        mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                        mTextView!!.maxLines = maxLines
                        super.onMeasure(widthMeasureSpec, origHeightMeasureSpec)
                    }
                }
            }
        }

        fun reset() {
            tab = null
            isSelected = false
        }

        fun update() {
            val tab = this.tab
            val custom = tab?.getCustomView()
            if (custom != null) {
                val customParent = custom.parent
                if (customParent !== this) {
                    if (customParent != null) {
                        (customParent as ViewGroup).removeView(custom)
                    }
                    addView(custom)
                }
                mCustomView = custom
                if (mTextView != null) {
                    mTextView!!.setVisibility(View.GONE)
                }
                if (mIconView != null) {
                    mIconView!!.setVisibility(View.GONE)
                    mIconView!!.setImageDrawable(null)
                }

                mCustomTextView = custom.findViewById(android.R.id.text1)
                if (mCustomTextView != null) {
                    mDefaultMaxLines = TextViewCompat.getMaxLines(mCustomTextView!!)
                }
                mCustomIconView = custom.findViewById(android.R.id.icon)
            } else {
                // We do not have a custom view. Remove one if it already exists
                if (mCustomView != null) {
                    removeView(mCustomView)
                    mCustomView = null
                }
                mCustomTextView = null
                mCustomIconView = null
            }

            if (mCustomView == null) {
                // If there isn't a custom view, we'll us our own in-built layouts
                if (mIconView == null) {
                    val iconView = LayoutInflater.from(context)
                            .inflate(R.layout.design_layout_tab_icon, this, false) as ImageView
                    addView(iconView, 0)
                    mIconView = iconView
                }
                if (mTextView == null) {
                    val textView = LayoutInflater.from(context)
                            .inflate(R.layout.design_layout_tab_text, this, false) as TextView
                    addView(textView)
                    mTextView = textView
                    mDefaultMaxLines = TextViewCompat.getMaxLines(mTextView!!)
                }
                TextViewCompat.setTextAppearance(mTextView!!, mTabTextAppearance)
                if (mTabTextColors != null) {
                    mTextView!!.setTextColor(mTabTextColors)
                }
                updateTextAndIcon(mTextView, mIconView)
            } else {
                // Else, we'll see if there is a TextView or ImageView present and update them
                if (mCustomTextView != null || mCustomIconView != null) {
                    updateTextAndIcon(mCustomTextView, mCustomIconView)
                }
            }

            // Finally update our selected state
            setSelected(tab != null && tab.isSelected)
        }

        private fun updateTextAndIcon(textView: TextView?,
                                      iconView: ImageView?) {
            val icon = if (tab != null) tab!!.getIcon() else null
            val text = if (tab != null) tab!!.getText() else null
            val contentDesc = if (tab != null) tab!!.getContentDescription() else null

            if (iconView != null) {
                if (icon != null) {
                    iconView.setImageDrawable(icon)
                    iconView.visibility = View.VISIBLE
                    visibility = View.VISIBLE
                } else {
                    iconView.visibility = View.GONE
                    iconView.setImageDrawable(null)
                }
                iconView.contentDescription = contentDesc
            }

            val hasText = !TextUtils.isEmpty(text)
            if (textView != null) {
                if (hasText) {
                    textView.text = text
                    textView.visibility = View.VISIBLE
                    visibility = View.VISIBLE
                } else {
                    textView.visibility = View.GONE
                    textView.text = null
                }
                textView.contentDescription = contentDesc
            }

            if (iconView != null) {
                val lp = iconView.layoutParams as ViewGroup.MarginLayoutParams
                var bottomMargin = 0
                if (hasText && iconView.visibility == View.VISIBLE) {
                    // If we're showing both text and icon, add some margin bottom to the icon
                    bottomMargin = dpToPx(DEFAULT_GAP_TEXT_ICON)
                }
                if (bottomMargin != lp.bottomMargin) {
                    lp.bottomMargin = bottomMargin
                    iconView.requestLayout()
                }
            }
            TooltipCompat.setTooltipText(this, if (hasText) null else contentDesc)
        }

        /**
         * Approximates a given lines width with the new provided text size.
         */
        private fun approximateLineWidth(layout: Layout, line: Int, textSize: Float): Float {
            return layout.getLineWidth(line) * (textSize / layout.getPaint().getTextSize())
        }
    }

    private inner class SlidingTabStrip internal constructor(context: Context) : LinearLayout(context) {
        private val mSelectedIndicatorPaint: Paint
        internal var mSelectedPosition = -1
        internal var mSelectionOffset: Float = 0.toFloat()
        private var mSelectedIndicatorHeight: Int = 0
        private var mLayoutDirection = -1

        private var mIndicatorLeft = -1
        private var mIndicatorRight = -1

        private var mIndicatorAnimator: ValueAnimator? = null

        internal val indicatorPosition: Float
            get() = mSelectedPosition + mSelectionOffset

        init {
            setWillNotDraw(false)
            mSelectedIndicatorPaint = Paint()
        }

        internal fun setSelectedIndicatorColor(color: Int) {
            if (mSelectedIndicatorPaint.getColor() != color) {
                mSelectedIndicatorPaint.setColor(color)
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        internal fun setSelectedIndicatorHeight(height: Int) {
            if (mSelectedIndicatorHeight != height) {
                mSelectedIndicatorHeight = height
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        internal fun childrenNeedLayout(): Boolean {
            var i = 0
            val z = childCount
            while (i < z) {
                val child = getChildAt(i)
                if (child.width <= 0) {
                    return true
                }
                i++
            }
            return false
        }

        internal fun setIndicatorPositionFromTabPosition(position: Int, positionOffset: Float) {
            if (mIndicatorAnimator != null && mIndicatorAnimator!!.isRunning()) {
                mIndicatorAnimator!!.cancel()
            }

            mSelectedPosition = position
            mSelectionOffset = positionOffset
            updateIndicatorPosition()
        }

        override fun onRtlPropertiesChanged(layoutDirection: Int) {
            super.onRtlPropertiesChanged(layoutDirection)

            // Workaround for a bug before Android M where LinearLayout did not relayout itself when
            // layout direction changed.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                if (mLayoutDirection != layoutDirection) {
                    requestLayout()
                    mLayoutDirection = layoutDirection
                }
            }
        }

        protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

            if (View.MeasureSpec.getMode(widthMeasureSpec) != View.MeasureSpec.EXACTLY) {
                // HorizontalScrollView will first measure use with UNSPECIFIED, and then with
                // EXACTLY. Ignore the first call since anything we do will be overwritten anyway
                return
            }

            if (mMode == MODE_FIXED && mTabGravity == GRAVITY_CENTER) {
                val count = childCount

                // First we'll find the widest tab
                var largestTabWidth = 0
                val paint = Paint()
                paint.setTextSize(mTabBigTextSize)
                run {
                    var i = 0
                    while (i < count) {
                        val child = getChildAt(i)
                        val lp = child.layoutParams as LinearLayout.LayoutParams
                        val strWidth = (paint.measureText((child as TabView).mTextView!!.getText().toString()) + lp.leftMargin.toFloat() + lp.rightMargin.toFloat() + mTabPaddingStart.toFloat() + mTabPaddingEnd.toFloat()).toInt()
                        if (child.visibility == View.VISIBLE) {
                            largestTabWidth = Math.max(largestTabWidth, strWidth)
                        }
                        i++
                    }
                }

                if (largestTabWidth <= 0) {
                    // If we don't have a largest child yet, skip until the next measure pass
                    return
                }

                val gutter = dpToPx(FIXED_WRAP_GUTTER_MIN)
                var remeasure = false

                if (largestTabWidth * count <= measuredWidth - gutter * 2) {
                    // If the tabs fit within our width minus gutters, we will set all tabs to have
                    // the same width
                    for (i in 0 until count) {
                        val lp = getChildAt(i).layoutParams as LinearLayout.LayoutParams
                        if (lp.width != largestTabWidth || lp.weight != 0f) {
                            lp.width = largestTabWidth
                            lp.weight = 0f
                            remeasure = true
                        }
                    }
                } else {
                    // If the tabs will wrap to be larger than the width minus gutters, we need
                    // to switch to GRAVITY_FILL
                    mTabGravity = GRAVITY_FILL
                    updateTabViews(false)
                    remeasure = true
                }

                if (remeasure) {
                    // Now re-measure after our changes
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                }
            }
        }

        protected override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
            super.onLayout(changed, l, t, r, b)

            if (mIndicatorAnimator != null && mIndicatorAnimator!!.isRunning()) {
                // If we're currently running an animation, lets cancel it and start a
                // new animation with the remaining duration
                mIndicatorAnimator!!.cancel()
                val duration = mIndicatorAnimator!!.getDuration()
                animateIndicatorToPosition(mSelectedPosition,
                        Math.round((1f - mIndicatorAnimator!!.getAnimatedFraction()) * duration))
            } else {
                // If we've been layed out, update the indicator position
                updateIndicatorPosition()
            }
        }

        private fun updateIndicatorPosition() {
            val selectedTitle = getChildAt(mSelectedPosition)
            var left: Int
            var right: Int

            if (selectedTitle != null && selectedTitle.width > 0) {
                left = selectedTitle.left
                right = selectedTitle.right

                if (mSelectionOffset > 0f && mSelectedPosition < childCount - 1) {
                    // Draw the selection partway between the tabs
                    val nextTitle = getChildAt(mSelectedPosition + 1)
                    left = (mSelectionOffset * nextTitle.left + (1.0f - mSelectionOffset) * left).toInt()
                    right = (mSelectionOffset * nextTitle.right + (1.0f - mSelectionOffset) * right).toInt()
                }
            } else {
                right = -1
                left = right
            }

            setIndicatorPosition(left, right)
        }

        internal fun setIndicatorPosition(left: Int, right: Int) {
            if (left != mIndicatorLeft || right != mIndicatorRight) {
                // If the indicator's left/right has changed, invalidate
                mIndicatorLeft = left
                mIndicatorRight = right
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        internal fun animateIndicatorToPosition(position: Int, duration: Int) {
            if (mIndicatorAnimator != null && mIndicatorAnimator!!.isRunning) {
                mIndicatorAnimator!!.cancel()
            }

            val isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL

            val targetView = getChildAt(position)
            if (targetView == null) {
                // If we don't have a view, just update the position now and return
                updateIndicatorPosition()
                return
            }

            val targetLeft = targetView.left
            val targetRight = targetView.right
            val startLeft: Int
            val startRight: Int

            if (Math.abs(position - mSelectedPosition) <= 1) {
                // If the views are adjacent, we'll animate from edge-to-edge
                startLeft = mIndicatorLeft
                startRight = mIndicatorRight
            } else {
                // Else, we'll just grow from the nearest edge
                val offset = dpToPx(MOTION_NON_ADJACENT_OFFSET)
                if (position < mSelectedPosition) {
                    // We're going end-to-start
                    if (isRtl) {
                        startRight = targetLeft - offset
                        startLeft = startRight
                    } else {
                        startRight = targetRight + offset
                        startLeft = startRight
                    }
                } else {
                    // We're going start-to-end
                    if (isRtl) {
                        startRight = targetRight + offset
                        startLeft = startRight
                    } else {
                        startRight = targetLeft - offset
                        startLeft = startRight
                    }
                }
            }

            if (startLeft != targetLeft || startRight != targetRight) {
                mIndicatorAnimator = ValueAnimator()
                val animator = mIndicatorAnimator
                animator!!.interpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR
                animator.duration = duration.toLong()
                animator.setFloatValues(0.toFloat(), 1.toFloat())
                animator.addUpdateListener {
                    val fraction = animator.animatedFraction
                    setIndicatorPosition(
                            AnimationUtils.lerp(startLeft, targetLeft, fraction),
                            AnimationUtils.lerp(startRight, targetRight, fraction))
                }
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animator: Animator) {
                        mSelectedPosition = position
                        mSelectionOffset = 0f
                    }
                })
                animator.start()
            }
        }

        override fun draw(canvas: Canvas) {
            super.draw(canvas)

            // Thick colored underline below the current selection
            if (mIndicatorLeft in 0..(mIndicatorRight - 1)) {
                canvas.drawRect(mIndicatorLeft.toFloat(), (height - mSelectedIndicatorHeight).toFloat(),
                        mIndicatorRight.toFloat(), height.toFloat(), mSelectedIndicatorPaint)
            }
        }
    }

    private inner class PagerAdapterObserver internal constructor() : DataSetObserver() {

        override fun onChanged() {
            populateFromPagerAdapter()
        }

        override fun onInvalidated() {
            populateFromPagerAdapter()
        }
    }

    private inner class AdapterChangeListener internal constructor() : ViewPager.OnAdapterChangeListener {
        private var mAutoRefresh: Boolean = false

        override fun onAdapterChanged(viewPager: ViewPager,
                                      oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {
            if (mViewPager === viewPager) {
                setPagerAdapter(newAdapter, mAutoRefresh)
            }
        }

        internal fun setAutoRefresh(autoRefresh: Boolean) {
            mAutoRefresh = autoRefresh
        }
    }
}