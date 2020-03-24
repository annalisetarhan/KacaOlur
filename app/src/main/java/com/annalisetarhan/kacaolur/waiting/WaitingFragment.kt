package com.annalisetarhan.kacaolur.waiting

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
import com.annalisetarhan.kacaolur.databinding.WaitingFragmentBinding
import javax.inject.Inject


class WaitingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: WaitingFragmentBinding
    private lateinit var viewModel: WaitingViewModel
    private lateinit var adapter: CourierMessageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.waiting_fragment, container, false)
        Application.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WaitingViewModel::class.java)

        watchForNewCustomerMessages()
        watchForNewCourierMessages()

        setOrderInfo()
        setUpRecyclerView()
        setUpTimerOrButton()
        setUpCheaterButton()

        return binding.root
    }

    private fun setOrderInfo() {
        val deliveryPrice = viewModel.getDeliveryPrice()
        val deliveryTime = viewModel.getDeliveryTime()

        binding.courierNameFormatted = viewModel.getCourierName()
        binding.deliveryPriceFormatted = getString(R.string.delivery_price_header, deliveryPrice)
        binding.deliveryTimeFormatted = getString(R.string.delivery_time_header, deliveryTime)
    }

    private fun setUpTimerOrButton() {
        if (viewModel.isWaitingForItemInspection()) {
            setUpInspectItemButton()
        } else {
            setUpCountdownTimer()
        }
    }

    private fun setUpCountdownTimer() {
        binding.countdownTimer.visibility = View.VISIBLE
        binding.inspectItemButton.visibility = View.GONE

        viewModel.setUpCountdownTimer(requireContext())
        viewModel.timeRemaining.observe(viewLifecycleOwner, Observer { time ->
            binding.deliveryTimeForCountdown = time
        })
    }

    private fun setUpInspectItemButton() {
        binding.countdownTimer.visibility = View.INVISIBLE
        binding.inspectItemButton.visibility = View.VISIBLE

        binding.inspectItemButton.setOnClickListener {
            if (it.findNavController().currentDestination?.id == R.id.waitingFragment) {
                findNavController().navigate(R.id.action_waitingFragment_to_confirmingFragment)
            }
        }
    }

    private fun watchForNewCustomerMessages() {
        binding.sendMessageButton.setOnClickListener {
            val message = binding.chatboxEdittext.text.toString()
            if (message != "") {
                viewModel.sendMessage(message)
                binding.chatboxEdittext.text.clear()
            }
            if (message == "0000") {        //  FOR TESTING ONLY
                viewModel.nukeData()
                findNavController().navigate(R.id.action_waitingFragment_to_authFragment)
            }
            scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun watchForNewCourierMessages() {
        viewModel.allMessages.observe(viewLifecycleOwner, Observer { messages ->
            messages?.let { adapter.setMessages(it) }
            scrollToPosition(adapter.itemCount - 1)
        })
    }

    private fun setUpCheaterButton() {
        binding.cheaterButton.setOnClickListener {
            viewModel.pauseTimer(requireContext())
            if (it.findNavController().currentDestination?.id == R.id.waitingFragment) {
                findNavController().navigate(R.id.action_waitingFragment_to_confirmingFragment)
            }
        }
    }

    private fun setUpRecyclerView() {
        adapter = CourierMessageAdapter(requireContext())
        binding.courierMessageRecyclerView.adapter = adapter
        binding.courierMessageRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun scrollToPosition(position: Int) {
        binding.courierMessageRecyclerView.scrollToPosition(position)
    }
}