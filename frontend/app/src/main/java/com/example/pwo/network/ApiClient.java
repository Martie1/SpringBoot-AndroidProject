package com.example.pwo.network;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//building retrofit client,
//configuring the URL to backend
//this retrofit instance will use the ApiClient and its routes
public class ApiClient {

    private static final String BASE_URL = "https://your-backend-url.com/api/";
    private static ApiClient instance;
    private final ApiService apiService;

    private ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //retrofit handles gson convert
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    //singleton
    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
