package com.annalisetarhan.kacaolur.bidding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.BidListBidBinding
import com.annalisetarhan.kacaolur.databinding.BidListQuestionBinding
import java.lang.IllegalArgumentException

const val BID = 1
const val QUESTION = 2

class BidTableEntryListAdapter(val context: Context, val shouldHideButtons: Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var entries = emptyList<BidTableEntry>()

    val newAnswer = MutableLiveData<String>()
    val answeredEntry = MutableLiveData<BidTableEntry>()
    val acceptedBid = MutableLiveData<BidTableEntry>()

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

    override fun getItemViewType(position: Int): Int {
        return if (entries[position].bidNotQuestion) {
            BID
        } else {
            QUESTION
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = entries[position]
        when (holder) {
            is BidViewHolder -> holder.bind(current)
            is QuestionViewHolder -> holder.bind(current)
        }
        println(current.courierName + " bound")
    }

    fun setEntries(entries: List<BidTableEntry>) {
        this.entries = entries
        notifyDataSetChanged()
    }

    override fun getItemCount() = entries.size

    /*
     *      BID VIEW HOLDER
     */

    inner class BidViewHolder(private val binding: BidListBidBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: BidTableEntry) {
            displayBid(entry)
            if (shouldHideButtons) {
                binding.acceptBidButton.visibility = View.GONE
            } else {
                setUpAcceptBidButton(entry)
            }
        }

        private fun displayBid(entry: BidTableEntry) {
            val price = context.resources.getString(R.string.delivery_price_header, entry.deliveryPrice)
            val time = context.resources.getString(R.string.delivery_time_header, entry.deliveryTime)

            binding.courierNameFormatted = entry.courierName
            binding.deliveryPriceFormatted = price
            binding.deliveryTimeFormatted = time
        }

        private fun setUpAcceptBidButton(entry: BidTableEntry) {
            binding.acceptBidButton.setOnClickListener {
                acceptedBid.value = entry
            }
        }
    }

    /*
     *      QUESTION VIEW HOLDER
     */

    inner class QuestionViewHolder(private val binding: BidListQuestionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: BidTableEntry) {
            showQuestion(entry)
            hideEverything()
            if (entry.answer != null) {
                showAnswer(entry.answer)
            } else if (!shouldHideButtons) {
                showAnswerConfiguration(entry)
            }
        }

        private fun showQuestion(entry: BidTableEntry) {
            val question = context.resources.getString(R.string.question_header, entry.question)
            binding.courierNameFormatted = entry.courierName
            binding.questionFormatted = question
        }

        private fun hideEverything() {
            binding.answerButton.visibility = View.GONE
            binding.saveButton.visibility = View.GONE
            binding.answerEditText.visibility = View.GONE
            binding.answerSavedText.visibility = View.GONE
        }

        private fun showAnswer(answer: String) {
            binding.answerSavedText.text = context.resources.getString(R.string.saved_answer_header, answer)
            binding.answerSavedText.visibility = View.VISIBLE
        }

        private fun showAnswerConfiguration(entry: BidTableEntry) {
            showAnswerButton()
            setUpAnswerButtonClickListener()
            setUpSaveButtonClickListener(entry)
        }

        private fun showAnswerButton() {
            binding.answerButton.visibility = View.VISIBLE
        }

        private fun setUpAnswerButtonClickListener() {
            binding.answerButton.setOnClickListener {
                binding.answerButton.visibility = View.GONE
                binding.answerEditText.visibility = View.VISIBLE
                binding.saveButton.visibility = View.VISIBLE
            }
        }

        private fun setUpSaveButtonClickListener(entry: BidTableEntry) {
            binding.saveButton.setOnClickListener {
                val answer = binding.answerEditText.text.toString()
                if (answer != "") {
                    saveAnswerToDatabase(answer, entry)
                    hideAnsweringTools()
                    showAnswer(answer)
                }
            }
        }

        private fun hideAnsweringTools() {
            binding.answerEditText.visibility = View.GONE
            binding.saveButton.visibility = View.GONE
        }

        private fun saveAnswerToDatabase(answer: String, entry: BidTableEntry) {
            answeredEntry.value = entry
            newAnswer.value = answer
        }
    }
}