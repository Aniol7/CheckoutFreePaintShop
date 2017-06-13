package com.hfad.checkoutfreepaintshop;

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

public class PaymentActivity extends AppCompatActivity {

    String id;
    RequestQueue requestQueue;
    TextView ShowStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ShowStatus = (TextView) findViewById(R.id.showstatus);
        final ProgressDialog dialog = ProgressDialog.show(PaymentActivity.this, "", "Please wait...", true);
        id = getIntent().getSerializableExtra("id").toString();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        String showUrl = "http://52.169.28.209:8080/payment/" + id;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, showUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) { // odpowiedz z serwera
                try {   // informacje o produkcie
                    String status = response.getString("status");
                    String saldo = response.getString("saldo");
                    String text = response.getString("text");
                    dialog.cancel();

                    ShowStatus.setText(status + "\n" + saldo + "\n" + text);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    // Logout
    public void onClickLogout(View view) {

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Logging out...", true);
        // Utworzenie JSONObject
        JSONObject object = new JSONObject();
        try {
            //pobranie parametrów które zostaną wysłane na serwer
            object.put("idklienta", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // POST - wysyłanie obiektu object na serwer
        String insertUrl = "http://52.169.28.209:8080/wylog/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, insertUrl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) { // odpowiedz z serwera

                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                dialog.cancel();
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


}
