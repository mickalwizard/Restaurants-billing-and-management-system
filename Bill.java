package restaurant.model;

import restaurant.util.FileUtil;

public class Bill {
    private int id;
    private int orderId;
    private double subtotal;
    private double taxPercent;
    private double discountPercent;
    private double totalAmount;
    private String paymentMethod; // Cash / Card / UPI / Unpaid
    private String status;        // Paid / Unpaid
    private String billDate;      // yyyy-MM-dd HH:mm

    public Bill(int id, int orderId, double subtotal, double taxPercent, double discountPercent,
                double totalAmount, String paymentMethod, String status, String billDate) {
        this.id = id;
        this.orderId = orderId;
        this.subtotal = subtotal;
        this.taxPercent = taxPercent;
        this.discountPercent = discountPercent;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.billDate = billDate;
    }

    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public double getSubtotal() { return subtotal; }
    public double getTaxPercent() { return taxPercent; }
    public double getDiscountPercent() { return discountPercent; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBillDate() { return billDate; }

    public String toCsv() {
        return FileUtil.join(String.valueOf(id), String.valueOf(orderId), String.valueOf(subtotal),
                String.valueOf(taxPercent), String.valueOf(discountPercent), String.valueOf(totalAmount),
                paymentMethod, status, billDate);
    }

    public static Bill fromCsv(String line) {
        String[] f = FileUtil.split(line);
        return new Bill(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Double.parseDouble(f[2]),
                Double.parseDouble(f[3]), Double.parseDouble(f[4]), Double.parseDouble(f[5]),
                f[6], f[7], f[8]);
    }

    @Override
    public String toString() {
        return String.format("BillID:%-5d OrderID:%-5d Subtotal:%-10.2f Tax:%-6.1f%% Discount:%-6.1f%% " +
                        "Total:%-10.2f Payment:%-8s Status:%-8s Date:%s",
                id, orderId, subtotal, taxPercent, discountPercent, totalAmount, paymentMethod, status, billDate);
    }
}
