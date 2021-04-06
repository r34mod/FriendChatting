package com.example.friendchatting.compraPojos;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class CompraApi {

    public static FourSquareService fourSquareService=null;
    public static FourSquareService getFourSquareService(){
        //logging

        OkHttpClient.Builder okhttp=new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        HttpLoggingInterceptor httpLoggingInterceptor=logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp.addInterceptor(logging);


        if (fourSquareService==null){

            Retrofit.Builder retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.foursquare.com/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttp.build());

            fourSquareService=retrofit.build().create(FourSquareService.class);
        }
        return fourSquareService;

    }

    public interface FourSquareService {
        @GET("venues/explore/")
        Call<FourSquareResource> getForsquareResponse(
                @Query("client_id") String client_id,
                @Query("client_secret") String client_secret,
                @Query("v") String v,
                @Query("ll") String ll,
                @Query("query") String query);


    }
}
