// package-less, default package. Make this a separate file Item.java in the same folder.
public class Item {
    private String name;
    private String description;

    public Item(String name) {
        this(name, "");
    }

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + (description.isEmpty() ? "" : " - " + description);
    }
}
