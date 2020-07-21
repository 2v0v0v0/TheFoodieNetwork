package com.fbu.thefoodienetwork.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SendFRDialog extends DialogFragment {
    private static final String ARG_USER = "selectedUser";

    public SendFRDialog() {
    }// Empty constructor is required for DialogFragment

    public static SendFRDialog newInstance(String selectedUsername) {
        SendFRDialog frag = new SendFRDialog();
        Bundle args = new Bundle();
        args.putString(ARG_USER, selectedUsername);
        frag.setArguments(args);
        return frag;
    }


}
