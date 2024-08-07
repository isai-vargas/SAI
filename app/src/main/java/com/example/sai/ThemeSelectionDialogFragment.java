package com.example.sai;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeSelectionDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String[] themes = {"Claro", "Oscuro", "Predeterminado"};
        int checkedItem = 2; // Por defecto, el tema predeterminado

        return new AlertDialog.Builder(requireContext())
                .setTitle("Seleccionar Tema")
                .setSingleChoiceItems(themes, checkedItem, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case 1:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case 2:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancelar", null)
                .create();
    }
}
