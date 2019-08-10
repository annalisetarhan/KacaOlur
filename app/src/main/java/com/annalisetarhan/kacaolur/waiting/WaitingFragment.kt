package com.annalisetarhan.kacaolur.waiting

import android.content.SharedPreferences
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
import com.annalisetarhan.kacaolur.Time
import com.annalisetarhan.kacaolur.databinding.WaitingFragmentBinding
import kotlinx.android.synthetic.main.waiting_fragment.*


class WaitingFragment : Fragment() {

    private lateinit var binding: WaitingFragmentBinding
    private lateinit var viewModel: WaitingViewModel
    private lateinit var adapter: CourierMessageAdapter
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.waiting_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WaitingViewModel::class.java)

        watchForNewCustomerMessages()
        watchForNewCourierMessages()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPrefs = activity!!.getSharedPreferences(com.annalisetarhan.kacaolur.R.string.shared_prefs_filename.toString(), 0)

        setOrderInfo()
        setUpRecyclerView()
        setUpTimerOrButton()
        setUpCheaterButton()
    }

    private fun setOrderInfo() {
        val courierName = sharedPrefs.getString("courier_name", "")!!
        val deliveryPrice = sharedPrefs.getFloat("delivery_price", 0f)
        val deliveryTime = Time(sharedPrefs.getInt("delivery_time_in_seconds", 0))
        val deliveryTimeInMinutes = deliveryTime.getTimeInMinutes()

        binding.courierNameFormatted = courierName
        binding.deliveryPriceFormatted = getString(R.string.delivery_price_header, deliveryPrice)
        binding.deliveryTimeFormatted = getString(R.string.delivery_time_header, deliveryTimeInMinutes)
    }

    private fun setUpTimerOrButton() {
        val buttonNotTimer = sharedPrefs.getBoolean("waiting_for_item_inspection", false)
        if (buttonNotTimer) {
            setUpInspectItemButton()
        } else {
            setUpCountdownTimer()
        }
    }

    private fun setUpCountdownTimer() {
        binding.countdownTimer.visibility = View.VISIBLE
        binding.inspectItemButton.visibility = View.GONE

        viewModel.setUpCountdownTimer(context!!)
        viewModel.timeRemaining.observe(this, Observer { time ->
            binding.deliveryTimeForCountdown = time
        })
    }

    private fun setUpInspectItemButton() {
        binding.countdownTimer.visibility = View.INVISIBLE
        binding.inspectItemButton.visibility = View.VISIBLE

        binding.inspectItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_waitingFragment_to_confirmingFragment)
        }
    }

    private fun watchForNewCustomerMessages() {
        binding.sendMessageButton.setOnClickListener {
            val message = chatbox_edittext.text.toString()
            if (message != "") {
                viewModel.sendMessage(message)
                chatbox_edittext.text.clear()
            }
            scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun watchForNewCourierMessages() {
        viewModel.allMessages.observe(this, Observer { messages ->
            messages?.let { adapter.setMessages(it) }
            scrollToPosition(adapter.itemCount - 1)
        })
    }

    private fun setUpCheaterButton() {
        binding.cheaterButton.setOnClickListener {
            viewModel.pauseTimer(context!!)
            findNavController().navigate(R.id.action_waitingFragment_to_confirmingFragment)
        }
    }

    private fun setUpRecyclerView() {
        adapter = CourierMessageAdapter(context!!)
        courierMessageRecyclerView.adapter = adapter
        courierMessageRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun scrollToPosition(position: Int) {
        courierMessageRecyclerView.scrollToPosition(position)
    }
}