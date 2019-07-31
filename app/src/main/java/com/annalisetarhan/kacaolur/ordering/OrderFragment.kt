package com.annalisetarhan.kacaolur.ordering

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.OrderFragmentBinding
import kotlinx.android.synthetic.main.order_fragment.*

class OrderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: OrderFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.order_fragment, container, false
        )

        // I think I'm playing with fire by accessing sharedpreferences here,
        // but a user couldn't actually click the button before onActivityCreated, right?

        binding.kacaButton.setOnClickListener() {
            val itemName = item_name_edit_text.text.toString()
            val itemDescription = item_description_edit_text.text.toString()

            // If user didn't fill in item name, prompt them
            if (itemName == "") {
                // Why can I use "context" like this? Because Kotlin?
                val toast = Toast.makeText(context, R.string.what_do_you_need, Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 300)
                toast.show()

            } else {
                // Create new order and send it to server
                // Should this be context!! or activity!! or neither?
                val sharedPrefs = context!!.getSharedPreferences(getString(R.string.shared_prefs_filename), 0)
                val editor = sharedPrefs.edit()
                editor.putString("item_name", itemName)
                editor.putString("item_description", itemDescription)
                editor.apply()

                // Go to set location fragment
                // TODO Get Maps API key & implement SetLocationFragment

                // But for now, just go to bidding fragment
                findNavController().navigate(R.id.action_orderFragment_to_biddingFragment)
            }
        }
        return binding.root
    }
}