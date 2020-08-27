package ru.skillbranch.devintensive.ui.custom

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * Drawable с текстом
 *
 * @property drawable объект [Drawable] поверх которого будут написан текст
 * @property initials текст
 * @property textSize размер текста
 */
class AvatarInitialsDrawable(private val drawable: Drawable, private val initials: String, private val textSize: Float = 80f) : Drawable() {

    private lateinit var paint: Paint
    private var textBounds: Rect = Rect()

    init {
        paint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            style = Paint.Style.FILL
        }
    }

    override fun draw(canvas: Canvas) {
        paint.textSize = textSize
        paint.getTextBounds(initials, 0, initials.length, textBounds)
        val bitmap: Bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth * 3, drawable.intrinsicHeight * 3, Bitmap.Config.ARGB_8888)
        }
        val canvas2 = Canvas(bitmap)
        drawable.setBounds(0, 0, bounds.width(), bounds.height())
        drawable.draw(canvas2)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.drawText(initials, bounds.centerX().toFloat(), bounds.centerY().toFloat() + textBounds.height() / 2, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}