package com.example.awizom.jihuzur.AdminActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.awizom.jihuzur.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoCoder extends AppCompatActivity {
    TextView pincode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_coder);

         pincode = (TextView) findViewById(R.id.pincode);
        try {
            getPostalCodeByCoordinates(this,Double.parseDouble("21.322"),Double.parseDouble("81.66"),pincode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPostalCodeByCoordinates(Context context, double lat, double lon, TextView pincode) throws IOException {

        Geocoder mGeocoder = new Geocoder(context, Locale.getDefault());
        String zipcode=null;
        Address address=null;

        if (mGeocoder != null) {

            List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 5);

            if (addresses != null && addresses.size() > 0) {

                for (int i = 0; i < addresses.size(); i++) {
                    address = addresses.get(i);
                    if (address.getPostalCode() != null) {
                        zipcode = address.getPostalCode();
                        Log.d("postcode", "Postal code: " + address.getPostalCode());
                        pincode.setText(address.getPostalCode().toString());
                        break;
                    }

                }
                return zipcode;
            }
        }

        return null;
    }
}
