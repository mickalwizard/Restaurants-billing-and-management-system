package restaurant.model;

import restaurant.util.FileUtil;

public class DiningTable {
    private int id;
    private int tableNumber;
    private int capacity;
    private String status; // Available / Occupied / Reserved

    public DiningTable(int id, int tableNumber, int capacity, String status) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public int getId() { return id; }
    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toCsv() {
        return FileUtil.join(String.valueOf(id), String.valueOf(tableNumber), String.valueOf(capacity), status);
    }

    public static DiningTable fromCsv(String line) {
        String[] f = FileUtil.split(line);
        return new DiningTable(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2]), f[3]);
    }

    @Override
    public String toString() {
        return String.format("ID:%-4d TableNo:%-6d Capacity:%-6d Status:%s", id, tableNumber, capacity, status);
    }
}
