package restaurant.model;

import restaurant.util.FileUtil;

public class Order {
    private int id;
    private int tableId;
    private int staffId;      // waiter handling the order
    private String orderTime; // yyyy-MM-dd HH:mm
    private String status;    // Open / Served / Billed / Cancelled

    public Order(int id, int tableId, int staffId, String orderTime, String status) {
        this.id = id;
        this.tableId = tableId;
        this.staffId = staffId;
        this.orderTime = orderTime;
        this.status = status;
    }

    public int getId() { return id; }
    public int getTableId() { return tableId; }
    public int getStaffId() { return staffId; }
    public String getOrderTime() { return orderTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toCsv() {
        return FileUtil.join(String.valueOf(id), String.valueOf(tableId), String.valueOf(staffId), orderTime, status);
    }

    public static Order fromCsv(String line) {
        String[] f = FileUtil.split(line);
        return new Order(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2]), f[3], f[4]);
    }

    @Override
    public String toString() {
        return String.format("OrderID:%-5d TableID:%-6d StaffID:%-6d Time:%-18s Status:%s",
                id, tableId, staffId, orderTime, status);
    }
}
