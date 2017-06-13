package com.hfad.checkoutfreepaintshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ShowProductActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    String imie;
    String cena;
    TextView scanView;
    String scanContent;
    int liczbasztuk =1;
    int counter;

    String id ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        scanContent=getIntent().getSerializableExtra("kod_kreskowy").toString();
        scanView = (TextView) findViewById(R.id.scanresult);
        id = getIntent().getSerializableExtra("id").toString();
        final ProgressDialog dialog = ProgressDialog.show(ShowProductActivity.this,"","Please wait...",true);


        //pobieranie danych produktu z serwera
        String showUrl = "http://52.169.28.209:8080/prod/" + scanContent;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, showUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) { // odpowiedz z serwera
                try {   // informacje o produkcie
                    JSONObject o = response.getJSONObject(0);
                    imie = o.getString("name");
                    cena = o.getString("price");
                    dialog.cancel();

                    scanView.setText("Scanned product: "+ imie +"\n"+"Price: "+ cena+"\n"+ "Amount of products: "+ liczbasztuk);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(),"Connection error",Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
    public void onClickPlus(View view){
        liczbasztuk ++;
        scanView.setText("Scanned product: "+ imie +"\n"+"Price: "+ cena+"\n"+ "Amount of products: "+ liczbasztuk);
    }
    public void onClickMinus(View view){
        liczbasztuk --;
        if (liczbasztuk==0)
            liczbasztuk=1;

        scanView.setText("Scanned product: "+ imie +"\n"+"Price: "+ cena+"\n"+ "Amount of products: "+ liczbasztuk);
    }
    public void onClickAddProduct(View view) {
        String insertUrl = "http://52.169.28.209:8080/receipt";

        // Utworzenie JSONObject
        JSONObject object = new JSONObject();
        try {


            object.put("idklienta", id);
            object.put("name", imie);
            object.put("barcode", scanContent);
            object.put("price", cena);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (counter = 1; counter <= liczbasztuk; counter++) {
            // POST - wysyłanie obiektu object na serwer
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, insertUrl, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {// odpowiedz z serwera



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Connection error",Toast.LENGTH_LONG).show();

                }
            });

            requestQueue.add(jsonObjectRequest);
        }
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        counter=1;


    }

    public void onClickBacktomenu(View view){
        Intent intent = new Intent(this,MainMenuActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }


}
