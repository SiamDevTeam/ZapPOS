/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.progress

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProgressViewModel : ViewModel() {

    var steps by mutableStateOf<List<String>>(emptyList())
        private set

    var currentStep by mutableIntStateOf(0)
        private set

    fun setup(steps: List<String>, currentStep: Int) {
        this.steps = steps
        this.currentStep = currentStep
    }
}