<div align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="doc/logo/ZapPOS_White_Horizontal_v2.svg" width="400">
    <img alt="ZapPOS logo" src="doc/logo/ZapPOS_Dark_Horizontal_v2.svg" width="400">
  </picture>

  <p>A point of sale built with Kotlin Multiplatform, powered by Nostr NWC.</p>
</div>

---

## Platforms

Android · Desktop (JVM) · Web (Wasm · JS)

---

## Roadmap

| Feature                    | Status |
| -------------------------- | ------ |
| NWC connector              | ⬜     |
| Product listing & cart     | ✅     |
| Product management         | ⬜     |
| Checkout & payment         | ✅     |
| Billing & receipt printing | ⬜     |
| Local fiat tracking        | ⬜     |
| Data export & dashboard    | ⬜     |
| Sub-account management     | ⬜     |
| Backup & recovery          | ⬜     |
| Language support           | ⬜     |

---

**SqlDelight**
```bash
./gradlew generateSqlDelightInterface
```

**Android**
```bash
./gradlew :composeApp:assembleDebug
```

**Desktop**
```bash
./gradlew hotRunJvm --mainClass "org.siamdev.zappos.MainKt"
```

**Web**
```bash
# JS
./gradlew jsBrowserDevelopmentRun
```