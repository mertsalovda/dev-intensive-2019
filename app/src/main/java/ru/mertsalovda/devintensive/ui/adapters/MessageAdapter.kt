package ru.mertsalovda.devintensive.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_message_in.* // TODO разобраться с биндингом
import ru.mertsalovda.devintensive.R
import ru.mertsalovda.devintensive.extensions.shortFormat
import ru.mertsalovda.devintensive.models.BaseMessage
import ru.mertsalovda.devintensive.models.ImageMessage
import ru.mertsalovda.devintensive.models.TextMessage

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    companion object {
        private const val TEXT_IN = 10
        private const val TEXT_OUT = 11
        private const val IMAGE_IN = 20
        private const val IMAGE_OUT = 21
    }

    private var items = listOf<BaseMessage>()

    override fun getItemViewType(position: Int): Int = when {
        items[position] is TextMessage && items[position].isIncoming -> TEXT_IN
        items[position] is TextMessage && !items[position].isIncoming -> TEXT_OUT
        items[position] is ImageMessage && items[position].isIncoming -> IMAGE_IN
        items[position] is ImageMessage && !items[position].isIncoming -> IMAGE_OUT
        else -> throw IllegalStateException("Неизвествный формат сообщения")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TEXT_IN -> {
                val convertView = inflater.inflate(R.layout.item_message_in, parent, false)
                TextMessageViewHolder(convertView)
            }
            TEXT_OUT -> {
                val convertView = inflater.inflate(R.layout.item_message_out, parent, false)
                TextMessageViewHolder(convertView)
            }
            IMAGE_IN -> {
                val convertView = inflater.inflate(R.layout.item_message_in, parent, false)
                ImageMessageViewHolder(convertView)
            }
            IMAGE_OUT -> {
                val convertView = inflater.inflate(R.layout.item_message_out, parent, false)
                ImageMessageViewHolder(convertView)
            }
            else -> throw IllegalStateException("Неизвествный формат сообщения")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        when (holder) {
            is TextMessageViewHolder -> holder.bind(items[position])
            is ImageMessageViewHolder -> holder.bind(items[position])
            else -> throw IllegalStateException("Неизвествный формат сообщения")
        }
    }

    fun updateData(data: List<BaseMessage>) {

        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
    }


    abstract inner class MessageViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        abstract fun bind(item: BaseMessage)
        /**
         * Устанавливает заголовок, формируемый из имени пользователя и даты отправки сообщения
         *
         * @param item сообщение [BaseMessage]
         */
        protected fun setTitleMessage(item: BaseMessage) {
            val from = item.from
            tv_name.text = "${from.firstName ?: ""} ${from.lastName ?: ""}".trim()
            tv_date.text = item.date.shortFormat()
        }
    }

    inner class TextMessageViewHolder(convertView: View) : MessageViewHolder(convertView) {
        override fun bind(item: BaseMessage) {
            if (item !is TextMessage) return
            iv_content.visibility = View.GONE
            super.setTitleMessage(item)
            tv_message.apply {
                text = item.text
                visibility = View.VISIBLE
            }
        }
    }

    inner class ImageMessageViewHolder(convertView: View) : MessageViewHolder(convertView) {
        override fun bind(item: BaseMessage) {
            if (item !is ImageMessage) return
            tv_message.visibility = View.VISIBLE
            super.setTitleMessage(item)
            iv_content.apply {
                visibility = View.GONE
                Glide.with(containerView!!.context).load(item.image).into(iv_content)
            }
        }
    }

}