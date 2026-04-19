<div align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="doc/logo/ZapPOS_White_Horizontal_v2.svg" width="445">
    <img alt="ZapPOS logo" src="doc/logo/ZapPOS_Dark_Horizontal_v2.svg" width="445">
  </picture>
</div>

<p align="center">
  A simple point of sale built with Kotlin Multiplatform, powered by Nostr NWC.
</p>

---

## Overview

ZapPOS is a simple point of sale system designed for modern payments using Nostr Wallet Connect (NWC).  
It focuses on simplicity, speed, and cross-platform support.

---

## Roadmap

- [ ] NWC connector
- ✅ Product listing  
  - [ ] Cart  
  - [ ] Product management  
- [ ] Checkout & payment  
  - [ ] Billing  
  - [ ] Receipt printing  
- [ ] iOS, Android, Desktop, Web support  
- [ ] Local fiat tracking  
- [ ] Data export  
- [ ] Dashboard  
- [ ] Sub-account management  
- [ ] Backup & recovery  
- [ ] Language support  

---

## Run the project

### Desktop

```bash
# JVM
./gradlew hotRunJvm --mainClass "org.siamdev.zappos.MainKt"
````

### Web

```bash
# Wasm
./gradlew wasmJsBrowserDevelopmentRun

# JS
./gradlew jsBrowserDevelopmentRun
```
