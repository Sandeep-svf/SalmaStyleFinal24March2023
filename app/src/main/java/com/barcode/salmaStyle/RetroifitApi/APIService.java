package com.barcode.salmaStyle.RetroifitApi;


import com.barcode.salmaStyle.model.ApprovedProductModel;
import com.barcode.salmaStyle.model.ChangePasswordModel;
import com.barcode.salmaStyle.model.ClearNotificationModel;
import com.barcode.salmaStyle.model.CountryCodeMOdel;
import com.barcode.salmaStyle.model.DeleteAccModel;
import com.barcode.salmaStyle.model.EditImageModel;
import com.barcode.salmaStyle.model.EmilaVerificationOtpModel;
import com.barcode.salmaStyle.model.LogoutModel;
import com.barcode.salmaStyle.model.NotificationDeleteModel;
import com.barcode.salmaStyle.model.NotificationModel;
import com.barcode.salmaStyle.model.OccassionModel;
import com.barcode.salmaStyle.model.OnDietModel;
import com.barcode.salmaStyle.model.PendingProductModel;
import com.barcode.salmaStyle.model.ProductScanModel;
import com.barcode.salmaStyle.model.RejectedProductModel;
import com.barcode.salmaStyle.model.SubmitImageModel;
import com.barcode.salmaStyle.model.UpdateProfileModel;
import com.barcode.salmaStyle.model.LoginModel;
import com.barcode.salmaStyle.model.ProfileModel;
import com.barcode.salmaStyle.model.SignupModel;
import com.barcode.salmaStyle.model.UserRecentProductModel;
import com.barcode.salmaStyle.model.VarifyModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface APIService {

    @FormUrlEncoded
    @POST(ServiceUrlList.signup)
    Call<SignupModel> getSignup(@Field("name") String name,
                                @Field("phone") String phone,
                                @Field("password") String password,
                                @Field("email") String email);

    @FormUrlEncoded
    @POST(ServiceUrlList.login)
    Call<LoginModel> getLogin(@Field("password") String password,
                              @Field("email") String email,
                              @Field("device_token") String deviceToken
                              );


    @GET(ServiceUrlList.profile_info)
    Call<ProfileModel> getprofile();

    @Multipart
    @PUT(ServiceUrlList.profile_info)
    Call<UpdateProfileModel> getupdateprofile(@Part("name") String name,
                                              @Part("phone") String phone,
                                              @Part("email") String email,
    @Part MultipartBody.Part image
    );


    @FormUrlEncoded
    @POST(ServiceUrlList.change_pass)
    Call<ChangePasswordModel> changepassword(@Field("old_password") String old_password,
                                             @Field("new_password") String new_password);
    @FormUrlEncoded
    @POST(ServiceUrlList.product_scan)
    Call<ProductScanModel> productscan(@Field("barcode") String barcode);

    @GET(ServiceUrlList.product_approved)
    Call<ApprovedProductModel> getapprovedProduct();

    @GET(ServiceUrlList.product_pending)
    Call<PendingProductModel> getpendingProduct();

    @GET(ServiceUrlList.product_rejected)
    Call<RejectedProductModel> getrejectedProduct();

    @Multipart
    @POST(ServiceUrlList.submit_image)
    Call<SubmitImageModel> submitimages( @Part MultipartBody.Part image_one,
                                         @Part MultipartBody.Part image_two,
                                         @Part MultipartBody.Part image_three,
                                         @Part MultipartBody.Part image_four,
                                         @Part("barcode") String barcode
                                         );


    @Multipart
    @PUT(ServiceUrlList.edit_image)
    Call<EditImageModel> editimages(@Part MultipartBody.Part image_one,
                                    @Part MultipartBody.Part image_two,
                                    @Part MultipartBody.Part image_three,
                                    @Part MultipartBody.Part image_four,
                                    @Part("id") String id
    );



    @GET(ServiceUrlList.recent_product)
    Call<UserRecentProductModel> getrecentProduct();

    @FormUrlEncoded
    @POST(ServiceUrlList.logout)
    Call<LogoutModel> logout(@Field("refresh") String refresh);



    @GET(ServiceUrlList.notification)
    Call<NotificationModel> get_notification();

    @FormUrlEncoded
    @POST(ServiceUrlList.notification)
    Call<ClearNotificationModel> clearNotifications(@Field("id") String refresh);

    @FormUrlEncoded
    @POST(ServiceUrlList.notification_delete)
    Call<NotificationDeleteModel> get_notificationdelete(@Field("id")String id);


    @GET(ServiceUrlList.on_diet)
    Call<OnDietModel> ondietList();

    @GET(ServiceUrlList.on_occassion)
    Call<OccassionModel> onoccassionList();


    @GET(ServiceUrlList.country_list)
    Call<CountryCodeMOdel> countryList();

    @FormUrlEncoded
    @POST(ServiceUrlList.varify_phone)
    Call<VarifyModel> varify(@Field("phone")String phone);

    @FormUrlEncoded
    @POST(ServiceUrlList.reset_pass)
    Call<VarifyModel> reset_pass(@Field("phone")String phone,
                                 @Field("password")String password);

    @GET(ServiceUrlList.delete_acc)
    Call<DeleteAccModel> getdeleteAcc();

    @FormUrlEncoded
    @POST(ServiceUrlList.email)
    Call<EmilaVerificationOtpModel> verify_email(@Field("email") String email);


    @FormUrlEncoded
    @POST(ServiceUrlList.email_verify_otp)
    Call<SignupModel> verify_email_otp2(@Field("verify_email_otp") String verify_email_otp);

}
