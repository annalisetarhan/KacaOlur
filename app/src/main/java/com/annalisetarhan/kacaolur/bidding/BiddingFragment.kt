package com.annalisetarhan.kacaolur.bidding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.BiddingFragmentBinding
import kotlinx.android.synthetic.main.bidding_fragment.*

class BiddingFragment : Fragment() {

    private lateinit var binding: BiddingFragmentBinding

    private lateinit var biddingViewModel: BiddingViewModel
    private lateinit var adapter: BidTableEntryListAdapter

    private var itemName: String? = null
    private var itemDescription: String? = null

    // OnCreateView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bidding_fragment, container, false)

        getItemInfo()
        displayItemInfo()

        return binding.root
    }

    private fun getItemInfo() {
        val sharedPrefs = activity?.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)
        itemName = sharedPrefs?.getString("item_name", "")
        itemDescription = sharedPrefs?.getString("item_description", "")
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

    // OnActivityCreated

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpRecyclerView()
        getViewModel()
        setLiveDataObservers()
    }

    private fun setUpRecyclerView() {
        adapter = BidTableEntryListAdapter(context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun getViewModel() {
        // ViewModelProviders creates ViewModel when activity starts, and persists it after activity is destroyed
        biddingViewModel = ViewModelProviders.of(this).get(BiddingViewModel::class.java)
    }

    // LiveData Observers

    private fun setLiveDataObservers() {
        watchForNewBidTableEntries()
        watchForNewAnswers()
        watchForAcceptedBid()
    }

    private fun watchForNewBidTableEntries() {
        biddingViewModel.allEntries.observe(this, Observer { entries ->
            entries?.let { adapter.setEntries(it) }
        })
    }

    private fun watchForNewAnswers() {
        // I'm still iffy on this. I think it works, but if related things break, look here first
        adapter.newAnswer.observe(this, Observer { answer ->
            biddingViewModel.addAnswer(answer, adapter.answeredEntry.value!!.rowNum)
        })
    }

    private fun watchForAcceptedBid() {
        adapter.acceptedBid.observe(this, Observer { acceptedBid ->
            biddingViewModel.saveAcceptedBid(acceptedBid)
            findNavController().navigate(R.id.action_biddingFragment_to_waitingFragment)
        })
    }
}