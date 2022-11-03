public class Product {
    private int id;
    private String name;
    private int price;
    private String image;
    private String link;

    public Product(int id, String name, int price, String image, String link) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toString() {
        return "<a href=\"" + image + "\">&#160;</a>" + "<a href=\"" + link + "\">" + name + "</a>" + "\n" + " Price: " + price + " &#8381; \n";
    }
}
