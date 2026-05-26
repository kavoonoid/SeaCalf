package com.teaml.seacalf

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class CurvedBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private val curvePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_blue)
        style = Paint.Style.FILL
    }

    private val path = Path()
    private var selectedPosition = 0
    private var curveProgress = 0f

    init {
        background = null
        elevation = 0f

        setOnItemSelectedListener { item ->
            val newPosition = getMenuItemPosition(item)
            if (newPosition != selectedPosition) {
                animateToPosition(newPosition)
            }
            true
        }
    }

    private fun getMenuItemPosition(item: android.view.MenuItem): Int {
        val menu = menu
        for (i in 0 until menu.size()) {
            if (menu.getItem(i) == item) {
                return i
            }
        }
        return 0
    }

    private fun animateToPosition(newPosition: Int) {
        ValueAnimator.ofFloat(selectedPosition.toFloat(), newPosition.toFloat()).apply {
            duration = 280
            interpolator = OvershootInterpolator(0.8f)
            addUpdateListener { animator ->
                curveProgress = animator.animatedValue as Float
                invalidate()
            }
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    selectedPosition = newPosition
                    curveProgress = newPosition.toFloat()
                }
            })
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()
        val itemWidth = width / 3f

        val centerX = itemWidth * (curveProgress + 0.5f)

        // Основной фон
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(width, 0f)
        path.lineTo(width, height)

        // Выпуклость
        val curveWidth = itemWidth * 0.85f
        val curveHeight = dpToPx(32).toFloat()

        path.lineTo(centerX + curveWidth / 2, height)
        path.cubicTo(
            centerX + curveWidth * 0.35f, height,
            centerX + curveWidth * 0.25f, height - curveHeight,
            centerX, height - curveHeight * 0.9f
        )
        path.cubicTo(
            centerX - curveWidth * 0.25f, height - curveHeight * 0.9f,
            centerX - curveWidth * 0.35f, height,
            centerX - curveWidth / 2, height
        )
        path.lineTo(0f, height)
        path.close()

        canvas.drawPath(path, curvePaint)

        super.onDraw(canvas)
    }

    // Extension как member function
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}