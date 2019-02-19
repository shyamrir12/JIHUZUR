package com.example.awizom.jihuzur.Helper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.example.awizom.jihuzur.Config.AppConfig;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CustomerOrderHelper extends AppCompatActivity {

    public static final class GetMyOrderRunning extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String userid = params[0];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "MyRunningOrderGet/" + userid);
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                FormBody.Builder parameters = new FormBody.Builder();
                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e);
            }

            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static final class AcceptOtp extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String orderId = params[0];
            String otpCode = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "OrderStartPost/" + orderId + "/" + otpCode);
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                FormBody.Builder parameters = new FormBody.Builder();
                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e);
            }

            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static final class CancelOrder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String orderId = params[0];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "OrderCancelPost/" + orderId + "/" + "CancelReason");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                FormBody.Builder parameters = new FormBody.Builder();
                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e);
            }

            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static final class GetCustomerPricing extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String serviceId = params[0];

            String json = "";
            String s;
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "Getpricing/" + serviceId);
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                FormBody.Builder parameters = new FormBody.Builder();

                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e);
            }

            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static final class GetMyCompleteOrderGet extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String userId = params[0];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "MyCompleteOrderGet/" + userId);
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
                FormBody.Builder parameters = new FormBody.Builder();

                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e);
            }

            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static final class GETCustomerCategoryList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String catalogNameOne = params[0];


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetCategoryName");

                builder.addHeader("Content-Type", "Application/json");
                builder.addHeader("Accept", "application/json");


                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CatalogName", catalogNameOne);
                builder.post(parameters.build());


                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static final class OrderPost extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream = null;
            String customerid = params[0];
            String empId = params[1];
            String orderDate = params[2];
            String catalogId = params[3];
            String priceId = params[4];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "OrderPost");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CustomerID", customerid);
                parameters.add("EmployeeID", empId);
                parameters.add("OrderDate", orderDate);
                parameters.add("CatalogID", catalogId);
                parameters.add("PricingID", priceId);
                builder.post(parameters.build());

                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e);
            }

            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static final class GetReviewByServiceList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String orderID = params[0];


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetReviewByOrder/" + orderID);

                builder.addHeader("Content-Type", "Application/json");
                builder.addHeader("Accept", "application/json");
                FormBody.Builder parameters = new FormBody.Builder();


                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static final class GetReviewReplyList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String reviewID = params[0];


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetReviewReply/" + reviewID);

                builder.addHeader("Content-Type", "Application/json");
                builder.addHeader("Accept", "application/json");
                FormBody.Builder parameters = new FormBody.Builder();


                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static final class PostReviewReply extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String replyid = params[0];
            String reply = params[1];
            String reviewID = params[2];
            String active = params[3];

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "SubmitReviewReply");
                builder.addHeader("Content-Type", "Application/json");
                builder.addHeader("Accept", "application/json");
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("ReplyID", replyid);
                parameters.add("Reply1", reply);
                parameters.add("ReviewID", reviewID);
                parameters.add("Active", active);

                builder.post(parameters.build());
                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static final class CustomerPOSTComplaint extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String customerID = params[0];
            String complaint = params[1];
            String active = params[2];
            String status = params[3];


            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "SubmitComplaint");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("ComplaintID", "0");
                parameters.add("CustomerID", customerID);
                parameters.add("Complaint1", complaint);
                parameters.add("Active", active);
                parameters.add("Status", status);

                builder.post(parameters.build());


                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();


                }
            } catch (Exception e) {
                e.printStackTrace();

                // System.out.println("Error: " + e);

            }
            return json;
        }


        protected void onPostExecute(String result) {

            if (result.isEmpty()) {

            } else {
                super.onPostExecute(result);
//
            }


        }

    }

    public static final class GETCustomerComplaint extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String customerid = params[0];
            String json = "";

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetCustomerComplaint/" + customerid);

                builder.addHeader("Content-Type", "Application/json");
                builder.addHeader("Accept", "application/json");

                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static final class GETCustomerComplaintReply extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String complaintid = params[0];

            String json = "";


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetComplaintReply/" + complaintid);

                builder.addHeader("Content-Type", "Application/json");
                builder.addHeader("Accept", "application/json");

                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {

                } else {
                    super.onPostExecute(result);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static final class CustomerPostRating extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String review = params[0];
            String rate = params[1];
            String orderId = params[2];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "SubmitReview ");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("ReviewID", "0");
                parameters.add("Review1", review);
                parameters.add("Rate", rate);


                parameters.add("Active", "True");
                parameters.add("OrderId", orderId);


                builder.post(parameters.build());


                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();


                }
            } catch (Exception e) {
                e.printStackTrace();

                // System.out.println("Error: " + e);

            }
            return json;
        }


        protected void onPostExecute(String result) {

            if (result.isEmpty()) {

            } else {
                super.onPostExecute(result);
//
            }


        }

    }

}
