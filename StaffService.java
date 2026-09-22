package restaurant.service;

import restaurant.model.Staff;
import restaurant.util.FileUtil;

import java.util.*;

public class StaffService {
    private static final String FILE = "staff.csv";
    private final List<Staff> staffList = new ArrayList<>();
    private int nextId = 1;

    public StaffService() {
        load();
    }

    private void load() {
        staffList.clear();
        for (String line : FileUtil.readLines(FILE)) {
            try {
                Staff s = Staff.fromCsv(line);
                staffList.add(s);
                if (s.getId() >= nextId) nextId = s.getId() + 1;
            } catch (Exception ignored) { }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Staff s : staffList) lines.add(s.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public Staff add(String name, String role, String contact, double salary) {
        Staff s = new Staff(nextId++, name, role, contact, salary);
        staffList.add(s);
        save();
        return s;
    }

    public List<Staff> getAll() {
        return Collections.unmodifiableList(staffList);
    }

    public Optional<Staff> findById(int id) {
        return staffList.stream().filter(s -> s.getId() == id).findFirst();
    }

    public List<Staff> findByRole(String role) {
        List<Staff> result = new ArrayList<>();
        for (Staff s : staffList) {
            if (s.getRole().equalsIgnoreCase(role)) result.add(s);
        }
        return result;
    }

    public List<Staff> searchByName(String keyword) {
        List<Staff> result = new ArrayList<>();
        String k = keyword.toLowerCase();
        for (Staff s : staffList) {
            if (s.getName().toLowerCase().contains(k)) result.add(s);
        }
        return result;
    }

    public boolean update(int id, String name, String role, String contact, double salary) {
        Optional<Staff> opt = findById(id);
        if (opt.isEmpty()) return false;
        Staff s = opt.get();
        s.setName(name);
        s.setRole(role);
        s.setContact(contact);
        s.setSalary(salary);
        save();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = staffList.removeIf(s -> s.getId() == id);
        if (removed) save();
        return removed;
    }

    public boolean exists(int id) {
        return findById(id).isPresent();
    }
}
