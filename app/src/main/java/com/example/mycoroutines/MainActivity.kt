package com.example.mycoroutines

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val Main_TAG = "我这是Main_TAG"
    val TAG = "协程:8年老Android开发谈；Context都没弄明白凭什么拿高薪？"
    var i = 0L
    private lateinit var adapter: MyAdapter
    private lateinit var inflater: LayoutInflater

    private var endX = 0f
    private var endY = 0f
    private var startX = 0f
    private var startY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inflater = LayoutInflater.from(this)
        adapter = MyAdapter(this)
        var linearLayoutManager = LinearLayoutManager(this)

        linearLayoutManager.stackFromEnd = true
        rvList.layoutManager = linearLayoutManager
        rvList.adapter = adapter
        var itemAnimation = ItemAnimation(this)
        itemAnimation.addDuration = 300
        itemAnimation.removeDuration = 800
        itemAnimation.moveDuration = 300
        itemAnimation.changeDuration = 300
        rvList.itemAnimator = itemAnimation

        delayAddMsg(1000)
        ivStart.post {
            endX = ivEnd.x + ivEnd.width / 2f - ivStart.width / 2f
            endY = ivEnd.y + ivEnd.height / 2f - ivStart.height / 2f
            startX = ivStart.x
            startY = ivStart.y
        }

        ivStart.setOnClickListener { anim() }
    }

    private fun anim() {

        var ivMove = ImageView(this)
        ivMove.setImageResource(R.mipmap.ic_launcher)
        rlParent.addView(
            ivMove,
            RelativeLayout.LayoutParams(
                ScreenUtils.dip2px(this, 50f),
                ScreenUtils.dip2px(this, 50f)
            )
        )
        var animate = ivMove.animate()
        ivMove.translationX = startX
        ivMove.translationY = startY
        animate.translationX(endX).translationY(endY).setDuration(1500).setListener(object :
            AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
            }

            override fun onAnimationCancel(animator: Animator) {
                rlParent.removeView(ivMove)
            }

            override fun onAnimationEnd(animator: Animator) {
                animate.setListener(null)
                ivMove.clearAnimation()
                rlParent.removeView(ivMove)
            }
        }).start()
    }


    private fun delayAddMsg(time: Long) {
        rvList.postDelayed({
            var str = TAG.substring(0, 5 + java.util.Random().nextInt(TAG.length - 5))
            var createView = createView(str)
            llParent.addView(
                createView,
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
            )
            llParent.post {
                animateAddImpl(createView)
            }
            i++
            if (i < 20)
                delayAddMsg(1000)
            else {
                i = 0
                delayAddMsg(4000)
            }
        }, time)
    }

    private fun createView(str: String): View {
        var itemView = inflater.inflate(R.layout.item, null, false)
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
        var dp36 = ScreenUtils.dip2px(this, 36f)
        var animation = AnimationUtils.loadAnimation(this, R.anim.timing_discuss_anim)
        animation.duration = 800
        view.findViewById<TextView>(R.id.tvContent).startAnimation(animation)
        val width = view.width
        val height = ScreenUtils.dip2px(this, 44f)
        var animator = ValueAnimator.ofInt(1, height)
        animator.addUpdateListener { animation ->
            var percent = animation?.animatedValue as Int
            Log.d("xxxx", percent.toString())
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
        animator.duration = 900
        animator.interpolator = DecelerateInterpolator()
        animator.start()

    }

}
