package com.lyeng.coffeebarista;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class SettingsFragment extends PreferenceFragmentCompat {
    public static String sortOrder;
    public static Boolean coffeeAlarm;
    public static final int JOB_ID = 39;
    public static final String TAG = "NotifService";
    private JobScheduler mScheduler;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        Log.e(TAG, "Inside onCreatePref");
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        final String prefKey = "sort";
        final String notificationKey = "notification";

        if (preference.getKey().equals(notificationKey)) {
            if (((SwitchPreference) preference).isChecked()) {
//                mScheduler = (JobScheduler) getActivity().getSystemService(JOB_SCHEDULER_SERVICE);
//                ComponentName serviceName = new ComponentName(getContext(),
//                        NotificationJobService.class);
//                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
//                        .setPeriodic(15000);
//                JobInfo myJobInfo = builder.build();
//                mScheduler.schedule(myJobInfo);
//                Toast.makeText(getContext(), "Job Scheduled, job will run when " +
//                        "the constraints are met.", Toast.LENGTH_SHORT).show();
//            } else {
//                if (mScheduler != null) {
//                    mScheduler.cancelAll();
//                    mScheduler = null;
//                    Toast.makeText(getContext(), "Jobs cancelled", Toast.LENGTH_SHORT).show();
//                }
            }
        } else if (preference.getKey().equals(prefKey)) {
            ListPreference pref = (ListPreference) findPreference(prefKey);
            sortOrder = pref.getValue();
        }
        //return super.onPreferenceTreeClick(preference);
        return true;
    }
}
