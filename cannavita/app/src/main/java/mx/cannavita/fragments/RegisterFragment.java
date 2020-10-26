package mx.cannavita.fragments;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

import mx.cannavita.R;
import mx.cannavita.model.User;

public class RegisterFragment extends Fragment {
    private int smallerDimension;


    public RadioButton rbMasculino,rbFemenino;
    public EditText edtNombre,edtApellidos,edtTelefono,edtCorreo,edtNacimiento;
    public Context context;
    private View view;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String gender="";

    public RegisterFragment() {
        // Required empty public constructor
    }

     public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
         return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        context = getContext();
        this.view = inflater.inflate(R.layout.fragment_userdata, container, false);
        rbFemenino=this.view.findViewById(R.id.femenino);
        rbMasculino=this.view.findViewById(R.id.masculino);

        edtNombre=this.view.findViewById(R.id.nombre);
        edtApellidos=this.view.findViewById(R.id.apellidos);
        edtTelefono=this.view.findViewById(R.id.telefono);
        edtTelefono.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        edtCorreo=this.view.findViewById(R.id.correo);
        edtNacimiento=this.view.findViewById(R.id.fecha);
        edtNacimiento.addTextChangedListener(txtWatcherDateListener);
         rbFemenino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    gender="F";


            }
        });

        rbMasculino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    gender="M";


            }
        });

        return this.view;
    }

      @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        }




     TextWatcher txtWatcherDateListener = new TextWatcher(){
        private String current = "";
        private String ddmmyyyy = "DDMMAAAA";
        private Calendar cal = Calendar.getInstance();
        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + ddmmyyyy.substring(clean.length());
                }else{
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    if(mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                edtNacimiento.setText(current);
                edtNacimiento.setSelection(sel < current.length() ? sel : current.length());
            }


        }};



public User getData(){
    User user=new User();
    user.Acepta=true;
    user.Nombre=edtNombre.getText().toString();
    user.Apellidos=edtApellidos.getText().toString();
    user.Genero=gender;
    user.Correo=edtCorreo.getText().toString();
    user.FechaNacimiento=edtNacimiento.getText().toString();
    user.Telefono= edtTelefono.getText().toString();
    return user;
}

}





