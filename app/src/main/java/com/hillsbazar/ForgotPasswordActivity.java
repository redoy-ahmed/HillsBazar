package com.hillsbazar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.hillsbazar.networksync.CheckInternetConnection;
import com.kaopiz.kprogresshud.KProgressHUD;

import es.dmoral.toasty.Toasty;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView appname;
    private EditText forgotpassemail;
    private String getEmail;
    private Button sendotp;
    private TextView back;

    private RequestQueue requestQueue;
    private String sessionname,sessionmobile,sessionemail,sessionpassword;
    public static final String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        new CheckInternetConnection(this).checkConnection();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);

        forgotpassemail = findViewById(R.id.forgotpassemail);
        sendotp = findViewById(R.id.sendotp);
        back = findViewById(R.id.goback);

        requestQueue = Volley.newRequestQueue(ForgotPasswordActivity.this);
        /*sendotp.setOnClickListener(view -> {

            getEmail = forgotpassemail.getText().toString();

            final KProgressHUD progressDialog = KProgressHUD.create(ForgotPasswordActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

            PasswordRequest passwordRequest = new PasswordRequest(getEmail, response -> {

                Log.e("values recieved" , response);

                progressDialog.dismiss();
                // Response from the server is in the form if a JSON, so we need a JSON Object
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.e("Flag recieved", jsonObject.getBoolean("success")+" ");

                    if (jsonObject.getBoolean("success")) {


                        //Passing all received data from server to next activity
                        sessionname = jsonObject.getString("name");
                        sessionmobile = jsonObject.getString("mobile");
                        sessionemail = jsonObject.getString("email");
                        sessionpassword = jsonObject.getString("password");

                        Log.e("session values ", sessionemail);

                        //sending mail to the specified info
                        sendMail();

                    } else {
                        if (jsonObject.getString("status").equals("INVALID"))
                            Toasty.error(ForgotPasswordActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(ForgotPasswordActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                }
            }, error -> {
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toasty.error(ForgotPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toasty.error(ForgotPasswordActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toasty.error(ForgotPasswordActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            });
            requestQueue.add(passwordRequest);
        });*/

        back.setOnClickListener(view -> finish());
    }

    public void sendMail() {

        BackgroundMail.newBuilder(ForgotPasswordActivity.this)
                .withSendingMessage("Sending Password to your Email")
                .withSendingMessageSuccess("Kindly Check Your email For Password")
                .withSendingMessageError("Failed to send password ! Try Again !")
                .withUsername("beingdevofficial@gmail.com")
                .withPassword("Singh@30")
                .withMailto(sessionemail)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Magic Print Password")
                .withBody("Hello Mr/Miss " + sessionname + "\n " + getString(R.string.send_password1) + sessionpassword + getString(R.string.send_password2))
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {

                        //do some magic

                        Toasty.success(ForgotPasswordActivity.this, "Password sent to Email Account",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .send();

    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }
}
