package ru.kpfu.plugin.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FigmaRetrofit {
    private final String baseUrl = "https://api.figma.com";
    private FigmaService service;

    public FigmaRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
            .build();

        service = retrofit.create(FigmaService.class);

    }

    public FigmaService getService() {
        return service;
    }
}
