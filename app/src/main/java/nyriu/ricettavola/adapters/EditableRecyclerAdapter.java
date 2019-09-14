package nyriu.ricettavola.adapters;

/**
 * Astrae la un RecyclerView i cui elementi possono avere due layout
 * uno in modalita' standard ed uno in modalita' Edit
 */
public interface EditableRecyclerAdapter {


    boolean isEditMode();

    void putEditModeOn();

    void putEditModeOff();
}
