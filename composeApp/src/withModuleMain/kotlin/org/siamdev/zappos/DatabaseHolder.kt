/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import org.siamdev.module.db.AppDatabase
import org.siamdev.zappos.data.source.local.SettingLocalInterfaceImpl
import org.siamdev.zappos.data.source.local.settingSource

object DatabaseHolder {
    lateinit var db: AppDatabase
}

fun AppDatabase.registerDependencies() {
    DatabaseHolder.db = this
    settingSource = SettingLocalInterfaceImpl(this)
}