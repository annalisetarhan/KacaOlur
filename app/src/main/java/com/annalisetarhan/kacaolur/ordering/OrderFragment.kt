package com.annalisetarhan.kacaolur.ordering

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.OrderFragmentBinding
import kotlinx.android.synthetic.main.order_fragment.*

class OrderFragment : Fragment() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var binding: OrderFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.order_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)

        if (viewModel.orderAccepted) {
            sterilizeFragment()
        } else {
            setUpNormalFragment()
        }

        return binding.root
    }

    private fun sterilizeFragment() {
        binding.itemNameEditText.isEnabled = false
        binding.itemDescriptionEditText.isEnabled = false
        binding.attachPictureButton.visibility = View.GONE
        binding.kacaButton.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_biddingFragment)
        }
    }

    private fun setUpNormalFragment() {
        binding.kacaButton.setOnClickListener {
            val itemName = item_name_edit_text.text.toString()
            val itemDescription = item_description_edit_text.text.toString()
            val order = Order(itemName, itemDescription)

            if (itemName == "") {
                insistOnItemName()
            } else {
                acceptOrder(order)
            }
        }
    }

    private fun acceptOrder(order: Order) {
        viewModel.acceptOrder(order)
        findNavController().navigate(R.id.action_orderFragment_to_biddingFragment)
    }

    private fun insistOnItemName() {
        val toast = Toast.makeText(context, R.string.what_do_you_need, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 300)
        toast.show()
    }
}
