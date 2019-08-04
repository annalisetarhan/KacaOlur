package com.annalisetarhan.kacaolur.waiting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.annalisetarhan.kacaolur.databinding.SentMessageBinding
import com.annalisetarhan.kacaolur.databinding.ReceivedMessageBinding
import java.lang.IllegalArgumentException

const val RECEIVED = 1
const val SENT = 2

class CourierMessageAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var messages = emptyList<CourierMessage>()

    val newMessage = MutableLiveData<CourierMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RECEIVED -> {
                val binding = ReceivedMessageBinding.inflate(inflater, parent, false)
                ReceivedMessageViewHolder(binding)
            }
            SENT -> {
                val binding = SentMessageBinding.inflate(inflater, parent, false)
                SentMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].fromCourier) {
            RECEIVED
        } else {
            SENT
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = messages[position]
        when (holder) {
            is ReceivedMessageViewHolder -> holder.bind(current)
            is SentMessageViewHolder -> holder.bind(current)
        }
    }

    fun setMessages(messages: List<CourierMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemCount() = messages.size

    inner class ReceivedMessageViewHolder(private val binding: ReceivedMessageBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: CourierMessage) {
            binding.message = message.message
            binding.timeStamp = message.timeStamp
        }
    }

    inner class SentMessageViewHolder(private val binding: SentMessageBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: CourierMessage) {
            binding.message = message.message
            binding.timeStamp = message.timeStamp
        }
    }
}