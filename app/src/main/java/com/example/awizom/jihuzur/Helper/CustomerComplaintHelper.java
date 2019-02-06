package com.example.awizom.jihuzur.Helper;

import android.os.AsyncTask;

import com.example.awizom.jihuzur.Config.AppConfig;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CustomerComplaintHelper {
    public static final class POSTComplaint extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String customerID=params[0];
            String complaint=params[1];
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

    public static final class GETCustomerComplaint extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {

            String customerid=params[0];

            String json = "";



            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetCustomerComplaint/"+customerid);

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

    public static final class GETComplaintReply extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {

            String complaintid=params[0];

            String json = "";



            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetComplaintReply/"+complaintid);

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


}
