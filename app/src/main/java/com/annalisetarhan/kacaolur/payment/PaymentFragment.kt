package com.annalisetarhan.kacaolur.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.PaymentFragmentBinding

class PaymentFragment : Fragment() {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var binding: PaymentFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.payment_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.update()

        setPriceInfo()
        setUpButtons()
    }

    private fun setPriceInfo() {
        binding.itemPriceFormatted = getString(R.string.item_price_capitalized, viewModel.itemPrice)
        binding.deliveryPriceFormatted = getString(R.string.delivery_price_capitalized , viewModel.deliveryPrice)
        binding.totalPriceFormatted = getString(R.string.total_price_header, viewModel.totalPrice)
    }

    private fun setUpButtons() {
        binding.geriButton.setOnClickListener {
            if (it.findNavController().currentDestination?.id == R.id.paymentFragment) {
                findNavController().navigate(R.id.action_paymentFragment_to_confirmingFragment)
            }
        }
        binding.payButton.setOnClickListener {
            viewModel.pay(context!!)
            if (it.findNavController().currentDestination?.id == R.id.paymentFragment) {
                findNavController().navigate(R.id.action_paymentFragment_to_waitingFragment)
            }
        }
    }
}