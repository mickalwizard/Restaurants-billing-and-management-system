package restaurant.model;

import restaurant.util.FileUtil;

public class Staff {
    private int id;
    private String name;
    private String role;     // Waiter / Chef / Manager / Cashier
    private String contact;
    private double salary;

    public Staff(int id, String name, String role, String contact, double salary) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.contact = contact;
        this.salary = salary;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String toCsv() {
        return FileUtil.join(String.valueOf(id), name, role, contact, String.valueOf(salary));
    }

    public static Staff fromCsv(String line) {
        String[] f = FileUtil.split(line);
        return new Staff(Integer.parseInt(f[0]), f[1], f[2], f[3], Double.parseDouble(f[4]));
    }

    @Override
    public String toString() {
        return String.format("ID:%-4d Name:%-20s Role:%-10s Contact:%-15s Salary:%.2f",
                id, name, role, contact, salary);
    }
}
