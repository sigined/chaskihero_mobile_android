package pe.chaskihero.chaskihero;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.chaskihero.chaskihero.model.Chasqui;
import pe.chaskihero.chaskihero.model.MapPersonasVulnerables;


public class ChasquiesActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;

    ValueEventListener listenerChasquies;
    DatabaseReference referenciaChasquies;

    ArrayList<Chasqui> lstChasquies;
    ChasquiesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view);

        getSupportActionBar().setTitle("Chasquis");

        //obtener personas
        database = FirebaseDatabase.getInstance();

        lstChasquies = new ArrayList<>();

        adapter = new ChasquiesAdapter(this, new ArrayList<Chasqui>());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ChasquiesActivity.this, "Chasqui", Toast.LENGTH_SHORT).show();
                /*
                SolicitudMarcacion solicitudMarcacion= solicitudes.get(position);
                Intent intent = new Intent(ListaSolicitudesActivity.this, SolicitarMarcacionActivity.class);
                intent.putExtra("solicitudKeySolicitud",solicitudMarcacion.key);
                intent.putExtra("solicitudFecha",solicitudMarcacion.fecha);
                intent.putExtra("solicitudHora",solicitudMarcacion.hora);
                intent.putExtra("solicitudTipo",solicitudMarcacion.tipo);
                startActivity(intent);
                */
                //Llamar
                Chasqui c = lstChasquies.get(position);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c.telefono));
                if (ActivityCompat.checkSelfPermission(ChasquiesActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    startActivity(intent);
                    return;
                }
                startActivity(intent);
            }
        });

        //PERSONAS VULNERABLES
        referenciaChasquies = database.getReference("chasqui");
        listenerChasquies = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MapPersonasVulnerables> tmp = new ArrayList<>();
                lstChasquies = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Chasqui u = data.getValue(Chasqui.class);
                    u.key = data.getKey();
                    lstChasquies.add(u);
                    Log.i("HOLA"," --- chasquie: "+u.nombre);
                }

                //llenar lista
                llenarLista();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        referenciaChasquies.addListenerForSingleValueEvent(listenerChasquies);
    }

    public void llenarLista(){
        listView.setAdapter(adapter);
        adapter.mDataSource = lstChasquies;
        adapter.notifyDataSetChanged();
    }


    class ChasquiesAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<Chasqui> mDataSource;
        public ChasquiesAdapter(Context context, ArrayList<Chasqui> items) {
            mContext = context;
            mDataSource = items;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return mDataSource.size();
        }
        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = mInflater.inflate(R.layout.item_chasqui, parent, false);
            Chasqui p = mDataSource.get(position);
            TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);
            TextView txtTipo = (TextView) rowView.findViewById(R.id.txtTipo);
            TextView txtEdad = (TextView) rowView.findViewById(R.id.txtEdad);


            txtNombre.setText(p.nombre);
            txtEdad.setText(p.telefono);
            txtTipo.setText(p.comite.nombre);

            return rowView;
        }
    }

}
