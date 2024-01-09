package com.example.proiectfacultate.util;

import android.view.View;
import android.widget.TextView;

public class Messages {

    public static void showMessage(String message, TextView messageTextView) {

        messageTextView.setText(message);

        messageTextView.setVisibility(View.VISIBLE);

        messageTextView.postDelayed(() -> messageTextView.setVisibility(View.GONE), 10000);
    }
}
