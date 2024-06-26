package com.example.project.Dates;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.util.Log;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DatesFragment extends Fragment {
    private static final String TAG = "DatesFragment";

    private TextView currentDateTextView;
    private Button addReminderButton, nextDayButton, previousDayButton;
    private ListView remindersListView;
    private ArrayAdapter<String> remindersAdapter;
    private List<Event> remindersList;
    private FirebaseDatabase database;
    private DatabaseReference eventsRef;
    private FirebaseAuth mAuth;
    private Calendar calendar;
    private AlarmManager alarmManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates, container, false);

        currentDateTextView = view.findViewById(R.id.textview_current_date);
        addReminderButton = view.findViewById(R.id.button_add_reminder);
        nextDayButton = view.findViewById(R.id.button_next_day);
        previousDayButton = view.findViewById(R.id.button_previous_day);
        remindersListView = view.findViewById(R.id.listview_reminders);

        database = FirebaseDatabase.getInstance();
        eventsRef = database.getReference("events");
        mAuth = FirebaseAuth.getInstance();

        calendar = Calendar.getInstance();

        displayCurrentDate();

        remindersList = new ArrayList<>();
        remindersAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

        remindersListView.setAdapter(remindersAdapter);

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddReminderDialog();
            }
        });

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDay(1);
            }
        });

        previousDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDay(-1);
            }
        });

        loadRemindersForCurrentDate();

        createNotificationChannel();

        return view;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "StudyMasterReminderChannel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("StudyMaster", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d(TAG, "Notification channel created");
        }
    }
    // Display the current date
    private void displayCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        currentDateTextView.setText(currentDate);
    }

    private void changeDay(int days) {
        Calendar today = Calendar.getInstance(); // Get the current date

        // Check if the new date would be before the current date
        Calendar newDate = (Calendar) calendar.clone();
        newDate.add(Calendar.DAY_OF_MONTH, days);

        // Allow going back to the current date
        if (newDate.before(today) && newDate.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)) {
            Toast.makeText(getContext(), "Cannot go back to a previous date", Toast.LENGTH_SHORT).show();
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, days);
            displayCurrentDate();
            loadRemindersForCurrentDate();
        }
    }

    // Open the dialog to add a new reminder
    private void openAddReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_reminder, null);
        builder.setView(dialogView);

        final EditText reminderNameEditText = dialogView.findViewById(R.id.edittext_reminder_name);
        final TimePicker timePicker = dialogView.findViewById(R.id.time_picker);
        final CheckBox notificationCheckBox = dialogView.findViewById(R.id.checkbox_notification);
        Button saveButton = dialogView.findViewById(R.id.button_save);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);

        final AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderName = reminderNameEditText.getText().toString();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                boolean wantsNotification = notificationCheckBox.isChecked();

                if (!reminderName.isEmpty()) {
                    if (wantsNotification && !canScheduleExactAlarms()) {
                        Toast.makeText(getContext(), "אין הרשאה לקביעת תזכורות עם התראות.", Toast.LENGTH_LONG).show();
                        requestExactAlarmPermission();
                        return;
                    }

                    addReminderToFirebase(reminderName, hour, minute, wantsNotification);
                    if (wantsNotification && canScheduleExactAlarms()) {
                        setAlarm(reminderName, hour, minute, calendar); // Pass the selected date
                    }
                    dialog.dismiss();
                } else {
                    reminderNameEditText.setError("נא להכניס שם תזכורת");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean canScheduleExactAlarms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            return alarmManager != null && alarmManager.canScheduleExactAlarms();
        }
        return true;
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        }
    }
    // Set an alarm for the selected date and time
    private void setAlarm(String reminderName, int hour, int minute, Calendar selectedDate) {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Log.d(TAG, "Cannot schedule exact alarms. Requesting permission.");
            requestExactAlarmPermission();
            Toast.makeText(getContext(), "אין הרשאה לקביעת התראות מדויקות. ", Toast.LENGTH_LONG).show();
            return; // חזרה במקרה של חוסר בהרשאה
        }

        try {
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            Calendar alarmCalendar = (Calendar) selectedDate.clone(); // Clone the selected date
            alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
            alarmCalendar.set(Calendar.MINUTE, minute);
            alarmCalendar.set(Calendar.SECOND, 0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
            Log.d(TAG, "Alarm set for " + String.format("%02d:%02d", hour, minute) + " at " + alarmCalendar.getTimeInMillis());
            Toast.makeText(getContext(), "Alarm set for " + String.format("%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException: " + e.getMessage());
            Toast.makeText(getContext(), "Failed to set alarm: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addReminderToFirebase(String reminderName, int hour, int minute, boolean wantsNotification) {
        String userId = mAuth.getCurrentUser().getUid();
        String id = UUID.randomUUID().toString();
        String eventId = UUID.randomUUID().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        String eventDate = dateFormat.format(calendar.getTime());
        String eventType = "תזכורת";
        String description = reminderName + " בשעה " + String.format("%02d:%02d", hour, minute) + (wantsNotification ? " עם תזכורת" : "");

        Event event = new Event(id, userId, eventId, eventDate, String.format("%02d:%02d", hour, minute), eventType, description, wantsNotification);
        eventsRef.child(id).setValue(event);
    }
    // Load reminders for the current date
    private void loadRemindersForCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        Query query = eventsRef.orderByChild("eventDate").equalTo(currentDate);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                remindersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    remindersList.add(event);
                }

                Collections.sort(remindersList, new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        return o1.getEventTime().compareTo(o2.getEventTime());
                    }
                });

                remindersAdapter.clear();
                for (Event event : remindersList) {
                    remindersAdapter.add(event.getDescription());
                }

                remindersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading reminders", databaseError.toException());
            }
        });
    }
}
