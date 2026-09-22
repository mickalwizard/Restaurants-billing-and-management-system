package restaurant.model;

import restaurant.util.FileUtil;

public class Reservation {
    private int id;
    private String customerName;
    private String contact;
    private int tableId;
    private String date;    // yyyy-MM-dd
    private String time;    // HH:mm
    private int numGuests;
    private String status;  // Confirmed / Cancelled / Completed

    public Reservation(int id, String customerName, String contact, int tableId, String date,
                        String time, int numGuests, String status) {
        this.id = id;
        this.customerName = customerName;
        this.contact = contact;
        this.tableId = tableId;
        this.date = date;
        this.time = time;
        this.numGuests = numGuests;
        this.status = status;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getNumGuests() { return numGuests; }
    public void setNumGuests(int numGuests) { this.numGuests = numGuests; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toCsv() {
        return FileUtil.join(String.valueOf(id), customerName, contact, String.valueOf(tableId), date, time,
                String.valueOf(numGuests), status);
    }

    public static Reservation fromCsv(String line) {
        String[] f = FileUtil.split(line);
        return new Reservation(Integer.parseInt(f[0]), f[1], f[2], Integer.parseInt(f[3]), f[4], f[5],
                Integer.parseInt(f[6]), f[7]);
    }

    @Override
    public String toString() {
        return String.format("ID:%-4d Customer:%-18s Contact:%-14s TableID:%-6d Date:%-12s Time:%-7s Guests:%-4d Status:%s",
                id, customerName, contact, tableId, date, time, numGuests, status);
    }
}
