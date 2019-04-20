package com.example.awizom.jihuzur.AdminActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.MyEmployeeListAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.MyEmployeeListModel;
import com.example.awizom.jihuzur.Model.ResultModel;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminMyEmployeeActivity extends AppCompatActivity implements View.OnTouchListener {

    RecyclerView recyclerView;
    String result = "";
    List<MyEmployeeListModel> myEmployeeListModels;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MyEmployeeListAdapter myEmployeeListAdapter;
    private ProgressDialog progressDialog;
    FloatingActionButton addEmployee;
    FirebaseFirestore db;
    ViewDialog viewDialog;
    FrameLayout rootlayout;
    int _xDelta;
    int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_myemployee);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Employee List");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        viewDialog = new ViewDialog(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        initView();
    }

    private void initView() {
        db = FirebaseFirestore.getInstance();
        rootlayout = (FrameLayout) findViewById(R.id.rootlayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getMyEmployeeList();
        addEmployee = (FloatingActionButton) findViewById(R.id.addEmployee);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(120, 120);
        addEmployee.setLayoutParams(layoutParams);
        addEmployee.setOnTouchListener(AdminMyEmployeeActivity.this);
        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaddEmployee();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getMyEmployeeList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showaddEmployee() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogue_add_employee, null);
        dialogBuilder.setView(dialogView);
        final AutoCompleteTextView Name = (AutoCompleteTextView) dialogView.findViewById(R.id.Name);
        final AutoCompleteTextView Phonenumber = (AutoCompleteTextView) dialogView.findViewById(R.id.phonenumber);
        final AutoCompleteTextView Email = (AutoCompleteTextView) dialogView.findViewById(R.id.email);
        final AutoCompleteTextView Address = (AutoCompleteTextView) dialogView.findViewById(R.id.address);
        final Button buttonaddEmployee = (Button) dialogView.findViewById(R.id.buttonaddemployee);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        dialogBuilder.setTitle("Add Employee");
        dialogBuilder.setIcon(R.drawable.useredit);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonaddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Name.getText().toString().isEmpty()) {
                    Name.setError("Enter a valid Employee Name");
                    Name.requestFocus();
                } else if (Phonenumber.getText().toString().isEmpty() ||Phonenumber.getText().toString().length()>10 || Phonenumber.getText().toString().length()<10) {
                    Phonenumber.setError("Please Enter Valid Phonenumber");
                    Phonenumber.requestFocus();
                }
                else if (Address.getText().toString().isEmpty()) {
                    Address.setError("Please Enter Valid Address");
                    Address.requestFocus();
                }

                else {
                    showcustomloadindialogue();
                    String name = Name.getText().toString();
                    String phonenumber = Phonenumber.getText().toString();
                    String email = Email.getText().toString();
                    String address = Address.getText().toString();
                    String userName = Phonenumber.getText().toString();
                    String password = "Jihuzur@123";
                    String ConfirmPassword = "Jihuzur@123";
                    try {
                        result = new AdminHelper.POSTAddEmployee().execute(name, phonenumber, email, address, userName, password, ConfirmPassword).get();
                        Gson gson = new Gson();
                        Type getType = new TypeToken<ResultModel>() {
                        }.getType();
                        ResultModel resultModel = new Gson().fromJson(result, getType);
                        try {
                            String employeid = resultModel.getMessage().split(",")[1];
                            Map<String, Object> profile = new HashMap<>();
                            profile.put("busystatus", false);
                            profile.put("lat", 21.22);
                            profile.put("long", 81.66);
                            db.collection("Profile").document(employeid)
                                    .set(profile)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //   Log.d(TAG, "DocumentSnapshot successfully written!");
                                            Toast.makeText(getApplicationContext(), "Success!",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Failed!",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getMyEmployeeList();
                       b.dismiss();
                    } catch (Exception e) {
                    }
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    public void showcustomloadindialogue() {

        //..show gif
        viewDialog.showDialog();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog();
            }
        }, 1000);
    }

    public void getMyEmployeeList() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result = new AdminHelper.GetMyEmployeeList().execute().get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MyEmployeeListModel>>() {
            }.getType();
            myEmployeeListModels = new Gson().fromJson(result, listType);
            myEmployeeListAdapter = new MyEmployeeListAdapter(AdminMyEmployeeActivity.this, myEmployeeListModels);
            mSwipeRefreshLayout.setRefreshing(false);
            recyclerView.setAdapter(myEmployeeListAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                showaddEmployee();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //  showaddEmployee();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //  showaddEmployee();
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                v.setLayoutParams(layoutParams);
                break;
        }


        rootlayout.invalidate();
        return true;
    }
}
