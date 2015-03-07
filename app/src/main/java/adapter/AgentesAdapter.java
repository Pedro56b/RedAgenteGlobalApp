package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dataservicios.redagenteglobalapp.R;

import java.util.List;

import app.AppController;
import model.Agentes;


/**
 * Created by usuario on 12/01/2015.
 */
public class AgentesAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Agentes> agenteItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AgentesAdapter(Activity activity, List<Agentes> rutaItems) {
        this.activity = activity;
        this.agenteItems = rutaItems;
    }

    @Override
    public int getCount() {
        return agenteItems.size();
    }

    @Override
    public Object getItem(int location) {
        return agenteItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_detail, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        TextView tvTienda = (TextView) convertView.findViewById(R.id.tienda);
        TextView tvDireccion = (TextView) convertView.findViewById(R.id.direccion);
        TextView tvIdAgente = (TextView) convertView.findViewById(R.id.id);
        ImageView imgStatus = (ImageView) convertView.findViewById(R.id.imgStatus);
        // getting ruta data for the row
        Agentes m = agenteItems.get(position);
        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        // rutaDia

        tvTienda.setText(m.getNombreAgente());
        // pdvs
        tvDireccion.setText( m.getDireccion());
        // release year
        tvIdAgente.setText(String.valueOf(m.getId()) );



        if(m.getStatus()==0){
            imgStatus.setImageResource(R.drawable.ic_check_off);
        } else if(m.getStatus()==1){
            imgStatus.setImageResource(R.drawable.ic_check_on);
        }

        return convertView;
    }

}
