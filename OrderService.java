package restaurant.service;

import restaurant.model.Order;
import restaurant.util.FileUtil;

import java.util.*;

public class OrderService {
    private static final String FILE = "orders.csv";
    private final List<Order> orders = new ArrayList<>();
    private int nextId = 1;

    public OrderService() {
        load();
    }

    private void load() {
        orders.clear();
        for (String line : FileUtil.readLines(FILE)) {
            try {
                Order o = Order.fromCsv(line);
                orders.add(o);
                if (o.getId() >= nextId) nextId = o.getId() + 1;
            } catch (Exception ignored) { }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Order o : orders) lines.add(o.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public Order add(int tableId, int staffId, String orderTime) {
        Order o = new Order(nextId++, tableId, staffId, orderTime, "Open");
        orders.add(o);
        save();
        return o;
    }

    public List<Order> getAll() {
        return Collections.unmodifiableList(orders);
    }

    public Optional<Order> findById(int id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst();
    }

    public List<Order> findByTable(int tableId) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getTableId() == tableId) result.add(o);
        }
        return result;
    }

    public List<Order> findByStatus(String status) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus().equalsIgnoreCase(status)) result.add(o);
        }
        return result;
    }

    public boolean updateStatus(int id, String status) {
        Optional<Order> opt = findById(id);
        if (opt.isEmpty()) return false;
        opt.get().setStatus(status);
        save();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = orders.removeIf(o -> o.getId() == id);
        if (removed) save();
        return removed;
    }

    public boolean exists(int id) {
        return findById(id).isPresent();
    }
}
