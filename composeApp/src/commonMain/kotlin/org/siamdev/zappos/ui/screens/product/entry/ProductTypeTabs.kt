/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import org.siamdev.zappos.ui.components.common.TabItem

internal val entryTabs =
    listOf(
        TabItem("Goods", Icons.Default.Inventory),
        TabItem("Service", Icons.Default.Person),
        TabItem("Rental", Icons.Default.Schedule),
    )
