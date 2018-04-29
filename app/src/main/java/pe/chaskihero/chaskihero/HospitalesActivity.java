package pe.chaskihero.chaskihero;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import pe.chaskihero.chaskihero.model.Hospital;
import pe.chaskihero.chaskihero.model.MapPersonasVulnerables;


public class HospitalesActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;

    ValueEventListener listenerHospitales;
    DatabaseReference referenciaHospitales;

    ArrayList<Hospital> lstHospitales;
    HospitalesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitales);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view);

        getSupportActionBar().setTitle("Hospitales");

        //obtener personas
        database = FirebaseDatabase.getInstance();

        lstHospitales = new ArrayList<>();

        adapter = new HospitalesAdapter(this, new ArrayList<Hospital>());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(HospitalesActivity.this, "Chasqui", Toast.LENGTH_SHORT).show();
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
                Hospital c = lstHospitales.get(position);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c.telefono));
                if (ActivityCompat.checkSelfPermission(HospitalesActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        referenciaHospitales = database.getReference("hospital");
        listenerHospitales = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MapPersonasVulnerables> tmp = new ArrayList<>();
                lstHospitales = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Hospital u = data.getValue(Hospital.class);
                    u.key = data.getKey();
                    lstHospitales.add(u);
                    Log.i("HOLA"," --- hospital: "+u.nombre);
                }

                //llenar lista
                llenarLista();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        referenciaHospitales.addListenerForSingleValueEvent(listenerHospitales);
    }

    public void llenarLista(){
        listView.setAdapter(adapter);
        adapter.mDataSource = lstHospitales;
        adapter.notifyDataSetChanged();
    }


    class HospitalesAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<Hospital> mDataSource;
        public HospitalesAdapter(Context context, ArrayList<Hospital> items) {
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

            View rowView = mInflater.inflate(R.layout.item_hospital, parent, false);
            Hospital p = mDataSource.get(position);
            TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);
            TextView txtTelefono = (TextView) rowView.findViewById(R.id.txtTelefono);
            TextView txtDireccion = (TextView) rowView.findViewById(R.id.txtDireccion);


            txtNombre.setText(p.nombre);
            txtTelefono.setText(p.telefono);
            txtDireccion.setText(p.direccion);

            return rowView;
        }
    }

}
