# Restaurant Billing & Management System (Java)

A console-based Java application for running a restaurant's daily operations:
**Menu, Tables, Staff, Orders, Billing, Inventory, and Reservations.**

All data is saved to plain CSV files under the `data/` folder, so your
records persist automatically between runs — no database server required.

## Features

- **Menu** — add, view, search by name, filter by category, update price/availability, delete
- **Tables** — add, view, view available tables, update seating status (Available/Occupied/Reserved), delete
- **Staff** — add, view, search by name, filter by role (Waiter/Chef/Manager/Cashier), update, delete
- **Orders** — open a new order against a table + waiter, add menu items with quantities, view a
  running order with line totals, update order status, remove items, delete
- **Billing** — generate a bill from an order's items with tax % and discount % applied, view
  unpaid bills, mark bills as paid, and see total revenue from paid bills
- **Inventory** — track ingredient stock levels, adjust stock up/down, flag low-stock items
  automatically against a reorder threshold
- **Reservations** — book a table for a customer on a date/time, view by date or table, update status

## How Orders and Billing Connect

This is the core workflow of the whole system:

1. Create an **Order** for a Table + Staff member → the table is marked `Occupied`
2. **Add Items** to that order from the Menu (each line stores quantity × price at that moment)
3. View the order to see a running subtotal
4. **Generate a Bill** for the order → applies tax % and discount % to the subtotal, the order is
   marked `Billed`, and the table is freed back to `Available`
5. Mark the bill **Paid** once payment is received

## Requirements

- Java JDK 8 or later (JDK 17+ recommended). Check with:
  ```
  java -version
  javac -version
  ```
  If these commands aren't found, install a JDK first (e.g. from
  https://adoptium.net).

## Project Structure

```
RestaurantManagementSystem/
├── src/main/java/restaurant/
│   ├── Main.java                     # Entry point & console menus
│   ├── model/                        # Data classes
│   │   ├── MenuItem.java
│   │   ├── DiningTable.java
│   │   ├── Staff.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Bill.java
│   │   ├── InventoryItem.java
│   │   └── Reservation.java
│   ├── service/                      # Business logic + CSV persistence
│   │   ├── MenuService.java
│   │   ├── TableService.java
│   │   ├── StaffService.java
│   │   ├── OrderService.java
│   │   ├── OrderItemService.java
│   │   ├── BillService.java
│   │   ├── InventoryService.java
│   │   └── ReservationService.java
│   └── util/
│       ├── FileUtil.java             # Generic CSV read/write helper
│       └── InputHelper.java          # Validated console input helper
├── data/                             # Auto-created CSV data files (your records)
├── run.sh                            # Build & run script (macOS/Linux)
├── run.bat                           # Build & run script (Windows)
└── README.md
```

## How to Run

### Option A — using the scripts
```bash
# macOS / Linux
./run.sh

# Windows
run.bat
```

### Option B — manual commands
```bash
mkdir -p out
javac -d out $(find src -name "*.java")   # macOS/Linux
java -cp out restaurant.Main
```
On Windows (PowerShell), replace the `find` line with:
```
Get-ChildItem -Recurse -Filter *.java src | Foreach-Object { $_.FullName } > sources.txt
javac -d out "@sources.txt"
java -cp out restaurant.Main
```

### Option C — open in an IDE
Import the `RestaurantManagementSystem` folder into IntelliJ IDEA, Eclipse, or
VS Code as a plain Java project (source root: `src/main/java`), then run
`restaurant.Main`.

## Suggested Workflow

1. **Manage Tables** → add your dining tables (table number + capacity)
2. **Manage Staff** → add waiters, chefs, a manager, a cashier
3. **Manage Menu Items** → add dishes with category and price
4. **Manage Inventory** → add ingredients with a starting quantity and reorder level
5. **Manage Orders** → create an order for a table, add menu items to it
6. **Billing** → generate the bill once the order is complete, then mark it paid
7. **Manage Reservations** → book tables ahead of time for walk-in customers

IDs (Table ID, Staff ID, Menu Item ID, Order ID, Bill ID) are shown whenever
you add or view a record — use them to link records together.

## Data Storage

Each module keeps its own CSV file in `data/`:
`menu.csv`, `tables.csv`, `staff.csv`, `orders.csv`, `order_items.csv`,
`bills.csv`, `inventory.csv`, `reservations.csv`.

Fields are separated with `|` (not `,`) so ordinary text like names can
safely contain commas. You generally won't need to open these files by hand —
the app manages them for you — but they're plain text if you ever want to
inspect, back up, or import them into a spreadsheet.

## Extending the Project

This project is intentionally built with clear layers so it's easy to extend:

- Add a new field → update the model class's fields, constructor, `toCsv`/`fromCsv`, and `toString`
- Add a new operation → add a method to the relevant `*Service` class
- Add a new menu option → add a case to the relevant menu method in `Main.java`
- Ideas for extra credit: customer loyalty points, a daily sales report, a
  kitchen order ticket queue, multiple discount coupon codes, split billing
  across multiple payment methods
- Swap CSV for a real database → only the `service` classes need to change
  (e.g. replace `FileUtil` calls with JDBC calls); models and `Main.java`
  stay the same
