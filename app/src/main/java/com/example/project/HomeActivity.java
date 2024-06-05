package com.example.project;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project.Dates.DatesFragment;
import com.example.project.Home.HomeFragment;
import com.example.project.Summaries.SummariesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.project.Account.AccountFragment;
import com.example.project.Chat.ChatFragment;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1;
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
            } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            checkExactAlarmPermission();
        }
    }
}
