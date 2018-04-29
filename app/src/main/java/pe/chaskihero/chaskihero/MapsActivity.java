package pe.chaskihero.chaskihero;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.chaskihero.chaskihero.model.Chasqui;
import pe.chaskihero.chaskihero.model.MapAreaSegura;
import pe.chaskihero.chaskihero.model.MapPersonasVulnerables;
import pe.chaskihero.chaskihero.model.MapPuntoAcopio;
import pe.chaskihero.chaskihero.model.MapViviendaVulnerable;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    LatLng latLngMyPosition;
    int ZoomMyPosition=11;
    int distance=5;

    //FirebaseUser user;
    FirebaseDatabase database;

    ValueEventListener listenerMapsPersonas;
    DatabaseReference referenciaMapsPersonas;

    ValueEventListener listenerMapAreas;
    DatabaseReference referenciaMapAreas;

    ValueEventListener listenerMapPuntosAcopio;
    DatabaseReference referenciaMapPuntosAcopio;

    ValueEventListener listenerMapViviendas;
    DatabaseReference referenciaMapViviendas;

    boolean isMoveCameraInicial=false;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    boolean mapPersonas = true;
    boolean mapCasas = true;
    boolean mapLugares = true;
    boolean mapZonas = true;
    boolean mapRutas = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        configureToolbar();
        configureNavigationDrawer();
        //configureToolbar();

        database = FirebaseDatabase.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        getSupportActionBar().setTitle("Buscar por");

        /*
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        */
    }


    private void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.menu_people)
                {
                    // do something
                    Intent i = new Intent(MapsActivity.this, PeopleActivity.class);
                    startActivity(i);
                }

                //Toast.makeText(MapsActivity.this,"gogogo",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Toast.makeText(MapsActivity.this, "hola", Toast.LENGTH_SHORT).show();
                /*
                int id = item.getItemId();
                if (id == R.id.nav_account) {
                    if(wandoSession.values.isNotLogin){
                        irLogin();
                    }else{
                        irMyAccount();
                    }
                    // Handle the camera action
                } else if (id == R.id.nav_notification) {
                    irNotification();
                } else if (id == R.id.nav_help) {
                    howDoesItWork();
                } else if (id == R.id.nav_logout) {
                    closeSessionDialog();
                }
                */
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //TODO
        //centrar en trujillo
        LatLng trux = new LatLng(-8.09, -79.0);
        //mMap.addMarker(new MarkerOptions().position(trux).title("Marker in Trujillo"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(trux));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trux, ZoomMyPosition));

        cargarSuperMapa();
    }

    void cargarSuperMapa(){
        //TODO limpiar mapa
        mMap.clear();
        if (mapPersonas){

            //PERSONAS VULNERABLES
            referenciaMapsPersonas = database.getReference("mapa/1/1/personasVulnerables/");
            listenerMapsPersonas = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MapPersonasVulnerables> tmp = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        MapPersonasVulnerables u = data.getValue(MapPersonasVulnerables.class);
                        u.key = data.getKey();
                        //tmp.add(u);
                        Log.i("HOLA"," --- persona: "+u.nombre);

                        //Actualizar pines
                        LatLng sydney = new LatLng(u.latitud, u.longitud);
                        //mMap.addMarker(new MarkerOptions().position(sydney).title("Persona Vulnerable").snippet(u.nombre+"\n"+u.direccion));

                        //mMap.addMarker(new MarkerOptions().position(sydney).();

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(sydney)
                                .title("Persona Vulnerable")
                                .snippet(u.nombre+"\n"+u.direccion)
                                //.icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));
                                .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.pin_people_c));

                        /*
                        InfoWindowData info = new InfoWindowData();
                        info.setImage("snowqualmie");
                        info.setHotel("Hotel : excellent hotels available");
                        info.setFood("Food : all types of restaurants available");
                        info.setTransport("Reach the site by bus, car and train.");*/
                        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(MapsActivity.this);
                        mMap.setInfoWindowAdapter(customInfoWindow);

                        Marker m = mMap.addMarker(markerOptions);
                        //m.showInfoWindow();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            referenciaMapsPersonas.addListenerForSingleValueEvent(listenerMapsPersonas);



            //AREAS SEGURAS
            referenciaMapAreas = database.getReference("mapa/1/1/areasSeguras/");
            listenerMapAreas = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MapPersonasVulnerables> tmp = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        MapAreaSegura u = data.getValue(MapAreaSegura.class);
                        u.key = data.getKey();
                        //Log.i("HOLA"," --- persona: "+u.nombre);
                        LatLng sydney = new LatLng(u.latitud, u.longitud);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(sydney)
                                .title("Area Segura")
                                .snippet("")
                                //.icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_CYAN));
                                .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_locker_c));
                        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(MapsActivity.this);
                        mMap.setInfoWindowAdapter(customInfoWindow);
                        Marker m = mMap.addMarker(markerOptions);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            referenciaMapAreas.addListenerForSingleValueEvent(listenerMapAreas);


            //PUNTOS ACOPIO
            referenciaMapPuntosAcopio = database.getReference("mapa/1/1/puntosAcopio/");
            listenerMapPuntosAcopio = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MapPersonasVulnerables> tmp = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        MapPuntoAcopio u = data.getValue(MapPuntoAcopio.class);
                        u.key = data.getKey();
                        Log.i("HOLA"," --- puntosAcopio: ...");
                        LatLng sydney = new LatLng(u.latitud, u.longitud);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(sydney)
                                .title("Punto Acopio")
                                .snippet("")
                                //.icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_ORANGE));
                                .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_pickup_c));
                        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(MapsActivity.this);
                        mMap.setInfoWindowAdapter(customInfoWindow);
                        Marker m = mMap.addMarker(markerOptions);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            referenciaMapPuntosAcopio.addListenerForSingleValueEvent(listenerMapPuntosAcopio);



            //VIVIENDAS VULNERABLES
            referenciaMapViviendas = database.getReference("mapa/1/1/viviendasVulnerables/");
            listenerMapViviendas = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MapPersonasVulnerables> tmp = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        MapViviendaVulnerable u = data.getValue(MapViviendaVulnerable.class);
                        u.key = data.getKey();
                        //Log.i("HOLA"," --- persona: "+u.nombre);
                        LatLng sydney = new LatLng(u.latitud, u.longitud);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(sydney)
                                .title("Vivienda Vulnerable")
                                .snippet(u.direccion)
                                //.icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_RED));
                                .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.pin_vivienda_c));
                        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(MapsActivity.this);
                        mMap.setInfoWindowAdapter(customInfoWindow);

                        Marker m = mMap.addMarker(markerOptions);

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            referenciaMapViviendas.addListenerForSingleValueEvent(listenerMapViviendas);

        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent i = new Intent(MapsActivity.this, LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_chasquis) {
            Intent i = new Intent(MapsActivity.this, ChasquiesActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_hospitals) {
            Intent i = new Intent(MapsActivity.this, HospitalesActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_phones) {
            Intent i = new Intent(MapsActivity.this, EmergenciaActivity.class);
            startActivity(i);
        }


        drawer.closeDrawer(GravityCompat.START);


        return false;
    }


    @Override
    public void onStart() {
        super.onStart();
        //validatePermission();
        if(latLngMyPosition!=null){
            if(mMap!=null){
                setMyLocation(true); //para activar la detección de locación
            }

        }
    }


    /*
    private void validatePermission(){
        if(!PermisosUtil.hasLocationPermission()){
            PermisosUtil.askLocationPermission(getActivity());
            MessageUtil.message(getActivity(), "Se requiere brindar permisos de ubicación para mostrar los eventos correctamente.");
        }
        setDefaultMyPosition();
    }
*/


    private void setMyLocation(boolean isActive){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(isActive);
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permiso requerido")
                        .setMessage("Esta aplicación necesita acceder a tu ubicación para mostrar los eventos en el mapa")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    /*---------------------------------------------------------------------------------------------*/

}
