package com.hfad.checkoutfreepaintshop;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class DepositActivity extends AppCompatActivity {
    String id;
    RequestQueue requestQueue;
    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        id = getIntent().getSerializableExtra("id").toString();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public void onClickDeposit(View view){

        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Please scan your Card barcode");
        integrator.setCameraId(0);
        integrator.initiateScan();

    }

    public void onClickAccountBalance (View view){

        Intent intent = new Intent(this,AccountBalanceActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void onClickBacktomenu(View view){
        Intent intent = new Intent(this,NewStartActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);


        //obsługa skanowania QRCode'a który jest przypisany do wozka
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Try again !", Toast.LENGTH_LONG).show();
            } else {
                String scanContent = result.getContents();
                String scanFormat = result.getFormatName();

                if ("QR_CODE".equals(scanFormat)) {
                    Toast.makeText(this, "Try again!", Toast.LENGTH_LONG).show();
                } else {

                    String insertUrl = "http://52.169.28.209:8080/saldo/"+id+"/"+scanContent;


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, insertUrl, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) { // odpowiedz z serwera

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }

}
