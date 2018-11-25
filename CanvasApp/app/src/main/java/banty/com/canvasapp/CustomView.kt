package banty.com.canvasapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * Created by Banty on 25/11/18.
 */
class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private lateinit var drawPath: Path
    private lateinit var canvasPaint: Paint
    private lateinit var drawPaint: Paint
    private lateinit var drawCanvas: Canvas
    private lateinit var canvasBitmap: Bitmap
    private var paintColor: Int = resources.getColor(R.color.initial_paint_color)

    private var currentBrushSize: Float = 1F
    private var lastBrushSize: Float = 1F


    private val paths = ArrayList<Path>()
    private val undonePaths = ArrayList<Path>()
    private var mX: Float = 0F
    private var mY: Float = 0F
    private val TOUCH_TOLERANCE = 4

    init {
        initComponents()
    }


    fun initComponents() {
        currentBrushSize = resources.getInteger(R.integer.medium_size).toFloat()
        lastBrushSize = currentBrushSize

        drawPath = Path()
        drawPaint = Paint()
        drawPaint.color = paintColor
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = currentBrushSize
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND

        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onDraw(canvas: Canvas?) {
        for (p in paths) {
            canvas?.drawPath(p, drawPaint)
        }
        canvas?.drawPath(drawPath, drawPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var touchX = event?.x
        var touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (touchX != null && touchY != null) {
                    touchStart(touchX, touchY)
                    invalidate()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (touchX != null && touchY != null) {
                    touchMove(touchX, touchY)
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    private fun touchMove(touchX: Float, touchY: Float) {
        var dx = Math.abs(touchX - mX)
        var dy = Math.abs(touchY - mY)

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            drawPath.quadTo(mX, mY, (touchX + mX) / 2, (touchY + mY) / 2)
            mX = touchX
            mY = touchY
        }
    }

    private fun touchStart(touchX: Float, touchY: Float) {
        undonePaths.clear()
        drawPath.reset()
        drawPath.moveTo(touchX, touchY)
        mX = touchX
        mY = touchY
    }

    private fun touchUp() {
        drawPath.lineTo(mX, mY)
        drawCanvas.drawPath(drawPath, drawPaint)
        paths.add(drawPath)
        drawPath = Path()
    }

    fun eraseAll() {
        drawPath = Path()
        paths.clear()
        drawCanvas.drawColor(Color.WHITE)
        invalidate()
    }
}
