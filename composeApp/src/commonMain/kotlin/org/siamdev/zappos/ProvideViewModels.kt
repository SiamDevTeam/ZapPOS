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
import org.siamdev.zappos.ui.components.progress.ProgressSurface
import org.siamdev.zappos.ui.components.progress.ProgressSurfaceImpl
import org.siamdev.zappos.ui.components.progress.ProgressViewModel
import org.siamdev.zappos.ui.screens.count.CounterSurface
import org.siamdev.zappos.ui.screens.count.CounterSurfaceImpl
import org.siamdev.zappos.ui.screens.count.CounterViewModel
import org.siamdev.zappos.ui.screens.sale.MainMenuSurface
import org.siamdev.zappos.ui.screens.sale.MainMenuSurfaceImpl
import org.siamdev.zappos.ui.screens.sale.MainMenuViewModel
import org.siamdev.zappos.ui.screens.sale.checkout.CheckoutSurface
import org.siamdev.zappos.ui.screens.sale.checkout.CheckoutSurfaceImpl
import org.siamdev.zappos.ui.screens.sale.checkout.CheckoutViewModel
import org.siamdev.zappos.ui.screens.setting.SettingSurface
import org.siamdev.zappos.ui.screens.setting.SettingSurfaceImpl
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

val LocalProductBrowserVM = staticCompositionLocalOf<ProductBrowser> {
    error("Missing ProductBrowser in composition tree")
}

val LocalMenuVM = staticCompositionLocalOf<MainMenuSurface> {
    error("Missing ProvideViewModels in composition tree")
}

val LocalCheckoutVM = staticCompositionLocalOf<CheckoutSurface> {
    error("Missing ProvideViewModels in composition tree")
}

val LocalCounterVM = staticCompositionLocalOf<CounterSurface> {
    error("Missing ProvideViewModels in composition tree")
}

val LocalSettingVM = staticCompositionLocalOf<SettingSurface> {
    error("Missing ProvideViewModels in composition tree")
}

val LocalProgressVM = staticCompositionLocalOf<ProgressSurface> {
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

    val settingSurface = remember(settingVM) { SettingSurfaceImpl(settingVM) }
    val mainMenuSurface = remember(menuVM) { MainMenuSurfaceImpl(menuVM) }
    val checkoutSurface = remember(checkoutVM) { CheckoutSurfaceImpl(checkoutVM) }
    val counterSurface = remember(counterVM) { CounterSurfaceImpl(counterVM) }
    val progressSurface = remember(progressVM) { ProgressSurfaceImpl(progressVM) }

    CompositionLocalProvider(
        LocalMenuVM provides mainMenuSurface,
        LocalProductBrowserVM provides menuVM,
        LocalCheckoutVM provides checkoutSurface,
        LocalCounterVM provides counterSurface,
        LocalSettingVM provides settingSurface,
        LocalProgressVM provides progressSurface,
        content = content
    )
}