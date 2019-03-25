package com.example.awizom.jihuzur.Helper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.example.awizom.jihuzur.Config.AppConfig;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class EmployeeOrderHelper extends AppCompatActivity {

    public static final class EmployeeGetMyCurrentOrder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String userid = params[0];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "MyRunningOrderGet/" + userid);
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

    public static final class GenerateOtp extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String orderid = params[0];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "OrderOtpPost/" + orderid);
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

    public static final class StopOrder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String orderid = params[0];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "OrderEndPost/" + orderid);
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

    public static final class AcceptPayment extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String orderid = params[0];
            String empid = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "OrderPaymentPost/" + orderid + "/" + empid);
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

    public static final class GetEmployeeProfileForShow extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "AllProfileGet");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");
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
                builder.url(AppConfig.BASE_URL_API_Employee + "MyCompleteOrderGet/" + userId);
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

    public static final class EmployeePOSTSkill extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream

            String employeeid = params[0];
            String catalogid = params[1];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "SkillPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CatalogID", catalogid);
                parameters.add("EmployeeID", employeeid);
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
            if (result.isEmpty()) {
            } else {
                super.onPostExecute(result);
//
            }


        }

    }

    public static final class AddOrderImage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream

            String orderid = params[0];
            String image = params[1];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "AddOrderImage");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("OrderID", orderid);
                parameters.add("Photo", image);
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
            if (result.isEmpty()) {
            } else {
                super.onPostExecute(result);
//
            }


        }

    }

    public static final class GetEmployeeSkill extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String employeeid = params[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "GetSkill/" + employeeid);
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

    public static final class GetReviewByEmployee extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String employeeid = params[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetReviewByEmployee/" + employeeid);
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

    public static final class EmployeePOSTDeleteSkill extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //     InputStream inputStream
            String catalogid = params[0];
            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "SkillDelete/" + catalogid);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
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

            if (result.isEmpty()) {

            } else {
                super.onPostExecute(result);
//
            }


        }

    }
}
