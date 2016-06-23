package com.example.andreibuiza.buggymovi;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AndreiBuiza on 6/11/2016.
 */
public class MovieGridFragment extends Fragment {
    private final String LOG_TAG= MovieGridFragment.class.getSimpleName();
    private Data_Extracts allMovieData;
    private String category;


    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    //Draw the UI elements on the screen
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moviegrid, container, false);


        Bundle bundle = this.getArguments();
        int category = bundle.getInt(getString(R.string.menuToGridKey));

        //TODO: give this AsyncTask the choice on which category to download from the API
        //start downloading the data
        new Fetch_the_MovieDB_API(getContext(), rootView).execute(category);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        //I have to fill this in correctly
        public int getCount() {
            return 20;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null){
                imageView= new ImageView(mContext);
            } else{
                imageView = (ImageView) convertView;
            }

            //Use Picasso to set the image onto the GridView
            //The poster must fill the width of the grid element
            GridView grid = (GridView) parent.findViewById(R.id.gridView);
            //Log.d(LOG_TAG, "The width of the grid element in pixels is: " + grid.getColumnWidth());
            //Log.d(LOG_TAG, allMovieData.getBaseImgURL(grid.getColumnWidth()) + allMovieData.getPopularMovies()[position].getPosterURL() );
            Picasso.with(mContext)
                    .load(allMovieData.getBaseImgURL(grid.getColumnWidth()) + allMovieData.getPopularMovies()[position].getPosterURL())
                    .resize(grid.getColumnWidth(),(int) (grid.getColumnWidth()*1.5) )
                    .into(imageView);

            return imageView;
        }

    }


    //TODO: call this with a parameter on which categorey to be downloaded from the API and display.
    public class Fetch_the_MovieDB_API extends AsyncTask<Integer, String, theMovieDB_API_response> {
        private final String LOG_TAG= Fetch_the_MovieDB_API.class.getSimpleName();
        private View rootView;
        private Context mContext;

        public Fetch_the_MovieDB_API(Context mContext, View rootView){
            this.rootView=rootView;
            this.mContext=mContext;
        }

        @Override
        public void onPreExecute(){
            /**
             * Loading Toast
             */

            CharSequence text = "Loading...";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(mContext, text, duration);
            toast.show();

        }

        /**
         * Call the API for the configuration, popular movies and top rated movies.
         * Additional reference: http://docs.themoviedb.apiary.io/#reference/configuration/configuration/get
         * @param params
         * @return
         */
        @Override
        //TODO:Modify to download the configuration and only one of the categories.
        public theMovieDB_API_response doInBackground (Integer... params){
            int category = params[0];

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
            Log.d(LOG_TAG, "fetch popular with: " + popular );

            //http://api.themoviedb.org/3/movie/top_rated?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
            Uri.Builder top_rated=BuildBaseUrl()
                    .appendPath("movie")
                    .appendPath("top_rated")
                    .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                            getString(R.string.theMovieDBAPI_key));
            Log.d(LOG_TAG, "fetch top rated with: " + top_rated );

            return new theMovieDB_API_response(getData(config.build().toString()),
                    getData(popular.build().toString()),
                    getData(top_rated.build().toString()));
        }


        /**
         *
         * @param JSON_response_str
         */
        @Override
        public void onPostExecute(theMovieDB_API_response JSON_response_str){

            theMovieDB_API_response API_full_response = JSON_response_str;


            //convert the data to JSON
            theMovieDB_JSON JSON_response;

            try{
                //turn the String JSON to JSONObjects
                JSON_response= new theMovieDB_JSON(API_full_response.getConfiguration_str(),
                        API_full_response.getPopular_movies_str(),
                        API_full_response.getTop_Rated_movies_str());

                //Create the arrays inside allMovieData
                //TODO:Modify the Data_Extracts so it would only carry the respone from configuration and selected category queries
                allMovieData = new Data_Extracts(JSON_response.getTopRatedMovie_size(), JSON_response.getPopularMovie_size());


                //Extract the data from JSONObjects and put them in allMovieData
                JSON_response.setAll_of(allMovieData);

                //allMovieData.testContent();

            }catch(JSONException e){
                Log.e(LOG_TAG, "Failed to create JSON objects out of API response");
            }


            //We are now ready to create the gridView
            GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
            //The ImageAdapter expects the imageURL to be ready, I cannot add data to it dynamically like
            //ArrayAdapter from the Weather Application.
            gridview.setAdapter(new ImageAdapter(getActivity()));
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

            return JSON_API_response;
        }

    }


}
