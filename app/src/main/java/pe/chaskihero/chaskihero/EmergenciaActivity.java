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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.chaskihero.chaskihero.model.MapPersonasVulnerables;
import pe.chaskihero.chaskihero.model.NumeroEmergencia;


public class EmergenciaActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;

    ValueEventListener listenerEmergencias;
    DatabaseReference referenciaEmergencias;

    ArrayList<NumeroEmergencia> lstEmergencias;
    EmergenciasAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencias);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view);

        getSupportActionBar().setTitle("Números de emergencia");

        //obtener personas
        database = FirebaseDatabase.getInstance();

        lstEmergencias = new ArrayList<>();

        adapter = new EmergenciasAdapter(this, new ArrayList<NumeroEmergencia>());
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
                NumeroEmergencia c = lstEmergencias.get(position);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c.telefono));
                if (ActivityCompat.checkSelfPermission(EmergenciaActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        referenciaEmergencias = database.getReference("numeroEmergencia");
        listenerEmergencias = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MapPersonasVulnerables> tmp = new ArrayList<>();
                lstEmergencias = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    NumeroEmergencia u = data.getValue(NumeroEmergencia.class);
                    u.key = data.getKey();
                    lstEmergencias.add(u);
                    Log.i("HOLA"," --- emergencia: "+u.nombre);
                }

                //llenar lista
                llenarLista();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        referenciaEmergencias.addListenerForSingleValueEvent(listenerEmergencias);
    }

    public void llenarLista(){
        listView.setAdapter(adapter);
        adapter.mDataSource = lstEmergencias;
        adapter.notifyDataSetChanged();
    }


    class EmergenciasAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<NumeroEmergencia> mDataSource;
        public EmergenciasAdapter(Context context, ArrayList<NumeroEmergencia> items) {
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

            View rowView = mInflater.inflate(R.layout.item_numero_emergencia, parent, false);
            NumeroEmergencia p = mDataSource.get(position);
            TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);
            TextView txtTelefono = (TextView) rowView.findViewById(R.id.txtTelefono);


            txtNombre.setText(p.nombre);
            txtTelefono.setText(p.telefono);


            return rowView;
        }
    }

}
