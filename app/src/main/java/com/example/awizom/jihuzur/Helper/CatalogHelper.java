package com.example.awizom.jihuzur.Helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.awizom.jihuzur.Config.AppConfig;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CatalogHelper extends AppCompatActivity {
    ProgressDialog progressDialog= new ProgressDialog(this);

    private class POSTAddCustomer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream

            String customername = params[0];
            String mobile = params[1];
            String address = params[2];

            String interiorname = params[3];
            String interiormobile = params[4];
            String accesstoken = params[5];

            String json = "";
            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API + "CustomerPost");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CustomerID", "0");
                parameters.add("CustomerName", customername);
                parameters.add("Address", address);
                parameters.add("Mobile", mobile);
                parameters.add("InteriorName", interiorname);
                parameters.add("InteriorMobile", interiormobile);
                builder.post(parameters.build());

                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                // System.out.println("Error: " + e);
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {
            try {
                if (result.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "This User Id is not registered", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    super.onPostExecute(result);
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
