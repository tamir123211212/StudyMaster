package com.example.project;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.project.Dates.DatesFragment;
import com.example.project.Home.HomeFragment;
import com.example.project.Summaries.SummariesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.project.Account.AccountFragment;
import com.example.project.Chat.ChatFragment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 2;
    private static final int REQUEST_CODE_EDIT = 3;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("fragment_to_load")) {
            String fragmentToLoad = intent.getStringExtra("fragment_to_load");
            if ("DatesFragment".equals(fragmentToLoad)) {
                loadFragment(new DatesFragment());
            }
            else{
                // הצגת ה-Fragment הראשוני עם פתיחת האפליקציה
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

            }
        } else {
            // הצגת ה-Fragment הראשוני עם פתיחת האפליקציה
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        // בדיקת הרשאה ל-SCHEDULE_EXACT_ALARM
        checkExactAlarmPermission();
        // בדיקת התראות
        checkNotificationPermission();
        // בדיקת הרשאות למצלמה ואחסון
        checkCameraAndStoragePermissions();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.chat) {
            selectedFragment = new ChatFragment();
        } else if (itemId == R.id.account) {
            selectedFragment = new AccountFragment();
        } else if (itemId == R.id.summaries) {
            selectedFragment = new SummariesFragment();
        } else if (itemId == R.id.dates) {
            selectedFragment = new DatesFragment();
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
            } else {
                Log.d(TAG, "Exact alarm permission granted");
            }
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (!notificationManager.areNotificationsEnabled()) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            } else {
                Log.d(TAG, "Notifications are enabled");
            }
        }
    }

    private void checkCameraAndStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Camera and storage permissions granted");
            } else {
                Log.d(TAG, "Camera and storage permissions denied");
                Toast.makeText(this, "Permissions are required for the app to function correctly", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            // חזרה ל-Fragment הקודם
            getSupportFragmentManager().popBackStack();
        } else if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            checkExactAlarmPermission();
        }
    }
}
