package ru.imilka.randomfact.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import ru.imilka.randomfact.R;

public class FragmentAdvice extends Fragment {

    private View root_view;

    private TextView advice;
    private ImageView background;
    private LinearLayout box;

    private LinearLayout soc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_advice, null);

        advice = (TextView) root_view.findViewById(R.id.advice);
        background = (ImageView) root_view.findViewById(R.id.background);
        box = (LinearLayout) root_view.findViewById(R.id.box);
        soc = (LinearLayout) root_view.findViewById(R.id.soc);




        final String urlapi = "https://fucking-great-advice.ru/api/random";

        new GetAdvice(urlapi).execute();
        new setBackground().execute();





        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animout = AnimationUtils.loadAnimation(getContext(), R.anim.translate);
                box.startAnimation(animout);

                animout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new GetAdvice(urlapi).execute();
                        Animation animin = AnimationUtils.loadAnimation(getContext(), R.anim.transon);
                        box.startAnimation(animin);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                Animation animoutback = AnimationUtils.loadAnimation(getContext(), R.anim.shrink);
                background.startAnimation(animoutback);

                animoutback.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        new setBackground().execute();
//                        Animation animinback = AnimationUtils.loadAnimation(getContext(), R.anim.enlarge);
//                        background.startAnimation(animinback);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                Animation fadein = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
                soc.startAnimation(fadein);

                fadein.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation fadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
                        soc.startAnimation(fadeout);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }
        });

        soc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, advice.getText().toString());
                try
                {
                    startActivity(Intent.createChooser(intent, "Поделиться советом"));
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return root_view;
    }



    class setBackground extends AsyncTask<String, Void, String> {



        public setBackground() {


        }

        @Override
        protected String doInBackground(String... strings){


            String[] arr={"01", "02", "06", "15", "18", "19", "20", "22", "23", "33", "35", "37", "40", "41", "43", "46", "47", "50", "52", "54", "56", "10", "53", "60"};
            Random r=new Random();
            int randomNumber=r.nextInt(arr.length);

            String imageurl = "https://fucking-great-advice.ru/data/frontpage/_default/" + arr[randomNumber] + ".jpg";


            return imageurl;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Glide.with(root_view).load(s)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transition(GenericTransitionOptions.with(R.anim.enlarge))
                    .skipMemoryCache(true)
                    .into(background);




        }
    }

    class GetAdvice extends AsyncTask<String, Void, String>  {

        String urls;

        public GetAdvice(String url) {
            urls = url;

        }

        @Override
        protected String doInBackground(String... strings){
            URL url = null;
            try {
                url = new URL(urls);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            URLConnection request = null;
            try {
                request = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                request.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JsonParser jp = new JsonParser();
            JsonElement root = null;
            try {
                root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonObject rootobj = root.getAsJsonObject();
            String parseadvice = rootobj.get("text").getAsString();


            return parseadvice;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            advice.setText(s);
        }
    }
}
