package restaurant.model;

import restaurant.util.FileUtil;

public class OrderItem {
    private int id;
    private int orderId;
    private int menuItemId;
    private int quantity;
    private double priceAtOrder; // snapshot of the menu price at the time of ordering

    public OrderItem(int id, int orderId, int menuItemId, int quantity, double priceAtOrder) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getMenuItemId() { return menuItemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPriceAtOrder() { return priceAtOrder; }

    public double getLineTotal() {
        return quantity * priceAtOrder;
    }

    public String toCsv() {
        return FileUtil.join(String.valueOf(id), String.valueOf(orderId), String.valueOf(menuItemId),
                String.valueOf(quantity), String.valueOf(priceAtOrder));
    }

    public static OrderItem fromCsv(String line) {
        String[] f = FileUtil.split(line);
        return new OrderItem(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2]),
                Integer.parseInt(f[3]), Double.parseDouble(f[4]));
    }

    @Override
    public String toString() {
        return String.format("ItemID:%-5d OrderID:%-5d MenuItemID:%-6d Qty:%-4d UnitPrice:%-10.2f LineTotal:%.2f",
                id, orderId, menuItemId, quantity, priceAtOrder, getLineTotal());
    }
}
