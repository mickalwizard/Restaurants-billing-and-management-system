package restaurant.service;

import restaurant.model.Bill;
import restaurant.util.FileUtil;

import java.util.*;

public class BillService {
    private static final String FILE = "bills.csv";
    private final List<Bill> bills = new ArrayList<>();
    private int nextId = 1;

    public BillService() {
        load();
    }

    private void load() {
        bills.clear();
        for (String line : FileUtil.readLines(FILE)) {
            try {
                Bill b = Bill.fromCsv(line);
                bills.add(b);
                if (b.getId() >= nextId) nextId = b.getId() + 1;
            } catch (Exception ignored) { }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Bill b : bills) lines.add(b.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    /** Generates a new bill from a subtotal, applying tax and discount percentages. */
    public Bill generate(int orderId, double subtotal, double taxPercent, double discountPercent,
                          String paymentMethod, String billDate) {
        double afterDiscount = subtotal - (subtotal * discountPercent / 100.0);
        double taxAmount = afterDiscount * taxPercent / 100.0;
        double total = afterDiscount + taxAmount;
        String status = paymentMethod.equalsIgnoreCase("Unpaid") ? "Unpaid" : "Paid";
        Bill b = new Bill(nextId++, orderId, subtotal, taxPercent, discountPercent, total, paymentMethod, status, billDate);
        bills.add(b);
        save();
        return b;
    }

    public List<Bill> getAll() {
        return Collections.unmodifiableList(bills);
    }

    public Optional<Bill> findById(int id) {
        return bills.stream().filter(b -> b.getId() == id).findFirst();
    }

    public Optional<Bill> findByOrder(int orderId) {
        return bills.stream().filter(b -> b.getOrderId() == orderId).findFirst();
    }

    public List<Bill> findUnpaid() {
        List<Bill> result = new ArrayList<>();
        for (Bill b : bills) {
            if (b.getStatus().equalsIgnoreCase("Unpaid")) result.add(b);
        }
        return result;
    }

    public boolean markPaid(int id, String paymentMethod) {
        Optional<Bill> opt = findById(id);
        if (opt.isEmpty()) return false;
        Bill b = opt.get();
        b.setStatus("Paid");
        b.setPaymentMethod(paymentMethod);
        save();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = bills.removeIf(b -> b.getId() == id);
        if (removed) save();
        return removed;
    }

    /** Sum of totalAmount across all Paid bills — a simple revenue report. */
    public double totalRevenue() {
        double total = 0;
        for (Bill b : bills) {
            if (b.getStatus().equalsIgnoreCase("Paid")) total += b.getTotalAmount();
        }
        return total;
    }
}
