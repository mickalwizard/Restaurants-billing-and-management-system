package restaurant.service;

import restaurant.model.Reservation;
import restaurant.util.FileUtil;

import java.util.*;

public class ReservationService {
    private static final String FILE = "reservations.csv";
    private final List<Reservation> reservations = new ArrayList<>();
    private int nextId = 1;

    public ReservationService() {
        load();
    }

    private void load() {
        reservations.clear();
        for (String line : FileUtil.readLines(FILE)) {
            try {
                Reservation r = Reservation.fromCsv(line);
                reservations.add(r);
                if (r.getId() >= nextId) nextId = r.getId() + 1;
            } catch (Exception ignored) { }
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Reservation r : reservations) lines.add(r.toCsv());
        FileUtil.writeLines(FILE, lines);
    }

    public Reservation add(String customerName, String contact, int tableId, String date, String time, int numGuests) {
        Reservation r = new Reservation(nextId++, customerName, contact, tableId, date, time, numGuests, "Confirmed");
        reservations.add(r);
        save();
        return r;
    }

    public List<Reservation> getAll() {
        return Collections.unmodifiableList(reservations);
    }

    public Optional<Reservation> findById(int id) {
        return reservations.stream().filter(r -> r.getId() == id).findFirst();
    }

    public List<Reservation> findByDate(String date) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getDate().equals(date)) result.add(r);
        }
        return result;
    }

    public List<Reservation> findByTable(int tableId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getTableId() == tableId) result.add(r);
        }
        return result;
    }

    public boolean updateStatus(int id, String status) {
        Optional<Reservation> opt = findById(id);
        if (opt.isEmpty()) return false;
        opt.get().setStatus(status);
        save();
        return true;
    }

    public boolean delete(int id) {
        boolean removed = reservations.removeIf(r -> r.getId() == id);
        if (removed) save();
        return removed;
    }
}
