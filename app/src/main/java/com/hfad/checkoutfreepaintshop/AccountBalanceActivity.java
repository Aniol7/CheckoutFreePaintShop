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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountBalanceActivity extends AppCompatActivity {
    String id;
    RequestQueue requestQueue;
    final Activity activity = this;

    TextView ShowBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance);
        id = getIntent().getSerializableExtra("id").toString();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ShowBalance = (TextView) findViewById(R.id.showbalance);
        final ProgressDialog dialog = ProgressDialog.show(AccountBalanceActivity.this, "", "Please wait...", true);


        String showUrl = "http://52.169.28.209:8080/saldo/" + id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, showUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) { // odpowiedz z serwera
                try {   // informacje o produkcie
                    JSONObject o = response.getJSONObject(0);
                    String saldo = o.getString("saldo");
                    dialog.cancel();

                    ShowBalance.setText("Your account balance: " + saldo + " PLN");


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(), "Connection Error!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void onClickBacktomenu(View view) {

        Intent intent = new Intent(this, DepositActivity.class);
        intent.putExtra("id", id);

        startActivity(intent);


    }

}
