//LoginActivity
// -----------------------------------------
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText clientid;
    EditText password;
    JsonArrayRequest jsonArrayRequest;
    User user;
    final Activity activity = this;


    @Override // metoda onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        setContentView(R.layout.activity_login);
        clientid = (EditText) findViewById(R.id.clientid);
        password = (EditText) findViewById(R.id.password);
    }

    //Login - zalogowanie użytkownika
    public void onClickLogin(View view) {
        if (clientid.getText().length() == 0 || password.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_LONG).show();
        } else {

            final ProgressDialog dialog = ProgressDialog.show(this, "", "Logging in...", true);

            //Utworzenie JSONObject z danymi logowania
            JSONObject object = new JSONObject();
            try { // pobranie parametrów do wysłania
                object.put("email", clientid.getText());
                object.put("password", password.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //POST - wysyłanie danych logowania na serwer
            String insertUrl = "http://52.169.28.209:8080/log";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, insertUrl, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Pobranie informacji o użytkowniku z serwera
                                String showUrl = "http://52.169.28.209:8080/customers/email/" + clientid.getText();
                                jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, showUrl, null, new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) { // odpowiedz z serwera
                                        try {   // informacje o użytkowniku
                                            user = new User();
                                            JSONObject e = response.getJSONObject(0);
                                            user.email = e.getString("email");
                                            user.firstname = e.getString("firstname");
                                            user.lastname = e.getString("lastname");
                                            user.id = e.getString("id");
                                            user.password = e.getString("password");
                                            user.wozek = e.getString("wozek");

                                            Intent intent = new Intent(getApplicationContext(), NewStartActivity.class);
                                            intent.putExtra("id", user.id);
                                            dialog.cancel();
                                            startActivity(intent);


                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                });
                                requestQueue.add(jsonArrayRequest);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Login error!", Toast.LENGTH_LONG).show();
                    dialog.cancel();

                }
            });
            requestQueue.add(jsonObjectRequest);

        }

    }


}
