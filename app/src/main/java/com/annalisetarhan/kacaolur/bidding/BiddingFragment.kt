package com.annalisetarhan.kacaolur.bidding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.BiddingFragmentBinding
import kotlinx.android.synthetic.main.bidding_fragment.*

class BiddingFragment : Fragment() {

    private lateinit var bidTableEntryViewModel: BidTableEntryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: BiddingFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.bidding_fragment, container, false)

        val sharedPrefs = activity?.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)
        val itemName = sharedPrefs?.getString("item_name", "")
        val itemDescription = sharedPrefs?.getString("item_description", "")

        binding.itemNameVar = itemName
        binding.itemDescriptionVar = itemDescription

        // This is janky af
        binding.itemNameTextview.text = getString(R.string.item_name_header) + "  " + itemName
        binding.itemDescriptionTextview.text = getString(R.string.item_description_header) + "  " + itemDescription

        bidTableEntryViewModel = ViewModelProviders.of(this).get(BidTableEntryViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.adapter = BidTableEntryListAdapter(context!!)       // Can I really put !! here?
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }
}