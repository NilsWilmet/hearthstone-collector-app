package com.example.lpiem.hearthstonecollectorapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lpiem.hearthstonecollectorapp.Manager.APIManager;
import com.example.lpiem.hearthstonecollectorapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ConnexionActivity extends AppCompatActivity {
    CallbackManager callbackManager;

    private LoginButton facebookSignInButton;
    private boolean isLoggedIn;

    private SignInButton googleSignInButton;
    private Button googleSignOutButton;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private static int RC_SIGN_IN = 100;

    private APIManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // TEST
        apiManager = new APIManager();
        apiManager.getCardByID(1);

        /*
         * FACEBOOK
         */
        callbackManager = CallbackManager.Factory.create();
        facebookSignInButton = findViewById(R.id.loginBtnFb);
        facebookSignInButton.setReadPermissions("email");

        // Callback registration
        facebookSignInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserFacebookDetails(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("facebookexception",exception.getMessage());
            }
        });

        /*
         * GOOGLE
         */

        account = GoogleSignIn.getLastSignedInAccount(this);
        googleSignInButton = findViewById(R.id.google_sign_in);
        googleSignOutButton = findViewById(R.id.google_sign_out);

        googleConnexion(account);

        //Click listener : Google SignIn Button
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        googleSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignOut();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on vérifie si une personne est déjà connectée sur Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            getUserFacebookDetails(accessToken);
        }

         // Check for existing Google Sign In account, if the user is already signed in
         // the GoogleSignInAccount will be non-null.
         account = GoogleSignIn.getLastSignedInAccount(this);

        googleConnexion(account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //FACEBOOK PART
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        //GOOGLE PART
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    //
    protected void getUserFacebookDetails(AccessToken loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        Log.e("connexion", json_object.toString());
                        Intent intent = new Intent(ConnexionActivity.this, NavigationActivity.class);
                        intent.putExtra("userProfile", json_object.toString());
                        startActivity(intent);
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    //Google Sign-In Functions

    public void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void googleSignOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("INFO", "Successfully disconnected user");
                    googleConnexion(null);
                }
            });
    }

    public void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            googleConnexion(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("ERROR", "signInResult:failed code=" + e.getStatusCode());
            googleConnexion(null);
        }
    }

    public void googleConnexion(GoogleSignInAccount signInAccount) {
        if (signInAccount == null) {
            Log.i("INFO", "Not connected user...");
            googleSignInButton.setVisibility(View.VISIBLE);
            googleSignOutButton.setVisibility(View.GONE);
        } else {
            Log.i("INFO", "USER CONNECTED: " + signInAccount.getDisplayName() + ", " + signInAccount.getEmail());
            googleSignInButton.setVisibility(View.GONE);
            googleSignOutButton.setVisibility(View.VISIBLE);
        }
    }
}
