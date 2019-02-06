package com.example.awizom.jihuzur.Helper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.awizom.jihuzur.Config.AppConfig;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DiscountHelper extends AppCompatActivity {

    public static final class EditPricingPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String orderId = params[0];
            String priceID = params[1];


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "OrderEditPricingPost/" + orderId + "/" + priceID);

                builder.addHeader("Content-Type", "Application/json");
                builder.addHeader("Accept", "application/json");


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

    public static final class EditPostDiscount extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String orderId = params[0];
            String discountname = params[1];



            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Employee + "OrderEditDiscountPost/" + orderId + "/" + discountname);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
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
