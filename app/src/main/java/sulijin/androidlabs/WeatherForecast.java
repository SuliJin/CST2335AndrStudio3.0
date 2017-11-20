package sulijin.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import static java.lang.System.in;

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private TextView curTempTextView, minTempTextView, maxTempTextView;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        imageView = (ImageView) findViewById(R.id.temperatureImageView);
        curTempTextView = (TextView) findViewById(R.id.currentTemperature);
        minTempTextView = (TextView) findViewById(R.id.minTemperature);
        maxTempTextView = (TextView) findViewById(R.id.maxTemperature);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
   //     progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute();
    }

    class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String maxTemperature;
        private String minTemperature;
        private String currentTemperature;
        private String iconName;
        private Bitmap bitmap;

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            XmlPullParser parser = Xml.newPullParser();
            try {
                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                InputStream inputStream = conn.getInputStream();

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);

                int event = parser.getEventType();

                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = parser.getName();

                    switch (event) {

                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("temperature")) {
                                currentTemperature = parser.getAttributeValue(null, "value") + "°C";
                                this.publishProgress(25);
                                minTemperature =parser.getAttributeValue(null, "min")+ "°C";
                                this.publishProgress(50);
                                maxTemperature =parser.getAttributeValue(null, "max")+ "°C";
                                this.publishProgress(75);
                            }

                            if (name.equalsIgnoreCase("weather")) {
                                iconName = parser.getAttributeValue(null, "icon");
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;

                        default:
                            break;
                    }
                    event = parser.next();
                }

                if (fileExistance(iconName + ".png")) {
                    bitmap = this.readImage(iconName + ".png");
                }
                
                else {
                    String bitmapURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                    bitmap = getImage(new URL(bitmapURL));
                }
                this.publishProgress(100);

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            bitmap = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());

                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            Log.i(ACTIVITY_NAME, "Get image from the website");
            return bitmap;
        }

        public Bitmap readImage(String image) {
            Bitmap bm = null;
            try {
                FileInputStream fis = openFileInput(image);

                bm = BitmapFactory.decodeStream(fis);
                fis.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(ACTIVITY_NAME, "Read the image from local directory");
            return bm;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        public boolean fileExistance(String imageName){
            File file = getBaseContext().getFileStreamPath(imageName);
            return file.exists();
        }

        @Override
        protected void onPostExecute(String result) {
            curTempTextView.setText(this.currentTemperature);
            minTempTextView.setText(this.minTemperature);
            maxTempTextView.setText(this.maxTemperature);
            imageView.setImageBitmap(this.bitmap);
        }
    }
}