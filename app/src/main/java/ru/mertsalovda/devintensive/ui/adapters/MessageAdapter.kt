package ru.mertsalovda.devintensive.ui.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_message.*
import ru.mertsalovda.devintensive.R
import ru.mertsalovda.devintensive.extensions.shortFormat
import ru.mertsalovda.devintensive.models.BaseMessage
import ru.mertsalovda.devintensive.models.ImageMessage
import ru.mertsalovda.devintensive.models.TextMessage
import ru.mertsalovda.devintensive.utils.Utils

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    companion object {
        private const val TEXT = 10
        private const val IMAGE = 20
    }

    private var items = listOf<BaseMessage>()

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is TextMessage -> TEXT
        is ImageMessage -> IMAGE
        else -> throw IllegalStateException("Неизвествный формат сообщения")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.item_message, parent, false)
        return when (viewType) {
            TEXT -> TextMessageViewHolder(convertView)
            else -> ImageMessageViewHolder(convertView)
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

        /**
         * Устанавливает положение сообщения на разметке в зависимости от воходящее или исходящее
         *
         * @param item сообщение [BaseMessage]
         */
        protected fun setPositionMessage(item: BaseMessage) {
            if (item.isIncoming) {
                container.gravity = Gravity.START
                iv_avatar.visibility = View.VISIBLE
                bubble.setCardBackgroundColor(itemView.context.resources.getColor(R.color.color_message_in, itemView.context.theme))

                if (item.from.avatar == null) {
                    Glide.with(itemView).clear(iv_avatar)
                    val user = item.from
                    val initials = Utils.toInitials(user.firstName, user.lastName)
                    iv_avatar.setInitials(initials ?: "??")
                } else {
                    Glide.with(itemView)
                            .load(item.from.avatar)
                            .into(iv_avatar)
                }
            } else {
                container.gravity = Gravity.END
                iv_avatar.visibility = View.GONE
                bubble.setCardBackgroundColor(itemView.context.resources.getColor(R.color.color_message_out, itemView.context.theme))
            }
        }
    }

    inner class TextMessageViewHolder(convertView: View) : MessageViewHolder(convertView) {
        override fun bind(item: BaseMessage) {
            if (item !is TextMessage) return
            iv_content.visibility = View.GONE
            super.setPositionMessage(item)
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
            super.setPositionMessage(item)
            super.setTitleMessage(item)
            iv_content.apply {
                visibility = View.GONE
                Glide.with(containerView!!.context).load(item.image).into(iv_content)
            }
        }
    }

}