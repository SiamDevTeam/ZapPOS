package org.siamdev.module.db

import app.cash.sqldelight.async.coroutines.await
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.siamdev.module.db.biz.ZapPOSBiz
import org.siamdev.module.db.sys.ZapPOSSys
import org.w3c.dom.Worker

private fun workerDriver(): Worker =
    Worker(js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""").unsafeCast<String>())

actual suspend fun createBizDriver(): SqlDriver {
    val driver = WebWorkerDriver(workerDriver())
    ZapPOSBiz.Schema.create(driver).await()
    console.log("[ZapPOS] ZapPOSBiz ready")
    return driver
}

actual suspend fun createSysDriver(): SqlDriver {
    val driver = WebWorkerDriver(workerDriver())
    ZapPOSSys.Schema.create(driver).await()
    console.log("[ZapPOS] ZapPOSSys ready")
    return driver
}