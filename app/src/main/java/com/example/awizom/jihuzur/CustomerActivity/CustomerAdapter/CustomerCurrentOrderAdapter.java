package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerCommentActivity;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.CustomerActivity.TrackActivity;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.Model.ResultModel;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Service.AlarmService;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.support.v4.content.ContextCompat.startForegroundService;
import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class CustomerCurrentOrderAdapter extends RecyclerView.Adapter<CustomerCurrentOrderAdapter.OrderItemViewHolder> {

    private Context mCtx;
    private List<Order> orderitemList;
    private Order order;
    Order orderList;
    private String orderId = "", otpCode = "", result = "";
    private Intent intent;
    FirebaseFirestore db;
    ViewDialog viewDialog;

    public CustomerCurrentOrderAdapter(Context currentOrderActivity, List<Order> orderList) {
        this.mCtx = currentOrderActivity;
        this.orderitemList = orderList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.display_customer_current_order_layout, null);
        return new OrderItemViewHolder(view, mCtx, orderitemList);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);
        try {
            order = orderitemList.get(position);
            orderId = String.valueOf(order.getOrderID());
            final String ordid = String.valueOf(order.getOrderID());
            final String cusid = order.getCustomerID();
            final String empid = order.getEmployeeID();
            final String endtym = order.getOrderEndTime();
            final String categorynm = order.getCatalogName();
            final String servicnam = order.getServiceName();
            final String prictrm = order.getPricingTerms();
            final String empname = order.getEmpName();
            final String empmob = order.getEmpMob();
            final String imagelink = order.getImage();
            final  String catalogId = String.valueOf(order.getCatalogID());
            viewDialog = new ViewDialog((Activity) mCtx);
            holder.empName.setText(order.getServiceName());
            holder.serviceId.setText(String.valueOf(order.getServiceID()));
            holder.empContAct.setText(order.getCategory());
            holder.timercount.setText(order.getTotalTime());
            holder.startTime.setText(order.getOrderStartTime());
            holder.endtime.setText(order.getOrderEndTime());
            holder.catagryName.setText(order.getCategory());
            holder.servicName.setText(order.getServiceName());
            holder.pricingterm.setText(order.getPricingTerms());
            holder.dctName.setText(order.getDiscountName());
            holder.orderIds.setText(String.valueOf(order.getOrderID()));
            holder.cusid.setText(order.getCustomerID());
            holder.empid.setText(order.getEmployeeID());
            holder.trackinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCustomLoadingDialog(v);
                    intent = new Intent(mCtx, TrackActivity.class);
                    intent.putExtra("CustomerID", holder.cusid.getText().toString());
                    intent.putExtra("EmployeeID", holder.empid.getText().toString());
                    mCtx.startActivity(intent
                    );
                }
            });

          //  getPayment(ordid,catalogId);

//            if(holder.endtime.equals(null)){
//                AlertDialog.Builder alertbox = new AlertDialog.Builder(mCtx);
//                alertbox.setMessage(" Your order is Completed. Price is");
//                alertbox.setTitle("Completed!");
//                alertbox.setIcon(R.drawable.ic_dashboard_black_24dp);
//                alertbox.setNeutralButton("Ok",
//                        new DialogInterface.OnClickListener() {
//                            Class fragmentClass = null;
//
//                            public void onClick(DialogInterface arg0,
//                                                int arg1) {
//
//
//                            }
//                        });
//
//
//                alertbox.show();
//            }


            final DocumentReference docRef = db.collection("Order").document(ordid);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.getData() != null) {
                        String starttime = documentSnapshot.get("startTime").toString();
                        String endtime = documentSnapshot.get("endTime").toString();
                        holder.acceptBtn.setVisibility(View.GONE);
                        holder.chronometer.setVisibility(View.VISIBLE);
                        try {

                            final Intent emptyIntent = new Intent(mCtx, MyBokingsActivity.class);
                            NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
                            String channelId = "channel-01";
                            String channelName = "Channel Name";
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                NotificationChannel mChannel = new NotificationChannel(
                                        channelId, channelName, importance);
                                notificationManager.createNotificationChannel(mChannel);
                            }

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, channelId)
                                    .setSmallIcon(R.drawable.shopping)
                                    .setContentTitle("Jihuzzur")
                                    .setContentText("Order is Started from:-" + starttime);

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mCtx);
                            /*   stackBuilder.addNextIntent(intent);*/
                            PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, Integer.parseInt(String.valueOf(ordid)), emptyIntent, 0);
                            mBuilder.setContentIntent(pendingIntent);
                            notificationManager.notify(Integer.parseInt(String.valueOf(ordid)), mBuilder.build());
                            try {
                                if (!endtime.equals("0")) {
                                    notificationManager.cancel(Integer.parseInt(String.valueOf(ordid)));

                                }

                            } catch (Exception d) {
                                d.printStackTrace();
                            }
                        } catch (Exception d) {
                            d.printStackTrace();
                        }
                    } else {
                        holder.acceptBtn.setVisibility(View.GONE);
                        holder.chronometer.setVisibility(View.GONE);
                    }
                }
            });
            holder.viewdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCustomLoadingDialog(v);
                    intent = new Intent(mCtx, CustomerCommentActivity.class);
                    intent.putExtra("OrderID", ordid);
                    intent.putExtra("CustomerID", cusid);
                    intent.putExtra("EmployeeID", empid);
                    intent.putExtra("OrderEndTime", endtym);
                    intent.putExtra("CategoryName", categorynm);
                    intent.putExtra("ServiceName", servicnam);
                    intent.putExtra("PricingTerms", prictrm);
                    intent.putExtra("EmployeeName", empname);
                    intent.putExtra("EmployeeContact", empmob);
                    intent.putExtra("ImageLink",imagelink);
                    // intent.putExtra("OrderIDs", holder.orderIds.getText().toString());
                    mCtx.startActivity(intent);
                }
            });

            holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCustomLoadingDialog(v);
                    final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                    LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
                    final View dialogView = inflater.inflate(R.layout.accept_otp_for_order_layout, null);
                    dialogBuilder.setView(dialogView);
                    final EditText enterOtp = dialogView.findViewById(R.id.editTextOtp);
                    Button verify = dialogView.findViewById(R.id.buttonVerify);
                    dialogBuilder.setTitle("Accept Otp");
                    final android.support.v7.app.AlertDialog b = dialogBuilder.create();
                    b.show();
                    if (enterOtp.getText().toString().isEmpty()) {
                        enterOtp.setError("Enter a valid value");
                        enterOtp.requestFocus();
                    }
                    verify.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onClick(final View v) {
                            try {
                                result = new CustomerOrderHelper.AcceptOtp().execute(ordid, enterOtp.getText().toString()).get();
                                Gson gson = new Gson();
                                Type getType = new TypeToken<ResultModel>() {
                                }.getType();
                                ResultModel resultModel = new Gson().fromJson(result, getType);
                                try {
                                    if (resultModel.getMessage().contains("Order Started")) {
                                        holder.acceptBtn.setVisibility(View.GONE);
                                        String employeeid = resultModel.getEmployeeID().toString();
                                        Map<String, Object> profile = new HashMap<>();
                                        profile.put("busystatus", true);
                                        db.collection("Profile").document(employeeid).update(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error writing document", e);
                                                    }
                                                });
                                        Date today = new Date();
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                        String dateToStr = format.format(today);
                                        Map<String, Object> order = new HashMap<>();
                                        order.put("startTime", dateToStr);
                                        order.put("endTime", 00);

                                        db.collection("Order").document(ordid).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //
                                                        Log.w(TAG, "Error writing document", e);
                                                    }
                                                });
                                        try {
                                          /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                Intent serviceIntent = new Intent(mCtx, AlarmService.class);
                                                serviceIntent.putExtra("inputExtra", servicnam + " Your Order Is Started");
                                                serviceIntent.putExtra("orderId", ordid);
                                                startForegroundService(mCtx, serviceIntent);*/

                                        } catch (Exception e) {
                                            e.printStackTrace();

                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //   canclBtn.setVisibility(View.GONE);
                                Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("result", result.toString());
                                Intent intent = new Intent(mCtx, MyBokingsActivity.class);
                                mCtx.startActivity(intent);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    private void showpaymentdialog(String s,String servieid) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View dialogView = inflater.inflate(R.layout.show_payment, null);
        dialogBuilder.setView(dialogView);
        TextView amount = (TextView) dialogView.findViewById(R.id.amount);
        try {
            result = new EmployeeOrderHelper.GetPayment().execute(s.toString(),servieid.toString()).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<Order>() {
            }.getType();
            orderList = new Gson().fromJson(result, listType);
            try {
                String payment = orderList.getAmount().toString();
                amount.setText(String.valueOf(payment));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //     Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final Button buttonadd = (Button) dialogView.findViewById(R.id.changePosition);
        buttonadd.setVisibility(View.GONE);
        dialogBuilder.setTitle("Order Amount is-");
        dialogBuilder.setIcon(R.drawable.coupons);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.show();


    }

    private void getPayment(String ordid, String catalogId) {
        try {
            result = new CustomerOrderHelper.GetPaymentRupees().execute(ordid,catalogId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderitemList.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context mCtx;
        private TextView chronometer, serviceId, orderIds, otps;
        private TextView startTime, endtime, empName, timercount, empContAct, catagryName, servicName, pricingterm, dctName, empid, cusid;
        private Button acceptBtn, trackinBtn, canclBtn, viewdetail;
        private List<Order> orderitemList;
        private LinearLayout linearLayout;
        private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

//        private TextView servicesName,bookingAccepted,description,timing;
//        private Button viewBtn;


        public OrderItemViewHolder(View view, final Context mCtx, List<Order> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);

//            servicesName = itemView.findViewById(R.id.servicesName);
//            bookingAccepted = itemView.findViewById(R.id.bookingAccepted);
//            description = itemView.findViewById(R.id.description);
//            timing = itemView.findViewById(R.id.timing);
            serviceId=itemView.findViewById(R.id.serviceId);
            empName = itemView.findViewById(R.id.cusName);
            startTime = itemView.findViewById(R.id.starttime);
            cusid = itemView.findViewById(R.id.cusID);
            empid = itemView.findViewById(R.id.empId);
            endtime = itemView.findViewById(R.id.endtime);
            timercount = itemView.findViewById(R.id.timeCount);
            acceptBtn = itemView.findViewById(R.id.acceptOtpBtn);
            trackinBtn = itemView.findViewById(R.id.trackBtn);
            canclBtn = itemView.findViewById(R.id.cancelBtn);
            chronometer = itemView.findViewById(R.id.chronometer);
            chronometer.setVisibility(View.GONE);
            empContAct = itemView.findViewById(R.id.empMobile);
            catagryName = itemView.findViewById(R.id.catagoryName);
            servicName = itemView.findViewById(R.id.serviceName);
            pricingterm = itemView.findViewById(R.id.pricingterm);
            dctName = itemView.findViewById(R.id.discountName);
            viewdetail = itemView.findViewById(R.id.viewDetail);
            linearLayout = itemView.findViewById(R.id.l4);
            otps = itemView.findViewById(R.id.otp);
            orderIds = itemView.findViewById(R.id.orderId);
            db = FirebaseFirestore.getInstance();
            acceptBtn.setOnClickListener(this);
            canclBtn.setOnClickListener(this);
            timercount.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            v.startAnimation(buttonClick);
            switch (v.getId()) {

                case R.id.trackBtn:


                    break;
                case R.id.cancelBtn:
                    try {
                        result = new CustomerOrderHelper.CancelOrder().execute(orderId).get();
                        Gson gson = new Gson();
                        Type getType = new TypeToken<ResultModel>() {
                        }.getType();
                        ResultModel resultModel = new Gson().fromJson(result, getType);
                        if (resultModel.getMessage().contains("Order End")) {
                            String employeeid = resultModel.getEmployeeID().toString();
                            Map<String, Object> profile = new HashMap<>();
                            profile.put("busystatus", false);

                            db.collection("Profile").document(employeeid).update(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                            //   canclBtn.setVisibility(View.GONE);
                        }

                        Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;

                case R.id.timeCount:

                    break;

            }
        }

    }

    public void sendNotification(View view) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx);
        //Create the intent thatâ€™ll fire when the user taps the notification//

        Intent intent = new Intent(mCtx, CustomerHomePage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.jihuzurapplogo);
        mBuilder.setContentTitle("Your order is running");
        mBuilder.setContentText("Your order is completed one hour");
        NotificationManager mNotificationManager =
                (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }

    public void showCustomLoadingDialog(View view) {

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


}
