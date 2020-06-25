package com.example.mycoroutines

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView

/**
 * time：2020/6/25 0025
 * author：hexun
 * describe：计时消息pop动画
 */
class PopMsgHelper {
    private var llParent: LinearLayout
    private var mContext: Context
    private lateinit var mInflater: LayoutInflater
    private lateinit var mRunnable: Runnable
    private var dp36 = 0
    private var height = 0
    private var index = 0
    private var mMsgList = mutableListOf<String>()
    private var mParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)

    constructor(llParent: LinearLayout, mContext: Context) {
        this.llParent = llParent
        this.mContext = mContext
        init()
    }

    private fun init() {
        mInflater = LayoutInflater.from(mContext)
        dp36 = ScreenUtils.dip2px(mContext, 36f)
        height = ScreenUtils.dip2px(mContext, 44f)
        mRunnable = Runnable {
            var size = mMsgList.size
            var createView = createView(mMsgList[index])
            llParent?.addView(createView, mParams)
            llParent.post {
                animateAddImpl(createView)
            }
            index++
            if (index < size)
                startMsgPop(1000)
            else {
                index = 0
                startMsgPop(4000)
            }
        }
    }

    /**
     * 开始执行pop消息动画
     */
    fun startMsgPop() {
        startMsgPop(1000)
    }

    fun stopPop() {
        llParent.removeCallbacks(mRunnable)
    }

    private fun startMsgPop(time: Long) {
        llParent.postDelayed(mRunnable, time)
    }

    private fun createView(str: String): View {
        var itemView = mInflater.inflate(R.layout.item, null, false)
        var tvContent = itemView.findViewById<TextView>(R.id.tvContent)
        tvContent.text = str
        itemView.postDelayed({ animateRemoveImpl(itemView) }, 3000)
        return itemView
    }

    private fun animateRemoveImpl(view: View) {
        view.alpha = 1f
        val animation = view.animate()
        animation.setDuration(1000).alpha(0f).setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator) {
                }

                override fun onAnimationEnd(animator: Animator) {
                    animation.setListener(null)
                    if (view.parent != null) {
                        var parent = view.parent as ViewGroup
                        parent.removeView(view)
                    }
                }
            }).start()
    }

    private fun animateAddImpl(view: View) {

        var animation = AnimationUtils.loadAnimation(mContext, R.anim.timing_pop_msg_anim)
        animation.duration = 500
        view.findViewById<TextView>(R.id.tvContent).startAnimation(animation)

        var animator = ValueAnimator.ofInt(1, height)
        animator.addUpdateListener { animation ->
            var percent = animation?.animatedValue as Int
            view.layoutParams.height = percent
            view.requestLayout()
            if (llParent.childCount > 1) {
                var childHeight = height - percent
                childHeight = if (childHeight < dp36) dp36 else childHeight
                var childAt = llParent.getChildAt(llParent.childCount - 2)
                childAt.layoutParams.height = childHeight
                childAt.requestLayout()
            }
        }
        animator.duration = 500
//        animator.interpolator = DecelerateInterpolator()
        animator.start()

    }
}