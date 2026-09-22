package restaurant.service;

import restaurant.model.MenuItem;
import restaurant.util.FileUtil;

import java.util.*;

public class MenuService {
    private static final String FILE = "menu.csv";
    private final List<MenuItem> items = new ArrayList<>();
    private int nextId = 1;

    public MenuService() {
        load();
    }

    private void load() {
        items.clear();
        for (String line : FileUtil.readLines(FILE)) {
            try {
                MenuItem m = MenuItem.fromCsv(line);
                items.add(m);
                if (m.getId() >= nextId) nextId = m.getId() + 1;
            } catch (Exception ignored) { }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (MenuItem m : items) lines.add(m.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public MenuItem add(String name, String category, double price, boolean available) {
        MenuItem m = new MenuItem(nextId++, name, category, price, available);
        items.add(m);
        save();
        return m;
    }

    public List<MenuItem> getAll() {
        return Collections.unmodifiableList(items);
    }

    public Optional<MenuItem> findById(int id) {
        return items.stream().filter(m -> m.getId() == id).findFirst();
    }

    public List<MenuItem> findByCategory(String category) {
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem m : items) {
            if (m.getCategory().equalsIgnoreCase(category)) result.add(m);
        }
        return result;
    }

    public List<MenuItem> searchByName(String keyword) {
        List<MenuItem> result = new ArrayList<>();
        String k = keyword.toLowerCase();
        for (MenuItem m : items) {
            if (m.getName().toLowerCase().contains(k)) result.add(m);
        }
        return result;
    }

    public boolean update(int id, String name, String category, double price, boolean available) {
        Optional<MenuItem> opt = findById(id);
        if (opt.isEmpty()) return false;
        MenuItem m = opt.get();
        m.setName(name);
        m.setCategory(category);
        m.setPrice(price);
        m.setAvailable(available);
        save();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = items.removeIf(m -> m.getId() == id);
        if (removed) save();
        return removed;
    }

    public boolean exists(int id) {
        return findById(id).isPresent();
    }
}
