package com.example.android.blends.Utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class NetworkUtils {

    private static final String PLACES_API_KEY = "AIzaSyAjdGsFkv2-PUTi2I2TsiP7PIZ_bn7vrDA";
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // detail API
    private static final String URL_SCHEME = "https";
    private static final String URL_AUTHORITY = "maps.googleapis.com/maps/api/place";
    private static final String URL_PATH_NEARBY = "nearbysearch";
    private static final String URL_LOCATION_KEY = "location";
    private static final String URL_LOCATION_VALUE = "51.520599,-0.120670";
    private static final String URL_RADIUS_KEY = "radius";
    private static final String URL_RADIUS_VALUE = "2000";
    private static final String URL_PATH_DETAILS = "details";
    private static final String URL_TYPES_KEY = "types";
    private static final String URL_TYPES_VALUE = "cafe";
    private static final String URL_KEYWORD_KEY = "keyword";
    private static final String URL_KEYWORD_VALUE = "coffee,hipster,fresh";
    private static final String URL_PATH_FORMAT = "json";
    private static final String URL_PATH_PLACE_ID_KEY = "placeid";
    private static final String URL_PATH_FIELDS_KEY = "fields";
    private static final String URL_PATH_FIELDS_VALUE = "name,website,geometry,vicinity," +
            "price_level,formatted_address,photo,rating,reviews,international_phone_number," +
            "opening_hours";
    private static final String URL_API_KEY = "key";

    // image API
    private static final String URL_PATH_PHOTO = "photo";
    private static final String URL_IMAGE_WIDTH_KEY
            = "maxwidth";
    private static final String URL_IMAGE_WIDTH_VALUE = "400";
    private static final String URL_PATH_PHOTO_REFERENCE_KEY = "photoreference";

    private static final int READ_TIME_OUT = 1000;
    private static final int CONN_TIME_OUT = 1001;
    Context context;

    private NetworkUtils() { }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        if (url == null) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIME_OUT);
            urlConnection.setConnectTimeout(CONN_TIME_OUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.w(TAG, "makeHttpRequestError");
            }
        } catch (IOException ignored) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static URL buildUrl(Uri.Builder uri) {
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "buildUrl: Request could not be completed", e);
        }
        return null;
    }

    public static URL buildPlaceListUrl(Context context) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(URL_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath(URL_PATH_NEARBY)
                .appendPath(URL_PATH_FORMAT)
                .appendQueryParameter(URL_LOCATION_KEY, URL_LOCATION_VALUE)
                .appendQueryParameter(URL_RADIUS_KEY, URL_RADIUS_VALUE)
                .appendQueryParameter(URL_TYPES_KEY, URL_TYPES_VALUE)
                .appendQueryParameter(URL_PATH_FIELDS_KEY, URL_PATH_FIELDS_VALUE)
                .appendQueryParameter(URL_KEYWORD_KEY, URL_KEYWORD_VALUE)
                .appendQueryParameter(URL_API_KEY, PLACES_API_KEY)
                .build();
        try {
            Log.w(TAG, "buildPlaceListUrl: " + URLDecoder.decode(builder.build().toString(),
                    "UTF-8"));

            return new URL(URLDecoder.decode(builder.build().toString(), "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildDetailPlaceUrl(String placeId) {
        Uri.Builder uriBuilderDetailPlace = new Uri.Builder();
        uriBuilderDetailPlace.scheme(URL_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath(URL_PATH_DETAILS)
                .appendPath(URL_PATH_FORMAT)
                .appendQueryParameter(URL_PATH_PLACE_ID_KEY, placeId)
                .appendQueryParameter(URL_PATH_FIELDS_KEY, URL_PATH_FIELDS_VALUE)
                .appendQueryParameter(URL_API_KEY, PLACES_API_KEY)
                .build();
        try {
            Log.w(TAG, "buildPlaceListUrl: " + URLDecoder.decode(uriBuilderDetailPlace.build()
                    .toString(), "UTF-8"));
            return new URL(URLDecoder.decode(uriBuilderDetailPlace.build().toString(), "UTF-8"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buildUrl(uriBuilderDetailPlace);
    }

    public static String buildPlaceImage(String image) {
        Uri.Builder uriBuilderImage = new Uri.Builder();
        uriBuilderImage.scheme(URL_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath(URL_PATH_PHOTO)
                .appendQueryParameter(URL_IMAGE_WIDTH_KEY, URL_IMAGE_WIDTH_VALUE)
                .appendQueryParameter(URL_PATH_PHOTO_REFERENCE_KEY, image)
                .appendQueryParameter(URL_API_KEY, PLACES_API_KEY)
                .build();
        try {
            URL decodedUrl = new URL(URLDecoder.decode(uriBuilderImage.build().toString(),
                    "UTF-8"));
            return decodedUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
