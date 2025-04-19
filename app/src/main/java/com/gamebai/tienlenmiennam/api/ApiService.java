package com.gamebai.tienlenmiennam.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("timTran")
    Call<PhanHoiDonGian> timTran(@Header("Authorization") String token);
    @GET("taoPhong")
    Call<PhanHoiDonGian> taoPhong(@Header("Authorization") String token);
    @GET("timPhongTheoIdPhong/{maPhong}")
    Call<PhanHoiDonGian> timPhongTheoIdPhong(@Header("Authorization") String token,
                                             @Path("maPhong") String maPhong);
    @POST("createZaloPayOrder")
    Call<PhanHoiGiaoDichZaloPay> taoDonGiaoDichZaloPay(@Header("Authorization") String token,
                                                       @Body NoiDungGiaoDichZaloPay noiDungGiaoDichZaloPay);
}
