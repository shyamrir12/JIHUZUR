package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerCommentActivity;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.CustomerActivity.TrackActivity;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.Model.ResultModel;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.ShowNotificationClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class CustomerCurrentOrderAdapter extends RecyclerView.Adapter<CustomerCurrentOrderAdapter.OrderItemViewHolder> {

    private Context mCtx;
    private List<Order> orderitemList;
    private Order order;
    private String orderId = "", otpCode = "", result = "";
    private Intent intent;
    FirebaseFirestore db;

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
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);
        try {
            order = orderitemList.get(position);
            orderId = String.valueOf(order.getOrderID());
//            holder.servicesName.setText(order.getServiceName());
//           holder.bookingAccepted.setText(order.getEmpMob());
//            holder.description.setText(order.getCatalogName());
//            holder.timing.setText(order.getOrderStartTime());

            holder.empName.setText(order.getServiceName());
            holder.empContAct.setText(order.getCatalogName());
            holder.timercount.setText(order.getTotalTime());
            holder.startTime.setText(order.getOrderStartTime());
            holder.endtime.setText(order.getOrderEndTime());
            holder.catagryName.setText(order.getCategory());
            holder.servicName.setText(order.getServiceName());
            holder.pricingterm.setText(order.getPricingTerms());
            holder.dctName.setText(order.getDiscountName());

//            if (!order.getPricingTerms().equals(null)) {
//                holder.pricingterm.setVisibility(View.VISIBLE);
//                holder.dctName.setVisibility(View.VISIBLE);
//                holder.linearLayout.setVisibility(View.VISIBLE);
//            }
//            if (order.getOrderStartTime().equals("NULL")) {
//                holder.canclBtn.setVisibility(View.VISIBLE);
//            } else {
//                holder.canclBtn.setVisibility(View.GONE);
//                holder.acceptBtn.setVisibility(View.GONE);
//            }

        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderitemList.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context mCtx;
        private TextView startTime, endtime, empName, timercount, empContAct, catagryName, servicName, pricingterm, dctName;
        private Button acceptBtn, trackinBtn, canclBtn, commentBtn;
        private List<Order> orderitemList;
        private LinearLayout linearLayout;

//        private TextView servicesName,bookingAccepted,description,timing;
//        private Button viewBtn;


        public OrderItemViewHolder(View view, Context mCtx, List<Order> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);

//            servicesName = itemView.findViewById(R.id.servicesName);
//            bookingAccepted = itemView.findViewById(R.id.bookingAccepted);
//            description = itemView.findViewById(R.id.description);
//            timing = itemView.findViewById(R.id.timing);

            empName = itemView.findViewById(R.id.cusName);
            startTime = itemView.findViewById(R.id.starttime);
            endtime = itemView.findViewById(R.id.endtime);
            timercount = itemView.findViewById(R.id.timeCount);
            acceptBtn = itemView.findViewById(R.id.acceptOtpBtn);
            trackinBtn = itemView.findViewById(R.id.trackBtn);
            canclBtn = itemView.findViewById(R.id.cancelBtn);
            empContAct = itemView.findViewById(R.id.empMobile);
            catagryName = itemView.findViewById(R.id.catagoryName);
            servicName = itemView.findViewById(R.id.serviceName);
            pricingterm = itemView.findViewById(R.id.pricingterm);
            dctName = itemView.findViewById(R.id.discountName);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            linearLayout = itemView.findViewById(R.id.l4);
            db = FirebaseFirestore.getInstance();
            acceptBtn.setOnClickListener(this);
            trackinBtn.setOnClickListener(this);
            canclBtn.setOnClickListener(this);
            timercount.setOnClickListener(this);
            commentBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {

            switch (v.getId()) {
                case R.id.acceptOtpBtn:

                    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
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
                        @Override
                        public void onClick(final View v) {
                            try {
                                result = new CustomerOrderHelper.AcceptOtp().execute(orderId, enterOtp.getText().toString()).get();
                                Gson gson = new Gson();
                                Type getType = new TypeToken<ResultModel>() {
                                }.getType();
                                ResultModel resultModel = new Gson().fromJson(result, getType);
                                if (resultModel.getMessage().contains("Order Started")) {

                                    new java.util.Timer().schedule(
                                            new java.util.TimerTask() {
                                                @Override
                                                public void run() {
                                                    sendNotification(v);
                                                    // your code here
                                                }
                                            },
                                            3600000
                                    );
                                }
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

                                    //   canclBtn.setVisibility(View.GONE);


                                Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("result", result.toString());
                                Intent intent = new Intent(mCtx, CustomerHomePage.class);
                                mCtx.startActivity(intent);

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    break;
                case R.id.trackBtn:
                    intent = new Intent(mCtx, TrackActivity.class);
                    intent.putExtra("CustomerID", order.getCustomerID());
                    intent.putExtra("EmployeeID", order.getEmployeeID());
                    mCtx.startActivity(intent
                    );

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
                case R.id.commentBtn:
                    intent = new Intent(mCtx, CustomerCommentActivity.class);
                    intent.putExtra("OrderID", order.getOrderID());
                    intent.putExtra("CustomerID", order.getCustomerID());
                    intent.putExtra("EmployeeID", order.getEmployeeID());
                    intent.putExtra("OrderEndTime", order.getOrderEndTime());
                    intent.putExtra("CategoryName", order.getCatalogName());
                    intent.putExtra("ServiceName", order.getServiceName());
                    intent.putExtra("PricingTerms", order.getPricingTerms());
                    intent.putExtra("EmployeeName", order.getEmpName());
                    intent.putExtra("EmployeeContact", order.getEmpMob());
                    mCtx.startActivity(intent);
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

}
