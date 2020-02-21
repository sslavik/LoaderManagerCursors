package com.example.calllogprovider;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

class CallPresenter implements CallContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_CALL = 100;
    private static final int LOADER_CONTACT = 101;
    private CallContract.View view;

    public CallPresenter(CallContract.View view) {
        this.view = view;
    }

    @Override
    public void load() {
        //Inicia el LoaderManager
        ((AppCompatActivity)view.getContext()).getSupportLoaderManager().restartLoader(LOADER_CALL,null,this);


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        CursorLoader cursorLoader=null;
        switch (id){
            case LOADER_CALL:
                String[] projection={CallLog.Calls._ID,CallLog.Calls.NUMBER,CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE};
                cursorLoader=new CursorLoader(view.getContext(),CallLog.Calls.CONTENT_URI,projection,null,null,null);
                break;
            case LOADER_CONTACT:
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        view.swap(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        view.swap(null);

    }
}
