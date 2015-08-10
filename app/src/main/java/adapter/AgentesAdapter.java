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
    public boolean isEnabled(int position) {
        // Deshabilitando los items del adptador segun el statu
//        if( agenteItems.get(position).getStatus()==1){
//            return false;
//
//        }
        return true;
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
       // RelativeLayout v = (RelativeLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail, null);
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_detail, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        TextView tvTienda = (TextView) convertView.findViewById(R.id.idinterbank);
        TextView tvDireccion = (TextView) convertView.findViewById(R.id.nombres);
        TextView tvIdAgente = (TextView) convertView.findViewById(R.id.id);
        TextView tvEstatus = (TextView) convertView.findViewById(R.id.tvEstatus);
        TextView tvVisita = (TextView) convertView.findViewById(R.id.visita);
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
        tvEstatus.setText(String.valueOf(m.getStatus()));

        //tvVisita.setText(String.valueOf(m.getInicio()));


        if(m.getStatus()==0){
            tvVisita.setText("");
            imgStatus.setImageResource(R.drawable.ic_check_off);
            //convertView.setBackgroundColor(Color.BLUE);
        } else if(m.getStatus()==1){
            tvVisita.setText("Visita: "+String.valueOf(m.getInicio()));
            imgStatus.setImageResource(R.drawable.ic_check_on);
            //convertView.setBackgroundColor(Color.CYAN);
        }
        //convertView.setEnabled(true);
        return convertView;
    }

}
