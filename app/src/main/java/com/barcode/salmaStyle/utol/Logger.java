package com.barcode.salmaStyle.utol;

import android.content.Context;

import java.util.Locale;

public class Logger {

    public static void line(LoggerMessage loggerSchema, boolean print, Object... objects) {
    }

    public static void analyser(Context context, LoggerMessage loggerSchema, boolean print, Object... objects) {
    }

    public enum LoggerMessage {
        MAKER,
        saveImagesPathList,
        addCloset_pagerActivity,
        STR_COLORS,
        TAG_DASH_BOARD,
        ITEM_SELECTED,
        MultiChoiceItems,
        Request_Api,
        str_gender_type,
        category_type_code,
        strOutfitCode,
        MySuggestionDetails,
        updateOutfitDetails,
        updateOutfitDetails22,
        updateOutfitDetails44,
        updateOutfitDetails55,
        updateOutfitDetails88,
        updateOutfitDetails99,
        RESULT_SHOE_CROP_API,
        MySuggestionOutfitDetailsActivity1,
        MySuggestionOutfitDetailsActivity2,
        OutfitClosetMySuggestionOutfitActivity2,
        OutfitClosetMySuggestionOutfitActivity1,
        Save_Outfit_Activity1,
        Save_Outfit_Activity2,
        Create_Post_Activity1,
        Items_Fragment,
        get_product_temp_outfit,
        MultyImageMultyImage,
        addCloset_pagerActivity22,
        addCloset_pagerActivity33,
        AppointmentAvailability,
        CALENDAR_CHECK,
        Timestamp,
        ViewAll_Store_Products,
        Appointment_Activity,
        ALLSTORES_FRAGMENTS,
        UPDATE_PROFILE_ACTIVITY,
        SERVICE_PROVIDER_DETAILS_ACTIVITY2,
        STORE_PRODUCT_ACTIVITY2,
        AddStoreClosetItemsWithCategory2,
        AddStoreClosetItemsWithCategory3,
        AddStoreClosetItemsWithCategory4,
        PAYMENT_ACTIVITY,
        OWNER_PROFILE_ACTIVITY,
        CampaignAdapter,
        AdvertiseViewPagerAdapter,
        LaundryBasketActivity,
        OutfitMakeActivity,
        Friend_Request_Adapter,
        check_expired_outfit,
        StorageBasketAdapter,
        Save_Outfit_Activity101,
        Outfit_List_Fragment101,
        OutfitViewFragment101,
        Service_provider_List_Fragment101,
        Save_Outfit_Activity103,
        My_Cart_Activity,
        AddStoreClosetItemsWithCategory401,
        EVENT_CALENDER,
        MySuggestionOutfitDetailsActivity,
        CropImageActivity,
        product_image_url,
        Service_provider_List_Fragment102,
        Wardrobes_Fragment,
        sell_status,
        DESTINATION101,
        DESTINATION102,
        DESTINATION103,
        DESTINATION104,
        users_new_category_list,
        GOOGLE_SIGN,
        saveImagesPathListFeb17_1002,
        MultyImageMultyImage1002,
        ServiceOutfitMakeActivity,
        GET_SIGNATURES,
        COUNT_SERVICE_CHAT,
        LocationManager,
        SelectLocationOutfitActivity200,
        camera_analyser,
        ANALYSIS_123,
        IMAGE_DETECTOR,
        IMAGE_DETECTION_123,
        COLOR_IMAGE_DETECTION_123,
        Service_provider_List_Fragment166, DeleteClosetItems, RENT
    }

    public static String getThread(StackTraceElement stackTraceElement) {
        return String.format(Locale.ENGLISH, "(%s:%d)",
                stackTraceElement.getFileName(),
                stackTraceElement.getLineNumber());
    }

}