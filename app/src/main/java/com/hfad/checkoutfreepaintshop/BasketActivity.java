//BasketA
// -----------------------------------------
package com.hfad.checkoutfreepaintshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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


public class BasketActivity extends AppCompatActivity {
    EditText enterid;
    String id;
    TextView showproducts;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        enterid = (EditText) findViewById(R.id.enterid);
        id = getIntent().getSerializableExtra("id").toString();
        showproducts = (TextView) findViewById(R.id.showproducts) ;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog dialog = ProgressDialog.show(BasketActivity.this,"","Please wait...",true);


        String showUrl = "http://52.169.28.209:8080/receipt/" + id;

        showproducts.append("ID"+ " " + "Item" + " " + "Price" + "\n"); // wyświetlanie danych z serwera

        //GET - pobranie danych z serwera
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, showUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String name[] = new String[response.length()];
                String id[] = new String[response.length()];
                String cena[] = new String[response.length()];

                float suma=0;

                try {
                    for (int i = 0; i < response.length(); i++) { // pobranie wszystkich danych
                        JSONObject e = response.getJSONObject(i);
                        id[i] = e.getString("id");
                        name[i] = e.getString("name");
                        cena[i] = e.getString("price");

                        suma += Float.valueOf(cena[i]);


                        showproducts.append(id[i] + " " + name[i] + " " + cena[i] + " PLN" +"\n"); // wyświetlanie danych z serwera

                    }
                   String s = String.format("%.2f",suma);
                    showproducts.append("Total price: "+ s);
                    dialog.cancel();

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(),"Connection error!",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void onClickMinus(View view) {


        JSONObject object = new JSONObject();
        final ProgressDialog dialog = ProgressDialog.show(this,"","Deleting...",true);

        try {
            //pobranie parametrów które zostaną wysłane na serwer
            object.put("id", enterid.getText());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // POST - wysyłanie obiektu object na serwer
        String insertUrl = "http://52.169.28.209:8080/receipt/"+enterid.getText();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, insertUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) { // odpowiedz z serwera

                dialog.cancel();
                finish();
                startActivity(getIntent());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.cancel();
                Toast.makeText(getApplicationContext(), "Wrong product id", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void onClickPayment(View view){
        Intent intent = new Intent(this,PaymentActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void onClikBacktomainmenu(View view){
        Intent intent = new Intent(this,MainMenuActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
