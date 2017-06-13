//MainMenu
// -----------------------------------------
package com.hfad.checkoutfreepaintshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainMenuActivity extends AppCompatActivity {

    final Activity activity = this;

    RequestQueue requestQueue;

    String scanContent;

    String id;



    @Override // metoda onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Menu");
        setContentView(R.layout.activity_main_menu);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        id = getIntent().getSerializableExtra("id").toString();

    }

    // Logout
    public void onClickBacktomenu(View view) {
       Intent intent = new Intent(this,NewStartActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    //Scan
    public void onClickScan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Please scan product");
        integrator.setCameraId(0);
        integrator.initiateScan();
    }

    public void onClickBasket(View view){
        Intent intent = new Intent(getApplicationContext(),BasketActivity.class);
        intent.putExtra("id",id);
        try {
            startActivity(intent);
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        //obsługa skanowania kodów kreskowych
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Try again!", Toast.LENGTH_LONG).show();
            } else {
                scanContent = result.getContents();
                String scanFormat = result.getFormatName();

                if ("QR_CODE".equals(scanFormat)) {

                    Toast.makeText(this, "Try again!", Toast.LENGTH_LONG).show();
                } else {

                    Intent Ekran = new Intent(getApplicationContext(), ShowProductActivity.class);
                    Ekran.putExtra("kod_kreskowy", scanContent);
                    Ekran.putExtra("id",id);
                    startActivity(Ekran);
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }


}





