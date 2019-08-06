package com.annalisetarhan.kacaolur.confirming

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.databinding.ConfirmingFragmentBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.confirming_fragment.*

class ConfirmingFragment : Fragment() {

    private lateinit var binding: ConfirmingFragmentBinding
    private lateinit var viewModel: ConfirmingViewModel

    private val salcaUrl = "https://www.altunbilekler.com/Uploads/UrunResimleri/tat-domates-salca-720-gr-cam-d586.jpg"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.confirming_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(ConfirmingViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getItemPrice()
        loadPicture()

        setUpButtons()
    }

    private fun getItemPrice() {
        val sharedPrefs = activity!!.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)
        val itemPrice = sharedPrefs.getFloat("item_price", 5.50f)
        val itemPriceFormatted = getString(R.string.item_price_header, itemPrice)
        binding.itemPriceFormatted = itemPriceFormatted
    }

    private fun loadPicture() {
        Picasso
            .get()
            .load(salcaUrl)
            .into(imageView)
    }

    private fun setUpButtons() {
        setAcceptButton()
        setRejectButton()
        setGeriButton()
        setGonderButton()
    }

    private fun setAcceptButton() {
        binding.acceptButton.setOnClickListener {
            // TODO: go to yay screen
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
            binding.complaintCardview.visibility = View.INVISIBLE
            binding.acceptButton.visibility = View.VISIBLE
            binding.rejectButton.visibility = View.VISIBLE
            binding.geriButton.visibility = View.GONE
            binding.gonderButton.visibility = View.GONE
        }
    }

    private fun setGonderButton() {
        binding.gonderButton.setOnClickListener {
            val complaintString = complaintBox.text.toString()

            if (complaintString == "") {
                askForReason()
            } else {
                acceptApproval()
            }
        }
    }

    private fun acceptApproval() {
        viewModel.unPauseTimer(context!!)
        findNavController().navigate(R.id.action_confirmingFragment_to_waitingFragment)
        // TODO: fill in server
    }

    private fun askForReason() {
        val toast = Toast.makeText(context, "Please tell your courier how to fix the problem.", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0, 300)
        toast.show()
    }

}