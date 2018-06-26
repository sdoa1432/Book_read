package com.yuexun.book_read.view.custom.factory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.util.ContentLengthInputStream;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.SpfControl;
import com.yuexun.book_read.utils.BitmapUtil;
import com.yuexun.book_read.utils.CommonUtil;
import com.yuexun.book_read.view.custom.ContentPageView;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuexun on 2018/6/21.
 */

public class ContentPageFactory {

    private final static String TAG = ContentPageFactory.class.getSimpleName();

    private static ContentPageFactory contentPageFactory;

    private Context context;
    private SpfControl spfControl;
    //页面宽
    private int mWidth;
    //页面高
    private int mHeight;
    //文字字体大小
    private float m_fontSize;
    //时间格式
    private SimpleDateFormat sdf;
    //时间
    private String date;
    //进度格式
    private DecimalFormat df;
    //电池边界宽度
    private float mBorderWidth;
    // 上下与边缘的距离
    private float marginHeight;
    // 左右与边缘的距离
    private float measureMarginWidth;
    // 左右与边缘的距离
    private float marginWidth;
    //状态栏距离底部高度
    private float statusMarginBottom;
    //行间距
    private float lineSpace;
    //字体
    private Typeface typeface;
    //文字画笔
    private TextPaint mPaint;
    //加载画笔
    private Paint waitPaint;
    //文字颜色
    private int m_textColor = Color.rgb(50, 65, 78);
    // 绘制内容的宽
    private float mVisibleHeight;
    // 绘制内容的宽
    private float mVisibleWidth;
    // 每页可以显示的行数
    private int mLineCount;
    // 每行可以显示的字数
    private int mLineShowFontCount;
    // 每页可以显示的总字数(以 中文(全角)空格 为标准)
    private int mPageFontCount;
    // 内容的总页数
    private int mPageCount;
    // 当前显示的页数
    private int mPageindex = 0;
    //电池画笔
    private Paint mBatterryPaint;
    //电池字体大小
    private float mBatterryFontSize;
    //背景图片
    private Bitmap m_book_bg = null;
    //电池信息接收Intent
    private Intent batteryInfoIntent;
    //电池电量百分比
    private float mBatteryPercentage;
    //电池外边框
    private RectF rect1 = new RectF();
    //电池内边框
    private RectF rect2 = new RectF();
    //当前是否为第一页
    private boolean m_isfirstPage;
    //当前是否为最后一页
    private boolean m_islastPage;
    //书本widget
    private ContentPageView mContentPageView;
    //书本名字
    private String bookName = "";
    //章节名字
    private String chapterName = "";
    //当前电量
    private int level = 0;

    private static Status mStatus = Status.LOADING;

    public enum Status {
        LOADING,
        FINISH,
        FAIL,
    }

    public static synchronized ContentPageFactory getInstance() {
        return contentPageFactory;
    }

    public static synchronized ContentPageFactory createContentPageFactory(Context context) {
        if (contentPageFactory == null) {
            contentPageFactory = new ContentPageFactory(context);
        }
        return contentPageFactory;
    }

    private ContentPageFactory(Context context) {
        this.context = context.getApplicationContext();
        spfControl = SpfControl.getInstance();
        mContentPages = new ArrayList<>();
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mWidth = metric.widthPixels;
        mHeight = metric.heightPixels;

        sdf = new SimpleDateFormat("HH:mm");//HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(new java.util.Date());
        df = new DecimalFormat("#0.0");

        marginWidth = context.getResources().getDimension(R.dimen.readingMarginWidth);
        marginHeight = context.getResources().getDimension(R.dimen.readingMarginHeight);
        statusMarginBottom = context.getResources().getDimension(R.dimen.reading_status_margin_bottom);
        lineSpace = context.getResources().getDimension(R.dimen.reading_line_spacing);
//        paragraphSpace = context.getResources().getDimension(R.dimen.reading_paragraph_spacing);
        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - marginHeight * 2;

        typeface = spfControl.getTypeface();
        m_fontSize = spfControl.getFontSize();
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);// 画笔
        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        mPaint.setTextSize(m_fontSize);// 字体大小
        mPaint.setColor(m_textColor);// 字体颜色
        mPaint.setTypeface(typeface);
        mPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        waitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        waitPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        waitPaint.setTextSize(context.getResources().getDimension(R.dimen.reading_max_text_size));// 字体大小
        waitPaint.setColor(m_textColor);// 字体颜色
        waitPaint.setTypeface(typeface);
        waitPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果
        calculateLineCount();
        countLineShowFont();

        mBorderWidth = context.getResources().getDimension(R.dimen.reading_board_battery_border_width);
        mBatterryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBatterryFontSize = CommonUtil.sp2px(context, 12);
        mBatterryPaint.setTextSize(mBatterryFontSize);
        mBatterryPaint.setTypeface(typeface);
        mBatterryPaint.setTextAlign(Paint.Align.LEFT);
        mBatterryPaint.setColor(m_textColor);
        batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));//注册广播,随时获取到电池电量信息

        initBg(spfControl.getDayOrNight());
        measureMarginWidth();
    }


    /**
     * 显示页面
     */
    public void show() {
        //清空数据
        mPageindex = 0;
        initBg(spfControl.getDayOrNight());
//        mStatus = Status.LOADING;
//        drawStatus(mContentPageView.getCurPage());
//        drawStatus(mContentPageView.getNextPage());
        if (mContentPageView != null) {
            currentPage();
        }
    }

    /**
     * 绘制加载中以及加载失败的页面
     *
     * @param bitmap
     */

    public void drawStatus(Bitmap bitmap) {
        String status = "";
        switch (mStatus) {
            case LOADING:
                status = "正在打开书本...";
                break;
            case FAIL:
                status = "打开书本失败！";
                break;
        }

        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        waitPaint.setColor(getTextColor());
        waitPaint.setTextAlign(Paint.Align.CENTER);

        Rect targetRect = new Rect(0, 0, mWidth, mHeight);
        Paint.FontMetricsInt fontMetrics = waitPaint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        waitPaint.setTextAlign(Paint.Align.CENTER);
        c.drawText(status, targetRect.centerX(), baseline, waitPaint);
        mContentPageView.postInvalidate();
    }

    /**
     * 绘制主体显示部分
     *
     * @param bitmap    传入的画布
     * @param pageindex 绘制的页码 从0开始
     */

    public void onDraw(Bitmap bitmap, int pageindex) {
        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);

        //画进度及时间
        mBatterryPaint.setColor(getTextColor());
        int dateWith = (int) (mBatterryPaint.measureText(date) + mBorderWidth);//时间宽度
        c.drawText(date, marginWidth, mHeight - statusMarginBottom, mBatterryPaint);
        // 画电池
        level = batteryInfoIntent.getIntExtra("level", 0);
        int scale = batteryInfoIntent.getIntExtra("scale", 100);
        mBatteryPercentage = (float) level / scale;
        float rect1Left = marginWidth + dateWith + statusMarginBottom;//电池外框left位置
        //画电池外框
        float width = CommonUtil.convertDpToPixel(context, 20) - mBorderWidth;
        float height = CommonUtil.convertDpToPixel(context, 10);
        rect1.set(rect1Left, mHeight - height - statusMarginBottom, rect1Left + width, mHeight - statusMarginBottom);
        rect2.set(rect1Left + mBorderWidth, mHeight - height + mBorderWidth - statusMarginBottom, rect1Left + width - mBorderWidth, mHeight - mBorderWidth - statusMarginBottom);
        c.save(Canvas.ALL_SAVE_FLAG);
        c.clipRect(rect2, Region.Op.DIFFERENCE);
        c.drawRect(rect1, mBatterryPaint);
        c.restore();
        //画电量部分
        rect2.left += mBorderWidth;
        rect2.right -= mBorderWidth;
        rect2.right = rect2.left + rect2.width() * mBatteryPercentage;
        rect2.top += mBorderWidth;
        rect2.bottom -= mBorderWidth;
        c.drawRect(rect2, mBatterryPaint);
        //画电池头
        int poleHeight = (int) CommonUtil.convertDpToPixel(context, 10) / 2;
        rect2.left = rect1.right;
        rect2.top = rect2.top + poleHeight / 4;
        rect2.right = rect1.right + mBorderWidth;
        rect2.bottom = rect2.bottom - poleHeight / 4;
        c.drawRect(rect2, mBatterryPaint);
        //画书名
        c.drawText(CommonUtil.subString(bookName, 8) + " " + CommonUtil.subString(chapterName, 18), marginWidth, statusMarginBottom + mBatterryFontSize, mBatterryPaint);

        mPaint.setTextSize(getFontSize());
        mPaint.setColor(getTextColor());
        //逐行绘制文章内容
        if (mContentPages.size() > 0) {
            float y = marginHeight;
            for (String strLine : mContentPages.get(pageindex)) {
                y += m_fontSize + lineSpace;
                c.drawText(strLine, measureMarginWidth, y, mPaint);
            }
        }
        //使用自动换行绘制页面内容
//        if (pagecache.size() > 0) {
//            StaticLayout layout = new StaticLayout(pagecache.get(pageindex), mPaint, (int) mVisibleWidth, Layout.Alignment.ALIGN_NORMAL, 0.9f, lineSpace, true);
//            //默认在0，0点绘制，需要移动画布
//            c.translate(measureMarginWidth, marginHeight);
//            layout.draw(c);
//        }


        mContentPageView.postInvalidate();
    }

    //改变字体大小
    public void changeFontSize(int fontSize) {
        this.m_fontSize = fontSize;
        mPaint.setTextSize(m_fontSize);
        calculateLineCount();
        measureMarginWidth();
        mesurepage(false);
        currentPage();
    }

    //改变字体
    public void changeTypeface(Typeface typeface) {
        this.typeface = typeface;
        mPaint.setTypeface(typeface);
        mBatterryPaint.setTypeface(typeface);
        calculateLineCount();
        measureMarginWidth();
        mesurepage(false);
        currentPage();
    }

    private void calculateLineCount() {
        mLineCount = (int) (mVisibleHeight / (m_fontSize + lineSpace));// 可显示的行数
    }

    //改变背景
    public void changeBookBg(int type) {
        setBookBg(type);
        currentPage();
    }


    //绘制当前页面
    public void currentPage() {
        onDraw(mContentPageView.getNextPage(), mPageindex);
    }

    //更新电量
    public void updateBattery(int mLevel) {
        if (mContentPageView != null) {
            if (level != mLevel) {
                level = mLevel;
                currentPage();
            }
        }
    }

    public void updateTime() {
        if (mContentPageView != null) {
            String mDate = sdf.format(new java.util.Date());
            if (date != mDate) {
                date = mDate;
                currentPage();
            }
        }
    }

    public void setmContentPageView(ContentPageView mContentPageView) {
        this.mContentPageView = mContentPageView;
    }

    public void clear() {
        bookName = "";
    }

    //向前翻页
    public void prePage() {
        if (mPageindex <= 0) {
            Log.e(TAG, "当前是第一页");
            if (!m_isfirstPage) {
                Toast.makeText(context, "当前是第一页", Toast.LENGTH_SHORT).show();
            }
            m_isfirstPage = true;
            return;
        } else {
            m_isfirstPage = false;
        }
        cancepageindex = mPageindex;
        onDraw(mContentPageView.getCurPage(), mPageindex);
        mPageindex--;
        onDraw(mContentPageView.getNextPage(), mPageindex);
    }

    //向后翻页
    public void nextPage() {
        if (mPageindex >= mPageCount - 1) {
            Log.e(TAG, "已经是最后一页了");
            if (!m_islastPage) {
                Toast.makeText(context, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            }
            m_islastPage = true;
            return;
        } else {
            m_islastPage = false;
        }
        cancepageindex = mPageindex;
        onDraw(mContentPageView.getCurPage(), mPageindex);
        mPageindex++;
        onDraw(mContentPageView.getNextPage(), mPageindex);
        Log.e("nextPage", "nextPagenext");
    }

    int cancepageindex;

    //取消翻页
    public void cancelPage() {
        mPageindex = cancepageindex;
    }

    private String content;

    public void setContent(String content, boolean islast) {
        this.content = content;
        mPageindex = 0;
        mesurepage(islast);
        currentPage();
    }

    private List<List<String>> mContentPages;
//    private List<String> pagecache = new ArrayList<>();

    public void mesurepage(boolean islast) {
        mContentPages.clear();
        //自动换行绘制方法分割页面内容
//        pagecache.clear();
//        mPageFontCount = mLineCount * mLineShowFontCount;
//        int index = 0;
//        String sub;
//        Log.i("yc.zhang", content.length() + "  is lenght");
//        while (index < content.length()) {
//            Log.i("yc.zhang", index + "  is index");
//            if (content.length() >= index + mPageFontCount) {
//                sub = content.substring(index, index + mPageFontCount);
//                pagecache.add(sub);
//                index += mPageFontCount;
//            } else {
//                sub = content.substring(index, content.length() - 1);
//                pagecache.add(sub);
//                break;
//            }
//        }
        //逐行绘制方法分割页面内容
        StringBuilder stringBuilder = new StringBuilder();
        List<String> contentPage = new ArrayList<>();
        char[] chars = content.toCharArray();
        int offset = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != '\n') {
                stringBuilder.append(chars[i]);
                offset++;
                if (offset == mLineShowFontCount) {
                    Log.i("yc.zhang", "current line --> " + stringBuilder.toString());
                    contentPage.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    offset = 0;
                }
            } else {
                if (chars[i - 1] != '\n') {
                    if (offset == 0)
                        continue;
                    stringBuilder.append(chars[i]);
                    Log.i("yc.zhang", "current line --> " + stringBuilder.toString());
                    contentPage.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    offset = 0;
                } else {
                    if (i != chars.length - 1)
                        continue;
                }
            }
            if (contentPage.size() == mLineCount || i == chars.length - 1) {
                Log.i("yc.zhang", "current line count--> " + contentPage.size() + " current index --> " + i);
                if (contentPage.size() == 0)
                    continue;
                mContentPages.add(contentPage);
                contentPage = new ArrayList<>();
            }
        }
        mPageCount = mContentPages.size();
        if (islast)
            mPageindex = mPageCount - 1;
    }

    //
    private void measureMarginWidth() {
        float wordWidth = mPaint.measureText("\u3000");
        float width = mVisibleWidth % wordWidth;
        measureMarginWidth = marginWidth + width / 2;
    }

    //
    private void countLineShowFont() {
        float wordWidth = mPaint.measureText("\u3000");
        mLineShowFontCount = (int) (mVisibleWidth / wordWidth);
    }

    //初始化背景
    private void initBg(Boolean isNight) {
        if (isNight) {
            //设置背景
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.BLACK);
            setBgBitmap(bitmap);
            //设置字体颜色
            setM_textColor(Color.rgb(128, 128, 128));
            setBookPageBg(Color.BLACK);
        } else {
            //设置背景
            setBookBg(spfControl.getBookBgType());
        }
    }


    //设置页面的背景
    public void setBookBg(int type) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int color = 0;
        switch (type) {
            case DataConstants.BOOK_BG_DEFAULT:
                canvas = null;
                bitmap.recycle();
                if (getBgBitmap() != null) {
                    getBgBitmap().recycle();
                }
                bitmap = BitmapUtil.decodeSampledBitmapFromResource(
                        context.getResources(), R.mipmap.paper, mWidth, mHeight);
                color = context.getResources().getColor(R.color.read_font_default);
                setBookPageBg(context.getResources().getColor(R.color.read_bg_default));
                break;
            case DataConstants.BOOK_BG_1:
                canvas.drawColor(context.getResources().getColor(R.color.read_bg_1));
                color = context.getResources().getColor(R.color.read_font_1);
                setBookPageBg(context.getResources().getColor(R.color.read_bg_1));
                break;
            case DataConstants.BOOK_BG_2:
                canvas.drawColor(context.getResources().getColor(R.color.read_bg_2));
                color = context.getResources().getColor(R.color.read_font_2);
                setBookPageBg(context.getResources().getColor(R.color.read_bg_2));
                break;
            case DataConstants.BOOK_BG_3:
                canvas.drawColor(context.getResources().getColor(R.color.read_bg_3));
                color = context.getResources().getColor(R.color.read_font_3);
                if (mContentPageView != null) {
                    mContentPageView.setBgColor(context.getResources().getColor(R.color.read_bg_3));
                }
                break;
            case DataConstants.BOOK_BG_4:
                canvas.drawColor(context.getResources().getColor(R.color.read_bg_4));
                color = context.getResources().getColor(R.color.read_font_4);
                setBookPageBg(context.getResources().getColor(R.color.read_bg_4));
                break;
        }

        setBgBitmap(bitmap);
        //设置字体颜色
        setM_textColor(color);
    }

    public void setBookPageBg(int color) {
        if (mContentPageView != null) {
            mContentPageView.setBgColor(color);
        }
    }

    //设置页面背景
    public void setBgBitmap(Bitmap BG) {
        m_book_bg = BG;
    }

    //获取页面背景
    public Bitmap getBgBitmap() {
        return m_book_bg;
    }

    //设置文字颜色
    public void setM_textColor(int m_textColor) {
        this.m_textColor = m_textColor;
    }

    //获取文字颜色
    public int getTextColor() {
        return this.m_textColor;
    }

    //获取文字大小
    public float getFontSize() {
        return this.m_fontSize;
    }

    //是否是第一页
    public boolean isfirstPage() {
        return m_isfirstPage;
    }

    //是否是最后一页
    public boolean islastPage() {
        return m_islastPage;
    }

    //传入书名
    public void setBookName(String bookname) {
        this.bookName = bookname;
    }

    //传入章节名
    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    //设置日间或者夜间模式
    public void setDayOrNight(Boolean isNgiht) {
        initBg(isNgiht);
        currentPage();
    }
}
