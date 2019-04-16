package com.example.awizom.jihuzur.Helper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.Result;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminHelper extends AppCompatActivity {

    public static final class GETCategoryList extends AsyncTask<String, Void, String> {
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

    public static final class GetMyEmployeeList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetEmployeeList");
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

    public static final class SubmitEmployeeActive extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String id = params[0];
            String active = params[1];

            String json = "";

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "SubmitEmployeeActive/" + id + "/" + active);
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

    public static final class POSTCategory extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String catalogname = params[0];
            String catalogID = params[1];
            String categoryName = params[2];
            String image = params[3];


            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "CreateCatalog");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CatalogID", catalogID);
                parameters.add("CatalogName", catalogname);
                parameters.add("Category", categoryName);
                parameters.add("Image", image);


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

    public static final class ChangeCategoryIndex extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String catalogname = params[0];
            String oldindex = params[1];
            String newindex = params[2];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "UpdateIndex/" + catalogname + "/" + oldindex + "/" + newindex);
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

    public static final class GETPricingList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String serviceid = params[0];


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetPricing/" + serviceid);


                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                mSwipeRefreshLayout.setRefreshing(false);
                // System.out.println("Error: " + e);
//                Toast.makeText(getContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
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


        @Override
        public void onClick(View v) {

        }
    }

    public static final class POSTPricing extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String description = params[0];
            String pricing = params[1];
            String amount = params[2];
            String catalogID = params[3];
            String pricingslots = params[4];
            String pricingtype = params[5];
            String pricingendslot = params[6];
            String pricingid = params[7];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "AddPricing");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("PricingID", pricingid);
                parameters.add("Description", description);
                parameters.add("PricingTerms", pricing);
                parameters.add("Amount", amount);
                parameters.add("CatalogID", catalogID);
                parameters.add("PricingSlot", pricingslots);
                parameters.add("PricingType", pricingtype);
                parameters.add("PricingEndSlot", pricingendslot);
//                parameters.add("CatalogID", catalogID.split("-")[0]);
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
//                mSwipeRefreshLayout.setRefreshing(false);
                // System.out.println("Error: " + e);
//                Toast.makeText(getContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
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

    public static final class POSTService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String serviceid = params[0];
            String catalogid = params[1];
            String service = params[2];
            String description = params[3];
            String displaytype = params[4];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "CreateService");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("ServiceID", serviceid);
                parameters.add("CatalogID", catalogid);


                parameters.add("ServiceName", service);
                parameters.add("Description", description);
                parameters.add("DisplayType", displaytype);

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

    public static final class GETDiscountList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetDiscountList");

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

    public static final class DeleteDiscount extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String discid = params[0];
            String json = "";


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "DiscountDelete/" + discid);

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

    public static final class POSTComplaint extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String customerID = params[0];
            String complaint = params[1];
            String active = params[2];
            String status = params[3];
            String complainid = params[4];


            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "SubmitComplaint");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("ComplaintID", complainid);
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

    public static final class POSTComplaintReply extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String complaintreply = params[0];
            String complaintid = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "SubmitComplaintReply");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CReply", complaintreply);
                parameters.add("ComplaintID", complaintid);

                parameters.add("CReplyID", "0");
                parameters.add("Active", "True");
                parameters.add("Status", "True");


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

    public static final class GETComplaintList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetCustomerComplaint");

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

    public static final class GetOrderPhoto extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetOrderPhoto");

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

    public static final class GETReviewListByORderID extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String json = "";


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "GetReviewByOrder/53");

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

    public static final class POSTReviewReply extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String reviewreply = params[0];
            String reviewid = params[1];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "SubmitReviewReply");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("ReplyID", "0");
                parameters.add("Reply1", reviewreply);
                parameters.add("ReviewID", reviewid);

                parameters.add("Active", "True");


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
                parameters.add("Name", name);
                parameters.add("Image", image);
                parameters.add("IdentityImage", identityimage);
                parameters.add("Lat", latitude);
                parameters.add("Long", logitude);


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

    public static final class POSTDiscount extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //     InputStream inputStream
            String discountname = params[0];
            String discounttype = params[1];
            String discountamount = params[2];
            String edtcategory = params[3];
            String img_str = params[4];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "AddDiscount");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);
                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("DiscountID", "0");
                parameters.add("DiscountName", discountname);
                parameters.add("DiscountType", discounttype);
                parameters.add("Discount1", discountamount);
                parameters.add("Category", edtcategory);
                parameters.add("Photo", img_str);
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

    public static final class POSTAddEmployee extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String name = params[0];
            String phonenumber = params[1];
            String email = params[2];
            String address = params[3];
            String userName = params[4];
            String password = params[5];
            String ConfirmPassword = params[6];

            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_Account_API + "AddEmployee");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("UserName", userName);
                parameters.add("Name", name);
                parameters.add("Email", email);
                parameters.add("Address", address);
                parameters.add("PhoneNumber", phonenumber);
                parameters.add("Password", password);
                parameters.add("ConfirmPassword", ConfirmPassword);

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

    public static final class GetProfileForShow extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Customer + "ProfileGet/" + id);
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

    public static final class POSTProfileLatLong extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String id = params[0];
            String latitude = params[1];
            String logitude = params[2];


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
                parameters.add("Lat", latitude);
                parameters.add("Long", logitude);
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
