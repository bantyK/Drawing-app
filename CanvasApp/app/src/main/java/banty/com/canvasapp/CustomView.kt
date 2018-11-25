package banty.com.canvasapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

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
        canvas?.drawBitmap(canvasBitmap, 0F, 0F, canvasPaint)
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
                    drawPath.moveTo(touchX, touchY)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (touchX != null && touchY != null) {
                    drawPath.lineTo(touchX!!, touchY!!)
                }
            }

            MotionEvent.ACTION_UP -> {
                if (touchX != null && touchY != null) {
                    drawPath.lineTo(touchX, touchY);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                }
            }
            else -> {
                return false
            }
        }

        invalidate()
        return true
    }
}
