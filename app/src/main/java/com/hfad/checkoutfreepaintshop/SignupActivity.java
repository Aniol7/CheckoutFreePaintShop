//SignupActivity
// -----------------------------------------
package com.hfad.checkoutfreepaintshop;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;




public class SignupActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String insertUrl = "http://52.169.28.209:8080/customers";

    EditText firstname;
    EditText surname;
    EditText email;
    TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sign up");
        setContentView(R.layout.activity_signup);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //przypisanie identyfikatorów do pól EditText
        firstname = (EditText) findViewById(R.id.firstname);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        result = (TextView) findViewById(R.id.result2);
    }

       // metoda CreateAccount
    public void onClickcreateaccount(View view) {
        if (firstname.getText().length() == 0 || surname.getText().length() == 0 || email.getText().length() == 0) {
            Toast.makeText(getApplicationContext(),"Fill all fields",Toast.LENGTH_LONG).show();
        }
        else {
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Creating account...", true);

        // Utworzenie JSONObject
        JSONObject object = new JSONObject();
        try {
            //pobranie parametrów które zostaną wysłane na serwer
            object.put("firstname", firstname.getText());
            object.put("lastname", surname.getText());
            object.put("email", email.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }


            // POST - wysyłanie obiektu object na serwer
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, insertUrl, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) { // odpowiedz z serwera
                    try {
                        String odpowiedz = response.getString("text");
                        result.setText(odpowiedz);
                        Toast.makeText(getApplicationContext(), "Password has been sent to your email address", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        dialog.cancel();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.cancel();
                    result.setText("Registration failed.Please try again!");

                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }
}
