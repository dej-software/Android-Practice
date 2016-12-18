package com.jikexueyuan.duanzi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dej on 2016/10/29.
 */
public class Joke implements Parcelable {
    private String id;
    private String title;
    private String date;
    private String content;

    public Joke() {
    }

    public Joke(String id, String title, String date, String content) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
    }

    protected Joke(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        content = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static final Creator<Joke> CREATOR = new Creator<Joke>() {
        @Override
        public Joke createFromParcel(Parcel in) {
            return new Joke(in);
        }

        @Override
        public Joke[] newArray(int size) {
            return new Joke[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(content);
    }
}
