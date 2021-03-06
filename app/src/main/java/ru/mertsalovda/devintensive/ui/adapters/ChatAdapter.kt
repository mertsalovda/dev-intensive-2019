package ru.mertsalovda.devintensive.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_archive.*
import kotlinx.android.synthetic.main.item_chat_group.*
import kotlinx.android.synthetic.main.item_chat_single.*
import ru.mertsalovda.devintensive.R
import ru.mertsalovda.devintensive.models.data.ChatItem
import ru.mertsalovda.devintensive.models.data.ChatType

class ChatAdapter(
        private val listener: (ChatItem) -> Unit,
        private val archiveListener: (ChatItem) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder>() {

    companion object {
        private const val ARCHIVE_TYPE = 0
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    var items: List<ChatItem> = listOf()

    override fun getItemViewType(position: Int): Int = when (items[position].chatType) {
        ChatType.ARCHIVE -> ARCHIVE_TYPE
        ChatType.SINGLE -> SINGLE_TYPE
        ChatType.GROUP -> GROUP_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SINGLE_TYPE -> SingleViewHolder(inflater.inflate(R.layout.item_chat_single, parent, false))
            GROUP_TYPE -> GroupViewHolder(inflater.inflate(R.layout.item_chat_group, parent, false))
            ARCHIVE_TYPE -> ArchiveViewHolder(inflater.inflate(R.layout.item_chat_archive, parent, false))
            else -> SingleViewHolder(inflater.inflate(R.layout.item_chat_single, parent, false))
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        if (holder is ArchiveViewHolder) {
            holder.bind(items[position], archiveListener)
        } else {
            holder.bind(items[position], listener)
        }
    }

    fun updateData(data: List<ChatItem>) {

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

    /**
     * Базовый класс для [ViewHolder] саписка чатов.
     * Реализует [LayoutContainer] из библиотеки kotlinx.android.extensions
     *
     * @see SingleViewHolder ViewHolder для одиночного чата
     * @see GroupViewHolder ViewHolder для группового чата
     * @see ArchiveViewHolder ViewHolder для архива
     *
     * @param convertView view разметки элемента списка
     */
    abstract inner class ChatItemViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        abstract fun bind(item: ChatItem, listener: (ChatItem) -> Unit)

    }

    /**
     * Наследник [ChatItemViewHolder]. Представляет ViewHolder для одиночного чата.
     * Реализует [ItemTouchViewHolder]
     *
     */
    inner class SingleViewHolder(convertView: View) : ChatItemViewHolder(convertView), ItemTouchViewHolder {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            if (item.avatar == null) {
                Glide.with(itemView).clear(iv_avatar_single)
                iv_avatar_single.setInitials(item.initials ?: "??")
            } else {
                Glide.with(itemView)
                        .load(item.avatar)
                        .into(iv_avatar_single)
            }
            sv_indicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE

            tv_date_single.apply {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            tv_counter_single.apply {
                visibility = if (item.messageCount != 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            tv_title_single.text = item.title
            tv_message_single.text = item.shortDescription

            itemView.setOnClickListener { listener.invoke(item) }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }

    }

    /**
     * Наследник [ChatItemViewHolder]. Представляет ViewHolder для группового чата.
     * Реализует [ItemTouchViewHolder]
     *
     */
    inner class GroupViewHolder(convertView: View) : ChatItemViewHolder(convertView), ItemTouchViewHolder {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            iv_avatar_group.setInitials(item.initials)
            tv_date_group.apply {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            tv_counter_group.apply {
                visibility = if (item.messageCount != 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            tv_title_group.text = item.title
            tv_message_group.text = item.shortDescription
            tv_message_author.apply {
                visibility = if (item.messageCount != 0) View.VISIBLE else View.GONE
                text = item.author
            }

            itemView.setOnClickListener { listener.invoke(item) }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }

    }

    /**
     * Наследник [ChatItemViewHolder]. Представляет ViewHolder для архивированных чатов.
     * Реализует [ItemTouchViewHolder]
     *
     */
    inner class ArchiveViewHolder(convertView: View) : ChatItemViewHolder(convertView) {

        override fun bind(item: ChatItem, archiveListener: (ChatItem) -> Unit) {
            tv_counter_archive.apply {
                visibility = if (item.messageCount != 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            tv_title_archive.text = containerView!!.context.resources.getString(R.string.title_archive_chats)
            tv_author_archive.apply {
                visibility = if (item.messageCount != 0) View.VISIBLE else View.GONE
                text = item.title
            }
            tv_message_archive.text = if (tv_author_archive.visibility == View.GONE) "" else item.shortDescription
            itemView.setOnClickListener { archiveListener.invoke(item) }
        }

    }
}