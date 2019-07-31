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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: OrderFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.order_fragment, container, false
        )

        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)

        binding.kacaButton.setOnClickListener {
            var itemName = item_name_edit_text.getText().toString()
            var itemDescription = item_description_edit_text.getText().toString()

            // If user didn't fill in item name, prompt them
            if (itemName == "") {
                val toast = Toast.makeText(context, R.string.what_do_you_need, Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 300)
                toast.show()
            } else {
                viewModel.setItemInfo(itemName, itemDescription)
                findNavController().navigate(R.id.action_orderFragment_to_biddingFragment)
            }

        }
        return binding.root
    }
}
