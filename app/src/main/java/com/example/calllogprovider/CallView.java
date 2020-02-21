package com.example.calllogprovider;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class CallView extends AppCompatActivity implements CallContract.View {
    private static final int PERMISSIONS_REQUEST_READ_CALL_LOG = 100;
    private RecyclerView rcvCalls;
    private CallAdapter callAdapter;
    private CallContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Se inicializa el Adapter y RecyclerView
        callAdapter = new CallAdapter(this);
        rcvCalls = findViewById(R.id.rcvCalls);
        rcvCalls.setLayoutManager(new LinearLayoutManager(this));
        rcvCalls.setAdapter(callAdapter);
        //Se inicializa el Presenter
        presenter = new CallPresenter(this);
        showCalls();
    }

    /**
     * Método que visualiz las llamadas del dispositivo
     */
    private void showCalls() {
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, PERMISSIONS_REQUEST_READ_CALL_LOG);
        else
            presenter.load();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CALL_LOG:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    showCalls();
                }
                break;
        }
    }

    @Override
    public void swap(Cursor cursor) {
        callAdapter.swap(cursor);

    }

    @Override
    public Activity getContext() {
        return this;
    }
}
