package com.example.pwo.network;
import static com.example.pwo.Constants.BASE_URL;

import android.content.Context;

import com.example.pwo.network.models.AuthResponse;
import com.example.pwo.utils.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {

    private static ApiClient instance;
    private final ApiService apiService;
    private final TokenManager tokenManager;

    private ApiClient(Context context) {
        tokenManager = new TokenManager(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        String accessToken = tokenManager.getAccessToken();
                        String refreshToken = tokenManager.getRefreshToken();

                        Request.Builder requestBuilder = originalRequest.newBuilder();

                        if (accessToken != null) {
                            requestBuilder.header("Authorization", "Bearer " + accessToken);
                        }

                        if (refreshToken != null) {
                            requestBuilder.header("Refresh", refreshToken);
                        }

                        Request requestWithToken = requestBuilder.build();
                        Response response = chain.proceed(requestWithToken);

                        if (response.code() == 401) {
                            tokenManager.refreshAccessToken(new Callback<AuthResponse>() {
                                @Override
                                public void onResponse(Call<AuthResponse> call, retrofit2.Response<AuthResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String newAccessToken = response.body().getAccessToken();
                                        Request newRequest = originalRequest.newBuilder()
                                                .header("Authorization", "Bearer " + newAccessToken)
                                                .header("Refresh", refreshToken)
                                                .build();
                                        try {
                                            chain.proceed(newRequest);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<AuthResponse> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }

                        return response;
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}