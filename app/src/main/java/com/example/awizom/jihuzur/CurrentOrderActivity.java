package com.example.awizom.jihuzur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.awizom.jihuzur.Helper.GetMyOrderRunningHelper;
import com.example.awizom.jihuzur.Util.SharedPrefManager;

import java.util.concurrent.ExecutionException;


public class CurrentOrderActivity extends AppCompatActivity {

    private String result="",userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_activity);

        InitView();
    }

    private void InitView() {
       userId= SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();
        getMyOrderRunning();
    }

    private void getMyOrderRunning() {
        try {
            result   = new GetMyOrderRunningHelper.GetMyOrderRunning().execute(userId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
