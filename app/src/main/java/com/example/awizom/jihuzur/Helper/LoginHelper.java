package com.example.awizom.jihuzur.Helper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.example.awizom.jihuzur.Config.AppConfig;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LoginHelper extends AppCompatActivity {

    public static final class GetLogin extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream = null;
            String name = params[0];
            String password = params[1];
            String cnfrmpassword = params[2];
            String role = params[3];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_REG + "Register");
                builder.addHeader("Content-Type", "application/json");
                builder.addHeader("Accept", "application/json");

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("UserName", name);
                parameters.add("Password", password);
                parameters.add("ConfirmPassword", cnfrmpassword);
                parameters.add("Role", role);
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

    public static final class PostVerifyMobile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String userId = params[0];
            String otp = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_REG + "Confirm/" + userId + "/" + otp);
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
}
