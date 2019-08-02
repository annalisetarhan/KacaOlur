package com.annalisetarhan.kacaolur.bidding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.BiddingFragmentBinding
import kotlinx.android.synthetic.main.bid_list_question.*
import kotlinx.android.synthetic.main.bidding_fragment.*

class BiddingFragment : Fragment() {

    private lateinit var bidTableEntryViewModel: BidTableEntryViewModel
    private lateinit var adapter: BidTableEntryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: BiddingFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.bidding_fragment, container, false)

        val sharedPrefs = activity?.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)
        val itemName = sharedPrefs?.getString("item_name", "")
        val itemDescription = sharedPrefs?.getString("item_description", "")

        binding.itemNameFormatted = getString(R.string.item_name_header, itemName)

        if (itemDescription == "") {
            binding.itemDescriptionTextview.visibility = View.GONE
        } else {
            binding.itemDescriptionFormatted = getString(R.string.item_description_header, itemDescription)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = BidTableEntryListAdapter(context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)       // Used to be "activity"

        // ViewModelProviders creates ViewModel when activity starts, and persists it after activity is destroyed
        bidTableEntryViewModel = ViewModelProviders.of(this).get(BidTableEntryViewModel::class.java)

        bidTableEntryViewModel.allEntries.observe(this, Observer { entries ->
            entries?.let { adapter.setEntries(it) }
        })

        // I'm still iffy on this. I think it works, but if related things break, look here first
        adapter.newAnswer.observe(this, Observer { answer ->
            bidTableEntryViewModel.addAnswer(answer, adapter.answeredEntry.value!!.rowNum)
        })

    }
}