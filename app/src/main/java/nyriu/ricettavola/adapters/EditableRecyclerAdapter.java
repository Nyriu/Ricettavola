package nyriu.ricettavola.adapters;

public interface EditableRecyclerAdapter {

    //private boolean mEditMode;


    boolean isEditMode();
        //return this.mEditMode;

    void putEditModeOn();
        //this.mEditMode = true;

    void putEditModeOff();
        //this.mEditMode = false;
}
