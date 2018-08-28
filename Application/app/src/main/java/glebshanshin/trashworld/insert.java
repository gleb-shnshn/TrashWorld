package glebshanshin.trashworld;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface insert {//интерфейс для обращения к скрипту добавления в серверную базу данных определенного QR кода
    @FormUrlEncoded
    @POST("/insert.php")
    Call<Object> performPostCall(@FieldMap HashMap<String, String> postDataParams);
}