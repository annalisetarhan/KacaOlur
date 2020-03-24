package com.annalisetarhan.kacaolur.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.annalisetarhan.kacaolur.authing.AuthViewModel
import com.annalisetarhan.kacaolur.bidding.BiddingViewModel
import com.annalisetarhan.kacaolur.confirming.ConfirmingViewModel
import com.annalisetarhan.kacaolur.ordering.OrderViewModel
import com.annalisetarhan.kacaolur.payment.PaymentViewModel
import com.annalisetarhan.kacaolur.waiting.WaitingViewModel
import com.annalisetarhan.kacaolur.welcoming.WelcomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    internal abstract fun authViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderViewModel::class)
    internal abstract fun orderViewModel(viewModel: OrderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BiddingViewModel::class)
    internal abstract fun biddingViewModel(viewModel: BiddingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConfirmingViewModel::class)
    internal abstract fun confirmingViewModel(viewModel: ConfirmingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PaymentViewModel::class)
    internal abstract fun paymentViewModel(viewModel: PaymentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WaitingViewModel::class)
    internal abstract fun waitingViewModel(viewModel: WaitingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    internal abstract fun welcomeViewModel(viewModel: WelcomeViewModel): ViewModel
}