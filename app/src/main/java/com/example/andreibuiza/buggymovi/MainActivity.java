package com.example.andreibuiza.buggymovi;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private theMovieDB_API_response API_full_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);




        if (savedInstanceState == null) {
            new Fetch_the_MovieDB_API().execute();
            //The FragmentManager and FragmentTransction are used to programmatically add the fragment to the activity
            //getSupportFragmentManager().beginTransaction().add(R.id.container, new MovieGrid()).commit();

            //What is the difference between the framework fragment and the support library fragment?

            //Framework fragment
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            MovieGrid fragment = new MovieGrid();
//            fragmentTransaction.add(R.id.container, fragment);
//            fragmentTransaction.commit();
        }
    }

    public theMovieDB_API_response get_theMovieDB_API_response(){
        return API_full_response;
    }

    public class Fetch_the_MovieDB_API extends AsyncTask <String, String, theMovieDB_API_response> {
        private final String LOG_TAG= Fetch_the_MovieDB_API.class.getSimpleName();


        @Override
        public void onPreExecute(){
            /**
             * Loading Toast
             */
            Context context = getApplicationContext();
            CharSequence text = "Loading...";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

        /**
         * Call the API for the configuration, popular movies and top rated movies.
         * Additional reference: http://docs.themoviedb.apiary.io/#reference/configuration/configuration/get
         * @param params
         * @return
         */
        @Override
        public theMovieDB_API_response doInBackground (String... params){
            //http://api.themoviedb.org/3/configuration?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
            Uri.Builder config= BuildBaseUrl()
                                .appendPath("configuration")
                                .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                                                        getString(R.string.theMovieDBAPI_key));
            //Log.d(LOG_TAG, "fetch config with: " + config );


            //http://api.themoviedb.org/3/movie/popular?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
            Uri.Builder popular= BuildBaseUrl()
                                .appendPath("movie")
                                .appendPath("popular")
                                .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                                                getString(R.string.theMovieDBAPI_key));
            //Log.d(LOG_TAG, "fetch popular with: " + popular );

            //http://api.themoviedb.org/3/movie/top_rated?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
            Uri.Builder top_rated=BuildBaseUrl()
                                .appendPath("movie")
                                .appendPath("top_rated")
                                .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                                                getString(R.string.theMovieDBAPI_key));
            //Log.d(LOG_TAG, "fetch config with: " + top_rated );

            return new theMovieDB_API_response(getData(config.build().toString()),
                                                getData(popular.build().toString()),
                                                getData(top_rated.build().toString()));
        }


        /**
         *
         * @param JSON_response
         */
        @Override
        public void onPostExecute(theMovieDB_API_response JSON_response){

            API_full_response = JSON_response;
            Bundle args = new Bundle();
            args.putParcelable(theMovieDB_API_response.DATA_LOCK, API_full_response);

            MovieGridFragment fragment= new MovieGridFragment();
            fragment.setArguments(args);
            //activate the fragment here.
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

        }



        /**
         * Build the base of the query
         * @return
         */
        public Uri.Builder BuildBaseUrl(){
            return new Uri.Builder().scheme("http").authority("api.themoviedb.org").appendPath("3");
        }

        /**
         * Return the JSONObject from the MovieDB query address specified
         * @param address   The URL of the query
         * @return
         */
        public String getData(String address){
            Log.v(LOG_TAG, "getData is starting...");
            HttpURLConnection http_connect=null;
            BufferedReader reader=null;
            String JSON_API_response;
            JSONObject jsonobect;
            try{
                URL url = new URL(address);

                //IOException may occur here
                //return a connection to the resource via the URL
                http_connect = (HttpURLConnection) url.openConnection();

                //GET method is used by default
                http_connect.setRequestMethod("GET");

                //IOException may occur here
                //Open a connection to the resource
                http_connect.connect();

                //Read the input stream bytes
                InputStream stream_byte= http_connect.getInputStream();

                if(stream_byte == null){
                    Log.e(LOG_TAG, "Failed to obtain input stream from: " + address);
                    return null;
                }

                //this converts the stream output from bytes into characters
                InputStreamReader stream_char= new InputStreamReader(stream_byte);

                //Transfer the data onto the string
                StringBuffer buffer = new StringBuffer();

                //Use BufferedReader to optimize reading of char outputs from the stream
                reader = new BufferedReader(stream_char);



                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    //Log.d(LOG_TAG, "while loop here should only run once!");
                }



                if (buffer.length() == 0) {
                    // Returned Stream was empty.  No point in parsing.
                    Log.d(LOG_TAG, "buffer was empty");
                    return null;
                }
                JSON_API_response=buffer.toString();



                try{
                    //close the input stream, although the doc says it does nothing!
                    stream_byte.close();
                }catch(final IOException e){
                    Log.e(LOG_TAG, "Error closing stream",e);
                }
            }catch (IOException e){
                Log.e(LOG_TAG, "Error!", e);
                return null;
            }
            finally {
                if(http_connect !=null){
                    //disconnect from the resource
                    http_connect.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing buffer and stream", e);
                    }
                }
            }

//            try{
//                Log.d(LOG_TAG, JSON_API_response);
//                jsonobect = new JSONObject(JSON_API_response);
//                return jsonobect;
//            }catch(JSONException e){
//                Log.e(LOG_TAG, "Unable to convert string to JSON Object", e);
//            }

            //return null;
            return JSON_API_response;
        }

    }

}
