package restaurant.model;

import restaurant.util.FileUtil;

public class InventoryItem {
    private int id;
    private String name;
    private double quantity;
    private String unit;        // kg, litre, pieces, etc.
    private double reorderLevel; // minimum quantity before it's considered low stock

    public InventoryItem(int id, String name, double quantity, String unit, double reorderLevel) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.reorderLevel = reorderLevel;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public double getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(double reorderLevel) { this.reorderLevel = reorderLevel; }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    public String toCsv() {
        return FileUtil.join(String.valueOf(id), name, String.valueOf(quantity), unit, String.valueOf(reorderLevel));
    }

    public static InventoryItem fromCsv(String line) {
        String[] f = FileUtil.split(line);
        return new InventoryItem(Integer.parseInt(f[0]), f[1], Double.parseDouble(f[2]), f[3], Double.parseDouble(f[4]));
    }

    @Override
    public String toString() {
        String flag = isLowStock() ? "  [LOW STOCK]" : "";
        return String.format("ID:%-4d Name:%-20s Quantity:%-10.2f Unit:%-8s ReorderLevel:%-10.2f%s",
                id, name, quantity, unit, reorderLevel, flag);
    }
}
