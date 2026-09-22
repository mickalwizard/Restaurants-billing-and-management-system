package restaurant;

import restaurant.model.*;
import restaurant.service.*;
import restaurant.util.InputHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final InputHelper in = new InputHelper(sc);

    private static final MenuService menuService = new MenuService();
    private static final TableService tableService = new TableService();
    private static final StaffService staffService = new StaffService();
    private static final OrderService orderService = new OrderService();
    private static final OrderItemService orderItemService = new OrderItemService();
    private static final BillService billService = new BillService();
    private static final InventoryService inventoryService = new InventoryService();
    private static final ReservationService reservationService = new ReservationService();

    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   WELCOME TO THE RESTAURANT BILLING & MANAGEMENT SYSTEM");
        System.out.println("=================================================");
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = in.readInt("Enter your choice: ");
            switch (choice) {
                case 1: menuMenu(); break;
                case 2: tableMenu(); break;
                case 3: staffMenu(); break;
                case 4: orderMenu(); break;
                case 5: billingMenu(); break;
                case 6: inventoryMenu(); break;
                case 7: reservationMenu(); break;
                case 0:
                    running = false;
                    System.out.println("\nThank you for using the Restaurant Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }

    private static void printMainMenu() {
        System.out.println("\n================ MAIN MENU ================");
        System.out.println("1. Manage Menu Items");
        System.out.println("2. Manage Tables");
        System.out.println("3. Manage Staff");
        System.out.println("4. Manage Orders");
        System.out.println("5. Billing");
        System.out.println("6. Manage Inventory");
        System.out.println("7. Manage Reservations");
        System.out.println("0. Exit");
        System.out.println("=============================================");
    }

    // ==================== MENU ITEMS ====================
    private static void menuMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- MENU MANAGEMENT -----");
            System.out.println("1. Add Menu Item");
            System.out.println("2. View All Menu Items");
            System.out.println("3. Search Menu Item by Name");
            System.out.println("4. View Menu Items by Category");
            System.out.println("5. Update Menu Item");
            System.out.println("6. Delete Menu Item");
            System.out.println("0. Back to Main Menu");
            int choice = in.readInt("Enter your choice: ");
            switch (choice) {
                case 1: {
                    String name = in.readNonEmptyString("Item Name: ");
                    String category = in.readNonEmptyString("Category (e.g. Starter, Main Course, Dessert, Beverage): ");
                    double price = in.readPositiveDouble("Price: ");
                    boolean available = in.readNonEmptyString("Available now? (yes/no): ").equalsIgnoreCase("yes");
                    MenuItem m = menuService.add(name, category, price, available);
                    System.out.println("Menu item added successfully with ID: " + m.getId());
                    break;
                }
                case 2: {
                    List<MenuItem> all = menuService.getAll();
                    if (all.isEmpty()) System.out.println("No menu items found.");
                    else all.forEach(System.out::println);
                    break;
                }
                case 3: {
                    String keyword = in.readNonEmptyString("Enter name keyword: ");
                    List<MenuItem> results = menuService.searchByName(keyword);
                    if (results.isEmpty()) System.out.println("No matching menu items found.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 4: {
                    String category = in.readNonEmptyString("Enter Category: ");
                    List<MenuItem> results = menuService.findByCategory(category);
                    if (results.isEmpty()) System.out.println("No menu items found in that category.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 5: {
                    int id = in.readInt("Enter Menu Item ID to update: ");
                    Optional<MenuItem> opt = menuService.findById(id);
                    if (opt.isEmpty()) { System.out.println("Menu item not found."); break; }
                    MenuItem existing = opt.get();
                    System.out.println("Current: " + existing);
                    String name = in.readString("New Name (blank to keep): ");
                    if (name.isEmpty()) name = existing.getName();
                    String category = in.readString("New Category (blank to keep): ");
                    if (category.isEmpty()) category = existing.getCategory();
                    double price = in.readPositiveDouble("New Price: ");
                    boolean available = in.readNonEmptyString("Available now? (yes/no): ").equalsIgnoreCase("yes");
                    menuService.update(id, name, category, price, available);
                    System.out.println("Menu item updated successfully.");
                    break;
                }
                case 6: {
                    int id = in.readInt("Enter Menu Item ID to delete: ");
                    System.out.println(menuService.delete(id) ? "Menu item deleted." : "Menu item not found.");
                    break;
                }
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== TABLES ====================
    private static void tableMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- TABLE MANAGEMENT -----");
            System.out.println("1. Add Table");
            System.out.println("2. View All Tables");
            System.out.println("3. View Available Tables");
            System.out.println("4. Update Table Status");
            System.out.println("5. Update Table Details");
            System.out.println("6. Delete Table");
            System.out.println("0. Back to Main Menu");
            int choice = in.readInt("Enter your choice: ");
            switch (choice) {
                case 1: {
                    int tableNumber = in.readInt("Table Number: ");
                    int capacity = in.readInt("Capacity (number of seats): ");
                    DiningTable t = tableService.add(tableNumber, capacity, "Available");
                    System.out.println("Table added successfully with ID: " + t.getId());
                    break;
                }
                case 2: {
                    List<DiningTable> all = tableService.getAll();
                    if (all.isEmpty()) System.out.println("No tables found.");
                    else all.forEach(System.out::println);
                    break;
                }
                case 3: {
                    List<DiningTable> results = tableService.findAvailable();
                    if (results.isEmpty()) System.out.println("No available tables right now.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 4: {
                    int id = in.readInt("Enter Table ID: ");
                    String status = in.readNonEmptyString("New Status (Available/Occupied/Reserved): ");
                    System.out.println(tableService.updateStatus(id, status) ? "Table status updated." : "Table not found.");
                    break;
                }
                case 5: {
                    int id = in.readInt("Enter Table ID to update: ");
                    Optional<DiningTable> opt = tableService.findById(id);
                    if (opt.isEmpty()) { System.out.println("Table not found."); break; }
                    DiningTable existing = opt.get();
                    System.out.println("Current: " + existing);
                    int tableNumber = in.readInt("New Table Number: ");
                    int capacity = in.readInt("New Capacity: ");
                    String status = in.readString("New Status (blank to keep): ");
                    if (status.isEmpty()) status = existing.getStatus();
                    tableService.update(id, tableNumber, capacity, status);
                    System.out.println("Table updated successfully.");
                    break;
                }
                case 6: {
                    int id = in.readInt("Enter Table ID to delete: ");
                    System.out.println(tableService.delete(id) ? "Table deleted." : "Table not found.");
                    break;
                }
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== STAFF ====================
    private static void staffMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- STAFF MANAGEMENT -----");
            System.out.println("1. Add Staff Member");
            System.out.println("2. View All Staff");
            System.out.println("3. Search Staff by Name");
            System.out.println("4. View Staff by Role");
            System.out.println("5. Update Staff Member");
            System.out.println("6. Delete Staff Member");
            System.out.println("0. Back to Main Menu");
            int choice = in.readInt("Enter your choice: ");
            switch (choice) {
                case 1: {
                    String name = in.readNonEmptyString("Name: ");
                    String role = in.readNonEmptyString("Role (Waiter/Chef/Manager/Cashier): ");
                    String contact = in.readString("Contact: ");
                    double salary = in.readPositiveDouble("Salary: ");
                    Staff s = staffService.add(name, role, contact, salary);
                    System.out.println("Staff member added successfully with ID: " + s.getId());
                    break;
                }
                case 2: {
                    List<Staff> all = staffService.getAll();
                    if (all.isEmpty()) System.out.println("No staff found.");
                    else all.forEach(System.out::println);
                    break;
                }
                case 3: {
                    String keyword = in.readNonEmptyString("Enter name keyword: ");
                    List<Staff> results = staffService.searchByName(keyword);
                    if (results.isEmpty()) System.out.println("No matching staff found.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 4: {
                    String role = in.readNonEmptyString("Enter Role: ");
                    List<Staff> results = staffService.findByRole(role);
                    if (results.isEmpty()) System.out.println("No staff found with that role.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 5: {
                    int id = in.readInt("Enter Staff ID to update: ");
                    Optional<Staff> opt = staffService.findById(id);
                    if (opt.isEmpty()) { System.out.println("Staff member not found."); break; }
                    Staff existing = opt.get();
                    System.out.println("Current: " + existing);
                    String name = in.readString("New Name (blank to keep): ");
                    if (name.isEmpty()) name = existing.getName();
                    String role = in.readString("New Role (blank to keep): ");
                    if (role.isEmpty()) role = existing.getRole();
                    String contact = in.readString("New Contact (blank to keep): ");
                    if (contact.isEmpty()) contact = existing.getContact();
                    double salary = in.readPositiveDouble("New Salary: ");
                    staffService.update(id, name, role, contact, salary);
                    System.out.println("Staff member updated successfully.");
                    break;
                }
                case 6: {
                    int id = in.readInt("Enter Staff ID to delete: ");
                    System.out.println(staffService.delete(id) ? "Staff member deleted." : "Staff member not found.");
                    break;
                }
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== ORDERS ====================
    private static void orderMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- ORDER MANAGEMENT -----");
            System.out.println("1. Create New Order");
            System.out.println("2. Add Item to Order");
            System.out.println("3. View Order Details (with items)");
            System.out.println("4. View All Orders");
            System.out.println("5. View Open Orders");
            System.out.println("6. Update Order Status");
            System.out.println("7. Remove Item from Order");
            System.out.println("8. Delete Order");
            System.out.println("0. Back to Main Menu");
            int choice = in.readInt("Enter your choice: ");
            switch (choice) {
                case 1: {
                    int tableId = in.readInt("Table ID: ");
                    if (!tableService.exists(tableId)) { System.out.println("No such table."); break; }
                    int staffId = in.readInt("Waiter/Staff ID: ");
                    if (!staffService.exists(staffId)) { System.out.println("No such staff member."); break; }
                    String now = LocalDateTime.now().format(DATETIME_FMT);
                    Order o = orderService.add(tableId, staffId, now);
                    tableService.updateStatus(tableId, "Occupied");
                    System.out.println("Order created successfully with ID: " + o.getId());
                    break;
                }
                case 2: {
                    int orderId = in.readInt("Order ID: ");
                    Optional<Order> orderOpt = orderService.findById(orderId);
                    if (orderOpt.isEmpty()) { System.out.println("No such order."); break; }
                    int menuItemId = in.readInt("Menu Item ID: ");
                    Optional<MenuItem> menuOpt = menuService.findById(menuItemId);
                    if (menuOpt.isEmpty()) { System.out.println("No such menu item."); break; }
                    if (!menuOpt.get().isAvailable()) { System.out.println("That item is currently unavailable."); break; }
                    int quantity = in.readInt("Quantity: ");
                    if (quantity <= 0) { System.out.println("Quantity must be at least 1."); break; }
                    OrderItem oi = orderItemService.add(orderId, menuItemId, quantity, menuOpt.get().getPrice());
                    System.out.printf("Item added (ID: %d). Line total: %.2f%n", oi.getId(), oi.getLineTotal());
                    break;
                }
                case 3: {
                    int orderId = in.readInt("Order ID: ");
                    Optional<Order> orderOpt = orderService.findById(orderId);
                    if (orderOpt.isEmpty()) { System.out.println("No such order."); break; }
                    System.out.println(orderOpt.get());
                    List<OrderItem> orderItems = orderItemService.findByOrder(orderId);
                    if (orderItems.isEmpty()) {
                        System.out.println("No items added to this order yet.");
                    } else {
                        System.out.println("--- Items ---");
                        for (OrderItem oi : orderItems) {
                            Optional<MenuItem> menuOpt = menuService.findById(oi.getMenuItemId());
                            String itemName = menuOpt.isPresent() ? menuOpt.get().getName() : "Item#" + oi.getMenuItemId();
                            System.out.printf("%-20s x%-4d @ %-10.2f = %.2f%n",
                                    itemName, oi.getQuantity(), oi.getPriceAtOrder(), oi.getLineTotal());
                        }
                        System.out.printf("Order Subtotal: %.2f%n", orderItemService.calculateOrderSubtotal(orderId));
                    }
                    break;
                }
                case 4: {
                    List<Order> all = orderService.getAll();
                    if (all.isEmpty()) System.out.println("No orders found.");
                    else all.forEach(System.out::println);
                    break;
                }
                case 5: {
                    List<Order> results = orderService.findByStatus("Open");
                    if (results.isEmpty()) System.out.println("No open orders.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 6: {
                    int id = in.readInt("Enter Order ID: ");
                    String status = in.readNonEmptyString("New Status (Open/Served/Billed/Cancelled): ");
                    System.out.println(orderService.updateStatus(id, status) ? "Order status updated." : "Order not found.");
                    break;
                }
                case 7: {
                    int itemId = in.readInt("Enter Order Item ID to remove: ");
                    System.out.println(orderItemService.delete(itemId) ? "Item removed from order." : "Order item not found.");
                    break;
                }
                case 8: {
                    int id = in.readInt("Enter Order ID to delete: ");
                    orderItemService.deleteByOrder(id);
                    System.out.println(orderService.delete(id) ? "Order and its items deleted." : "Order not found.");
                    break;
                }
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== BILLING ====================
    private static void billingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- BILLING -----");
            System.out.println("1. Generate Bill for an Order");
            System.out.println("2. View All Bills");
            System.out.println("3. View Bill by Order ID");
            System.out.println("4. View All Unpaid Bills");
            System.out.println("5. Mark Bill as Paid");
            System.out.println("6. View Total Revenue (Paid Bills)");
            System.out.println("7. Delete Bill");
            System.out.println("0. Back to Main Menu");
            int choice = in.readInt("Enter your choice: ");
            switch (choice) {
                case 1: {
                    int orderId = in.readInt("Order ID: ");
                    Optional<Order> orderOpt = orderService.findById(orderId);
                    if (orderOpt.isEmpty()) { System.out.println("No such order."); break; }
                    if (billService.findByOrder(orderId).isPresent()) {
                        System.out.println("A bill already exists for this order.");
                        break;
                    }
                    double subtotal = orderItemService.calculateOrderSubtotal(orderId);
                    if (subtotal <= 0) { System.out.println("This order has no items yet."); break; }
                    double taxPercent = in.readPositiveDouble("Tax Percent (e.g. 13 for 13%): ");
                    double discountPercent = in.readPositiveDouble("Discount Percent (0 if none): ");
                    String paymentMethod = in.readNonEmptyString("Payment Method (Cash/Card/UPI/Unpaid): ");
                    String now = LocalDateTime.now().format(DATETIME_FMT);
                    Bill b = billService.generate(orderId, subtotal, taxPercent, discountPercent, paymentMethod, now);
                    orderService.updateStatus(orderId, "Billed");
                    Optional<Order> ord = orderService.findById(orderId);
                    if (ord.isPresent()) tableService.updateStatus(ord.get().getTableId(), "Available");
                    System.out.println("\n========== BILL RECEIPT ==========");
                    System.out.println(b);
                    System.out.printf("Subtotal:  %.2f%n", b.getSubtotal());
                    System.out.printf("Discount:  %.1f%%%n", b.getDiscountPercent());
                    System.out.printf("Tax:       %.1f%%%n", b.getTaxPercent());
                    System.out.printf("TOTAL DUE: %.2f%n", b.getTotalAmount());
                    System.out.println("===================================");
                    break;
                }
                case 2: {
                    List<Bill> all = billService.getAll();
                    if (all.isEmpty()) System.out.println("No bills found.");
                    else all.forEach(System.out::println);
                    break;
                }
                case 3: {
                    int orderId = in.readInt("Order ID: ");
                    Optional<Bill> opt = billService.findByOrder(orderId);
                    if (opt.isEmpty()) System.out.println("No bill found for that order.");
                    else System.out.println(opt.get());
                    break;
                }
                case 4: {
                    List<Bill> unpaid = billService.findUnpaid();
                    if (unpaid.isEmpty()) System.out.println("No unpaid bills. All settled!");
                    else unpaid.forEach(System.out::println);
                    break;
                }
                case 5: {
                    int id = in.readInt("Enter Bill ID to mark as paid: ");
                    String paymentMethod = in.readNonEmptyString("Payment Method (Cash/Card/UPI): ");
                    System.out.println(billService.markPaid(id, paymentMethod) ? "Bill marked as paid." : "Bill not found.");
                    break;
                }
                case 6: {
                    System.out.printf("Total Revenue from Paid Bills: %.2f%n", billService.totalRevenue());
                    break;
                }
                case 7: {
                    int id = in.readInt("Enter Bill ID to delete: ");
                    System.out.println(billService.delete(id) ? "Bill deleted." : "Bill not found.");
                    break;
                }
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== INVENTORY ====================
    private static void inventoryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- INVENTORY MANAGEMENT -----");
            System.out.println("1. Add Inventory Item");
            System.out.println("2. View All Inventory Items");
            System.out.println("3. Search Inventory Item by Name");
            System.out.println("4. View Low Stock Items");
            System.out.println("5. Adjust Stock Quantity (add/remove)");
            System.out.println("6. Update Inventory Item Details");
            System.out.println("7. Delete Inventory Item");
            System.out.println("0. Back to Main Menu");
            int choice = in.readInt("Enter your choice: ");
            switch (choice) {
                case 1: {
                    String name = in.readNonEmptyString("Ingredient/Item Name: ");
                    double quantity = in.readPositiveDouble("Starting Quantity: ");
                    String unit = in.readNonEmptyString("Unit (kg/litre/pieces/etc.): ");
                    double reorderLevel = in.readPositiveDouble("Reorder Level (low-stock threshold): ");
                    InventoryItem i = inventoryService.add(name, quantity, unit, reorderLevel);
                    System.out.println("Inventory item added successfully with ID: " + i.getId());
                    break;
                }
                case 2: {
                    List<InventoryItem> all = inventoryService.getAll();
                    if (all.isEmpty()) System.out.println("No inventory items found.");
                    else all.forEach(System.out::println);
                    break;
                }
                case 3: {
                    String keyword = in.readNonEmptyString("Enter name keyword: ");
                    List<InventoryItem> results = inventoryService.searchByName(keyword);
                    if (results.isEmpty()) System.out.println("No matching inventory items found.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 4: {
                    List<InventoryItem> low = inventoryService.findLowStock();
                    if (low.isEmpty()) System.out.println("No items are low on stock.");
                    else low.forEach(System.out::println);
                    break;
                }
                case 5: {
                    int id = in.readInt("Enter Inventory Item ID: ");
                    double delta = in.readDouble("Quantity to add (use a negative number to remove): ");
                    System.out.println(inventoryService.adjustStock(id, delta) ? "Stock adjusted." : "Item not found.");
                    break;
                }
                case 6: {
                    int id = in.readInt("Enter Inventory Item ID to update: ");
                    Optional<InventoryItem> opt = inventoryService.findById(id);
                    if (opt.isEmpty()) { System.out.println("Item not found."); break; }
                    InventoryItem existing = opt.get();
                    System.out.println("Current: " + existing);
                    String name = in.readString("New Name (blank to keep): ");
                    if (name.isEmpty()) name = existing.getName();
                    double quantity = in.readPositiveDouble("New Quantity: ");
                    String unit = in.readString("New Unit (blank to keep): ");
                    if (unit.isEmpty()) unit = existing.getUnit();
                    double reorderLevel = in.readPositiveDouble("New Reorder Level: ");
                    inventoryService.update(id, name, quantity, unit, reorderLevel);
                    System.out.println("Inventory item updated successfully.");
                    break;
                }
                case 7: {
                    int id = in.readInt("Enter Inventory Item ID to delete: ");
                    System.out.println(inventoryService.delete(id) ? "Inventory item deleted." : "Item not found.");
                    break;
                }
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== RESERVATIONS ====================
    private static void reservationMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- RESERVATION MANAGEMENT -----");
            System.out.println("1. Add Reservation");
            System.out.println("2. View All Reservations");
            System.out.println("3. View Reservations by Date");
            System.out.println("4. View Reservations by Table");
            System.out.println("5. Update Reservation Status");
            System.out.println("6. Delete Reservation");
            System.out.println("0. Back to Main Menu");
            int choice = in.readInt("Enter your choice: ");
            switch (choice) {
                case 1: {
                    String customerName = in.readNonEmptyString("Customer Name: ");
                    String contact = in.readNonEmptyString("Contact Number: ");
                    int tableId = in.readInt("Table ID: ");
                    if (!tableService.exists(tableId)) { System.out.println("No such table."); break; }
                    String date = in.readNonEmptyString("Reservation Date (YYYY-MM-DD): ");
                    String time = in.readNonEmptyString("Reservation Time (HH:mm): ");
                    int numGuests = in.readInt("Number of Guests: ");
                    Reservation r = reservationService.add(customerName, contact, tableId, date, time, numGuests);
                    tableService.updateStatus(tableId, "Reserved");
                    System.out.println("Reservation created successfully with ID: " + r.getId());
                    break;
                }
                case 2: {
                    List<Reservation> all = reservationService.getAll();
                    if (all.isEmpty()) System.out.println("No reservations found.");
                    else all.forEach(System.out::println);
                    break;
                }
                case 3: {
                    String date = in.readNonEmptyString("Enter Date (YYYY-MM-DD): ");
                    List<Reservation> results = reservationService.findByDate(date);
                    if (results.isEmpty()) System.out.println("No reservations found for that date.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 4: {
                    int tableId = in.readInt("Enter Table ID: ");
                    List<Reservation> results = reservationService.findByTable(tableId);
                    if (results.isEmpty()) System.out.println("No reservations found for that table.");
                    else results.forEach(System.out::println);
                    break;
                }
                case 5: {
                    int id = in.readInt("Enter Reservation ID: ");
                    String status = in.readNonEmptyString("New Status (Confirmed/Cancelled/Completed): ");
                    System.out.println(reservationService.updateStatus(id, status) ? "Reservation status updated." : "Reservation not found.");
                    break;
                }
                case 6: {
                    int id = in.readInt("Enter Reservation ID to delete: ");
                    System.out.println(reservationService.delete(id) ? "Reservation deleted." : "Reservation not found.");
                    break;
                }
                case 0: back = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}
