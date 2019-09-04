package nyriu.ricettavola.models;

/**
 * Represents a recipe TODO
 */
public class Recipe {

    private String title;

    public Recipe() {
    }

    public Recipe(String title) {
        this.title = title;
    }


    // #############################################################################################
    // Getter & Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
