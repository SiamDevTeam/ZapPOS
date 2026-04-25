package org.siamdev.zappos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.reflect.KClass
import org.siamdev.zappos.ui.screens.count.CounterViewModel
import androidx.compose.runtime.remember
import org.siamdev.zappos.ui.screens.sale.MainMenuViewModel

val LocalMenuVM = staticCompositionLocalOf<MainMenuViewModel> {
    error("MainMenuViewModel not provided")
}

val LocalCounterVM = staticCompositionLocalOf<CounterViewModel> {
    error("CounterViewModel not provided")
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
fun VMContainer(
    content: @Composable () -> Unit
) {
    val mainMenuVM = viewModelOf { MainMenuViewModel() }
    val counterVM  = viewModelOf { CounterViewModel() }

    CompositionLocalProvider(
        LocalMenuVM provides mainMenuVM,
        LocalCounterVM  provides counterVM,
        content = content
    )
}