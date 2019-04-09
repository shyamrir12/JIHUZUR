package com.example.awizom.jihuzur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterViewFlipper;
import android.widget.Toast;

import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.DiscountModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ExampleFliper extends AppCompatActivity {

    private AdapterViewFlipper simpleAdapterViewFlipper;

    int[] fruitImages = {R.drawable.ic_cancel_black_24dp, R.drawable.ic_notifications_black_24dp,
            R.drawable.ic_add_shopping_cart_black_24dp, R.drawable.ic_add_circle_black_24dp, R.drawable.ic_arrow_back_black_24dp};     // array of images

    String fruitNames[] = {"Apple", "Pine Apple", "Litchi", "Mango", "Banana"};

    private List<DiscountModel> discountModel;
    private String result="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_fliper);
        simpleAdapterViewFlipper = (AdapterViewFlipper) findViewById(R.id.simpleAdapterViewFlipper);

//        ExampleFliperAdapter customAdapter = new ExampleFliperAdapter(getApplicationContext(), imageNames);
//        simpleAdapterViewFlipper.setAdapter(customAdapter);
//        simpleAdapterViewFlipper.setFlipInterval(3000);
//        simpleAdapterViewFlipper.setAutoStart(true);


        getDiscountImageList();
    }

    private void getDiscountImageList() {

        try {
            result = new CustomerOrderHelper.GETDiscountList().execute().get();

            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DiscountModel>>() {
                }.getType();
                discountModel = new Gson().fromJson(result, listType);
                String discountNames[] = new String[discountModel.size()];
                String dicountAmounts[] = new String[discountModel.size()];
                String imageNames[] = new String[discountModel.size()];
                for(int i = 0; i<= discountModel.size(); i++){
                       imageNames[i] = discountModel.get(i).getPhoto();
                        ExampleFliperAdapter customAdapter = new ExampleFliperAdapter(getApplicationContext(), imageNames,discountNames,dicountAmounts);
                        simpleAdapterViewFlipper.setAdapter(customAdapter);
                        simpleAdapterViewFlipper.setFlipInterval(3000);
                        simpleAdapterViewFlipper.setAutoStart(true);


                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


