package com.annalisetarhan.kacaolur.welcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.annalisetarhan.kacaolur.Application
import com.annalisetarhan.kacaolur.R
import javax.inject.Inject

class WelcomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: com.annalisetarhan.kacaolur.databinding.WelcomeFragmentBinding
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.welcome_fragment, container, false)
        Application.appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WelcomeViewModel::class.java)

        binding.button.setOnClickListener {
            if (viewModel.userIsLoggedIn()) {
                if (it.findNavController().currentDestination?.id == R.id.welcomeFragment) {
                    findNavController().navigate(R.id.action_welcomeFragment_to_orderFragment)
                }
            } else {
                if (it.findNavController().currentDestination?.id == R.id.welcomeFragment) {
                    findNavController().navigate(R.id.action_welcomeFragment_to_authFragment)
                }
            }
        }
        return binding.root
    }
}