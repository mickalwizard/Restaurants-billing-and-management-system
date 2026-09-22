package restaurant.model;

import restaurant.util.FileUtil;

public class MenuItem {
    private int id;
    private String name;
    private String category;   // e.g. Starter, Main Course, Dessert, Beverage
    private double price;
    private boolean available;

    public MenuItem(int id, String name, String category, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String toCsv() {
        return FileUtil.join(String.valueOf(id), name, category, String.valueOf(price), String.valueOf(available));
    }

    public static MenuItem fromCsv(String line) {
        String[] f = FileUtil.split(line);
        return new MenuItem(Integer.parseInt(f[0]), f[1], f[2], Double.parseDouble(f[3]), Boolean.parseBoolean(f[4]));
    }

    @Override
    public String toString() {
        return String.format("ID:%-4d Name:%-20s Category:%-14s Price:%-10.2f Status:%s",
                id, name, category, price, available ? "Available" : "Unavailable");
    }
}
