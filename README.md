# Malambe Tuk Tuk

A JavaFX desktop application for managing spare-parts inventory, dealers, and sales for a tuk-tuk (three-wheeler) parts shop. Built as coursework for CM1601.

## Overview

Malambe Tuk Tuk tracks product inventory, dealer contacts, and a shopping cart with discount rules, all backed by plain text files instead of a database. A core design constraint of the project is that the seed data is genuinely messy — mixed delimiters, inconsistent date formats, missing fields — so a large part of the system is dedicated to parsing that data safely before anything else in the app relies on it.

## Features

- **Inventory management** — add, update, delete products; view item pictures; set a per-item low-stock threshold.
- **Search & filtering** — keyword search across code/name/brand, category filter, price range, and a low-stock-only view, all combinable at once.
- **Manual sorting** — the inventory table is grouped by category and sorted by product code using a hand-written bubble sort, since the assignment brief does not allow `Arrays.sort`, `Comparator`, or streams.
- **Dealers** — dealer directory with a random 4-dealer draw.
- **Cart & checkout** — add to cart, adjust quantities, remove items, and check out with two independent discount rules:
  - 5% bulk discount on any line with 3 or more units of the same item
  - 10% synergy discount when the cart contains at least one engine-category item and one electrical-category item
- **Dashboard** — total parts, low-stock count, dealer count, and a recent-activity feed read from the audit log.
- **Dirty-data tolerant parsing** — the legacy data files mix `,` / `;` / `|` as delimiters and several different date formats (including humanised dates like `Oct 15, 2023`); a dedicated cleaning step normalises all of it before it reaches the rest of the app.

## Tech stack

- Java 25
- JavaFX 21 (Controls + FXML)
- Maven
- JUnit 5 (48 automated tests)

## Project structure

```
src/main/java/
  Controller/    JavaFX controllers, one per screen/popup
  Features/      business logic: ProductManager, CartManager, DealerManager, Search, LowStock, validator, AuditLogger
  Model/         plain data classes: Products, Dealers, CartItem
  Cleaner/       Cleaner (dirty-data parsing) and TextFileManager (file I/O)
  MainProgram/   application entry point

src/main/resources/
  FXML/          Scene Builder layouts
  CSS/           stylesheet
  Data/          text-file data store (see below)

Test/
  Cleaner/, Features/, Controller/   JUnit 5 tests, mirroring the src/main/java package layout
```

## Data

The app stores its data as plain text files under `src/main/resources/Data/`:

- `inventory_legacy.txt` / `dealers_legacy.txt` — read-only seed data, deliberately written with mixed delimiters and formatting to exercise the parsing logic
- `Inventory.txt` — the live, writable inventory, seeded from the legacy file on first run
- `audit_log.txt` — a running log of add/update/delete/threshold changes
- `ProceedPaymentItems.txt` — a receipt log appended to on every checkout

File paths are resolved relative to the working directory rather than the classpath, so the app needs to be run from the project root (see Known Limitations).

## Getting started

### Prerequisites

- JDK 25
- Maven (or use the bundled wrapper — no separate install needed)

### Run the app

```bash
# macOS/Linux
./mvnw clean javafx:run

# Windows
mvnw.cmd clean javafx:run
```

### Run the tests

```bash
# macOS/Linux
./mvnw test

# Windows
mvnw.cmd test
```

This runs the full JUnit 5 suite (48 tests) covering the Cleaner, Features, and sorting logic, including reflection-based tests against `InventoryController`'s private sort methods.

## Known limitations

- `TextFileManager` resolves data files relative to the working directory, not the classpath, so the packaged app is not location-independent — it must be launched from the project root.
