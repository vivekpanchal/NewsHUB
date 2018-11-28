package com.vivekpanchal.newshub.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsFeedModel implements Parcelable {

    private String name;
    private String date;
    private String imageUrl;
    private String authorName;
    private String description;
    private String newsSource;
    private String newsUrl;

    public NewsFeedModel(String name, String date, String imageUrl,
                         String authorName, String description,
                         String newsSource, String newsUrl) {
        this.name = name;
        this.date = date;
        this.imageUrl = imageUrl;
        this.authorName = authorName;
        this.description = description;
        this.newsSource = newsSource;
        this.newsUrl = newsUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.date);
        parcel.writeString(this.imageUrl);
        parcel.writeString(this.authorName);
        parcel.writeString(this.description);
        parcel.writeString(this.newsSource);
        parcel.writeString(this.newsUrl);
    }

    protected NewsFeedModel(Parcel in) {
        this.name = in.readString();
        this.date = in.readString();
        this.imageUrl = in.readString();
        this.authorName = in.readString();
        this.description = in.readString();
        this.newsSource = in.readString();
        this.newsUrl = in.readString();
    }

    public static final Parcelable.Creator<NewsFeedModel> CREATOR = new Parcelable.Creator<NewsFeedModel>() {

        @Override
        public NewsFeedModel createFromParcel(Parcel parcel) {
            return new NewsFeedModel(parcel);
        }

        @Override
        public NewsFeedModel[] newArray(int i) {
            return new NewsFeedModel[i];
        }
    };
}
