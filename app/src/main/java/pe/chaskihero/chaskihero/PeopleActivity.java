package pe.chaskihero.chaskihero;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pe.chaskihero.chaskihero.model.MapPersonasVulnerables;


public class PeopleActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;

    ValueEventListener listenerMapsPersonas;
    DatabaseReference referenciaMapsPersonas;

    ArrayList<MapPersonasVulnerables> lstPersonas;
    PersonasVulnerablesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view);

        getSupportActionBar().setTitle("Personas vulnerables");

        //obtener personas
        database = FirebaseDatabase.getInstance();

        lstPersonas = new ArrayList<>();

        adapter = new PersonasVulnerablesAdapter(this, new ArrayList<MapPersonasVulnerables>());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PeopleActivity.this,"Persona",Toast.LENGTH_SHORT).show();
                /*
                SolicitudMarcacion solicitudMarcacion= solicitudes.get(position);
                Intent intent = new Intent(ListaSolicitudesActivity.this, SolicitarMarcacionActivity.class);
                intent.putExtra("solicitudKeySolicitud",solicitudMarcacion.key);
                intent.putExtra("solicitudFecha",solicitudMarcacion.fecha);
                intent.putExtra("solicitudHora",solicitudMarcacion.hora);
                intent.putExtra("solicitudTipo",solicitudMarcacion.tipo);
                startActivity(intent);
                */
            }
        });

        //PERSONAS VULNERABLES
        referenciaMapsPersonas = database.getReference("mapa/1/1/personasVulnerables/");
        listenerMapsPersonas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MapPersonasVulnerables> tmp = new ArrayList<>();
                lstPersonas = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    MapPersonasVulnerables u = data.getValue(MapPersonasVulnerables.class);
                    u.key = data.getKey();
                    lstPersonas.add(u);
                    Log.i("HOLA"," --- persona: "+u.nombre);
                }

                //llenar lista
                llenarLista();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        referenciaMapsPersonas.addListenerForSingleValueEvent(listenerMapsPersonas);
    }

    public void llenarLista(){
        listView.setAdapter(adapter);
        adapter.mDataSource = lstPersonas;
        adapter.notifyDataSetChanged();
    }


    class PersonasVulnerablesAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<MapPersonasVulnerables> mDataSource;
        public PersonasVulnerablesAdapter(Context context, ArrayList<MapPersonasVulnerables> items) {
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

            View rowView = mInflater.inflate(R.layout.item_persona, parent, false);
            final MapPersonasVulnerables p = mDataSource.get(position);
            TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);
            TextView txtTipo = (TextView) rowView.findViewById(R.id.txtTipo);
            TextView txtEdad = (TextView) rowView.findViewById(R.id.txtEdad);

            TextView txtTelefono = (TextView) rowView.findViewById(R.id.txtTelefono);
            TextView txtDireccion = (TextView) rowView.findViewById(R.id.txtDireccion);

            CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);


            txtNombre.setText(p.nombre);
            txtEdad.setText(p.edad);
            txtTelefono.setText(p.telefono);
            txtDireccion.setText(p.direccion);
            switch (p.tipo){
                case 1:
                    txtTipo.setText("Niño");
                    break;
                case 2:
                    txtTipo.setText("Anciano");
                    break;
                case 3:
                    txtTipo.setText("Discapacitado");
                    break;
            }

            //checkBox.removeTextChangedListener();
            checkBox.setOnCheckedChangeListener(null);
            if (p.ok){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                       //Toast.makeText(PeopleActivity.this,"Hi, "+p.nombre,Toast.LENGTH_SHORT).show();

                       FirebaseDatabase database = FirebaseDatabase.getInstance();
                       DatabaseReference myRef = database.getReference("mapa/1/1/personasVulnerables/"+p.key+"/ok");
                       myRef.setValue(isChecked);

                       //myRef.setValue("Hello, World!");
                   }
               }
            );
            return rowView;
        }
    }
}
