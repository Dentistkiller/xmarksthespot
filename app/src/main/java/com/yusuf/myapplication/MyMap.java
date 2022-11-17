package com.yusuf.myapplication;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMap extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;
    private CarmenFeature attract1;
    private CarmenFeature attract2;
    private CarmenFeature attract3;
    private CarmenFeature attract4;
    private CarmenFeature attract5;
    private CarmenFeature attract6;
    private CarmenFeature attract7;
    private CarmenFeature attract8, attract9, attract10,attract11,attract12,attract13,attract14,attract15,attract16,attract17,attract18,attract19,attract21,attract23,attract24,attract25,attract26,attract27,attract28,attract30,attract31,attract32,attract33,attract34,attract35,attract40,attract41,attract42;


    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "MapsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private Button button;
    private FloatingActionButton location_search;
    private String geoJsonSourceLayerId = "GeoJsonSourceLayerId";
    private String symbolIconId = "SymbolIconId";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 7171;

    private LocalizationPlugin localizationPlugin;

    private static Point currentPoint, destinationPoint;
    private FirebaseAuth mAuth;
    private static String method, method1;
    User users;
    sqlDB db;
    private static String mMode;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_my_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);




        // initialize navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        // perform itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MyMap.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), Info.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.transport:
                        startActivity(new Intent(getApplicationContext(), MyMapTwo.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.park:
                        startActivity(new Intent(getApplicationContext(), ParkActivity.class));
                        overridePendingTransition(0, 0);
                        return true;


                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(getString(com.mapbox.services.android.navigation.ui.v5.R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // LANGUAGE FEATURE
                // Change map text to device language
                localizationPlugin = new LocalizationPlugin(mapView, mapboxMap, style);

                findViewById(R.id.language_one_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        localizationPlugin.setMapLanguage(MapLocale.ARABIC);
                    }
                });
                findViewById(R.id.language_two_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        localizationPlugin.setMapLanguage(MapLocale.ENGLISH);
                    }
                });
                findViewById(R.id.language_three_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        localizationPlugin.setMapLanguage(MapLocale.FRENCH);
                    }
                });
                findViewById(R.id.language_four_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        localizationPlugin.setMapLanguage(MapLocale.GERMAN);
                    }
                });

                findViewById(R.id.match_map_to_device_language).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Change the Language of the map", Snackbar.LENGTH_LONG).show();
                        try {
                            localizationPlugin.matchMapLanguageWithDeviceDefault();

                            mapboxMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(new CameraPosition.Builder()
                                            .target(new LatLng(-29.8393, 30.9252))
                                            .zoom(2.038777)
                                            .build()), 1000);

                        } catch (RuntimeException exception) {
                            Snackbar.make(view, exception.toString(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                Toast.makeText(MyMap.this, "Tap to change Language",
                        Toast.LENGTH_LONG).show();




                // TRAFFIC FEATURE

                TrafficPlugin trafficPlugin = new TrafficPlugin(mapView, mapboxMap, style);

                // Enable the traffic view by default
                trafficPlugin.setVisibility(true);

                findViewById(R.id.traffic_toggle_fab).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mapboxMap != null) {
                            trafficPlugin.setVisibility(!trafficPlugin.isVisible());
                        }
                    }
                });

                findViewById(R.id.recenterBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));



                    }
                });

                saveRouteHistory();

                enableLocationComponent(style);

                addDestinationIconSymbolLayer(style);

                mapboxMap.addOnMapClickListener(MyMap.this);
                button = findViewById(R.id.startButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(false)
                                .build();
                        double distance = currentRoute.distance() / 1000;
                        distance = Math.round(distance * 100.0) / 100.0;
                        double durationDouble = currentRoute.duration();
                        int minutes = (int)durationDouble % 3600 / 60;
                        Toast.makeText(MyMap.this, "Current Route: " + distance + " km (" + minutes + " minutes)", Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("Last Trip", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("Distance", String.valueOf(distance)).apply();
                        sharedPreferences.edit().putString("Duration", String.valueOf(durationDouble)).apply();
                        // Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(MyMap.this, options);
                    }
                });
                initSearch();
                addUserLocations();
                setUpSource(style);

                // Create an Icon object for the marker to use
                IconFactory iconFactory = IconFactory.getInstance(MyMap.this);
                Icon purple_marker = iconFactory.fromResource(R.drawable.purple);
                Icon blue_marker = iconFactory.fromResource(R.drawable.blue);
                Icon green_marker = iconFactory.fromResource(R.drawable.green);



                MarkerOptions markerOptions = new MarkerOptions();
                //Historical
                markerOptions.title("Durban Natural Science Museum");
                markerOptions.position(new LatLng(-29.858722868725167, 31.02667587121167));
                markerOptions.snippet("Historical\n\nScience museum featuring exhibits on the history of the Earth, including past & present life.");
                markerOptions.icon(blue_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Durban Holocaust & Genocide Centre");
                markerOptions.position(new LatLng(-29.84963, 31.03361));
                markerOptions.snippet("Historical\n\nA tribute to all those lost in WW2");
                markerOptions.icon(blue_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Kwa Muhle museum");
                markerOptions.position(new LatLng(-29.8528596, 31.023869));
                markerOptions.snippet("Historical\n\n Kwa Muhle Museum includes exhibits on life in and around Durban during and leading up to apartheid. Exhibits include photographs of township life and exhibits on contributions made by people responsible for the development of the city");
                markerOptions.icon(blue_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Inanda Township");
                markerOptions.position(new LatLng(-29.715, 30.95));
                markerOptions.snippet("Historical\n\n This is where Mahatma Gandhi came up with his passive-resistance philosophy while visiting in 1904 and also and where Nelson Mandela cast his vote in South Africa's first democratic elections in 1994");
                markerOptions.icon(blue_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Emmanuel Cathedral");
                markerOptions.position(new LatLng(-29.857505500000002, 31.015687301938748));
                markerOptions.snippet("Historical\n\n A Historical Catholic church recognized as the headquarters of the Metropolitan Archdiocese of Durban which was created in 1951 with the Suprema Nobis of Pope Pius XII");
                markerOptions.icon(blue_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Old Court House Museum");
                markerOptions.position(new LatLng(-29.85832355, 31.027583444936866));
                markerOptions.snippet("Historical\n\n Old Court house museum is Durban's Local museum for displaying and presenting the history of Durban since its establishment in 1835");
                markerOptions.icon(blue_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Port Natal Maritime Museum");
                markerOptions.position(new LatLng(-29.8615919, 31.0287017));
                markerOptions.snippet("Historical\n\n Durban's museum for ships and collectibles from deep below the sea which overlooks Durban harbor and offers a great view of the ocean");
                markerOptions.icon(blue_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Durban City Hall");
                markerOptions.position(new LatLng(-29.85851745, 31.026501555884444));
                markerOptions.snippet("Historical\n\n Home to the mayor's parlor and municipal chambers, It also includes a public library, an auditorium and the Durban Art Gallery");
                markerOptions.icon(blue_marker);
                mapboxMap.addMarker((markerOptions));


                //Modern
                markerOptions.title("Umhlanga Arch");
                markerOptions.position(new LatLng(-29.727618529595105, 31.0729009405428));
                markerOptions.snippet("Modern\n\nUmhlanga Arch is Durban's new lifestyle hub in Umhlanga. From luxury apartments and flexi workspaces to shops, restaurants, bars, and live events.");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Suncoast Casino, Hotels & Entertainment");
                markerOptions.position(new LatLng(-29.834997, 31.033031));
                markerOptions.snippet("Modern\n\nAn entertainment hub near the durban beach filled shops, restaurants, bars and a huge casino.");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Gateway Theatre Of Shopping");
                markerOptions.position(new LatLng(-29.72605, 31.066343));
                markerOptions.snippet("Modern\n\nThe largest mall in KZN, boast high end stores and restaurants");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Moses Mabhida Stadium");
                markerOptions.position(new LatLng(-29.82901535, 31.031416679964423));
                markerOptions.snippet("Modern\n\nMoses Mabhida Stadium is a world-class sporting and event venue on Durban's Golden Mile,Visitors can climb or use the sky car to enjoy a 360-degree view of Durban from the top of the stadium's arch");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Umhlanga Rocks");
                markerOptions.position(new LatLng(-29.7260475, 31.0715818));
                markerOptions.snippet("Modern\n\n A popular upscale resort which offers many aquatic pursuits such as townSurfing, deep-sea fishing, whale watching, dolphin viewing, scuba diving, and kiteboarding");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Pavillion Shopping Center");
                markerOptions.position(new LatLng(-29.8502, 30.9356));
                markerOptions.snippet("Modern\n\n The second largest shopping center near Durban that has gone through many extensions.Suitable for the whole family");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("The Kwazulu Natal Society of the Arts");
                markerOptions.position(new LatLng(29.8599, 30.9970));
                markerOptions.snippet("Modern\n\n Founded in 1902, The gallery hosts a variety of contemporary arts which are updated monthly with art pieces from new artists");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Varsity College Westville");
                markerOptions.position(new LatLng(-29.8394, 30.9252));
                markerOptions.snippet("Modern\n\n A higher education institution that resides in Westville. Here you can find many students struggling to finish this very assignment");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Hillcrest Corner Shopping Mall");
                markerOptions.position(new LatLng(-29.7814, 30.7628));
                markerOptions.snippet("Modern\n\n Convenience with a variety of popular stores in a popular area.");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Kloof Village Mall");
                markerOptions.position(new LatLng(-29.7926, 30.8332));
                markerOptions.snippet("Modern\n\n The oldest Mall in kloof that has free ground-level parking.");
                markerOptions.icon(purple_marker);
                mapboxMap.addMarker((markerOptions));



                //Popular
                markerOptions.title("North Beach");
                markerOptions.position(new LatLng(-29.833747097689628, 31.036676704116775));
                markerOptions.snippet("Popular\n\nSoak off in the warm water of Durban's beach.");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("uShaka Marine World");
                markerOptions.position(new LatLng( -29.870073,31.045397));
                markerOptions.snippet("Popular\n\nDurbans famous waterpark and aquarium");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("9th Avenue Waterside");
                markerOptions.position(new LatLng( -29.836460478,31.0141095003));
                markerOptions.snippet("Popular\n\nPopular tourist destination");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Durban Botanic Gardens");
                markerOptions.position(new LatLng(-29.84727435, 31.006522904801432));
                markerOptions.snippet("Popular\n\nSet on the slopes of Berea Hill, northwest of the city center, The Durban Botanic Gardens is the oldest surviving botanic garden in Africa with.");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Umgeni River Bird Park");
                markerOptions.position(new LatLng(-29.8079649, 31.017702422836877));
                markerOptions.snippet("Popular\n\nUmgeni River Bird Park features more than 200 species of birds,visitors can observe a variety of indigenous species, as well as birds from Southeast Asia and Australia");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Mitchell Park Zoo");
                markerOptions.position(new LatLng(-29.8079649, 31.017702422836877));
                markerOptions.snippet("Popular\n\n A park located in the upmarket suburb of Morningside that comes with a playground, mini zoo, wide wheelchair-friendly paths and walk-through aviary, Food is also available at the alfresco cafe");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Hluhluwe-iMfolozi Park");
                markerOptions.position(new LatLng(-28.2163689, 31.92690503619744));
                markerOptions.snippet("Popular\n\n South Africa's oldest game reserve, This is where visitors have a shot o seeing the big five (lion, leopard, elephant, rhino, and buffalo), It is also well known or rhino conservation");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Origin Nightclub");
                markerOptions.position(new LatLng(-29.8621478959, 31.0042933771));
                markerOptions.snippet("Popular\n\n An entertainment wonderland occupying four rooms in the famous Winston Hotel , a popular nightclub in its own right.");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Kloof Country Club");
                markerOptions.position(new LatLng(-29.7967, 30.82446));
                markerOptions.snippet("Popular\n\n An upscale country club that offers golf courses , squash courts and swimming pools.");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("KwaZulu Cruise Terminal");
                markerOptions.position(new LatLng(-29.873274199999997, 31.047895315228715));
                markerOptions.snippet("Popular\n\n One of Durban's most expensive and newest cruise harbors built in 2021 worth over R200m, a harbor for cruise ships");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("MiniTown");
                markerOptions.position(new LatLng(-29.8448511, 31.03592726401992));
                markerOptions.snippet("Popular\n\n A non-profit park that is used as a landmark viewing place for tourists wanting to see a model of durbans landmarks");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Blue Lagoon Beach");
                markerOptions.position(new LatLng(-29.8133, 31.0417));
                markerOptions.snippet("Popular\n\n Perfect beach for fisherman and birdwatchers with peaceful views and acres of space.");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Bluff Nature Reserve");
                markerOptions.position(new LatLng(-29.93406855, 30.994210253295428));
                markerOptions.snippet("Popular\n\n Durban's oldest nature reserve managed by Ezemvelo KZN Wildlife. The reserve offers bird watching and exploration of nature, wetlands and rivers");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("King Park Stadium");
                markerOptions.position(new LatLng(-29.8249, 31.0296));
                markerOptions.snippet("Popular\n\n The home stadium of the Sharks rugby team based in Durban. Cricket is also played in the stadium.");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Tiger Tiger Nightclub");
                markerOptions.position(new LatLng(-29.8257, 31.0320));
                markerOptions.snippet("Popular\n\n Perfect spot for Durban party animals of all ages , located close to the stadiums.");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));

                markerOptions.title("Snake Park Beach");
                markerOptions.position(new LatLng(-29.84458745, 31.037759764765347));
                markerOptions.snippet("Popular\n\n One of the most popular beaches in Durban known mostly for surfing and it's beautiful aesthetic ");
                markerOptions.icon(green_marker);
                mapboxMap.addMarker((markerOptions));




                setLayer(style);

                Drawable drawable = ResourcesCompat.getDrawable(getResources(), com.mapbox.mapboxsdk.R.drawable.mapbox_marker_icon_default, null);
                Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                style.addImage(symbolIconId, bitmap);



            }

        });
    }



    // Method to view hardcoded location (Popular, Modern and Historical)
    // This can also be taken as the Favourites
    private void addUserLocations() {
        attract1 = CarmenFeature.builder().text("Popular: Moses Mabhida Stadium")
                .geometry(Point.fromLngLat(31.030472, -29.828758))
                .placeName("44 Isaiah Ntshangase Rd, Stamford Hill, Durban, 4023")
                .id("mapbox-pmms")
                .properties(new JsonObject())
                .build();

        attract2 = CarmenFeature.builder().text("Popular: uShaka Marine World")
                .placeName("1 King Shaka Ave, Point, Durban, 4001")
                .geometry(Point.fromLngLat(31.045397, -29.870073))
                .id("mapbox-pumw")
                .properties(new JsonObject())
                .build();

        attract3 = CarmenFeature.builder().text("Popular: 9th Avenue Waterside")
                .placeName("2 Maritime Pl, Harbour, Durban, 4001")
                .geometry(Point.fromLngLat(31.0141095003, -29.836460478))
                .id("mapbox-paw")
                .properties(new JsonObject())
                .build();


        attract4 = CarmenFeature.builder().text("Modern: Suncoast Casino, Hotels & Entertainment ")
                .placeName("Suncoast Boulevard, O R Tambo Parade, Durban, 4056")
                .geometry(Point.fromLngLat(31.033031, -29.834997))
                .id("mapbox-msc")
                .properties(new JsonObject())
                .build();


        attract5 = CarmenFeature.builder().text("Modern: Gateway Theatre Of Shopping")
                .placeName("1 Palm Blvd, Umhlanga Ridge, Umhlanga, 4021")
                .geometry(Point.fromLngLat(31.066343, -29.72605))
                .id("mapbox-mgts")
                .properties(new JsonObject())
                .build();


        attract6 = CarmenFeature.builder().text("Historical: Durban Natural Science Museum")
                .placeName("1 City Hall, 234 Anton Lembede St, Durban Central, Durban, 4000")
                .geometry(Point.fromLngLat(31.026814, -29.858667))
                .id("mapbox-hdnsm")
                .properties(new JsonObject())
                .build();


        attract7 = CarmenFeature.builder().text("Historical: Durban Holocaust & Genocide Centre")
                .placeName("1 King Shaka Ave, Point, Durban, 4001")
                .geometry(Point.fromLngLat(31.03361, -29.84963))
                .id("mapbox-hdhgc")
                .properties(new JsonObject())
                .build();

        attract8 = CarmenFeature.builder().text("Modern: Umhlanga Arch")
                .placeName("1 Ncondo Pl, Umhlanga Ridge, Durban, 4320")
                .geometry(Point.fromLngLat(31.0729009405428,-29.727618529595105))
                .id("legacyyard")
                .properties(new JsonObject())
                .build();





        attract9 = CarmenFeature.builder().text("Popular: North Beach Durban ")
                .placeName("23 Sea View St, Durban")    .geometry(Point.fromLngLat(31.035285842256584,-29.846709099999998))
                .id("n-beach")
                .properties(new JsonObject())
                .build();

        attract10 = CarmenFeature.builder().text("Popular: Durban Botanic Gardens")
                .placeName("9A John Zikhali Rd, Berea, Durban, 4001")
                .geometry(Point.fromLngLat(31.006522904801432,-29.84727435))
                .id("botanics")
                .properties(new JsonObject())
                .build();


        attract11 = CarmenFeature.builder().text("Popular: Umgeni River Bird Park")
                .placeName("Riverside Rd, Durban North, Durban, 4051")
                .geometry(Point.fromLngLat(31.017702422836877,-29.8079649))
                .id("birdpark")
                .properties(new JsonObject())
                .build();

        attract12 = CarmenFeature.builder().text("Popular: Mitchell Park Zoo")
                .placeName("10 Ferndale Rd, Morningside, Durban, 4001")
                .geometry(Point.fromLngLat(31.017702422836877,-29.8079649))
                .id("zoo")
                .properties(new JsonObject())
                .build();

        attract13 = CarmenFeature.builder().text("Historical: Kwa Muhle museum")
                .placeName("130 Bram Fischer Rd, Durban Central, Durban, 4001")
                .geometry(Point.fromLngLat(31.023869,-29.8528596))
                .id("museum1")
                .properties(new JsonObject())
                .build();

        attract14 = CarmenFeature.builder().text("Modern: Umhlanga Rocks")
                .placeName("7 Lagoon Road, Umhlanga Rocks, Kwazulu")
                .geometry(Point.fromLngLat(31.0715818,-29.7260475))
                .id("u-rocks")
                .properties(new JsonObject())
                .build();

        attract15 = CarmenFeature.builder().text("Modern: Pavilion Shopping Center")
                .placeName("Jack Martens Dr, Dawncliffe, Westville, 3611")
                .geometry(Point.fromLngLat(30.9356,-29.8502))
                .id("pav")
                .properties(new JsonObject())
                .build();

        attract16 = CarmenFeature.builder().text("Historical: Ohlange Institute")
                .placeName("6 108818 St, Langalibalele, Inanda, 4310")
                .geometry(Point.fromLngLat(30.9529433,-29.7004804))
                .id("o-institute")
                .properties(new JsonObject())
                .build();

        attract17 = CarmenFeature.builder().text("Modern: The Kwazulu Natal Society of the Arts")
                .placeName("166 Bulwer Rd, Bulwer, Durban, 4001")
                .geometry(Point.fromLngLat( 30.997100199084187,-29.859746527435966))
                .id("atrs")
                .properties(new JsonObject())
                .build();

        attract18 = CarmenFeature.builder().text("Popular: Origin Nightclub")
                .placeName("9 Clark Rd, Glenwood, Lower, 4083")
                .geometry(Point.fromLngLat( 31.00394481072947,-29.861882460520235))
                .id("orries")
                .properties(new JsonObject())
                .build();

        attract19 = CarmenFeature.builder().text("Modern: Watercrest Mall")
                .placeName("141 Inanda Rd, Waterfall, Durban, 3652")
                .geometry(Point.fromLngLat( 30.81387994141015,-29.748393509465437))
                .id("w-mall")
                .properties(new JsonObject())
                .build();



        attract21 = CarmenFeature.builder().text("Popular: Kloof Country Club ")
                        .placeName("26 Victory Rd, Kloof, 3610")
                        .geometry(Point.fromLngLat( 30.824245341411608,-29.79668675291139))
                        .id("kcc")
                        .properties(new JsonObject())
                        .build();



        attract23 = CarmenFeature.builder().text("Popular: Kloof Library ")
                        .placeName("2 UMZWILILI Rd, Kloof, 3640")
                        .geometry(Point.fromLngLat( 30.831705353056563,-29.791311074621206))
                        .id("k-lib")
                        .properties(new JsonObject())
                        .build();

        attract24 = CarmenFeature.builder().text("Modern: Varsity College Westville ")
                        .placeName("1 Link Road St James Avenue &, Link Rd, Westville, Durban, 3630")
                        .geometry(Point.fromLngLat( 30.92520582607066,-29.839175746378743))
                        .id("depression")
                        .properties(new JsonObject())
                        .build();

        attract25 = CarmenFeature.builder().text("Popular: Snake Park Beach ")
                        .placeName("North Beach, Durban")
                        .geometry(Point.fromLngLat( 31.037235880473105,-29.843489979002904))
                        .id("spark")
                        .properties(new JsonObject())
                        .build();

        attract26 = CarmenFeature.builder().text("Modern: Kloof Village Mall ")
                        .placeName("Kloof Village Mall, 33 Village Rd, Kloof, Durban, 3610")
                        .geometry(Point.fromLngLat( 30.833220339563017,-29.792263046567196))
                        .id("k-vil")
                        .properties(new JsonObject())
                        .build();

        attract27 = CarmenFeature.builder().text("Modern: Hillcrest Corner Shopping Mall")
                .placeName("51, 53 Old Main Rd, Hillcrest, Durban, 3650")
                .geometry(Point.fromLngLat( 30.76284149723364,-29.781172497475477))
                .id("h-mall")
                .properties(new JsonObject())
                .build();

        attract28 = CarmenFeature.builder().text("Historical: Durban City Hall")
                .placeName("Smith St, Durban Central, Durban, 4000")
                .geometry(Point.fromLngLat( 31.026229468400285,-29.858430198017427))
                .id("c-hall")
                .properties(new JsonObject())
                .build();



        attract30 = CarmenFeature.builder().text("Popular: Tiger Tiger Nightclub")
                .placeName("67 Isaiah Ntshangase Rd, Morningside, Durban, 4001")
                .geometry(Point.fromLngLat( 31.03197546839932,-29.825986497729872))
                .id("tiger")
                .properties(new JsonObject())
                .build();

        attract31 = CarmenFeature.builder().text("Popular: Bluff Nature Reserve")
                .placeName("The Bluff, Brighton Beach, Durban, 4052")
                .geometry(Point.fromLngLat( 30.99097641073144,-29.93818235152309))
                .id("bnr")
                .properties(new JsonObject())
                .build();

        attract32 = CarmenFeature.builder().text("Popular: King Park Stadium")
                .placeName("Jacko Jackson Dr, Stamford Hill, Durban, 4025")
                .geometry(Point.fromLngLat( 31.02961203956388,-29.82461828670195))
                .id("kps")
                .properties(new JsonObject())
                .build();

        attract33 = CarmenFeature.builder().text("Historical: Port Natal Maritime Museum")
                .placeName("Aliwal St, Durban Central, Durban, 4001")
                .geometry(Point.fromLngLat( 31.028958868400462,-29.86205283866232))
                .id("pnmm")
                .properties(new JsonObject())
                .build();

        attract34 = CarmenFeature.builder().text("Popular: Blue Lagoon Beach")
                .placeName("North Beach, Durban")
                .geometry(Point.fromLngLat( 31.041656497234534,-29.8131410575925))
                .id("lugz")
                .properties(new JsonObject())
                .build();

        attract35 = CarmenFeature.builder().text("Historical: Old Court House Museum")
                .placeName("31 Diakonia Ave, Durban Central, Durban, 4001")
                .geometry(Point.fromLngLat(31.017156324222963,-29.86290907589043))
                .id("ochm")
                .properties(new JsonObject())
                .build();


        attract40 = CarmenFeature.builder().text("Popular: MiniTown")
                .placeName("114 Snell Parade, North Beach, Durban, 4001")
                .geometry(Point.fromLngLat( 31.035540739564418,-29.844520792545183))
                .id("mini-town")
                .properties(new JsonObject())
                .build();

        attract41 = CarmenFeature.builder().text("Historical: Emmanuel Cathedral")
                .placeName("Emmanuel Cathedral, 48 Cathedral Rd, Durban Central, Durban, 4001")
                .geometry(Point.fromLngLat(31.015785724222837,-29.857526682287364))
                .id("e-cat")
                .properties(new JsonObject())
                .build();

        attract42 = CarmenFeature.builder().text("Modern: KwaZulu Cruise Terminal")
                .placeName("65 Mahatma Gandhi Rd, Point, Durban, 4001")
                .geometry(Point.fromLngLat( 31.047943621264633,-29.8724679115389))
                .id("cruise")
                .properties(new JsonObject())
                .build();


    }

    private void setLayer(Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geoJsonSourceLayerId).withProperties(iconImage(symbolIconId),
                iconOffset(new Float[]{0f, -8f})));
    }

    private void setUpSource(Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geoJsonSourceLayerId));
    }


    private void initSearch() {
        location_search = findViewById(R.id.location_search);
        location_search.setOnClickListener(v -> {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .limit(10) //Limit the search index to 10
                            .addInjectedFeature(attract1)
                            .addInjectedFeature(attract2)
                            .addInjectedFeature(attract3)
                            .addInjectedFeature(attract4)
                            .addInjectedFeature(attract5)
                            .addInjectedFeature(attract6)
                            .addInjectedFeature(attract7)
                            .addInjectedFeature(attract8)
                            .addInjectedFeature(attract9)
                            .addInjectedFeature(attract10)
                            .addInjectedFeature(attract11)
                            .addInjectedFeature(attract12)
                            .addInjectedFeature(attract13)
                            .addInjectedFeature(attract14)
                            .addInjectedFeature(attract15)
                            .addInjectedFeature(attract16)
                            .addInjectedFeature(attract17)
                            .addInjectedFeature(attract18)
                            .addInjectedFeature(attract19)
                            .addInjectedFeature(attract21)
                            .addInjectedFeature(attract23)
                            .addInjectedFeature(attract24)
                            .addInjectedFeature(attract25)
                            .addInjectedFeature(attract26)
                            .addInjectedFeature(attract27)
                            .addInjectedFeature(attract28)
                            .addInjectedFeature(attract30)
                            .addInjectedFeature(attract31)
                            .addInjectedFeature(attract32)
                            .addInjectedFeature(attract33)
                            .addInjectedFeature(attract34)
                            .addInjectedFeature(attract35)
                            .addInjectedFeature(attract40)
                            .addInjectedFeature(attract41)
                            .addInjectedFeature(attract42)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(MyMap.this);

            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        });
    }


    //Filters
    public  void Popular(View view)
    {
        Intent reg = new Intent(MyMap.this, PopularMap.class);
        startActivity(reg);
    }
    public  void Modern(View view)
    {
        Intent reg = new Intent(MyMap.this, ModernMap.class);
        startActivity(reg);
    }
    public  void Historical(View view)
    {
        Intent reg = new Intent(MyMap.this, HistoryMap.class);
        startActivity(reg);
    }


    // Method to save all data from the route to the database
    private void saveRouteHistory() {
        try {
            boolean save;

            String cal = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String currentDateTime = cal;

            //get user current id
            String userId;
            userId = mAuth.getCurrentUser().getUid();

            LatLng startLatLng = new LatLng(currentPoint.latitude(), currentPoint.longitude());
            LatLng destinationLatLng = new LatLng(destinationPoint.latitude(), destinationPoint.longitude());
            String transport = method;
            Toast.makeText(this, transport, Toast.LENGTH_SHORT).show();

            Toast.makeText(this, "History Updated", Toast.LENGTH_SHORT).show();
            save = db.insertUserHistory(userId, currentDateTime, transport, startLatLng, destinationLatLng);

            if (save) {
                Toast.makeText(this, "Route saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save route", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "No route detected yet!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geoJsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                    ((Point) selectedCarmenFeature.geometry()).longitude())).zoom(14)
                            .build()), 4000);
                }
            }
            Point destinationPoint = Point.fromLngLat(((Point) selectedCarmenFeature.geometry()).longitude(), ((Point) selectedCarmenFeature.geometry()).latitude());
            Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                    locationComponent.getLastKnownLocation().getLatitude());

            getRoute(originPoint, destinationPoint);
            button.setEnabled(true);
        }
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), com.mapbox.mapboxsdk.R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }


    @SuppressWarnings({"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        getRoute(originPoint, destinationPoint);
        button.setEnabled(true);
        button.setBackgroundResource(com.mapbox.mapboxsdk.R.color.mapbox_blue);
        return true;
    }


    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .voiceUnits(DirectionsCriteria.METRIC)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, Style.SATELLITE_STREETS);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}