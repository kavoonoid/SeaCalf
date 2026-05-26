package com.teaml.seacalf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    private var progress = 0
    private var max = 0
    private var backgroundColor = Color.LTGRAY
    private var progressColor = Color.BLUE

    init {
        backgroundPaint.style = Paint.Style.FILL
        progressPaint.style = Paint.Style.FILL

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CircularProgressBar)
            backgroundColor = typedArray.getColor(R.styleable.CircularProgressBar_backgroundColor, backgroundColor)
            progressColor = typedArray.getColor(R.styleable.CircularProgressBar_progressColor, progressColor)
            progress = typedArray.getInt(R.styleable.CircularProgressBar_progress, progress)
            max = typedArray.getInt(R.styleable.CircularProgressBar_max, max)
            typedArray.recycle()
        }

        backgroundPaint.color = backgroundColor
        progressPaint.color = progressColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val size = Math.min(w, h)
        val left = (w - size) / 2f
        val top = (h - size) / 2f
        rectF.set(left, top, left + size, top + size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(rectF, 0f, 360f, true, backgroundPaint)

        val sweepAngle = (progress.toFloat() / max) * 360f
        if (sweepAngle > 0) {
            canvas.drawArc(rectF, -90f, sweepAngle, true, progressPaint)
        }
    }

    fun getMax(): Int = max

    fun setMax(value: Int) {
        max = value
        invalidate()
    }

    fun setProgress(value: Int) {
        progress = value.coerceIn(0, max)
        invalidate()
    }

    fun getProgress(): Int = progress
}