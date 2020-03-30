package com.annalisetarhan.kacaolur.bidding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.utils.Time
import com.annalisetarhan.kacaolur.databinding.ResponseListBidBinding
import com.annalisetarhan.kacaolur.databinding.ResponseListQuestionBinding
import java.lang.IllegalArgumentException

// TODO: sealed class?
const val BID = 1
const val QUESTION = 2

class CourierResponseListAdapter(val context: Context, val shouldHideButtons: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var responses = emptyList<CourierResponse>()

    private val _newAnswer = MutableLiveData<String>()
    private val _answeredQuestion = MutableLiveData<CourierResponse>()
    private val _acceptedBid = MutableLiveData<CourierResponse>()

    val newAnswer: LiveData<String>
        get() = _newAnswer

    val answeredQuestion: LiveData<CourierResponse>
        get() = _answeredQuestion

    val acceptedBid: LiveData<CourierResponse>
        get() = _acceptedBid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BID -> {
                val binding = ResponseListBidBinding.inflate(inflater, parent, false)
                BidViewHolder(binding)
            }
            QUESTION -> {
                val binding = ResponseListQuestionBinding.inflate(inflater, parent, false)
                QuestionViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (responses[position].bidNotQuestion) {
            BID
        } else {
            QUESTION
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = responses[position]
        when (holder) {
            is BidViewHolder -> holder.bind(current)
            is QuestionViewHolder -> holder.bind(current)
        }
    }

    fun setEntries(responses: List<CourierResponse>) {
        this.responses = responses
        notifyDataSetChanged()
    }

    override fun getItemCount() = responses.size

    /*
     *      BID VIEW HOLDER
     */

    inner class BidViewHolder(private val binding: ResponseListBidBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(response: CourierResponse) {
            displayBid(response)
            if (shouldHideButtons) {
                binding.acceptBidButton.visibility = View.GONE
            } else {
                setUpAcceptBidButton(response)
            }
        }

        private fun displayBid(response: CourierResponse) {
            val deliveryPrice = context.resources.getString(R.string.delivery_price_header_bold, response.deliveryPrice)
            val timeString = Time(response.deliveryTimeInSeconds!!).getTimeInMinutes()
            val deliveryTime = context.resources.getString(R.string.delivery_time_header_bold, timeString)

            val priceFormatted = HtmlCompat.fromHtml(deliveryPrice, HtmlCompat.FROM_HTML_MODE_LEGACY)
            val timeFormatted = HtmlCompat.fromHtml(deliveryTime, HtmlCompat.FROM_HTML_MODE_LEGACY)

            binding.courierNameFormatted = response.courierName
            binding.deliveryPriceFormatted = priceFormatted
            binding.deliveryTimeFormatted = timeFormatted
        }

        private fun setUpAcceptBidButton(response: CourierResponse) {
            binding.acceptBidButton.setOnClickListener {
                _acceptedBid.value = response
            }
        }
    }

    /*
     *      QUESTION VIEW HOLDER
     */

    inner class QuestionViewHolder(private val binding: ResponseListQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: CourierResponse) {
            resetViewHolderForNewData()
            showQuestion(response)
            if (response.answer != null) {
                showAnswer(response.answer)
            } else if (!shouldHideButtons) {
                showAnswerConfiguration(response)
            }
        }

        private fun showQuestion(response: CourierResponse) {
            val question = context.resources.getString(R.string.question_header, response.question)
            binding.courierNameFormatted = response.courierName
            binding.questionFormatted = question
        }

        private fun resetViewHolderForNewData() {
            binding.answerButton.visibility = View.GONE
            binding.saveButton.visibility = View.GONE
            binding.answerEditText.visibility = View.GONE
            binding.answerSavedText.visibility = View.GONE
        }

        private fun showAnswer(answer: String) {
            binding.answerSavedText.text = context.resources.getString(R.string.saved_answer_header, answer)
            binding.answerSavedText.visibility = View.VISIBLE
        }

        private fun showAnswerConfiguration(response: CourierResponse) {
            showAnswerButton()
            setUpAnswerButtonClickListener()
            setUpSaveButtonClickListener(response)
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

        private fun setUpSaveButtonClickListener(response: CourierResponse) {
            binding.saveButton.setOnClickListener {
                val answer = binding.answerEditText.text.toString().trim()
                if (answer != "") {
                    saveAnswerToDatabase(answer, response)
                    hideAnsweringTools()
                    showAnswer(answer)
                }
            }
        }

        private fun hideAnsweringTools() {
            binding.answerEditText.visibility = View.GONE
            binding.saveButton.visibility = View.GONE
        }

        private fun saveAnswerToDatabase(answer: String, response: CourierResponse) {
            _answeredQuestion.value = response
            _newAnswer.value = answer
        }
    }
}