package nyriu.ricettavola.models;

import android.os.Parcel;
import android.os.Parcelable;

//@Entity(tableName = "preparation_steps")
public class PreparationStep implements Comparable, Parcelable {

    //@PrimaryKey(autoGenerate = true)
    //private int id;

    //@ColumnInfo(name = "number")
    private int number;
    //@ColumnInfo(name = "description")
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




    // #############################################################################################
    // Parcelable Stuff

    protected PreparationStep(Parcel in) {
        number = in.readInt();
        description = in.readString();
    }

    public static final Creator<PreparationStep> CREATOR = new Creator<PreparationStep>() {
        @Override
        public PreparationStep createFromParcel(Parcel in) {
            return new PreparationStep(in);
        }

        @Override
        public PreparationStep[] newArray(int size) {
            return new PreparationStep[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(description);
    }

}
