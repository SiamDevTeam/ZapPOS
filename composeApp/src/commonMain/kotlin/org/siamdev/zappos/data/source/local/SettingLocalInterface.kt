package org.siamdev.zappos.data.source.local

data class ThemeItem(
    val id: String,
    val name: String,
    val mode: String,       // "SYSTEM" | "LIGHT" | "DARK"
    val isDefault: Boolean
)

data class FontItem(
    val id: String,
    val name: String,
    val size: Double
)

data class CurrencyItem(
    val id: String,
    val code: String,       // THB, USD, BTC …
    val name: String,
    val symbol: String
)


interface SettingLocalInterface {

    // Theme
    suspend fun getThemes(): List<ThemeItem>
    suspend fun getActiveTheme(): ThemeItem?
    suspend fun setActiveTheme(themeId: String): Long
    suspend fun seedTheme(id: String, name: String, mode: String, isDefault: Boolean): Long

    // Font
    suspend fun getFonts(): List<FontItem>
    suspend fun getActiveFont(): FontItem?
    suspend fun setActiveFont(fontId: String): Long
    suspend fun seedFont(id: String, name: String, size: Double): Long

    // Currency
    suspend fun getCurrencies(): List<CurrencyItem>
    suspend fun getPrimaryCurrency(): CurrencyItem?
    suspend fun getSecondaryCurrency(): CurrencyItem?
    suspend fun setPrimaryCurrency(currencyId: String): Long
    suspend fun setSecondaryCurrency(currencyId: String): Long
    suspend fun seedCurrency(id: String, code: String, name: String, symbol: String): Long

    // Settings row bootstrap
    suspend fun initSettings()
}

// Set by platform code after the database is ready (via DatabaseHolder.registerDependencies())
var settingSource: SettingLocalInterface? = null