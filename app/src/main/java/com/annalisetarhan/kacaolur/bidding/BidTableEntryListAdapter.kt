package com.annalisetarhan.kacaolur.bidding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annalisetarhan.kacaolur.R
import kotlinx.android.synthetic.main.bid_list_bid.view.*
import kotlinx.android.synthetic.main.bid_list_bid.view.courier_name
import kotlinx.android.synthetic.main.bid_list_question.view.*
import java.lang.IllegalArgumentException

const val BID = 1
const val QUESTION = 2

class BidTableEntryListAdapter(context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var entries = listOf<BidTableEntry>(
        BidTableEntry(0, "Mehmet T.", false, null, null, "domates or biber?"),
        BidTableEntry(1, "Mehmet B.", true, 5f, 50, null)
    )

    // By some magic, viewType needs to be passed in according to BID = 1, QUESTION = 2
    // That magic seems to be getItemViewType :) :) :)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BID -> {
                val itemView = inflater.inflate(R.layout.bid_list_bid, parent, false)
                BidViewHolder(itemView)
            }
            QUESTION -> {
                val itemView = inflater.inflate(R.layout.bid_list_question, parent, false)
                QuestionViewHolder(itemView)
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

    inner class BidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entry: BidTableEntry) {
            println("binding bid")
            itemView.courier_name.text = entry.courierName
            itemView.delivery_price.text = entry.deliveryPrice.toString()
            itemView.delivery_time.text = entry.deliveryTime.toString()
        }
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entry: BidTableEntry) {
            itemView.courier_name.text = entry.courierName
            itemView.question.text = entry.question
        }
    }
}