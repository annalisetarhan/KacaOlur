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

class BidTableEntryListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            setUpAcceptBidButton(entry)
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
                notifyDataSetChanged()
            }
        }
    }

    /*
     *      QUESTION VIEW HOLDER
     */

    inner class QuestionViewHolder(private val binding: BidListQuestionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: BidTableEntry) {
            showQuestion(entry)
            displayAppropriateAnsweringConfiguration(entry)
        }

        // Show Question

        private fun showQuestion(entry: BidTableEntry) {
            val question = context.resources.getString(R.string.question_header, entry.question)
            binding.courierNameFormatted = entry.courierName
            binding.questionFormatted = question
        }

        private fun displayAppropriateAnsweringConfiguration(entry: BidTableEntry) {
            if (entry.answer == null) {
                hideSaveAnswerButton()
                setUpAnswerButton()
                setUpSaveAnswerButton(entry)
            } else {
                hideEverything()
                showAnswer(entry.answer)
            }
        }

        private fun hideSaveAnswerButton() {
            binding.saveAnswerButton.visibility = View.GONE
        }

        // Set Up AnswerButton

        private fun setUpAnswerButton() {
            binding.answerButton.setOnClickListener {
                showAnswerButton()
                showAnsweringTools()
            }
        }

        private fun showAnswerButton() {
            binding.answerButton.visibility = View.GONE
        }

        private fun showAnsweringTools() {
            binding.answerEditText.visibility = View.VISIBLE
            binding.saveAnswerButton.visibility = View.VISIBLE
        }

        // Set Up SaveAnswerButton

        private fun setUpSaveAnswerButton(entry: BidTableEntry) {
            binding.saveAnswerButton.setOnClickListener {
                val answer = binding.answerEditText.text
                if (answer.toString() != "") {
                    saveAnswerToDatabase(answer.toString(), entry)
                    hideAnsweringTools()
                    showAnswer(answer.toString())
                }
            }
        }

        private fun showAnswer(answer: String) {
            binding.answerSavedText.text = context.resources.getString(R.string.saved_answer_header, answer)
            binding.answerSavedText.visibility = View.VISIBLE
        }

        private fun hideAnsweringTools() {
            binding.answerEditText.visibility = View.GONE
            binding.saveAnswerButton.visibility = View.GONE
        }

        // Hide Everything

        private fun hideEverything() {
            binding.answerButton.visibility = View.GONE
            binding.answerEditText.visibility = View.GONE
            binding.saveAnswerButton.visibility = View.GONE
        }

        private fun saveAnswerToDatabase(answer: String, entry: BidTableEntry) {
            answeredEntry.value = entry
            newAnswer.value = answer
            notifyDataSetChanged()
        }
    }
}