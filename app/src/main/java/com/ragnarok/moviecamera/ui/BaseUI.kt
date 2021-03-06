package com.ragnarok.moviecamera.ui

import android.animation.Animator
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.support.v7.app.ActionBarActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import com.ragnarok.moviecamera.R
import com.ragnarok.moviecamera.util.CamLogger
import com.ragnarok.moviecamera.util.getActionBarHeight


/**
 * Created by ragnarok on 15/6/8.
 */

public abstract class BaseUI: AppCompatActivity() {
    open val TAG: String = "MovieCamera.BaseUI"
    
    protected var mToolbar: Toolbar? = null
    protected var mTitleText: TextView? = null
    
    private var mUIHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
    
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initToolbar()
        
    }

    override fun onResume() {
        super.onResume()
        if (!isDisableToolbarAnim()) {
            startToobarInitAnim()
        }
    }

    private fun initToolbar() {
        try {
            mToolbar = findViewById(R.id.toolbar) as Toolbar
            setSupportActionBar(mToolbar)
            
            mTitleText = mToolbar?.findViewById(R.id.title) as TextView
            mTitleText?.setTypeface(Typeface.createFromAsset(getAssets(), "appleberry.ttf"))

            val actionBarSize = getActionBarHeight(this)
            mToolbar?.setTranslationY(-actionBarSize.toFloat())
            mTitleText?.setTranslationY(-actionBarSize.toFloat())
            
        } catch (e: Exception) {
            CamLogger.e(TAG, "initToolbar error: ${e.getMessage()}")
        }
        
    }
    
    protected fun getToolbar(): Toolbar? {
        return mToolbar
    }
    
    protected fun getTitleText(): TextView? {
        return mTitleText
    }
    
    private fun startToobarInitAnim() {
        mUIHandler.postDelayed({ startToolbarInitAnimInternal() }, 100)
    }
    
    val TOOLBAR_INIT_ANIM_DURATION: Long = 300
    val TITLE_TEXT_INIT_ANIM_DURATION: Long = 450
    
    private fun startToolbarInitAnimInternal() {
        
        val toolBar = getToolbar()
        val titleText = getTitleText()
        if (toolBar != null && titleText != null) {
            
            toolBar.animate().translationY(0f).setDuration(TOOLBAR_INIT_ANIM_DURATION).setStartDelay(500).start()
            
            titleText.animate().translationY(0f).
                    setInterpolator(OvershootInterpolator()).
                    setDuration(TITLE_TEXT_INIT_ANIM_DURATION).
                    setStartDelay(600).
                    setListener(object : Animator.AnimatorListener {
                        override fun onAnimationCancel(animation: Animator?) {
                            
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            CamLogger.d(TAG, "toolbar init anim finish")
                            onToolbarInitAnimFinish()
                        }

                        override fun onAnimationStart(animation: Animator?) {
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                    }).start()
        }
       
    }
    
    open protected fun onToolbarInitAnimFinish() {}
    
    open protected fun isDisableToolbarAnim(): Boolean { return false }
}