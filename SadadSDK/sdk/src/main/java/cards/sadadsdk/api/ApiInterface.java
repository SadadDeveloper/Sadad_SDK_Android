package cards.sadadsdk.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by ${Hitesh} on 12-12-2017.
 */

public interface ApiInterface {

    @POST
    Call<String> post(@Url String url, @Body JsonObject body);

    @POST
    Call<String> post(@Url String url);

    @GET
    Call<String> get(@Url String url);

    @PATCH
    Call<String> patch(@Url String url, @Body Object body);

    @DELETE
    Call<String> delete(@Url String url);
}
