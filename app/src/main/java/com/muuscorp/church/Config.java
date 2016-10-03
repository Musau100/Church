package com.muuscorp.church;

/**
 * Created by sydney on 6/26/2016.
 */
final class Config {

    static final String TAG = "church";
    static final String CATEGORY_NAME = "cat_name";
    static final String CATEGORY_DESCRIPTION = "cat_desc";
    static final String CATEGORY_ID = "cat_id";

    //    about
    static final String STREET_ADDRESS = "street_address";
    static final String POSTAL_ADDRESS = "postal_address";
    static final String TELEPHONE = "telephone";
    static final String MOBILE = "mobile";
    static final String EMAIL = "email";
    static final String AREAS_WE_SERVE = "areas_we_serve";
    //    service
    static final String SERVICE_NAME = "service_name";
    static final String SERVICE_DESCRIPTION = "service_desc";
    static final String SERVICE_IMAGE = "image";
    static final String SERVICE_ID = "service_id";

    //    testimonial
    static final String TESTIMONIAL_NAME = "name";
    static final String TESTIMONIAL_COMPANY = "company";
    static final String TESTIMONIAL_CONTENT = "testimonial";
    static final String TESTIMONIAL_IMAGE_URL = "image";

    //    user
    static final String USER_NAME = "user_name";
    static final String USER_PHONE_NUMBER = "user_phone_number";
    static final String USER_EMAIL_ADDRESS = "user_email_address";
    static final String USER_LOCATION = "user_location";
    static final String USER_ADDITIONAL_INFORMATION = "user_additional_information";

    //    preferences
    static final String FIRST_RUN = "first_run";
    static final String PUSH_NOTIFICATION = "push_notification";
    static final String RINGTONE_PATH = "ringtone_path";
    static final String DOMESTIC_SERVICES_MESSAGE = "domestic_services_message";

    //slider
    static final String CAPTION = "caption";
    static final String SLIDER_IMAGE = "image";

    static final String ANDROID_FORMAT = "_android.jpg";
    static final String IMAGE_REGEX = ".jpg";

    static final String PARCELABLE_CATEGORY_SERVICES = "category_services";
    static final String CATEGORY_ID_MAIN_DOMESTIC_ACTIVITY = "category_id";
    //    private static final String SERVER_URL = "http://192.168.59.1/jemmincl/";
    private static final String SERVER_URL = "http://jemmincleaners.co.ke/";
    private static final String ANDROID_SERVER_URL = SERVER_URL + "backend/includes/android/";
    //    urls
    static final String TESTIMONIALS_URL = ANDROID_SERVER_URL + "testimonials.php";
    static final String ABOUT_URL = ANDROID_SERVER_URL + "about.php";
    static final String SERVICES_URL = ANDROID_SERVER_URL + "services.php";
    static final String CATEGORIES_URL = ANDROID_SERVER_URL + "categories.php";
    static final String SEND_MAIL_URL = ANDROID_SERVER_URL + "send_mail.php";
    static final String SLIDER_URL = ANDROID_SERVER_URL + "slider_images.php";
    private static final String IMAGES_URL = SERVER_URL + "images/";
    static final String SERVICES_IMAGE_URL = IMAGES_URL + "services/";
    static final String TESTIMONIALS_IMAGE_URL = IMAGES_URL + "testimonial/";
    static final String SLIDER_IMAGE_URL = IMAGES_URL + "slider/";
}
