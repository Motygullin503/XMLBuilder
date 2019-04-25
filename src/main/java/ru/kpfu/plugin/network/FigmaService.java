package ru.kpfu.plugin.network;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import ru.kpfu.plugin.model.FigmaModel;

public interface FigmaService {

    @Headers("X-Figma-Token: 9099-b80f4dac-c30e-41c7-b6c0-c849777d4dab")
    @GET("v1/files/{id}")
    Call<FigmaModel> getJson(@Path("id") String id);
}