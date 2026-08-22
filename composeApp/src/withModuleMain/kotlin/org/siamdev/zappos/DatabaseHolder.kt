/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos

import org.siamdev.module.db.AppDatabase
import org.siamdev.zappos.data.source.local.LocalSources
import org.siamdev.zappos.data.source.local.datasource.ProductDataSource
import org.siamdev.zappos.data.source.local.datasource.SettingDataSource

object DatabaseHolder {
    lateinit var db: AppDatabase
}

fun AppDatabase.registerDependencies() {
    DatabaseHolder.db = this
    LocalSources.setting = SettingDataSource(this)
    LocalSources.product = ProductDataSource(this)
}