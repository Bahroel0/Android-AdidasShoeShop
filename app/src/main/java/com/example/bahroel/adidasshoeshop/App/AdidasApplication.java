package com.example.bahroel.adidasshoeshop.App;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AdidasApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }

    public static boolean deleteRealm(RealmConfiguration configuration) {
        return Realm.deleteRealm(configuration);
    }
}
