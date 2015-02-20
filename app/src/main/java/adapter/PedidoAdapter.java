package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


import java.util.List;

import app.AppController;
import model.Pedido;

import com.dataservicios.SQLite.DatabaseHelper;
import com.dataservicios.redagenteglobalapp.R;

/**
 * Created by usuario on 06/02/2015.
 */
public class PedidoAdapter  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Pedido> pedidoItems;


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public PedidoAdapter(Activity activity, List<Pedido> rutaItems) {
        this.activity = activity;
        this.pedidoItems = rutaItems;
    }

    @Override
    public int getCount() {
        return pedidoItems.size();
    }

    @Override
    public Object getItem(int location) {
        return pedidoItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_row_listapedido, null);


        TextView tvTipo = (TextView) convertView.findViewById(R.id.tvTipo);
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
        TextView tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);
        TextView tvPublicidad = (TextView) convertView.findViewById(R.id.tvPublicidad);
        TextView tvPedido = (TextView) convertView.findViewById(R.id.tvPedido);

        TextView tvEstado = (TextView) convertView.findViewById(R.id.tvEstado);
        TextView tvPublicidadDetalle = (TextView) convertView.findViewById(R.id.tvPublicidadDetalle);
        TextView tvFecha = (TextView) convertView.findViewById(R.id.tvFechaHora);

        // getting ruta data for the row
        Pedido m = pedidoItems.get(position);
        // thumbnail image

        // rutaDia
        tvNombre.setText("Nombre: " + m.getNombre());
        tvTipo.setText("Tipo: " + m.getTipo());

        // pdvs
        tvDescripcion.setText("Comentario: " +m.getDescripcion());

        tvEstado.setText("Estado: " +m.getEstado());
        tvPedido.setText("Pedido: " +m.getPedido());
        tvPublicidad.setText("Publicidad: " +m.getPublicidad());
        tvPublicidadDetalle.setText("Publicidad detalle: " +m.getPublicidadDetalle());
        tvEstado.setText("Estado: " +m.getEstado());
        tvFecha.setText("Fecha: " +m.getFecha());
        // release year

//        if(m.getStatus()==0){
//            imgStatus.setImageResource(R.drawable.ic_check_off);
//        } else if(m.getStatus()==1){
//            imgStatus.setImageResource(R.drawable.ic_check_on);
//        }

        return convertView;
    }

}
