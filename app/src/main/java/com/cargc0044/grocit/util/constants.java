package com.cargc0044.grocit.util;

/**
 * Created by Nguza on 31/01/16.
 */
public class constants {

    public static final String URL_VERIFY_CODE = "verifycode";
    public static final String URL_RESEND_VERIFICATION = "resendverifycode";
    public static final String URL_POST_BUSINESS = "listyourbusiness";


    //api values
    public static final String VALUE_LOGIN_SUCCESS = "Login Success";
    public static final String VALUE_LOGIN_FAILURE = "Login failure; wrong phone number or password";
    public static final String VALUE_UPDATE_SUCCESS = "Update Success";
    public static final String MSG_SIGN_UP_SUCCESS = "Signup Success";
    public static final String MSG_SIGN_UP_FAILURE = "Signup not successful";
    public static final String MSG_SIGN_UP_FAILURE_2 = "Signup not successful, Phone Number already exists.";
    public static final String MSG_CODE_RESEND_SUCCESS = "New verify code generated";
    public static final String MSG_BUSINESS_CREATED = "Business Created";
    public static final String MSG_AD_CREATED = "Advertise Created";
    public static final String MSG_VERIFICATION_SUCCESS = "Verification Success";
    public static final String MSG_PASSWORD_CHANGE_SUCCESS = "Password Successfully Changed";
    public static final String MSG_PASSWORD_CHANGE_SUCCESS_2 = "New password has been sent to phone";
    public static final String MSG_PASSWORD_CHANGE_FAILED = "Old password didn't matched";
    public static final String MSG_PASSWORD_SEND_SUCCESSFUL = "Password Successfully generated";
    public static final String MSG_PASSWORD_SEND_FAILED = "Password could not be generated, Invalid phone number";
    public static final String MSG_RATING_SUCCESSFUL = "Business Successfully Rated";
    public static final String MSG_RATING_UNSUCCESSFUL = "Rating not done! Try again later";

    //json fields

    public static final String JF_ITEM_TYPE = "item_type";
    public static final String JF_DISTRICT = "district";
    public static final String JF_COMPANY_LIST = "companylist";
    public static final String JF_AD_LIST = "addlist";

    public static final String JF_TITLE = "title";
    public static final String JF_ADDRESS = "address";
    public static final String JF_ROOT_ID = "root_id";

    public static final String JF_SORT = "sort";
    public static final String JF_SHOW_IN_FRONT = "showinfront";
    public static final String JF_ADMIN_ID = "admin_id";
    public static final String JF_HOLDING_NO = "holdingno";
    public static final String JF_ROAD_VILLAGE = "roadvill";
    public static final String JF_THANA = "thana";
    public static final String JF_AREA = "area";
    public static final String JF_DISTRICT_ID = "district_id";
    public static final String JF_FACEBOOK = "facebook";
    public static final String JF_TWITTER = "twitter";
    public static final String JF_GPLUS = "gplus";
    public static final String JF_LINKED_IN = "linkedin";
    public static final String JF_LATITUDE = "latitude";
    public static final String JF_LONGITUDE = "longitude";
    public static final String JF_RATING_ARRAY = "Rating";
    public static final String JF_USER_RATING = "rating";
    public static final String JF_RATING = "averagerating";
    //    public static final String JF_RATING_COUNT = "rating_count";
    public static final String JF_RATING_COUNT = "rating_count";
    public static final String JF_HOT_LINE = "hotline";

    public static final String JF_GENDER = "gender";
    public static final String JF_VERIFY_CODE = "verificationcode";
    public static final String JF_NEW_VERIFIED_CODE = "newverificationcode";


    //others
    public static int ITEM_TYPE_COMPANY = 0;
    public static int ITEM_TYPE_AD = 1;

    public static final double DUMMY_LOCATION_LATITUDE = 23.7967599;
    public static final double DUMMY_LOCATION_LONGITUDE = 90.4261766;

    //api url
    public static final String URL_APB_API = "http://quiz8tnow.com/grocit/api/";
    //json fields
    public static final String JF_DATA = "data";
    public static final String JF_ID = "user_id";
    public static final String JF_CONTACT_NUMBER = "phone";
    public static final String CAT_NAME = "category_name";
    public static final String JF_FNAME = "fname";
    public static final String JF_LNAME = "lname";
    public static final String JF_EMAIL = "email";
    public static final String JF_OTHER_NUMBER = "other_contact";
    public static final String JF_PASSWORD = "password";
    public static final String KEY_CATEGORY_ID = "cat_id";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_SHOP_ID = "p_shop";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "p_name";
    public static final String PRODUCT_PRICE = "p_price";
    public static final String PRODUCT_STATUS = "p_status";
    public static final String PRODUCT_DESC = "p_description";
    public static final String PRODUCT_CAT = "p_category";
    public static final String PRODUCT_SUB = "p_subcategory";
    public static final String PRODUCT_SHOP = "p_shop";
    public static final String PRODUCT_BRIEF = "p_highlights";
    public static final String PRODUCT_IMAGE = "p_Image";

    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_IMAGE = "image";
    public static final String SUB_CATEGORY_ID = "id";
    public static final String SUB_CATEGORY_NAME = "sub_category_name";
    public static final String CATEGORY_ID = "category_id";
    public static final String URL_GET_SUB_CATEGORY = "getsubcategory.php";
    public static final String JF_ENTRY = "Entry";
    public static final String URL_GET_CATEGORY = "getcategorylist.php";
    public static final String URL_GET_PRODUCT = "productdetails.php";
    public static final String URL_GET_PRODUCTLIST = "productlist.php";
    public static final String URL_GET_SEARCHRESULT = "searchresult.php";
    public static final String URL_GET_RESULT_LIST_WITH_AD = "getItemListadd";
    public static final String URL_GET_SEARCH_SUGGESTIONS = "getAutocompleteList";
    public static final String URL_GET_SEARCH_LIST_AD = "getSearchListadd";
    public static final String URL_SIGN_IN = "login";
    public static final String URL_PROFILE_UPDATE = "updateprofile.php";
    public static final String URL_SIGN_UP = "Signup";
    public static final String URL_POST_AD = "advertise";
    public static final String URL_GET_DISTRICT = "getDistrictList";
    public static final String URL_GET_AREA = "getAreaList";
    public static final String URL_CHANGE_PASSWORD = "changepassword.php";
    public static final String URL_FORGET_PASSWORD = "forgotpassword";
    public static final String URL_NEW_PASSWORD = "newpassword.php";
    public static final String URL_USER_RATING = "get_user_comments";
    public static final String URL_COMMENT_LIST = "commentlist";
    //api keys
    public static final String KEY_URL = "key";
    public static final String KEY_RESULT_LIMIT = "perpage";
    public static final String KEY_DISTRICT_ID = "districtid";
    public static final String KEY_PAGE = "page";
    public static final String KEY_AREA = "area";
    public static final String KEY_KEYWORD = "keyword";
    public static final String KEY_SEARCH_TERM = "searchTerm";
    public static final String KEY_MOBILE_NUMBER = "cellno";
    public static final String KEY_NAME = "fullname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_COMPANY_NAME = "companyname";
    public static final String KEY_CONTACT_PERSON = "contactperson";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_DISTRICT_2 = "District";
    public static final String KEY_MOBILE = "img_category_mobile";
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_SUCCESS_2 = "Success";
    public static final String KEY_FAILURE = "error";
    public static final String KEY_VERIFICATION = "verifycode";
    public static final String KEY_OLD_PASS = "oldpass";
    public static final String KEY_NEW_PASS = "newpass";
    public static final String KEY_COMPANY_ID = "company_id";
    public static final String KEY_CONTACT = "contact_id";
    public static final String KEY_REVIEW = "review";
    public static final String KEY_RATING = "rating";
    //json fields
    public static final String JF_WEIGHT = "size";
    public static final String JF_BACKGROUND_IMAGE = "background";
    public static final String JF_COLOR_CODE = "colorcode";
    public static final String JF_LARGE_IMAGE_PATH = "largeImagePath";
    public static final String JF_THUMB_IMAGE_PATH = "thumbImagePath";
    public static final String JF_CATEGORY = "Category";
    public static final String JF_ENTRY_TO_CATEGORY = "EntryToCategories";
    public static final String JF_NAME = "fname";
    public static final String JL_NAME = "lname";
    public static final String JF_IMAGE = "image";
    public static final String JF_CONTACT_PHONE = "contactphone";
    public static final String JF_TELEPHONE = "telephone";
    public static final String JF_MOBILE = "img_category_mobile";
    public static final String JF_WEB = "web";
    public static final String JF_TAG_LINE = "tag";
    public static final String JF_DESCRIPTION = "description";
    public static final String JF_DISTRICT_INFO = "District";
    public static final String JF_DISTRICT_TITLE = "title";
    public static final String JF_CATEGORY_ID = "category_id";
    public static final String JF_IMAGES = "Imagefile";
    public static final String JF_REVIEW = "review";
    public static final String JF_DATE = "date";
    public static final String JF_ICON = "icon";
    public static final String JF_AD_URL = "url";
    //preference key
    public static final String USER_FIRST_RUN = "user_first_run";
    //empty value
    public static final String NO_DATA_FOUND = "no_data_found";
    public static final Double NULL_LOCATION = 0.0;
    public static final String NO_IMAGE_FOUND = "no_image_found";
    public static final String INTENT_EXTRA = "extra";
    /*!
   * social page urls
   * change the urls to your desired business url
   */
    public static final String URL_FACEBOOK = "https://www.facebook.com/trinity.swagger";
    public static final String URL_TWITTER = "https://twitter.com/nguzajay";
    public static final String URL_GOOGLE_PLUS = "https://plus.google.com/u/0/115271420704857314494/posts";
    public static final String URL_YOUTUBE = "https://www.facebook.com/audacityit";
    public static final String URL_EMAIL = "grocit123@gmail.com";
    public static final String URL_LINKEDIN = "https://www.linkedin.com/in/nguza-yikona-40245355?trk=nav_responsive_tab_profile";
    public static String MenuAPI = "http://www.dimasword.com/demo/ecommerce/api/get-menu-data-by-category-id.php";
    public static String AccessKey = "12345";
    public static String MenuDetailAPI = "http://www.dimasword.com/demo/ecommerce/api/get-menu-detail.php";
    public static String AdminPageURL = "http://www.dimasword.com/demo/ecommerce/";
    public static String LocalCurrency = "Rs";
    //check flag
    public static boolean isHomeOpened = false;
    public static boolean isResultListFragmentOpened = false;
    public static boolean isSignedUp = false;
    // local database path configuration
    public static String DBPath = "/data/data/com.carg0044.grocit/databases/";

    //! change hotline number here
    public static final String GROCIT_HOTLINE = "+917814122457";
    public static final int MAX_EMS = 10;





}
