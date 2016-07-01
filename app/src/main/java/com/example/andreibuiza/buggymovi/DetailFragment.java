package com.example.andreibuiza.buggymovi;

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
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by AndreiBuiza on 6/23/2016.
 */
public class DetailFragment extends Fragment{
    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    private Movie_element movieSelected;
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    //Draw the UI elements on the screen
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detailfragment, container, false);

        Bundle bundle = this.getArguments();

        movieSelected = bundle.getParcelable(getString(R.string.movieSelectedkey));

        //TODO: add an image browser to browse through different images related to the movie
        ImageView poster = (ImageView) rootView.findViewById(R.id.posterImageView);

        //TODO: figure out a way to work with dp instead of raw px when drawing the images
        Picasso.with(getContext())
                .load(movieSelected.getFullPosterURL())
                .resize(780, (int)(780 * 1.5))
                .into(poster);

        TextView synopsis = (TextView) rootView.findViewById(R.id.sysnopsis);
        synopsis.setText(movieSelected.getSynopsis());

        TextView ratingtext = (TextView) rootView.findViewById(R.id.ratingText);
        ratingtext.setText(movieSelected.getRating() + " / 10");

        TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        releaseDate.setText("Release Date: "+ movieSelected.getReleaseDate());

        //turn off the spinner from moviegridfragment
        ActionBar myToolbar = (ActionBar)((AppCompatActivity) getActivity()).getSupportActionBar();
        myToolbar.setDisplayShowCustomEnabled(false);
        myToolbar.setDisplayShowTitleEnabled(true);
        myToolbar.setTitle(movieSelected.getTitle());


        //if the selected movie does not already have the movie review and movie trailer, the download it
//        if(!movieSelected.hasTrailer()){
//            new FetchTrailerandReviews().execute();
//        }
        new FetchTrailerandReviews().execute();


        return rootView;
    }

    public class FetchTrailerandReviews extends AsyncTask<Integer, String, rawMovieReviewResponse> {
        private final String LOG_TAG= FetchTrailerandReviews.class.getSimpleName();

        @Override
        public void onPreExecute(){


        }

        /**
         * Call the API for the Movie reviews and Movie trailers
         * @param params
         * @return
         */
        @Override

        public rawMovieReviewResponse doInBackground (Integer... params){


            //movie trailer
            //http://api.themoviedb.org/3/movie/246655/videos?api_key=7a0f090baebf83c2c4b2e49a59a85ebc
            Uri.Builder trailer= BuildBaseUrl()
                    .appendPath("movie")
                    .appendPath(movieSelected.getMovieID())
                    .appendPath("videos")
                    .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                            getString(R.string.theMovieDBAPI_key));
            Log.d(LOG_TAG, "fetch movie trailer with: " + trailer );

            //movie reviews
            //http://api.themoviedb.org/3/movie/269149/reviews?api_key=7a0f090baebf83c2c4b2e49a59a85ebc&page=1
            Uri.Builder reviews = BuildBaseUrl()
                    .appendPath("movie")
                    .appendPath(movieSelected.getMovieID())
                    .appendPath("reviews")
                    .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                            getString(R.string.theMovieDBAPI_key));
            Log.d(LOG_TAG, "fetch movie reviews with: " + reviews );


            return new rawMovieReviewResponse(getData(trailer.build().toString()),
                    getData(reviews.build().toString()));
        }


        /**
         *
         * @param JSON_response_str
         */
        @Override
        public void onPostExecute(rawMovieReviewResponse JSON_response_str){

            rawMovieReviewResponse API_full_response = JSON_response_str;

            try{
                //turn the String JSON to JSONObjects
                JSONObject movieTrailerJSON = new JSONObject(API_full_response.getMovieTrailer_str());
                JSONObject movieReviewJSON = new JSONObject(API_full_response.getMovieReview_str());

                //Extract the movie trailer data from movieTrailerJSON and put them in movieSelected
                if(movieTrailerJSON.getJSONArray("results").length() == 0){
                    //no trailer available
                }else{
                    movieTrailer[] movieTrailers_temp = new movieTrailer[movieTrailerJSON.getJSONArray("results").length()];
                    for(int i = 0; i < movieTrailers_temp.length ; i++){
                        movieTrailers_temp[i]= new movieTrailer(movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("key"),
                                movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("name"),
                                movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("site"),
                                movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("type"));

                        Log.d(LOG_TAG, movieTrailers_temp[i].toString() );
                    }
                    movieSelected.setMovieTrailers( movieTrailers_temp );

                }





                //Extract the movie trailer data from movieTrailerJSON and put them in movieSelected
                if(movieReviewJSON.getJSONArray("results").length() == 0){
                    movieReview[] movieReviews_temp= new movieReview[1] ;
                    movieReviews_temp[0] = new movieReview("", "No reviews");
                    movieSelected.setMovieReviews(movieReviews_temp);
                    Log.d(LOG_TAG, movieReviews_temp[0].toString());
                }
                else{
                    movieReview[] movieReviews_temp = new movieReview[ movieReviewJSON.getJSONArray("results").length()];


                    for(int i = 0 ; i < movieReviews_temp.length ; i++){
                        //TODO: extracting the review content data from the JSON is not working out too well, I have to fix that.
//                        movieReviews_temp[i]= new movieReview( movieReviewJSON.getJSONArray("results").getJSONObject(i).getString("author"),
//                            movieReviewJSON.getJSONArray("results").getJSONObject(i).getString("content"));

                        movieReviews_temp[i]= new movieReview( movieReviewJSON.getJSONArray("results").getJSONObject(i).getString("author"),
                                "This is where the content should be");

                        Log.d(LOG_TAG, movieReviews_temp[i].toString());
                    }
                    movieSelected.setMovieReviews(movieReviews_temp);
                }
                //Log.d(LOG_TAG, movieSelected.toString());

            }catch(JSONException e){
                Log.e(LOG_TAG, "Failed to create JSON objects out of movie trailer and review API response");
            }

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
