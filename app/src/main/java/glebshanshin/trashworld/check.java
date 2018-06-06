package glebshanshin.trashworld;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface check {//интерфейс для обращения к скрипту проверки наличия в серверной базе данных определенного QR кода
    @FormUrlEncoded
    @POST("/check.php")
    Call<Object> performPostCall(@FieldMap HashMap<String,String> postDataParams);
}