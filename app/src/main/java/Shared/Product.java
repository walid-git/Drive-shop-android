package Shared;


import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private int price;
    private String name;
    private String description;
    private String iconPath;
    private int location;
    private int available_qty;
    private byte img[];

    public Product(int id, int price, String name, String description,int available_qty, String iconPath, byte[] img, int location) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.img = img;
        this.location = location;
        this.available_qty = available_qty;
    }

    public Product(int id, int price, String name, String description, String iconPath,int available_qty, byte[] img) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.img = img;
        this.available_qty = available_qty;
    }

    public Product(int id, int price, String name, String description,int available_qty, byte[] img) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.img = img;
        this.available_qty = available_qty;
    }


    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(int available_qty) {
        this.available_qty = available_qty;
    }

    @Override
    public String toString() {
        return "Product : " + name + " costs : " + price + "$ description : " + description + " size " + img.length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
