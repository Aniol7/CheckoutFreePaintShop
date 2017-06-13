package com.hfad.checkoutfreepaintshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewStartActivity extends AppCompatActivity {
    RequestQueue requestQueue;

    String id;
    final Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_start);

        id = getIntent().getSerializableExtra("id").toString();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public void onClickShopping(View view) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Please scan trolley QR Code");
        integrator.setCameraId(0);
        integrator.initiateScan();
    }

    public void onClickDeposit(View view){
        Intent intent = new Intent(this,DepositActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    // Logout
    public void onClickLogout(View view) {
        // Utworzenie JSONObject
        JSONObject object = new JSONObject();
        final ProgressDialog dialog = ProgressDialog.show(this,"","Logging out...",true);

        try {
            //pobranie parametrów które zostaną wysłane na serwer
            object.put("idklienta", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // POST - wysyłanie obiektu object na serwer
        String insertUrl = "http://52.169.28.209:8080/wylog/"+id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, insertUrl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) { // odpowiedz z serwera

                Intent intent = new Intent(getApplicationContext(),StartActivity.class);
                dialog.cancel();
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Connection error",Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

   protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

            String insertUrl = "http://52.169.28.209:8080/customers";
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);


            //obsługa skanowania QRCode'a który jest przypisany do wozka
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Try again !", Toast.LENGTH_LONG).show();
                } else {
                    String scanContent = result.getContents();
                    String scanFormat = result.getFormatName();

                    if ("QR_CODE".equals(scanFormat)) {
                        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

                        JSONObject object = new JSONObject();
                        try {
                            object.put("id", id);
                            object.put("wozek", scanContent);
                            i.putExtra("id", id);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, insertUrl, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) { // odpowiedz z serwera

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requestQueue.add(jsonObjectRequest);

                        startActivity(i);

                    } else {
                        Toast.makeText(this, "Try again!", Toast.LENGTH_LONG).show();

                    }


                }
            } else {
                super.onActivityResult(requestCode, resultCode, intent);
            }

    }
}
