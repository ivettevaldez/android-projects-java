package com.silviavaldez.gettingstartedapp.daos;

import android.content.Context;

import com.silviavaldez.gettingstartedapp.datamodels.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Silvia Valdez on 9/26/16.
 */

public class UserDao {

    private Realm mRealm;

    public UserDao(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name("myExampleDb.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
        this.mRealm = Realm.getDefaultInstance();
    }


    public User getUserByName(String name) {
        User user = mRealm.where(User.class).equalTo("mName", name).findFirst();
        if (user != null) {
            return mRealm.copyFromRealm(user);
        }
        return null;
    }

    public long getLastId() {
        RealmResults<User> users = mRealm.where(User.class).findAllSorted("mCreatedAt");
        long lastId;
        if (!users.isEmpty()) {
            lastId = users.last().getId() + 1;
        } else {
            lastId = 1;
        }
        return lastId;
    }

    public boolean createOrUpdate(final User user) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(user);
            }
        });
        return true;
    }

    public boolean delete(final long userId) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).equalTo("mId", userId).findFirst();
                user.deleteFromRealm();
            }
        });
        return true;
    }

}

