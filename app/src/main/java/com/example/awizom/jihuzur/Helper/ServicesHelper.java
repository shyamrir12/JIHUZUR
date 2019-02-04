package com.example.awizom.jihuzur.Helper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import com.example.awizom.jihuzur.Config.AppConfig;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ServicesHelper extends AppCompatActivity {


    public static final class POSTService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String catalogid = params[0];
            String service = params[1];
            String description = params[2];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "CreateService");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CatalogID", catalogid);


                parameters.add("ServiceName", service);
                parameters.add("Description", description);

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
//                //System.out.println("CONTENIDO:  " + result);
//                Gson gson = new Gson();
//                final Result jsonbodyres = gson.fromJson(result, Result.class);
//                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();

            }


        }

    }


    public static final class GETServiceList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {


            String catalogid = params[0];
            String json = "";

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetCatalogService/" + catalogid);


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

//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<Service>>() {
//                    }.getType();
//                    serviceList = new Gson().fromJson(result, listType);
//                    serviceListAdapter = new ServiceListAdapter(getBaseContext(), serviceList);
//
//                    recyclerView.setAdapter(serviceListAdapter);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
