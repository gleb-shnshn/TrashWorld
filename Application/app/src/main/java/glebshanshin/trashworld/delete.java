package glebshanshin.trashworld;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface delete {//интерфейс для обращения к скрипту удаления из серверной базы данных определенного QR кода
    @FormUrlEncoded
    @POST("/delete.php")
    Call<Object> performPostCall(@FieldMap HashMap<String,String> postDataParams);
}