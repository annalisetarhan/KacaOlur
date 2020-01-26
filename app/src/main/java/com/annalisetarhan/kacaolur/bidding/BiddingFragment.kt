package com.annalisetarhan.kacaolur.bidding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.BiddingFragmentBinding
import kotlinx.android.synthetic.main.bidding_fragment.*

class BiddingFragment : Fragment() {

    private lateinit var binding: BiddingFragmentBinding
    private lateinit var viewModel: BiddingViewModel
    private lateinit var adapter: CourierResponseListAdapter

    private lateinit var sharedPrefs: SharedPreferences

    private lateinit var itemName: String
    private var itemDescription: String? = null

    private var responseListIsEmpty = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bidding_fragment, container, false)
        viewModel = ViewModelProvider(this).get(BiddingViewModel::class.java)

        watchForChangesInResponseTable()

        return binding.root
    }

    private fun watchForChangesInResponseTable() {
        viewModel.allEntries.observe(this, Observer { entries ->
            entries?.let { adapter.setEntries(it) }
            if (responseListIsEmpty &&  entries.isNotEmpty()) {                 // Part of trying to set up "wait for bids" screen
                responseListIsEmpty = false                                     // Can't say for sure if this will work with real data
                hideWaitForBidsMessage()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPrefs = activity!!.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)

        getItemInfo()
        displayItemInfo()
        setUpRecyclerView()

        if (sharedPrefs.getBoolean("has_accepted_bid", false)) {
            showViewOrderButton()
        } else {
            setLiveDataObservers()
        }

        if (responseListIsEmpty) {
            showWaitForBidsMessage()
        }
    }

    private fun getItemInfo() {
        itemName = sharedPrefs.getString("item_name", "")!!
        itemDescription = sharedPrefs.getString("item_description", "")
    }

    private fun displayItemInfo() {
        val item = getString(R.string.item_name_header, itemName)
        binding.itemNameFormatted = item

        if (itemDescription == "") {
            binding.itemDescriptionTextview.visibility = View.GONE
        } else {
            val description = getString(R.string.item_description_header, itemDescription)
            binding.itemDescriptionFormatted = description
        }
    }

    private fun setUpRecyclerView() {
        val hasAcceptedBid = sharedPrefs.getBoolean("has_accepted_bid", false)
        adapter = CourierResponseListAdapter(context!!, hasAcceptedBid)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setLiveDataObservers() {
        watchForNewAnswers()
        watchForAcceptedBid()
    }

    private fun watchForNewAnswers() {
        adapter.newAnswer.observe(this, Observer { answer ->
            viewModel.addAnswer(answer, adapter.answeredQuestion.value!!.splitSecondsSinceOrderPlaced)
        })
    }

    private fun watchForAcceptedBid() {
        adapter.acceptedBid.observe(this, Observer { acceptedBid ->
            viewModel.saveAcceptedBid(acceptedBid)
            if (this.findNavController().currentDestination?.id == R.id.biddingFragment) {
                findNavController().navigate(R.id.action_biddingFragment_to_waitingFragment)
            }
        })
    }

    private fun showViewOrderButton() {
        binding.viewOrderButton.visibility = View.VISIBLE
        binding.viewOrderButton.setOnClickListener {
            if (it.findNavController().currentDestination?.id == R.id.biddingFragment) {
                findNavController().navigate(R.id.action_biddingFragment_to_waitingFragment)
            }
        }
    }

    private fun showWaitForBidsMessage() {
        binding.recyclerView.visibility = View.GONE
        binding.waitForBidsTextview.visibility = View.VISIBLE
    }

    private fun hideWaitForBidsMessage() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.waitForBidsTextview.visibility = View.GONE
    }
}