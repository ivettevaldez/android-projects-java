package com.silviavaldez.gettingstartedapp.datamodels;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Realm model class for User.
 * Created by Silvia Valdez on 9/26/16.
 */

public class User extends RealmObject {

    @PrimaryKey
    @Index
    private long mId;

    private String mName;
    private String mPassword;
    private Date mCreatedAt;


    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

}
