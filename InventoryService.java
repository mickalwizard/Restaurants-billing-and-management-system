package restaurant.service;

import restaurant.model.InventoryItem;
import restaurant.util.FileUtil;

import java.util.*;

public class InventoryService {
    private static final String FILE = "inventory.csv";
    private final List<InventoryItem> items = new ArrayList<>();
    private int nextId = 1;

    public InventoryService() {
        load();
    }

    private void load() {
        items.clear();
        for (String line : FileUtil.readLines(FILE)) {
            try {
                InventoryItem i = InventoryItem.fromCsv(line);
                items.add(i);
                if (i.getId() >= nextId) nextId = i.getId() + 1;
            } catch (Exception ignored) { }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (InventoryItem i : items) lines.add(i.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public InventoryItem add(String name, double quantity, String unit, double reorderLevel) {
        InventoryItem i = new InventoryItem(nextId++, name, quantity, unit, reorderLevel);
        items.add(i);
        save();
        return i;
    }

    public List<InventoryItem> getAll() {
        return Collections.unmodifiableList(items);
    }

    public Optional<InventoryItem> findById(int id) {
        return items.stream().filter(i -> i.getId() == id).findFirst();
    }

    public List<InventoryItem> searchByName(String keyword) {
        List<InventoryItem> result = new ArrayList<>();
        String k = keyword.toLowerCase();
        for (InventoryItem i : items) {
            if (i.getName().toLowerCase().contains(k)) result.add(i);
        }
        return result;
    }

    public List<InventoryItem> findLowStock() {
        List<InventoryItem> result = new ArrayList<>();
        for (InventoryItem i : items) {
            if (i.isLowStock()) result.add(i);
        }
        return result;
    }

    /** Adds (positive) or removes (negative) stock quantity, e.g. after a delivery or usage. */
    public boolean adjustStock(int id, double delta) {
        Optional<InventoryItem> opt = findById(id);
        if (opt.isEmpty()) return false;
        InventoryItem i = opt.get();
        double newQty = i.getQuantity() + delta;
        if (newQty < 0) newQty = 0;
        i.setQuantity(newQty);
        save();
        return true;
    }

    public boolean update(int id, String name, double quantity, String unit, double reorderLevel) {
        Optional<InventoryItem> opt = findById(id);
        if (opt.isEmpty()) return false;
        InventoryItem i = opt.get();
        i.setName(name);
        i.setQuantity(quantity);
        i.setUnit(unit);
        i.setReorderLevel(reorderLevel);
        save();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = items.removeIf(i -> i.getId() == id);
        if (removed) save();
        return removed;
    }
}
