package restaurant.service;

import restaurant.model.OrderItem;
import restaurant.util.FileUtil;

import java.util.*;

public class OrderItemService {
    private static final String FILE = "order_items.csv";
    private final List<OrderItem> items = new ArrayList<>();
    private int nextId = 1;

    public OrderItemService() {
        load();
    }

    private void load() {
        items.clear();
        for (String line : FileUtil.readLines(FILE)) {
            try {
                OrderItem oi = OrderItem.fromCsv(line);
                items.add(oi);
                if (oi.getId() >= nextId) nextId = oi.getId() + 1;
            } catch (Exception ignored) { }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (OrderItem oi : items) lines.add(oi.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public OrderItem add(int orderId, int menuItemId, int quantity, double priceAtOrder) {
        OrderItem oi = new OrderItem(nextId++, orderId, menuItemId, quantity, priceAtOrder);
        items.add(oi);
        save();
        return oi;
    }

    public List<OrderItem> getAll() {
        return Collections.unmodifiableList(items);
    }

    public Optional<OrderItem> findById(int id) {
        return items.stream().filter(oi -> oi.getId() == id).findFirst();
    }

    public List<OrderItem> findByOrder(int orderId) {
        List<OrderItem> result = new ArrayList<>();
        for (OrderItem oi : items) {
            if (oi.getOrderId() == orderId) result.add(oi);
        }
        return result;
    }

    public double calculateOrderSubtotal(int orderId) {
        double total = 0;
        for (OrderItem oi : findByOrder(orderId)) {
            total += oi.getLineTotal();
        }
        return total;
    }

    public boolean updateQuantity(int id, int quantity) {
        Optional<OrderItem> opt = findById(id);
        if (opt.isEmpty()) return false;
        opt.get().setQuantity(quantity);
        save();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = items.removeIf(oi -> oi.getId() == id);
        if (removed) save();
        return removed;
    }

    public void deleteByOrder(int orderId) {
        boolean removed = items.removeIf(oi -> oi.getOrderId() == orderId);
        if (removed) save();
    }
}
