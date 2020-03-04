package com.replon.www.grace_thehealthapp.Emergency;

import android.os.Parcel;
import android.os.Parcelable;

public class ContentsEmergencyData implements Parcelable{

    String name;
    String place;
    String description;
    String keywords;
    String URL;
    int image;

    public ContentsEmergencyData(String name, String place, String description, String keywords, String URL, int image) {
        this.name = name;
        this.place = place;
        this.description = description;
        this.keywords = keywords;
        this.URL = URL;
        this.image = image;
    }

    protected ContentsEmergencyData(Parcel in){
        name = in.readString();
        place = in.readString();
        description = in.readString();
        keywords = in.readString();
        URL = in.readString();
        image = in.readInt();
    }

    public static final Parcelable.Creator<ContentsEmergencyData> CREATOR = new Parcelable.Creator<ContentsEmergencyData>() {
        @Override
        public ContentsEmergencyData createFromParcel(Parcel in) {
            return new ContentsEmergencyData(in);
        }

        @Override
        public ContentsEmergencyData[] newArray(int size) {
            return new ContentsEmergencyData[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);;
        dest.writeString(place);
        dest.writeString(description);
        dest.writeString(keywords);
        dest.writeString(URL);
        dest.writeInt(image);

    }
}
