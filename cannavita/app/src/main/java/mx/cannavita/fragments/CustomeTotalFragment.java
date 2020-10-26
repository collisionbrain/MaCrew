package mx.cannavita.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import mx.cannavita.R;
import mx.cannavita.model.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CustomeTotalFragment extends DialogFragment {
        /** The system calls this to get the DialogFragment's layout, regardless
         of whether it's being displayed as a dialog or an embedded fragment. */
        private static List<Objects> pedido;

    public static CustomeTotalFragment newInstance(List<Objects> _pedido) {

            CustomeTotalFragment fragment = new CustomeTotalFragment();
            pedido=_pedido;

            return fragment;
        }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout to use as dialog or embedded fragment
            return inflater.inflate(R.layout.fragment_dialog_body, container, false);
        }

        /** The system calls this only when creating the layout in a dialog. */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // The only reason you might override this method when using onCreateView() is
            // to modify any dialog characteristics. For example, the dialog includes a
            // title by default, but your custom layout might not need it. So here you can
            // remove the dialog title, but you must call the superclass to get the Dialog.
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }
    }