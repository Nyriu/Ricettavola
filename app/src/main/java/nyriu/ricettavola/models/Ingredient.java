package nyriu.ricettavola.models;


public class Ingredient {

    private String description;

    public Ingredient() {
        this.description = "Placeholder ingredients";
    }

    public Ingredient(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        String s = "Ingredient{";
        s += "description='" + this.description;

        s += '}';
        return s;
    }

    // #############################################################################################
    // Getter & Setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
