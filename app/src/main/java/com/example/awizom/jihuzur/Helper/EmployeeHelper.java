package com.example.awizom.jihuzur.Helper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.example.awizom.jihuzur.Config.AppConfig;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class EmployeeHelper extends AppCompatActivity {
    public static final class POSTProfile extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String id = params[0];
            String name = params[1];
            String image = params[2];
            String identityimage = params[3];
            String latitude = params[4];
            String logitude = params[5];
            String email = params[6];
            String address = params[7];


            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_Account_API + "UpdateProfile");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("ID", id);
                parameters.add("Name",
                        name);
                parameters.add("Image", image);
                parameters.add("IdentityImage", identityimage);
                parameters.add("Lat", latitude);
                parameters.add("Long", logitude);
                parameters.add("Email", email);
                parameters.add("Address", address);

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
            }

        }

    }
}
