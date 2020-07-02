package com.example.mycoroutines

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 测试QQ音乐弹幕动画
 */
class MainActivity : AppCompatActivity() {

    val TAG = "协程:8年老Android开发谈；Context都没弄明白凭什么拿高薪？"
    var i = 0L
    private lateinit var inflater: LayoutInflater

    private var endX = 0f
    private var endY = 0f
    private var startX = 0f
    private var startY = 0f
    private var list = mutableListOf<String>()
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inflater = LayoutInflater.from(this)

        ivStart.post {
            endX = ivEnd.x + ivEnd.width / 2f - ivStart.width / 2f
            endY = ivEnd.y + ivEnd.height / 2f - ivStart.height / 2f
            startX = ivStart.x
            startY = ivStart.y
        }

        ivStart.setOnClickListener {
            anim()
            handler?.sendEmptyMessage(1)
        }
        for (index in 0..30) {
            var str = TAG.substring(0, 5 + java.util.Random().nextInt(TAG.length - 5))
            list.add(str)
        }
        var popMsgHelper = PopMsgHelper(llParent, this)
        popMsgHelper.setMsgList(list)
        popMsgHelper.startMsgPop()
        thread.start()
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

    private var thread = object : Thread() {
        override fun run() {
            super.run()
            Looper.prepare()
            handler = object : Handler(Looper.myLooper()) {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    Log.d("TAG", "handleMessage: HELLO WORLD")
                }
            }

            Looper.loop()
            while (true) {
                sleep(1000)
            }
        }
    }

}
