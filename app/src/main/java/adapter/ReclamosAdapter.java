package adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dataservicios.redagenteglobalapp.R;

import java.util.List;

import app.AppController;
import model.Reclamos;


/**
 * Created by usuario on 12/01/2015.
 */
public class ReclamosAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Reclamos> reclamoItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ReclamosAdapter(Activity activity, List<Reclamos> rutaItems) {
        this.activity = activity;
        this.reclamoItems = rutaItems;
    }

    @Override
    public int getCount() {
        return reclamoItems.size();
    }

    @Override
    public Object getItem(int location) {
        return reclamoItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_reclamo_detail, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView tvComentario = (TextView) convertView.findViewById(R.id.comentario);
        TextView tvReclamo = (TextView) convertView.findViewById(R.id.reclamo);
        TextView tvIdReclamo = (TextView) convertView.findViewById(R.id.id);
        TextView tvTipo = (TextView) convertView.findViewById(R.id.tvTipo);
        TextView tvEstado = (TextView) convertView.findViewById(R.id.tvEstado);
        // getting ruta data for the row
        Reclamos m = reclamoItems.get(position);
        // rutaDia
        // release year
        tvComentario.setText(Html.fromHtml("<b>Comentario: </b>")+String.valueOf(m.getComentario()));
        tvReclamo.setText(Html.fromHtml("<b>Reclamo: </b>")+String.valueOf(m.getReclamo()) );
        tvIdReclamo.setText(String.valueOf(m.getId()) );
        tvTipo.setText(Html.fromHtml("<b>Tipo: </b>")+String.valueOf(m.getTipo()) );
        tvEstado.setText(Html.fromHtml("<b>Estado: </b>")+String.valueOf(m.getEstado()) );

        return convertView;
    }

}