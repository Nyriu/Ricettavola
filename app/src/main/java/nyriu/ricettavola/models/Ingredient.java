package nyriu.ricettavola.models;


import android.os.Parcel;
import android.os.Parcelable;

//@Entity(tableName = "ingredients")
public class Ingredient implements Parcelable {


    //@PrimaryKey(autoGenerate = true)
    //private int id;


    //@ColumnInfo(name = "description")
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



    // #############################################################################################
    // Parcelable Stuff

    protected Ingredient(Parcel in) {
        description = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
    }
}
