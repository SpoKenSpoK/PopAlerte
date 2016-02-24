package ptut_sdis30.popalerte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class HistoriqueItem{

    public String titre;
    public String desc;

    public HistoriqueItem(String _titre, String _desc){
        titre = _titre;
        desc = _desc;
    }
}

class HistoriqueAdapter extends ArrayAdapter<HistoriqueItem> {

    public HistoriqueAdapter(Context context, HistoriqueItem[] alertes) {
        super(context, R.layout.historique_item, alertes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater itemInflater = LayoutInflater.from(getContext());
        View historiqueView = itemInflater.inflate(R.layout.historique_item, parent, false);

        HistoriqueItem item = getItem(position);
        TextView titre = (TextView) historiqueView.findViewById(R.id.titre_item);
        TextView desc = (TextView) historiqueView.findViewById(R.id.desc_item);

        titre.setText(item.titre);
        desc.setText(item.desc);

        return historiqueView;

    }
}
