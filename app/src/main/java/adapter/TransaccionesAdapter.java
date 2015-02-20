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
import model.Reclamos;
import model.Transacciones;

/**
 * Created by user on 09/02/2015.
 */
public class TransaccionesAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Transacciones> transaccionItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public TransaccionesAdapter(Activity activity, List<Transacciones> rutaItems) {
        this.activity = activity;
        this.transaccionItems = rutaItems;
    }

    @Override
    public int getCount() {
        return transaccionItems.size();
    }

    @Override
    public Object getItem(int location) {
        return transaccionItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_trans_detail, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView tvPos = (TextView) convertView.findViewById(R.id.postv);
        TextView tv_desc_mes_1 = (TextView) convertView.findViewById(R.id.desc_mes_1);
        TextView tv_desc_mes_2 = (TextView) convertView.findViewById(R.id.desc_mes_2);
        TextView tv_desc_mes_3 = (TextView) convertView.findViewById(R.id.desc_mes_3);
        TextView tv_desc_mes_4 = (TextView) convertView.findViewById(R.id.desc_mes_4);
        TextView tv_desc_mes_5 = (TextView) convertView.findViewById(R.id.desc_mes_5);
        TextView tv_desc_mes_6 = (TextView) convertView.findViewById(R.id.desc_mes_6);
        TextView tv_trxs_mes_1 = (TextView) convertView.findViewById(R.id.trxs_mes_1);
        TextView tv_trxs_mes_2 = (TextView) convertView.findViewById(R.id.trxs_mes_2);
        TextView tv_trxs_mes_3 = (TextView) convertView.findViewById(R.id.trxs_mes_3);
        TextView tv_trxs_mes_4 = (TextView) convertView.findViewById(R.id.trxs_mes_4);
        TextView tv_trxs_mes_5 = (TextView) convertView.findViewById(R.id.trxs_mes_5);
        TextView tv_trxs_mes_6 = (TextView) convertView.findViewById(R.id.trxs_mes_6);
        TextView tv_trxs_lun = (TextView) convertView.findViewById(R.id.desc_trx_lun);
        TextView tv_trxs_mar = (TextView) convertView.findViewById(R.id.desc_trx_mar);
        TextView tv_trxs_mie = (TextView) convertView.findViewById(R.id.desc_trx_mie);
        TextView tv_trxs_jue = (TextView) convertView.findViewById(R.id.desc_trx_jue);
        TextView tv_trxs_vie = (TextView) convertView.findViewById(R.id.desc_trx_vie);
        TextView tv_trxs_sab = (TextView) convertView.findViewById(R.id.desc_trx_sab);
        TextView tv_trxs_dom = (TextView) convertView.findViewById(R.id.desc_trx_dom);
        // getting ruta data for the row
        Transacciones m = transaccionItems.get(position);
        // rutaDia
        // release year

        tvPos.setText(String.valueOf(m.getId() ) );
        tv_desc_mes_1.setText(String.valueOf(m.getdesc_mes_1() ) );
        tv_desc_mes_2.setText(String.valueOf(m.getdesc_mes_2() ) );
        tv_desc_mes_3.setText(String.valueOf(m.getdesc_mes_3() ) );
        tv_desc_mes_4.setText(String.valueOf(m.getdesc_mes_4() ) );
        tv_desc_mes_5.setText(String.valueOf(m.getdesc_mes_5() ) );
        tv_desc_mes_6.setText(String.valueOf(m.getdesc_mes_6() ) );
        tv_trxs_mes_1.setText(String.valueOf(m.gettrxs_mes_1() ) );
        tv_trxs_mes_2.setText(String.valueOf(m.gettrxs_mes_2() ) );
        tv_trxs_mes_3.setText(String.valueOf(m.gettrxs_mes_3() ) );
        tv_trxs_mes_4.setText(String.valueOf(m.gettrxs_mes_4() ) );
        tv_trxs_mes_5.setText(String.valueOf(m.gettrxs_mes_5() ) );
        tv_trxs_mes_6.setText(String.valueOf(m.gettrxs_mes_6() ) );
        tv_trxs_lun.setText(String.valueOf(m.gettrxs_lun() ) );
        tv_trxs_mar.setText(String.valueOf(m.gettrxs_mar() ) );
        tv_trxs_mie.setText(String.valueOf(m.gettrxs_mie() ) );
        tv_trxs_jue.setText(String.valueOf(m.gettrxs_jue() ) );
        tv_trxs_vie.setText(String.valueOf(m.gettrxs_vie() ) );
        tv_trxs_sab.setText(String.valueOf(m.gettrxs_sab() ) );
        tv_trxs_dom.setText(String.valueOf(m.gettrxs_dom() ) );

        return convertView;
    }

}