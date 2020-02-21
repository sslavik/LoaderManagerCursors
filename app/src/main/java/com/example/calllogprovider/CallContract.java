package com.example.calllogprovider;

import android.app.Activity;
import android.database.Cursor;

public interface CallContract {
    interface View{

        void swap(Cursor cursor);

        Activity getContext();
    }
    interface Presenter {

        void load();
    }
}
