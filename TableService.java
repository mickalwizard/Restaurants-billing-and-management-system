package restaurant.service;

import restaurant.model.DiningTable;
import restaurant.util.FileUtil;

import java.util.*;

public class TableService {
    private static final String FILE = "tables.csv";
    private final List<DiningTable> tables = new ArrayList<>();
    private int nextId = 1;

    public TableService() {
        load();
    }

    private void load() {
        tables.clear();
        for (String line : FileUtil.readLines(FILE)) {
            try {
                DiningTable t = DiningTable.fromCsv(line);
                tables.add(t);
                if (t.getId() >= nextId) nextId = t.getId() + 1;
            } catch (Exception ignored) { }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (DiningTable t : tables) lines.add(t.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public DiningTable add(int tableNumber, int capacity, String status) {
        DiningTable t = new DiningTable(nextId++, tableNumber, capacity, status);
        tables.add(t);
        save();
        return t;
    }

    public List<DiningTable> getAll() {
        return Collections.unmodifiableList(tables);
    }

    public Optional<DiningTable> findById(int id) {
        return tables.stream().filter(t -> t.getId() == id).findFirst();
    }

    public List<DiningTable> findAvailable() {
        List<DiningTable> result = new ArrayList<>();
        for (DiningTable t : tables) {
            if (t.getStatus().equalsIgnoreCase("Available")) result.add(t);
        }
        return result;
    }

    public boolean updateStatus(int id, String status) {
        Optional<DiningTable> opt = findById(id);
        if (opt.isEmpty()) return false;
        opt.get().setStatus(status);
        save();
        return true;
    }

    public boolean update(int id, int tableNumber, int capacity, String status) {
        Optional<DiningTable> opt = findById(id);
        if (opt.isEmpty()) return false;
        DiningTable t = opt.get();
        t.setTableNumber(tableNumber);
        t.setCapacity(capacity);
        t.setStatus(status);
        save();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = tables.removeIf(t -> t.getId() == id);
        if (removed) save();
        return removed;
    }

    public boolean exists(int id) {
        return findById(id).isPresent();
    }
}
