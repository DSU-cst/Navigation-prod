package com.ambulancepath.navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String[] PERMISSIONS_REQUIRED = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = MainActivity.this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        checkPermissions();
    }

    private void updatePreferences(String usertype, View view) {
        if (sharedPref.getString(getString(R.string.saved_user_type), "").equals("")) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.saved_user_type), usertype);
            editor.apply();

            // only for debugging
            Toast.makeText(view.getContext(), "User Type is now set to " + usertype, Toast.LENGTH_LONG).show();
        }
    }

    public void setup() {
        findViewById(R.id.button_driver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePreferences("driver", view);
            }
        });
        findViewById(R.id.button_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePreferences("user", view);
            }
        });

        // Further app flow can continue here

    }
    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.add(0, 32, 0, "Reset Preferences");
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.quit:
                System.exit(0);
                return true;
            case R.id.skip:
                Toast.makeText(this, "Loading Map", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MapActivity.class));
                return true;
            case 32: // only for debugging
                Toast.makeText(this, "Resetting Preferences", Toast.LENGTH_LONG).show();
                resetPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // function used only for debugging - will not make it to production
    private void resetPreferences() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setup();
//            startActivity(new Intent(this, MapActivity.class));
        } else {
            Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions() {
        if(checkSelfPermission(PERMISSIONS_REQUIRED[0]) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(PERMISSIONS_REQUIRED[1]) == PackageManager.PERMISSION_GRANTED) {
            setup();
//            startActivity(new Intent(this, MapActivity.class));
        } else {
            requestPermissions(PERMISSIONS_REQUIRED, 100);
        }
    }
}