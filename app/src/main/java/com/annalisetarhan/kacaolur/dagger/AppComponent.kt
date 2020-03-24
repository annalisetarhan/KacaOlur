package com.annalisetarhan.kacaolur.dagger

import android.content.Context
import com.annalisetarhan.kacaolur.authing.AuthFragment
import com.annalisetarhan.kacaolur.bidding.BiddingFragment
import com.annalisetarhan.kacaolur.confirming.ConfirmingFragment
import com.annalisetarhan.kacaolur.ordering.OrderFragment
import com.annalisetarhan.kacaolur.payment.PaymentFragment
import com.annalisetarhan.kacaolur.waiting.WaitingFragment
import com.annalisetarhan.kacaolur.welcoming.WelcomeFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    StorageModule::class,
    ViewModelModule::class,
    AppModule::class
])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun inject(authFragment: AuthFragment)
    fun inject(orderFragment: OrderFragment)
    fun inject(biddingFragment: BiddingFragment)
    fun inject(confirmingFragment: ConfirmingFragment)
    fun inject(paymentFragment: PaymentFragment)
    fun inject(waitingFragment: WaitingFragment)
    fun inject(welcomeFragment: WelcomeFragment)
}