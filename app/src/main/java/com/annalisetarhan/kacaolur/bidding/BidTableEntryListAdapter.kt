package com.annalisetarhan.kacaolur.bidding

import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.BidListBidBinding
import com.annalisetarhan.kacaolur.databinding.BidListQuestionBinding
import kotlinx.android.synthetic.main.bid_list_bid.view.*
import kotlinx.android.synthetic.main.bid_list_bid.view.courier_name
import kotlinx.android.synthetic.main.bid_list_question.view.*
import java.lang.IllegalArgumentException

const val BID = 1
const val QUESTION = 2

// saving reference to context scares me. surely there's a better way to do this

class BidTableEntryListAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var entries = emptyList<BidTableEntry>()
    val newAnswer = MutableLiveData<String>()
    val answeredEntry = MutableLiveData<BidTableEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BID -> {
                val binding = BidListBidBinding.inflate(inflater, parent, false)
                BidViewHolder(binding)
            }
            QUESTION -> {
                val binding = BidListQuestionBinding.inflate(inflater, parent, false)
                QuestionViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = entries[position]
        when (holder) {
            is BidViewHolder -> holder.bind(current)
            is QuestionViewHolder -> holder.bind(current)
        }
    }

    override fun getItemCount() = entries.size

    override fun getItemViewType(position: Int): Int {
        return if (entries[position].bidNotQuestion) {
            BID
        } else {
            QUESTION
        }
    }

    fun setEntries(entries: List<BidTableEntry>) {
        this.entries = entries
        notifyDataSetChanged()
    }

    inner class BidViewHolder(val binding: BidListBidBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: BidTableEntry) {
            binding.courierNameFormatted = entry.courierName
            binding.deliveryPriceFormatted = context.resources.getString(R.string.delivery_price_header, entry.deliveryPrice)
            binding.deliveryTimeFormatted = context.resources.getString(R.string.delivery_time_header, entry.deliveryTime)
        }
    }

    inner class QuestionViewHolder(val binding: BidListQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: BidTableEntry) {
            binding.courierNameFormatted = entry.courierName
            binding.questionFormatted = context.resources.getString(R.string.question_header, entry.question)
            binding.saveAnswerButton.visibility = View.GONE

            binding.answerButton.setOnClickListener {
                binding.answerButton.visibility = View.GONE
                binding.answerEditText.visibility = View.VISIBLE
                binding.saveAnswerButton.visibility = View.VISIBLE
            }

            binding.saveAnswerButton.setOnClickListener {
                val answer = binding.answerEditText.text
                binding.answerEditText.visibility = View.GONE
                binding.answerSavedText.text = context.resources.getString(R.string.saved_answer_header, answer)
                binding.answerSavedText.visibility = View.VISIBLE
                binding.saveAnswerButton.visibility = View.GONE
                saveAnswer(answer.toString(), entry)
            }
        }

        fun saveAnswer(answer: String, entry: BidTableEntry) {
            answeredEntry.value = entry
            newAnswer.value = answer
            notifyDataSetChanged()
        }
    }
}