package org.siamdev.zappos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.reflect.KClass
import androidx.compose.runtime.remember
import org.siamdev.zappos.ui.screens.checkout.CheckoutViewModel
import org.siamdev.zappos.ui.screens.count.CounterViewModel
import org.siamdev.zappos.ui.screens.sale.MainMenuViewModel
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

val LocalMenuVM = staticCompositionLocalOf<MainMenuViewModel> {
    error("Missing VMContainer in composition tree")
}

val LocalCheckoutVM = staticCompositionLocalOf<CheckoutViewModel> {
    error("Missing VMContainer in composition tree")
}

val LocalCounterVM = staticCompositionLocalOf<CounterViewModel> {
    error("Missing VMContainer in composition tree")
}

val LocalSettingVM = staticCompositionLocalOf<SettingViewModel> {
    error("Missing VMContainer in composition tree")
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
fun VMContainer(content: @Composable () -> Unit) {
    val menuVM = viewModelOf { MainMenuViewModel() }
    val checkoutVM = viewModelOf { CheckoutViewModel() }
    val counterVM = viewModelOf { CounterViewModel() }
    val settingVM = viewModelOf { SettingViewModel() }

    CompositionLocalProvider(
        LocalMenuVM provides menuVM,
        LocalCheckoutVM provides checkoutVM,
        LocalCounterVM provides counterVM,
        LocalSettingVM provides settingVM,
        content = content
    )
}