/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.reflect.KClass
import org.siamdev.zappos.ui.components.product.ProductBrowser
import org.siamdev.zappos.ui.components.progress.ProgressFacade
import org.siamdev.zappos.ui.components.progress.ProgressFacadeImpl
import org.siamdev.zappos.ui.components.progress.ProgressViewModel
import org.siamdev.zappos.ui.screens.count.CounterFacade
import org.siamdev.zappos.ui.screens.count.CounterFacadeImpl
import org.siamdev.zappos.ui.screens.count.CounterViewModel
import org.siamdev.zappos.ui.screens.sale.MainMenuFacade
import org.siamdev.zappos.ui.screens.sale.MainMenuFacadeImpl
import org.siamdev.zappos.ui.screens.sale.MainMenuViewModel
import org.siamdev.zappos.ui.screens.sale.checkout.CheckoutFacade
import org.siamdev.zappos.ui.screens.sale.checkout.CheckoutFacadeImpl
import org.siamdev.zappos.ui.screens.sale.checkout.CheckoutViewModel
import org.siamdev.zappos.ui.screens.setting.SettingFacade
import org.siamdev.zappos.ui.screens.setting.SettingFacadeImpl
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

val LocalProductBrowserVM = staticCompositionLocalOf<ProductBrowser> {
    error("Missing ProductBrowser in composition tree")
}

val LocalMenuVM = staticCompositionLocalOf<MainMenuFacade> {
    error("Missing ProvideViewModels in composition tree")
}

val LocalCheckoutVM = staticCompositionLocalOf<CheckoutFacade> {
    error("Missing ProvideViewModels in composition tree")
}

val LocalCounterVM = staticCompositionLocalOf<CounterFacade> {
    error("Missing ProvideViewModels in composition tree")
}

val LocalSettingVM = staticCompositionLocalOf<SettingFacade> {
    error("Missing ProvideViewModels in composition tree")
}

val LocalProgressVM = staticCompositionLocalOf<ProgressFacade> {
    error("Missing ProvideViewModels in composition tree")
}

@Composable
inline fun <reified VM : ViewModel> viewModelOf(
    noinline provider: () -> VM
): VM = viewModel(
    factory = remember {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: KClass<T>,
                extras: CreationExtras
            ): T = provider() as T
        }
    }
)

@Composable
fun ProvideViewModels(content: @Composable () -> Unit) {
    val settingVM = viewModelOf { SettingViewModel() }
    val menuVM = viewModelOf { MainMenuViewModel() }
    val checkoutVM = viewModelOf { CheckoutViewModel() }
    val counterVM = viewModelOf { CounterViewModel() }
    val progressVM = viewModelOf { ProgressViewModel() }

    val settingFacade = remember(settingVM) { SettingFacadeImpl(settingVM) }
    val mainMenuFacade = remember(menuVM) { MainMenuFacadeImpl(menuVM) }
    val checkoutFacade = remember(checkoutVM) { CheckoutFacadeImpl(checkoutVM) }
    val counterFacade = remember(counterVM) { CounterFacadeImpl(counterVM) }
    val progressFacade = remember(progressVM) { ProgressFacadeImpl(progressVM) }

    CompositionLocalProvider(
        LocalMenuVM provides mainMenuFacade,
        LocalProductBrowserVM provides menuVM,
        LocalCheckoutVM provides checkoutFacade,
        LocalCounterVM provides counterFacade,
        LocalSettingVM provides settingFacade,
        LocalProgressVM provides progressFacade,
        content = content
    )
}