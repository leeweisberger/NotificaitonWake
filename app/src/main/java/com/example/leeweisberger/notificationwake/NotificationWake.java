package com.example.leeweisberger.notificationwake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by leeweisberger on 2/24/16.
 */
public class NotificationWake extends DialogFragment {
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String IMAGE = "image";
    public static final String SOUND = "sound";


    public static NotificationWake newInstance(String title, String message, int image, int sound) {
        NotificationWake f = new NotificationWake();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putInt(IMAGE, image);
        args.putInt(SOUND, sound);


        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);
        int image = getArguments().getInt(IMAGE);
        int sound = getArguments().getInt(SOUND);


        View v = createView(title, message, image);
        ((Button) v.findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);

        turnScreenOn();
        playSound(sound);

        return builder.create();
    }

    private void playSound(int sound) {
        final MediaPlayer mp = MediaPlayer.create(getActivity(), sound);
        mp.start();
    }

    private void turnScreenOn() {
        PowerManager pm = (PowerManager) getActivity().getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }

    @NonNull
    private View createView(String title, String message, int image) {
        final LayoutInflater factory = getActivity().getLayoutInflater();
        View v = factory.inflate(R.layout.notification_wake,null);
        ((ImageView) v.findViewById(R.id.imageView)).setImageResource(image);
        ((TextView) v.findViewById(R.id.title)).setText(title);
        ((TextView) v.findViewById(R.id.message)).setText(message);
        return v;
    }
}
