package com.annalisetarhan.kacaolur.confirming

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.kacaolur.Application
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.ConfirmingFragmentBinding
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ConfirmingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ConfirmingViewModel
    private lateinit var binding: ConfirmingFragmentBinding

    private val salcaUrl = "https://www.altunbilekler.com/Uploads/UrunResimleri/tat-domates-salca-720-gr-cam-d586.jpg"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.confirming_fragment, container, false)
        Application.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ConfirmingViewModel::class.java)

        getItemPrice()
        loadPicture()
        setUpButtons()

        return binding.root
    }

    private fun getItemPrice() {
        binding.itemPriceFormatted = context?.let { viewModel.getFormattedItemPrice(it) }
    }

    private fun loadPicture() {
        // TODO: Loading picture
        Picasso
            .get()
            .load(salcaUrl)
            .into(binding.imageView)
    }

    private fun setUpButtons() {
        setAcceptButton()
        setRejectButton()
        setGeriButton()
        setGonderButton()
    }

    private fun setAcceptButton() {
        binding.acceptButton.setOnClickListener {
            if (it.findNavController().currentDestination?.id == R.id.confirmingFragment) {
                findNavController().navigate(R.id.action_confirmingFragment_to_paymentFragment)
            }
        }
    }

    private fun setRejectButton() {
        binding.rejectButton.setOnClickListener {
            binding.complaintCardview.visibility = View.VISIBLE
            binding.geriButton.visibility = View.VISIBLE
            binding.gonderButton.visibility = View.VISIBLE

            binding.acceptButton.visibility = View.GONE
            binding.rejectButton.visibility = View.GONE
        }
    }

    private fun setGeriButton() {
        binding.geriButton.setOnClickListener {
            binding.acceptButton.visibility = View.VISIBLE
            binding.rejectButton.visibility = View.VISIBLE

            binding.complaintCardview.visibility = View.INVISIBLE
            binding.geriButton.visibility = View.GONE
            binding.gonderButton.visibility = View.GONE
        }
    }

    private fun setGonderButton() {
        binding.gonderButton.setOnClickListener {
            val complaintString = binding.complaintBox.text.toString()

            if (complaintString == "") {
                askForReason()
            } else {
                viewModel.itemRejected(requireContext(), complaintString)
                // TODO: fill in server
                if (it.findNavController().currentDestination?.id == R.id.confirmingFragment) {
                    findNavController().navigate(R.id.action_confirmingFragment_to_waitingFragment)
                }
            }
        }
    }

    private fun askForReason() {
        val toast = Toast.makeText(context, getString(R.string.tell_courier_how_to_fix), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 300)
        toast.show()
    }

}