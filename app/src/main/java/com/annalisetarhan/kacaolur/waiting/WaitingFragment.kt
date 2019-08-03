package com.annalisetarhan.kacaolur.waiting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.WaitingFragmentBinding

class WaitingFragment : Fragment() {

    private lateinit var binding: WaitingFragmentBinding
    private lateinit var viewModel: WaitingViewModel
    //private lateinit var adapter: WaitingMessagesAdapter

    private lateinit var itemName: String
    private var itemDescription: String? = null
    private var deliveryPrice: Float = 0f
    private var deliveryTime: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.waiting_fragment, container, false)

        getOrderInfo()

        return binding.root
    }

    private fun getOrderInfo() {
        val sharedPrefs = activity!!.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)
        //itemName = sharedPrefs.getString("item_name")!!
       // itemDescription =
    }
}