package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.dataservicios.redagenteglobalapp.R;

import java.util.List;

import app.AppController;
import model.Contacto;

/**
 * Created by PEDRO QUISPE ALVAREZ on 08/07/2015.
 */
public class ContactoAdapter  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Contacto> contactoItems;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ContactoAdapter(Activity activity, List<Contacto> rutaItems) {
        this.activity = activity;
        this.contactoItems = rutaItems;
    }

    @Override
    public int getCount() {
        return contactoItems.size();
    }

    @Override
    public Object getItem(int location) {
        return contactoItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_contacto_detail, null);


        TextView tvCelular = (TextView) convertView.findViewById(R.id.tvcelular);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvContacto = (TextView) convertView.findViewById(R.id.tvcontacto);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvemail);

        // getting ruta data for the row
        Contacto m = contactoItems.get(position);
        // thumbnail image


        // pdvs
        tvCelular.setText(m.getCelular());
        tvContacto.setText(m.getContacto());
        tvEmail.setText(m.getEmail());
        tvId.setText(m.getId());

        return convertView;
    }

}