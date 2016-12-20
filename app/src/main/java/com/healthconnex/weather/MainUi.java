package com.healthconnex.weather;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.WeatherInfo.ForecastInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.SEARCH_MODE;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.UNIT;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherExceptionListener;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;


public class MainUi extends BaseActivity implements YahooWeatherInfoListener, YahooWeatherExceptionListener {
	public static final String tag = "MainUi";

	private ProgressBar progressweather;
    private ImageView mylocation;
    private ImageView search;
    private ImageView submitSearch;
    private String currentLocation;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_ui);

        // initialize the library
		mYahooWeather.setExceptionListener(this);
        mWeatherInfosLayout = (LinearLayout) findViewById(R.id.weather_infos);
        progressweather = (ProgressBar) findViewById(R.id.progressBarW);

        // click on my current location
        mylocation = (ImageView) findViewById(R.id.mylocation);
        mylocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByGPS();
            }
        });
        // click on search, display an input, and submit button
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout inputarea = (LinearLayout) findViewById(R.id.searcharea);
                inputarea.setVisibility(View.VISIBLE);
                LinearLayout titlearea = (LinearLayout) findViewById(R.id.titlearea);
                titlearea.setVisibility(View.INVISIBLE);
            }
        });

        submitSearch = (ImageView) findViewById(R.id.submit_area);
        submitSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchlocation = (EditText) findViewById(R.id.searchlocation);
                String searchname = searchlocation.getText().toString();
                Toast.makeText(getApplicationContext(), "search for:"+searchname, Toast.LENGTH_LONG).show();
                if(searchname != null && searchname.trim().length() > 0){
                    LinearLayout inputarea = (LinearLayout) findViewById(R.id.searcharea);
                    inputarea.setVisibility(View.INVISIBLE);
                    LinearLayout titlearea = (LinearLayout) findViewById(R.id.titlearea);
                    titlearea.setVisibility(View.VISIBLE);
                    searchByPlaceName(searchname);
                }
                hideKeyBoard();
            }
        });
        // click on the comming weather
        mWeatherInfosLayout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//refreshWeather();
			}
        });

        // first into the main ui, use current location to get weather info
    	searchByGPS();
    }

    @Override
	    public void onFailConnection(final Exception e) {
	        // TODO Auto-generated method stub
	        setNoResultLayout();
	        Toast.makeText(getApplicationContext(), "Fail Connection", Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public void onFailParsing(final Exception e) {
	        // TODO Auto-generated method stub
	        setNoResultLayout();
	        Toast.makeText(getApplicationContext(), "Fail Parsing", Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public void onFailFindLocation(final Exception e) {
	        // TODO Auto-generated method stub
	        setNoResultLayout();
	        Toast.makeText(getApplicationContext(), "Fail Find Location", Toast.LENGTH_SHORT).show();
	    }
		private void setNoResultLayout() {
            Toast.makeText(getApplicationContext(), "No data returnd.", Toast.LENGTH_SHORT).show();
		}

		// when user click on the ...
		private void refreshWeather(){
			if(currentLocation != null){
				searchByPlaceName(currentLocation);
			}else{
				searchByGPS();
			}
		}

		// search weather based on user input content
		private void searchByPlaceName(String location) {
			progressweather.setVisibility(View.VISIBLE);
			mWeatherInfosLayout.removeAllViews();
			mWeatherInfosLayout.addView(progressweather);

			mYahooWeather.setNeedDownloadIcons(true);
			mYahooWeather.setUnit(UNIT.CELSIUS);
			mYahooWeather.setSearchMode(SEARCH_MODE.PLACE_NAME);
			mYahooWeather.queryYahooWeatherByPlaceName(getApplicationContext(), location, MainUi.this);
		}

		// automatically use gps to search
		private void searchByGPS() {
			progressweather.setVisibility(View.VISIBLE);
			mWeatherInfosLayout.removeAllViews();
			mWeatherInfosLayout.addView(progressweather);

			mYahooWeather.setNeedDownloadIcons(true);
			mYahooWeather.setUnit(UNIT.CELSIUS);
			mYahooWeather.setSearchMode(SEARCH_MODE.GPS);
			mYahooWeather.queryYahooWeatherByGPS(getApplicationContext(), MainUi.this);
		}
		
		// weather forecast
		private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000, true);
		private LinearLayout mWeatherInfosLayout;

        // callback when data returned
		@Override
		public void gotWeatherInfo(WeatherInfo weatherInfo) {
			// TODO Auto-generated method stub
			try{
		        if (weatherInfo != null) {
		        	mWeatherInfosLayout.removeAllViews();
		        	@SuppressWarnings("deprecation")
					LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		        	layout.gravity= Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		        	layout.weight = 1;

		        	ImageView imagec = (ImageView) findViewById(R.id.currentimage);
		        	TextView descc = (TextView) findViewById(R.id.current_description);
		        	TextView tempch = (TextView) findViewById(R.id.current_temp_high);
                    TextView tempcc = (TextView) findViewById(R.id.current_temp_current);
                    TextView tempcl = (TextView) findViewById(R.id.current_temp_low);
		        	TextView weekc = (TextView) findViewById(R.id.current_weather_week);
                    TextView city = (TextView) findViewById(R.id.current_weather_city);
                    TextView timec = (TextView) findViewById(R.id.current_datetime);
                    TextView windc = (TextView) findViewById(R.id.current_wind);

                    city.setText(weatherInfo.getLocationCity());
                    weekc.setText(weatherInfo.getLastBuildDate());
                    if (weatherInfo.getCurrentConditionIcon() != null) {
                        imagec.setImageBitmap(weatherInfo.getCurrentConditionIcon());
                    }
                    descc.setText(weatherInfo.getCurrentText());
		        	tempch.setText(weatherInfo.getForecastInfoList().get(0).getForecastTempHigh() +"ºC");
                    tempcc.setText(weatherInfo.getCurrentTemp() +"ºC");
                    tempcl.setText(weatherInfo.getForecastInfoList().get(0).getForecastTempLow() +"ºC");

                    timec.setText(weatherInfo.getAstronomySunrise() + " / " +weatherInfo.getAstronomySunset());
                    windc.setText(weatherInfo.getWindSpeed());

//                    System.out.println("====== CURRENT ======" + "\n" +
//										           "date: " + weatherInfo.getCurrentConditionDate() + "\n" +
//												   "weather: " + weatherInfo.getCurrentText() + "\n" +
//											       "temperature in ºC: " + weatherInfo.getCurrentTemp() + "\n" +
//											       "wind chill: " + weatherInfo.getWindChill() + "\n" +
//										           "wind direction: " + weatherInfo.getWindDirection() + "\n" +
//											       "wind speed: " + weatherInfo.getWindSpeed() + "\n" +
//										           "Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
//										           "Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
//										           "Visibility: " + weatherInfo.getAtmosphereVisibility()
//										           );
					
					for (int i = 0; i < 4; i++) { //i < YahooWeather.FORECAST_INFO_MAX_SIZE
						final LinearLayout forecastInfoLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.forecast_info, null);
						forecastInfoLayout.setLayoutParams(layout);

			        	final ImageView imagecF = (ImageView) forecastInfoLayout.findViewById(R.id.weather_image);
			        	final TextView desccF = (TextView) forecastInfoLayout.findViewById(R.id.weather_desc);
			        	final TextView tempcF = (TextView) forecastInfoLayout.findViewById(R.id.weather_temp);
			        	final TextView weekcF = (TextView) forecastInfoLayout.findViewById(R.id.weather_week);

						final ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);

						desccF.setText(forecastInfo.getForecastText());
			        	tempcF.setText(forecastInfo.getForecastTempLow() +"ºC/" + forecastInfo.getForecastTempHigh() +"ºC");
			        	weekcF.setText(forecastInfo.getForecastDay());
						if (forecastInfo.getForecastConditionIcon() != null) {
							imagecF.setImageBitmap(forecastInfo.getForecastConditionIcon());
						}
                        mWeatherInfosLayout.addView(forecastInfoLayout);
					}

                    if(weatherInfo.getLocationRegion() != null){
                        currentLocation = weatherInfo.getLocationRegion().trim().toUpperCase();
                    }
		        } else {
		        	setNoResultLayout();
		        }
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
}
