package com.annalisetarhan.kacaolur.bidding

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
import com.annalisetarhan.kacaolur.Application
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.BiddingFragmentBinding
import javax.inject.Inject

class BiddingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BiddingViewModel
    private lateinit var binding: BiddingFragmentBinding
    private lateinit var adapter: CourierResponseListAdapter

    private lateinit var itemName: String
    private var itemDescription: String? = null

    private var responseListIsEmpty = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bidding_fragment, container, false)
        Application.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BiddingViewModel::class.java)

        watchForChangesInResponseTable()

        getItemInfo()
        displayItemInfo()
        setUpRecyclerView()

        if (viewModel.hasAcceptedBid()) {
            showViewOrderButton()
        } else {
            setLiveDataObservers()
        }

        if (responseListIsEmpty) {
            showWaitForBidsMessage()
        }

        return binding.root
    }

    private fun watchForChangesInResponseTable() {
        viewModel.allEntries.observe(viewLifecycleOwner, Observer { entries ->
            entries?.let { adapter.setEntries(it) }
            if (responseListIsEmpty &&  entries.isNotEmpty()) {                 // Part of trying to set up "wait for bids" screen
                responseListIsEmpty = false                                     // Can't say for sure if this will work with real data
                hideWaitForBidsMessage()
            }
        })
    }

    private fun getItemInfo() {
        itemName = viewModel.getItemName()
        itemDescription = viewModel.getItemDescription()
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
        adapter = CourierResponseListAdapter(requireContext(), viewModel.hasAcceptedBid())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setLiveDataObservers() {
        watchForNewAnswers()
        watchForAcceptedBid()
    }

    private fun watchForNewAnswers() {
        adapter.newAnswer.observe(viewLifecycleOwner, Observer { answer ->
            viewModel.addAnswer(answer, adapter.answeredQuestion.value!!.splitSecondsSinceOrderPlaced)
        })
    }

    private fun watchForAcceptedBid() {
        adapter.acceptedBid.observe(viewLifecycleOwner, Observer { acceptedBid ->
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