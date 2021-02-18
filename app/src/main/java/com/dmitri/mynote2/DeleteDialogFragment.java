package com.dmitri.mynote2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import javax.annotation.Nullable;

public class DeleteDialogFragment extends DialogFragment {

    private OnDeleteDialogListener deleteDlgListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View contentView = requireActivity().getLayoutInflater().
                inflate(R.layout.fragment_delete_dialog, null);
        MaterialButton confirmDelete = contentView.findViewById(R.id.confirm_delete_note_dialog_button);
        confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteDlgListener != null) {
                    deleteDlgListener.onDelete();
                    dismiss();
                }
            }
        });
        MaterialButton cancelDelete = contentView.findViewById(R.id.no_delete_note_dialog_button);
        cancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteDlgListener != null) {
                    deleteDlgListener.onCancelDelete();
                    dismiss();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setView(contentView);
        return builder.create();
    }

    public void setOnDialogListener(OnDeleteDialogListener deleteDlgListener) {
        this.deleteDlgListener = deleteDlgListener;
    }
}