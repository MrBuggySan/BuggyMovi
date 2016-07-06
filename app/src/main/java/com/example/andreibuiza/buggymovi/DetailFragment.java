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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.HashSet;
import java.util.Set;

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

        movieSelected = (Movie_element) bundle.getParcelable(getString(R.string.movieSelectedkey));

        //TODO: add an image carousel to browse through different images related to the movie
        ImageView poster = (ImageView) rootView.findViewById(R.id.posterImageView);

        //TODO: figure out a way to work with dp instead of raw px when drawing the images
        Picasso.with(getContext())
                .load(movieSelected.getFullPosterURL())
                .placeholder(R.drawable.dog_placeholder)
                .error(R.drawable.dog_error)
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
        //TODO: this will be a problem when I have this and the MovieGridFragment displayed together
        //myToolbar.setDisplayShowCustomEnabled(false);
//        myToolbar.setDisplayShowTitleEnabled(true);
//        myToolbar.setTitle(movieSelected.getTitle());



        //if the selected movie does not already have the movie review and movie trailer, then download it
        if(!movieSelected.hasLoadedTrailerandReview()){
            new FetchTrailerandReviews(getContext(), inflater, rootView).execute();
        }


        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.favourite, menu);
        MenuItem faveIcon = menu.findItem(R.id.FavouriteStar);
        SharedPreferences mPrefs = getActivity().getSharedPreferences(getString(R.string.favouritelistKEY),
                Context.MODE_PRIVATE);
        String json = mPrefs.getString(movieSelected.getMovieID(), "");

        if(json.equals("")){
            faveIcon.setIcon(R.drawable.fave_dog_2_unselected);
            faveIcon.setChecked(false);
        }else{
            faveIcon.setIcon(R.drawable.fave_dog_2_selected);
            faveIcon.setChecked(true);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.FavouriteStar){
            SharedPreferences mPrefs = getActivity().getSharedPreferences(getString(R.string.favouritelistKEY),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();

            //Retrieve the favourite list
            Set<String> fave_set = mPrefs.getStringSet(getString(R.string.favouritelistKEY), null);
            //remove the favourite list
            prefsEditor.remove(getString(R.string.favouritelistKEY));
            if(fave_set == null){
                fave_set = new HashSet<>();
            }

            if(item.isChecked()){
                //display the unchecked icon
                item.setIcon(R.drawable.fave_dog_2_unselected);
                item.setChecked(false);

                prefsEditor.remove(movieSelected.getMovieID());

                //update the favourite list
                if(!fave_set.remove(movieSelected.getMovieID())){
                    Log.e(LOG_TAG, "The movie was not there in the first place...");
                }
            }else{
                //display the checked icon
                item.setIcon(R.drawable.fave_dog_2_selected);
                item.setChecked(true);

                Gson gson = new Gson();
                String MovieJSON = gson.toJson(movieSelected);
                prefsEditor.putString(movieSelected.getMovieID(), MovieJSON);

                //update the favourite list
                fave_set.add(movieSelected.getMovieID());
            }

            //add the favourite list
            prefsEditor.putStringSet(getString(R.string.favouritelistKEY), fave_set);
            prefsEditor.commit();

        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchTrailerandReviews extends AsyncTask<Integer, String, rawMovieReviewResponse> {
        private final String LOG_TAG= FetchTrailerandReviews.class.getSimpleName();
        private View rootView;
        private LayoutInflater inflater;
        private Context mContext;

        public FetchTrailerandReviews(Context context_, LayoutInflater inflater_, View rootView_){
            mContext= context_;
            rootView = rootView_;
            inflater = inflater_;
        }

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
           //Log.d(LOG_TAG, "fetch movie trailer with: " + trailer );

            //movie reviews
            //http://api.themoviedb.org/3/movie/269149/reviews?api_key=7a0f090baebf83c2c4b2e49a59a85ebc&page=1
            Uri.Builder reviews = BuildBaseUrl()
                    .appendPath("movie")
                    .appendPath(movieSelected.getMovieID())
                    .appendPath("reviews")
                    .appendQueryParameter(getString(R.string.theMovieDBAPI_key_parameter),
                            getString(R.string.theMovieDBAPI_key));
//            Log.d(LOG_TAG, "fetch movie reviews with: " + reviews );


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
                    movieTrailer[] movieTrailers_temp = new movieTrailer[1];
                    movieTrailers_temp[0]= new movieTrailer("no trailer", "no trailer", "no trailer", "no trailer");
                    movieSelected.setMovieTrailers(movieTrailers_temp);


                }else{
                    movieTrailer[] movieTrailers_temp = new movieTrailer[movieTrailerJSON.getJSONArray("results").length()];
                    for(int i = 0; i < movieTrailers_temp.length ; i++){
                        String sitename_test = movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("site");
                        //TODO: I'm not sure why this is not comparing correctly.
//                        if( sitename_test.equals("YouTube") ){
//
//                            //the site shown must be from youtube
//                            //TODO: what happens if all available trailers are not on YouTube?
//                            continue;
//                        }
                        movieTrailers_temp[i]= new movieTrailer(movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("key"),
                                movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("name"),
                                movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("site"),
                                movieTrailerJSON.getJSONArray("results").getJSONObject(i).getString("type"));

                        //Log.d(LOG_TAG, movieTrailers_temp[i].toString() );
                    }
                    movieSelected.setMovieTrailers( movieTrailers_temp );

                }
                //Extract the movie trailer data from movieTrailerJSON and put them in movieSelected
                if(movieReviewJSON.getJSONArray("results").length() == 0){
                    movieReview[] movieReviews_temp= new movieReview[1] ;
                    movieReviews_temp[0] = new movieReview("", "No reviews");
                    movieSelected.setMovieReviews(movieReviews_temp);
//                    Log.d(LOG_TAG, movieReviews_temp[0].toString());
                }
                else{
                    movieReview[] movieReviews_temp = new movieReview[ movieReviewJSON.getJSONArray("results").length()];


                    for(int i = 0 ; i < movieReviews_temp.length ; i++){
                        movieReviews_temp[i]= new movieReview( movieReviewJSON.getJSONArray("results").getJSONObject(i).getString("author"),
                            movieReviewJSON.getJSONArray("results").getJSONObject(i).getString("content"));

//                        Log.d(LOG_TAG, movieReviews_temp[i].toString());
                    }
                    movieSelected.setMovieReviews(movieReviews_temp);
                }
                //Log.d(LOG_TAG, movieSelected.toString());

            }catch(JSONException e){
                Log.e(LOG_TAG, "Failed to create JSON objects out of movie trailer and review API response");
            }

            //TODO: Display the YouTube trailers on the fragment better
            LinearLayout TrailerContainer = (LinearLayout) rootView.findViewById(R.id.TrailerContainer);
            for(int i = 0 ; i < movieSelected.getMovieTrailers().length ; i++){
                if( movieSelected.getMovieTrailers()[i].getName() == "no trailer"){
                    //create a TextView which states there are no trailers available
                    View noTrailerView = (View) inflater.inflate(R.layout.notrailer, null);

                    TrailerContainer.addView(noTrailerView);
                    break;
                }

                //Display the ImageViews that contain the Trailer thumbnails
                View TrailerImageContainer = (View) inflater.inflate(R.layout.trailerthumbnail, null);
                ImageView TrailerImage = (ImageView) TrailerImageContainer.findViewById(R.id.TrailerImage);

                TrailerContainer.addView(TrailerImageContainer);

                //This is the default video image from YouTube.
                //http://img.youtube.com/vi/0Dx4SiyERVg/0.jpg

                Uri.Builder DefaultTrailerImage= new Uri.Builder()
                                .scheme("http")
                                .authority("img.youtube.com")
                                .appendPath("vi")
                                .appendPath(movieSelected.getMovieTrailers()[i].getTrailerKey())
                                .appendPath("0.jpg");

                //TODO: use a better value for width and height, possibly in dp
                Picasso.with(mContext)
                        .load(DefaultTrailerImage.toString())
                        .resize(480, 360)
                        .into(TrailerImage);
//                Log.d(LOG_TAG, "This is the URL used for the youtube image: " + DefaultTrailerImage.toString() );



                TrailerImage.setOnClickListener(new onMovieTrailerClickListener(movieSelected.getMovieTrailers()[i].getTrailerKey(), mContext));


            }


            //TODO: Design a better UI for the reviews, currently it is BLEEEEEEEEHH >:O
            LinearLayout ReviewContainer = (LinearLayout) rootView.findViewById(R.id.ReviewContainer);
            for(int i = 0 ; i < movieSelected.getMovieReviews().length ; i++){
                View review_entry = (View) inflater.inflate(R.layout.reviewview, null);

                //Set the review content
                TextView reviewContent = (TextView) review_entry.findViewById(R.id.reviewContent);
                reviewContent.setText(movieSelected.getMovieReviews()[i].getContent());

                //Set the review author
                TextView reviewAuthor = (TextView) review_entry.findViewById(R.id.reviewAuthor);
                reviewAuthor.setText(movieSelected.getMovieReviews()[i].getAuthor());

                //add the view to rootView
                ReviewContainer.addView(review_entry);

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
