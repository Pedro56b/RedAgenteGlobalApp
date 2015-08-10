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
import com.dataservicios.redagenteglobalapp.R;

import java.util.List;

import app.AppController;
import model.User;

/**
 * Created by PEDRO QUISPE ALVAREZ on 08/08/2015.
 */
public class UserAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> userItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public UserAdapter(Activity activity, List<User> userItems) {
        this.activity = activity;
        this.userItems = userItems;
    }

    @Override
    public int getCount() {
        return userItems.size();
    }

    @Override
    public Object getItem(int location) {
        return userItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_user_detail, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        TextView tvId = (TextView) convertView.findViewById(R.id.id);
        TextView tvNombre = (TextView) convertView.findViewById(R.id.nombres);
        TextView tvIdInterbnak = (TextView) convertView.findViewById(R.id.idinterbank);

        // getting ruta data for the row
        User m = userItems.get(position);
        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        thumbNail.setErrorImageResId(R.drawable.ic_user);
        // rutaDia

        tvId.setText(m.getId()+"");
        tvNombre.setText(m.getNombre());
        // pdvs
        tvIdInterbnak.setText( m.getIdInterbank());


        //convertView.setEnabled(true);
        return convertView;
    }

}