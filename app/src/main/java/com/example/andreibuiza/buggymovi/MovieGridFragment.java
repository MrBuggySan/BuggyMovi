package com.example.andreibuiza.buggymovi;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by AndreiBuiza on 6/11/2016.
 */
public class MovieGridFragment extends Fragment  {
    private final String LOG_TAG= MovieGridFragment.class.getSimpleName();
    private Data_Extracts allMovieData;

    OnPosterSelectedListener mListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        //This is the check to be used to make sure that the activity has implemented the required callback
        try {
            mListener = (OnPosterSelectedListener) context;
            //Log.d(LOG_TAG, "Activity has succesfully implemented callback function for selecting posters");
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "Activity did not implement the required OnPosterSelectedListener");
            throw new ClassCastException(context.toString() + " must implement OnPosterSelectedListener");

        }
    }

    @Override
    //Draw the UI elements on the screen
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moviegrid, container, false);

        Bundle bundle = this.getArguments();
        int category = bundle.getInt(getString(R.string.menuToGridKey));
        toolBarSetup(category, inflater);

        if(category != R.id.favouriteButton){
            //start downloading the data
            new Fetch_the_MovieDB_API(getContext(), rootView).execute(category);
        }
        else{
            //do not download any data
            setupFavouriteListGrid(rootView);
        }

        return rootView;
    }

    /**
     *Interface to be implemented by the Activity
     * This interface will call the method that will switch the grid fragment with the detail fragment
     */
    public interface OnPosterSelectedListener {
        public void OnPosterSelected(Movie_element movieSelected);
    }

    /**
     * Change the toolbar of the GridView fragment to be the spinner menu
     * @param categoryID
     */
    public void toolBarSetup(int categoryID, LayoutInflater inflater){

        View vi = inflater.inflate(R.layout.moviegridspinner, null);
        Spinner categorySpinner = (Spinner) vi.findViewById(R.id.movieGridSpinner);

        //Set the list of Strings to be displayed on the spinner
        ArrayList<String> spinnerContentValues= new ArrayList<>();
        spinnerContentValues.add(getString(R.string.popular));
        spinnerContentValues.add(getString(R.string.top));
        spinnerContentValues.add(getString(R.string.nowPlaying));
        spinnerContentValues.add(getString(R.string.upcoming));
        spinnerContentValues.add(getString(R.string.favourites));

        //Set the event callback
        categorySpinner.setOnItemSelectedListener((MainActivity) getActivity());

        //TODO: Style the spinner menu better
        ArrayAdapter<String> spinnerContent = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                spinnerContentValues);

        categorySpinner.setAdapter(spinnerContent);

        ActionBar myToolbar = (ActionBar) ((AppCompatActivity) getActivity()).getSupportActionBar();
        myToolbar.setDisplayShowTitleEnabled(false);
        myToolbar.setDisplayShowCustomEnabled(true);
        myToolbar.setCustomView(vi);



        switch(categoryID){
            case R.id.popButton :
                categorySpinner.setSelection(0);
                break;
            case R.id.topButton :
                categorySpinner.setSelection(1);
                break;
            case R.id.nowPlayingButton :
                categorySpinner.setSelection(2);
                break;
            case R.id.upcomingButton :
                categorySpinner.setSelection(3);
                break;
            case R.id.favouriteButton:
                categorySpinner.setSelection(4);
                break;
//            case R.id.latestButton :
//                myToolbar.setTitle(getString(R.string.latest));
//                break;
        }

    }

    /**
     * Setup the FavouriteListGrid
     * @param rootView
     */
    public void setupFavouriteListGrid(View rootView){
        //gather the favourite list from sharedPreferences
        SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> fave_set = (HashSet<String>) mPrefs.getStringSet(getString(R.string.favouritelistKEY), null);

        if(fave_set == null){
            //an empty screen
            return;
        }

        //fill in allMovieData
        Iterator<String> favouriteList = fave_set.iterator();
        int favouriteListSize = fave_set.size();
        int counter = 0;
        Movie_element[] favouriteMovies = new Movie_element[favouriteListSize];
        Log.d(LOG_TAG, "The size of the favourite list is: " + favouriteListSize);
        while(favouriteList.hasNext()){

            String movieID = favouriteList.next();

            //get the all the favourite movie information using the movieID
            String favouriteMovie_str = mPrefs.getString(movieID, "");
            if(favouriteMovie_str.equals("")){
                Log.d(LOG_TAG, "movieID was found in favouriteList but not found in SharedPreferences!");
                continue;
            }
            Gson gson = new Gson();
            Movie_element favouriteMovie = gson.fromJson(favouriteMovie_str, Movie_element.class);


            //counter can only be 0 to favouriteListSize-1
            if(counter == favouriteListSize){
                Log.e(LOG_TAG, "iterator has exceeded favouriteListSize");
            }

            //add the favouriteMovie onto favouriteMovies
            favouriteMovies[counter]=favouriteMovie;
            counter++;
            Log.d(LOG_TAG, favouriteMovie.getTitle());
        }

        //set allMovieData, note that we will not use baseImgURL
        allMovieData= new Data_Extracts();
        allMovieData.setCatMovies(favouriteMovies);

        setupGridView(rootView);

    }

    public void setupGridView(View rootView){
        //We are now ready to create the gridView
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        //The ImageAdapter expects the imageURL to be ready, I cannot add data to it dynamically like
        //ArrayAdapter from the Weather Application.
        gridview.setAdapter(new ImageAdapter(getActivity()));

        //set the listener when the user selects one of the posters
        gridview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

                //sets up the Activity's implementation to activate when a poster is clicked
                //set the URL to have w780, this is the largest poster we can get other than the orginial poster
                Movie_element selected_movie = allMovieData.getCatMovies()[i];
                mListener.OnPosterSelected(selected_movie);

            }
        });
    }

    //TODO: Move this class onto its own file
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return allMovieData.getCatMovies().length;
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
            Picasso.with(mContext)
                    .load(allMovieData.getCatMovies()[position].getFullPosterURL())
                    .placeholder(R.drawable.dog_placeholder)
                    .error(R.drawable.dog_error)
                    .resize(grid.getColumnWidth(),(int) (grid.getColumnWidth()*1.5) )
                    .into(imageView);

            //Log.d(LOG_TAG, "This is the url used for the little posters: " + allMovieData.getBaseImgURL(grid.getColumnWidth()) + allMovieData.getCatMovies()[position].getPosterURL());

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


            //Only download the selected movie category
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
                //If there is no internet connection then we do not display anything on the fragment
                if( API_full_response.getConfiguration_str() == null){

                    CharSequence text = "Please connect to the internet and then restart the app...";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(mContext, text, duration);
                    toast.show();

                    return ;
                }
                //turn the String JSON to JSONObjects
                JSON_response= new theMovieDB_JSON(API_full_response.getConfiguration_str(),
                        API_full_response.getCategory_str() );

                //Create the arrays inside allMovieData

                allMovieData = new Data_Extracts(JSON_response.getCategory_size());


                //Extract the data from JSONObjects and put them in allMovieData
                JSON_response.setAll_of(allMovieData);

            }catch(JSONException e){
                Log.e(LOG_TAG, "Failed to create JSON objects out of API response");
            }

            setupGridView(rootView);


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
