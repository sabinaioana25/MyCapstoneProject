package com.example.android.blends.Activities;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.blends.Adapters.CustomInfoWindowAdapter;
import com.example.android.blends.HelperClasses.GeofenceErrorMessages;
import com.example.android.blends.HelperClasses.GeofenceTransitionsIntentService;
import com.example.android.blends.HelperClasses.PlaceAutoCompleteAdapter;
import com.example.android.blends.Objects.PlaceModel;
import com.example.android.blends.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.location.places.ui.PlacePicker.getPlace;

//import com.google.android.gms.common.api.GoogleApiClient;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status> {

    private static final String TAG = MapActivity.class.getSimpleName();
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private final static float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );
    public static final int GEOFENCE_RADIUS = 500;
    public static final int GEOFENCE_EXPIRATION = 6000;
    private static final HashMap<String, LatLng> TAP_COFFEE =
            new HashMap<>();
    private static final HashMap<String, LatLng> MY_LOCATION =
            new HashMap<>();

    // widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps, mPlaceInfoIcon, mPlacePicker;

    // variables
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private GoogleApiClient googleApiClient;
    private PlaceModel mPlaceModel = new PlaceModel();
    private Marker marker;
    private static ArrayList<LatLng> locations;
    private ArrayList<Geofence> mGeofenceList;
    private Context context;
    private String details;


    private GeofencingClient geofencingClient;
    private static final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        TAP_COFFEE.put("TAP Coffee", new LatLng(51.517781, -0.1341225));
        MY_LOCATION.put("My Home", new LatLng(51.485110, 0.022156));

        getLocationPermission();
        mSearchText = findViewById(R.id.search_input_text);
        mGps = findViewById(R.id.ic_gps);
        mPlaceInfoIcon = findViewById(R.id.ic_place_info);
        mPlacePicker = findViewById(R.id.ic_place_picker);
        geofencingClient = LocationServices.getGeofencingClient(this);
        mGeofenceList = new ArrayList<>();
        populateGeofenceList();
    }

    public MapActivity() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //map is ready
        mGoogleMap = googleMap;
        locations = new ArrayList<>();
        locations.add(new LatLng(51.51778099999999, -0.1341225));
        locations.add(new LatLng(51.5257246, -0.099607));
        locations.add(new LatLng(51.523307, -0.137212));
        locations.add(new LatLng(51.524168, -0.09688899999999998));
        locations.add(new LatLng(51.5134109, -0.102602));
        locations.add(new LatLng(51.5320712, -0.1076122));
        locations.add(new LatLng(51.51437139999999, -0.1267678));
        locations.add(new LatLng(51.5165153, -0.1415986));

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission
                            .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat
                            .checkSelfPermission(this, Manifest.permission
                                    .ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    drawMarker(point);

                    String snippet =
                            "Address: " + mPlaceModel.getAddress() + "\n" +
                                    "Phone number: " + mPlaceModel.getPhoneNumber() + "\n" +
                                    "Website: " + mPlaceModel.getWebsiteUri() + "\n" +
                                    "Rating: " + mPlaceModel.getRating() + "\n";

                    for (LatLng location : locations) {
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(mPlaceModel.getName())
                                .snippet(snippet))
                                .showInfoWindow();
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    }
                }
            });

            mGoogleMap.setOnMarkerClickListener(this);
            buildGoogleApiClient();
            sendNotification(details);
        }
    }

    private void drawMarker(LatLng point) {

        MarkerOptions mOptions = new MarkerOptions();
        mOptions.position(point);
        mGoogleMap.addMarker(mOptions);
    }

    private void getDeviceLocation() {
        Log.d(TAG, "Get the device's current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: location found!");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCameraForTitle(new LatLng(currentLocation.getLatitude(),
                                                currentLocation
                                                        .getLongitude()),
                                        DEFAULT_ZOOM, "My location");
                            }

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, PlaceModel placeModel) {
        mGoogleMap.clear();
        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));

        if (placeModel != null) {
            try {
                String snippet =
                        "Address: " + placeModel.getAddress() + "\n" +
                                "Phone number: " + placeModel.getPhoneNumber() + "\n" +
                                "Website: " + placeModel.getWebsiteUri() + "\n" +
                                "Rating: " + placeModel.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeModel.getName())
                        .draggable(true)
                        .snippet(snippet);

                marker = mGoogleMap.addMarker(options);

            } catch (NullPointerException e) {
                Log.e(TAG, "moveCamera: NullpointerException " + e.getMessage());
            }
        } else {
            mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        }
        hideKeyboard();
    }


    private void moveCameraForTitle(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to lat: " + latLng.latitude + ", lng: " +
                latLng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My location")) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mGoogleMap.addMarker(markerOptions);
        }
        hideKeyboard();
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, Places.getGeoDataClient
                (this, null), LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(placeAutoCompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        mPlaceInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                    } else {
                        marker.showInfoWindow();
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, "onClick: NullpointerException" + e.getMessage());
                }

            }
        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        hideKeyboard();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = getPlace(this, data);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById
                        (googleApiClient, place.getId());
                placeResult.setResultCallback(updatePlaceDetails);
            }
        }
    }

    private void geoLocate() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);

        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 25);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: " + e);
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            moveCameraForTitle(new LatLng(address.getLatitude(), address.getLongitude()),
                    DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(MapActivity.this);
        }
    }

    public void registerAllGeofences() {
        // Check that the API client is connected and that the list has Geofences in it
        if (googleApiClient == null || !googleApiClient.isConnected() ||
                mGeofenceList == null || mGeofenceList.size() == 0) {
            return;
        }
        try {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Log.e(TAG, securityException.getMessage());
        }
    }


    public void unRegisterAllGeofences() {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            return;
        }
        try {
            LocationServices.GeofencingApi.removeGeofences(
                    googleApiClient,
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException securityException) {
            Log.e(TAG, securityException.getMessage());
        }
    }

    private boolean getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // set a boolean
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected:  API client connection succesful!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended: API client connection suspended!");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed: API client connection failed! " + result);
    }

    private final AdapterView.OnItemClickListener mAutoCompleteClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                    final AutocompletePrediction autocompleteItem = placeAutoCompleteAdapter
                            .getItem(i);
                    final String placeCafeId = autocompleteItem.getPlaceId();
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById
                            (googleApiClient, placeCafeId);
                    placeResult.setResultCallback(updatePlaceDetails);
                }
            };

    private final ResultCallback<PlaceBuffer> updatePlaceDetails = new
            ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if (!places.getStatus().isSuccess()) {
                        Log.d(TAG, "onResult: Place query did not complete successfully." + places
                                .getStatus().toString());
                        return;
                    }

                    final Place place = places.get(0);

                    String placeId = place.getId();
                    String name = (String) place.getName();
                    String address = (String) place.getAddress();
                    String phoneNumber = (String) place.getPhoneNumber();
                    String websiteUri = String.valueOf(place.getWebsiteUri());
                    float rating = place.getRating();
                    String attributions = (String) place.getAttributions();
                    LatLng latLng = place.getLatLng();

                    try {
                        mPlaceModel = new PlaceModel();
                        mPlaceModel.setPlaceId(placeId);
                        mPlaceModel.setName(name);
                        mPlaceModel.setAddress(address);
                        mPlaceModel.setPhoneNumber(phoneNumber);
                        mPlaceModel.setWebsiteUri(websiteUri);
                        mPlaceModel.setRating(rating);
                        mPlaceModel.setLat(attributions);
                    } catch (
                            NullPointerException e) {
                        Log.e(TAG, "onResult: NullpointerException" + e.getMessage());
                    }

                    moveCamera(latLng, mPlaceModel);
                    places.release();
                }
            };

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(this,
                    "Geofence added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnecting() || googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    private void populateGeofenceList() {

        for (Map.Entry<String, LatLng> entry : MY_LOCATION.entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            500)
                    .setExpirationDuration(10000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private void sendNotification(String notificationDetails) {

        details = notificationDetails;
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_cup_coffee)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_cup_coffee))
                .setColor(Color.MAGENTA)
                .setContentTitle(details)
                .setContentText(getString(R.string.geofence_transition_notif_text))
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}