package model;

public class Operation {
    private int id;
    private int a;
    private int b;
    private int c;

    public Operation(int id, int a, int b, int c) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public int getA() { return a; }
    public int getB() { return b; }
    public int getC() { return c; }

    public void setId(int id) { this.id = id; }
    public void setA(int a) { this.a = a; }
    public void setB(int b) { this.b = b; }
    public void setC(int c) { this.c = c; }
}