package com.yuexun.book_read.control;

/**
 * Created by yuexun on 2018/6/6.
 */

public class DataConstants {

    public final static String TAG = "Book_read";

    public final static String CONNECT_IP = "http://192.168.1.254:5001";
    public final static String API_MAIN = "/api/home/recommend";
    public final static String API_RANK = "/api/rank/book";
    public final static String API_LIBRARY_TITLE = "/api/book/title";
    public final static String API_LIBRARY = "/api/book/list";
    public final static String API_SEARCH_HOT = "/api/hot/search";
    public final static String API_SEARCH = "/api/search/list";
    public final static String API_BOOK_DETAIL = "/api/book/detail";
    public final static String API_BOOK_CHAPTERS = "/api/book/chapter";
    public final static String API_BOOK_CONTENT = "/api/book/read";
    public final static String API_ADD_BOOK_CASE = "/api/collect/book";
    public final static String API_DEL_BOOK_CASE = "/api/delete/book";
    public final static String API_BOOK_CASE = "/api/collect/list";
    public final static String API_LOGIN = "/api/book/login";
    public final static String API_REGISTER = "/api/book/register";

    public final static String SPF_KEY_HISTORY = "HISTORY";
    public final static String SPF_KEY_SESSION = "session";
    public final static String SPF_KEY_HEADERURL = "header";
    public final static String SPF_KEY_USERNAME = "username";

    public final static int PAGE_MODE_SIMULATION = 0;
    public final static int PAGE_MODE_COVER = 1;
    public final static int PAGE_MODE_SLIDE = 2;
    public final static int PAGE_MODE_NONE = 3;

    public final static int BOOK_BG_DEFAULT = 0;
    public final static int BOOK_BG_1 = 1;
    public final static int BOOK_BG_2 = 2;
    public final static int BOOK_BG_3 = 3;
    public final static int BOOK_BG_4 = 4;

    public final static String BOOK_BG_KEY = "bookbg";
    public final static String FONT_TYPE_KEY = "fonttype";
    public final static String FONT_SIZE_KEY = "fontsize";
    public final static String NIGHT_KEY = "night";
    public final static String LIGHT_KEY = "light";
    public final static String SYSTEM_LIGHT_KEY = "systemlight";
    public final static String PAGE_MODE_KEY = "pagemode";


    public final static String FONTTYPE_DEFAULT = "";
    public final static String FONTTYPE_QIHEI = "font/qihei.ttf";
    public final static String FONTTYPE_WAWA = "font/font1.ttf";
    public final static String FONTTYPE_FZXINGHEI = "font/fzxinghei.ttf";
    public final static String FONTTYPE_FZKATONG = "font/fzkatong.ttf";
    public final static String FONTTYPE_BYSONG = "font/bysong.ttf";
}
