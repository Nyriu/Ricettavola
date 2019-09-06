package nyriu.ricettavola.models;

public class PreparationStep implements Comparable {
    private int number;
    private String description;

    public PreparationStep() {
        this.number = 0;
        this.description = "Placeholder description";
    }

    public PreparationStep(int number, String description) {
        this.number = number;
        this.description = description;
    }

    @Override
    public int compareTo(Object o) {
        // return 1 if o should be before this
        // return -1 if this should be before p2
        // return o otherwise (meaning the order stays the same)
        if (o.getClass().equals(this.getClass())) {
            PreparationStep p = (PreparationStep) o;
            if (this.number < p.number) {
                return 1;
            } else if (this.number > p.number) {
                return -1;
            } else {
                return 0;
            }
        } else {
            // TODO come va gestito?
            return 0;
        }
    }

    @Override
    public String toString() {
        String s = "PreparationStep{";
        s += "number='" + this.number + "', ";
        s += "description='" + this.description;

        s += '}';
        return s;
    }

    // #############################################################################################
    // Getter & Setter
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
