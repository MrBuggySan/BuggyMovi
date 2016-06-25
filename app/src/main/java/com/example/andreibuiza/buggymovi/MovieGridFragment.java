package com.example.andreibuiza.buggymovi;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
//    private String category;
    OnPosterSelectedListener mListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        //This is the check to be used to make sure that the activity has implemented the required callback
        try {
            mListener = (OnPosterSelectedListener) context;
            Log.d(LOG_TAG, "Activity has succesfully implemented callback function for selecting posters");
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "Activity did not implement the required OnPosterSelectedListener");
            throw new ClassCastException(context.toString() + " must implement OnPosterSelectedListener");

        }
    }

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

        //start downloading the data
        new Fetch_the_MovieDB_API(getContext(), rootView).execute(category);

        //modify the toolbar
        changeToolbarTitle(category);


        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }


    // Container Activity must implement this interface
    public interface OnPosterSelectedListener {
        public void OnPosterSelected(Movie_element movieSelected, String baseImgURL);
    }

    /**
     * Change the toolbar title of the GridView fragment
     * @param categoryID
     */
    public void changeToolbarTitle(int categoryID){
        Toolbar myToolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        switch(categoryID){
            case R.id.popButton :
                myToolbar.setTitle(getString(R.string.popular));
                break;
            case R.id.topButton :
                myToolbar.setTitle(getString(R.string.top));
                break;
            case R.id.nowPlayingButton :
                myToolbar.setTitle(getString(R.string.nowPlaying));
                break;
            case R.id.upcomingButton :
                myToolbar.setTitle(getString(R.string.upcoming));
                break;
//            case R.id.latestButton :
//                myToolbar.setTitle(getString(R.string.latest));
//                break;
        }

    }


    //TODO: Create a spinner menu so the user can change the category without going back to the main page.



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
            //TODO: setup a default image and the title of a movie when the movie poster is not available.
            Picasso.with(mContext)
                    .load(allMovieData.getBaseImgURL(grid.getColumnWidth()) + allMovieData.getCatMovies()[position].getPosterURL())
                    .resize(grid.getColumnWidth(),(int) (grid.getColumnWidth()*1.5) )
                    .into(imageView);

            return imageView;
        }

    }



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

        public theMovieDB_API_response doInBackground (Integer... params){
            int categoryID = params[0];

            //http://api.themoviedb.org/3/configuration?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
            Uri.Builder config= BuildBaseUrl()
                    .appendPath("configuration")
                    .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                            getString(R.string.theMovieDBAPI_key));
            //Log.d(LOG_TAG, "fetch config with: " + config );

            Uri.Builder categoryURL = BuildBaseUrl()
                    .appendPath("movie")
                    .appendPath("popular")
                    .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                            getString(R.string.theMovieDBAPI_key));


            switch(categoryID){
                case R.id.popButton :
                    //http://api.themoviedb.org/3/movie/popular?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
                    categoryURL= BuildBaseUrl()
                            .appendPath("movie")
                            .appendPath("popular")
                            .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                                    getString(R.string.theMovieDBAPI_key));
                    //Log.d(LOG_TAG, "fetch popular with: " + categoryURL );
                    break;
                case R.id.topButton :
                    //http://api.themoviedb.org/3/movie/top_rated?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
                    categoryURL=BuildBaseUrl()
                            .appendPath("movie")
                            .appendPath("top_rated")
                            .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                                    getString(R.string.theMovieDBAPI_key));
                    //Log.d(LOG_TAG, "fetch top rated with: " + categoryURL );
                    break;
                case R.id.nowPlayingButton :
                    //http://api.themoviedb.org/3/movie/now_playing?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
                    categoryURL=BuildBaseUrl()
                            .appendPath("movie")
                            .appendPath("now_playing")
                            .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                                    getString(R.string.theMovieDBAPI_key));
                    //Log.d(LOG_TAG, "fetch now playing movies with: " + categoryURL );
                    break;
                case R.id.upcomingButton :
                    categoryURL=BuildBaseUrl()
                            .appendPath("movie")
                            .appendPath("upcoming")
                            .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                                    getString(R.string.theMovieDBAPI_key));
                    //Log.d(LOG_TAG, "fetch upcoming movies with: " + categoryURL );
                    break;
                default :
                    //We should not reach this state
                    Log.e(LOG_TAG, "Error with handling the category of the selected button");
                    break;
            }


            return new theMovieDB_API_response(getData(config.build().toString()),
                    getData(categoryURL.build().toString()));
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
                        API_full_response.getCategory_str() );

                //Create the arrays inside allMovieData

                allMovieData = new Data_Extracts(JSON_response.getCategory_size());


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



            gridview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

                    //sets up the Activity's implementation to activate when a poster is clicked
                    //set the URL to have w780
                    mListener.OnPosterSelected(allMovieData.getCatMovies()[i], allMovieData.getBaseImgURL(700));

                }
            });


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
