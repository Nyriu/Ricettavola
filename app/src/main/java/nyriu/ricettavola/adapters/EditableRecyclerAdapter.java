package nyriu.ricettavola.adapters;

public interface EditableRecyclerAdapter {


    boolean isEditMode();

    void putEditModeOn();

    void putEditModeOff();
}
