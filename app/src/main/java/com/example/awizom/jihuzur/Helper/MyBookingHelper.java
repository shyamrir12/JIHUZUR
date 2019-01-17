package com.example.awizom.jihuzur.Helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.awizom.jihuzur.Config.AppConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyBookingHelper extends AppCompatActivity {
    ProgressDialog progressDialog= new ProgressDialog(this);

    private class GetLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream = null;
            String cliente = params[0];
            String clave = params[1];
            //String res = params[2];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_REG + "UserLoginGet/" + cliente + "/" + clave);
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");

               /* FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("grant_type", "password");
                parameters.add("username", cliente);
                parameters.add("password", clave);
                builder.post(parameters.build());*/

                Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();
                    //System.out.println(json);
//                    Toast.makeText(getApplicationContext(), "Result is Successfull", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Error: " + e);
//                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
//                    Gson gson = new Gson();
//                    UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
//
//                    if (jsonbody.isStatus()) {
//                        Token user = new Token();
//                        user.userRole = jsonbody.Role;
//                        user.access_token = jsonbody.login.access_token;
//                        user.userName = jsonbody.login.userName;
//                        user.token_type = jsonbody.login.token_type;
//                        user.expires_in = jsonbody.login.expires_in;
//                        user.userActive=jsonbody.Active;
//                        user.userID = jsonbody.UserID;
//
//                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                        if (!SharedPrefManager.getInstance(SigninActivity.this).getUser().access_token.equals(null)) {
//
//                            if (user.getUserRole().contains("Admin")) {
//                                Intent log = new Intent(getApplicationContext(), HomeActivity.class);
//                                startActivity(log);
//
//                            } else
//
//                            if(user.getUserActive(true)) {
//
//                                Intent log = new Intent(SigninActivity.this, HomeActivityUser.class);
//                                startActivity(log);
//                            }
//
//                            else  Toast.makeText(getApplicationContext(), "User Is Not Active", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Invalid user id or password", Toast.LENGTH_SHORT).show();
//                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
