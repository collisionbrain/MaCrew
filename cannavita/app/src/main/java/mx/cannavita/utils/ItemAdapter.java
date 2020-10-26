package mx.cannavita.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mx.cannavita.R;

public class ItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> list;
    public ItemAdapter(Context _context, int layout, ArrayList<String> _list){
        context=_context;
        list=_list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        String  descripcion=list.get(position);
        convertView=layoutInflater.inflate(R.layout.list_item,null);


          TextView desc= convertView.findViewById(R.id.txt_description);
        // TextView count=view .findViewById(R.id.txt_count);

        desc.setText(descripcion);


        return convertView;
    }
}
