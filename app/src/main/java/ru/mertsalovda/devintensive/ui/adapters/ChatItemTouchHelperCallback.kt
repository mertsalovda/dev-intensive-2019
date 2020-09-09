package ru.mertsalovda.devintensive.ui.adapters

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.mertsalovda.devintensive.R
import ru.mertsalovda.devintensive.models.data.ChatItem

/**
 * Реализация [ItemTouchHelper.Callback] для обработки смахивания элементов списка
 */
class ChatItemTouchHelperCallback(
        private val adapter: ChatAdapter,
        private val type: TouchType = TouchType.ARCHIVE,
        private val swipeListener: (ChatItem) -> Unit) : ItemTouchHelper.Callback() {

    private val bgRect = RectF()
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val iconBounds = Rect()

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (viewHolder is ItemTouchViewHolder) {
            makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.START)
        } else {
            makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeListener.invoke(adapter.items[viewHolder.adapterPosition])

    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchViewHolder) {
            viewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is ItemTouchViewHolder) {
            viewHolder.onItemCleared()
        }
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            drawBackground(canvas, itemView, dX)
            drawItem(canvas, itemView, dX)
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * Нарисовать фон под смахиваемым элементом списка
     */
    private fun drawBackground(canvas: Canvas, itemView: View, dX: Float) {
        bgRect.apply {
            left = dX
            top = itemView.top.toFloat()
            right = itemView.right.toFloat()
            bottom = itemView.bottom.toFloat()
        }

        bgPaint.apply {
            color = itemView.resources.getColor(R.color.color_primary_dark, itemView.context.theme)
        }

        canvas.drawRect(bgRect, bgPaint)
    }

    /**
     * Нарисовать иконку для смахиваемого элемента списка
     */
    private fun drawItem(canvas: Canvas, itemView: View, dX: Float) {
        val icon = if(type == TouchType.ARCHIVE) {
            itemView.resources.getDrawable(R.drawable.ic_archive_white_24dp, itemView.context.theme)
        } else {
            itemView.resources.getDrawable(R.drawable.ic_unarchive_white_24dp, itemView.context.theme)
        }
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)
        val space = itemView.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)

        val margin = (itemView.bottom - itemView.top - iconSize) / 2
        iconBounds.apply {
            left = itemView.right + dX.toInt() + space
            top = itemView.top + margin
            right = itemView.right + dX.toInt() + iconSize + space
            bottom = itemView.bottom - margin
        }

        icon.bounds = iconBounds
        icon.draw(canvas)
    }
}

/**
 * Тип действия смахивания: Архивация или Разархивация
 *
 */
enum class TouchType{
    ARCHIVE, UNARCHIVE
}

/**
 * Интерфейс реализуют ViewHolder, которые можно смахивать.
 *
 */
interface ItemTouchViewHolder {
    fun onItemSelected()
    fun onItemCleared()
}