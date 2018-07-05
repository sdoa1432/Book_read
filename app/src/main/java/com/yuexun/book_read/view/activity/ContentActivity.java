package com.yuexun.book_read.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yuexun.book_read.MyApplication;
import com.yuexun.book_read.R;
import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.control.SpfControl;
import com.yuexun.book_read.utils.BrightnessUtil;
import com.yuexun.book_read.view.custom.ContentPageView;
import com.yuexun.book_read.view.custom.factory.ContentPageFactory;
import com.yuexun.book_read.view.dialog.PageModeDialog;
import com.yuexun.book_read.view.dialog.SettingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


public class ContentActivity extends AppCompatActivity {

    @BindView(R.id.content)
    ContentPageView contentPageView;
    //    @BindView(R.id.btn_return)
//    ImageButton btn_return;
//    @BindView(R.id.ll_top)
//    LinearLayout ll_top;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.tv_pre)
    TextView tv_pre;
    @BindView(R.id.sb_progress)
    SeekBar sb_progress;
    @BindView(R.id.tv_next)
    TextView tv_next;
    //    @BindView(R.id.tv_directory)
//    TextView tv_directory;
    @BindView(R.id.tv_dayornight)
    TextView tv_dayornight;
    @BindView(R.id.tv_pagemode)
    TextView tv_pagemode;
    @BindView(R.id.tv_setting)
    TextView tv_setting;
    @BindView(R.id.bookpop_bottom)
    LinearLayout bookpop_bottom;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    private int mBookId;
    private int mchapterId;
    private ContentPageFactory contentPageFactory;
    //    private ContentPageView contentPageView;
//    private Toolbar toolbar;
//    private AppBarLayout appBarLayout;
    private String mchaptertitle;
    private String bookname;
    private boolean next = false;
    private String contentString;
    private SpfControl spfControl;
    // popwindow是否显示
    private Boolean isShow = false;
    private SettingDialog mSettingDialog;
    private PageModeDialog mPageModeDialog;
    private Boolean mDayOrNight;
//    private TextView content, title;
//    private Button result_previous, result_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);

        mBookId = getIntent().getIntExtra("bookid", 0);
        mchapterId = getIntent().getIntExtra("chapterId", 0);
        bookname = getIntent().getStringExtra("bookName");

        spfControl = SpfControl.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (!spfControl.isSystemLight()) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = spfControl.getLight();
            getWindow().setAttributes(lp);
        }

//        contentPageView = findViewById(R.id.content);
//        toolbar = findViewById(R.id.toolbar);
//        appbar = findViewById(R.id.appbar);

        contentPageFactory = ContentPageFactory.getInstance();
        mSettingDialog = new SettingDialog(this);
        mPageModeDialog = new PageModeDialog(this);

        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);

        //隐藏
        hideSystemUI();
        initDayOrNight();
        contentPageFactory.setBookName(bookname);
        contentPageView.setPageMode(spfControl.getPageMode());
        contentPageFactory.setmContentPageView(contentPageView);

        initListenter();

//        testcontent();

//        title = findViewById(R.id.chapter_title);
//        content = findViewById(R.id.content);
//        result_next = findViewById(R.id.result_next);
//        result_previous = findViewById(R.id.result_previous);
//        result_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (next)
//                    initcontent(++mchapterId);
//            }
//        });
//        result_previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mchapterId > 1)
//                    initcontent(--mchapterId);
//            }
//        });

    }

    private void testcontent() {
        contentString = "“老婆，这一次恐怕会很难哦。”\n" +
                "    “傻老公，我们这一路行来，有哪一次是轻松的吗？”金发女精灵细腻修长的手掌按在男子的手背上，她笑得极美，眼里的温柔犹如水波一般。\n" +
                "    “也，是哦。”\n" +
                "    宗门毕竟还是底子比较薄的，虽然近些年来渐渐有了一些浮财，但要提升宗门福利、弟子待遇、大力投入教育，购买第二座巫塔、建立自己的巫塔配装与改造基地，在这些目标下，海量的魔石就像是开闸放水一样倾斜而出，一涌而落。\n" +
                "    并且，毕竟不能让宗门财政长年走钢丝，必须的库存储备还是要有的，因此，尽管这些年从李静玄到朱鹏乃至于方方面面，中华武士会真的是没少捞钱，但钱永远是不够花的，四面铺开如潺潺流水。\n" +
                "    私家的谍影基地内，许多设备依然用得是罪狱之手淘汰下来的二手货，当然是性能比较好的，若非朱鹏的关系，中华武士会别说这种档次的二手货，九手货都轮不到购买。\n" +
                "    伴随着磅礴魔力的涌动，核心区域内两座源质灵柩缓缓的闭合起来，朱鹏注视着夏洛特，金发的女精灵看着自己的男人：\n" +
                "    “别担心，我相信你，你一定会成功，一定会拿到自己想要的。”\n" +
                "    （即便你输了，我也会始终伴你左右……）\n" +
                "    厚重的机箱闭合，阻断了两人的视线，四周有大量的培养液从四面涌入进来，千百条金属导管犹如活蛇般附着上来，朱鹏要配合着它们运劲撕裂开自己的肌肉，不然无法进入源质休眠状态。\n" +
                "    （炎黄，华夏，一路行来，终究意难平……幼年，青山孤独院，也许只有那些最伶仃孤苦的孩子，才更知道相比小家，身后这个大家的宝贵之处，生身十年无父母，外无战火，内无饥馑，国家无负我，此生何偿报国恩？）\n" +
                "    在意识海的最深处，朱鹏将暗金色的卡牌缓缓翻转，时空的怒潮，暴乱的空轨，一条微弱的光之线，在他的眼前出现。\n" +
                "    虚拟灵魂复克完毕，异位面超远程投送启动。\n" +
                "    轰隆，伴随着一轮撕裂炸开的宇宙黑洞，一道深红，一道亮金隐隐的两颗陨石自中穿出高速飞行。\n" +
                "    与此同时，在极遥远的宇宙距离之外，近乎于宙空彼端，一座繁华鼎盛的世界内，一个胖胖的小男孩大力得挥舞了一下蒲扇，那般萌萌且憨态可掬的模样，把家里所有的人都逗笑了。\n" +
                "    然而在亚洲一只蝴蝶轻轻得煽动翅膀，却令北大西洋上有毁天灭地般的风暴汇聚成型，肆虐咆哮。\n" +
                "    在暗金色顶级的气运之力作用下，这个世界里年幼孩童用力得挥舞了一下蒲扇，外宇宙空间经过连锁的多米诺骨牌效应，刮起了笼罩整个星河的宇宙风暴。朱鹏与夏洛特可以说是刚刚从巫师世界宇宙空间钻出来，就一头扎入到了这疯狂肆虐的宇宙天灾中。\n" +
                "    （通过正常的方法甚至都无法探索到那个世界吗？就如同大航海时代，在技术条件本身不足的情况下，探索新大陆的超远距离航行本身甚至需要借助暴风天来加速航行？）无论是自然还是气运连锁产生出来的宇宙天灾，在杀伤破坏力上终究无法与卡萨的超阶禁咒相提并论，这就好像正常的火山爆发怎么也不可能像朱鹏的末日浩劫般杀人无算一样。\n" +
                "    也许声势规模会更加庞大，但在力量的凝聚上，四周的宇宙风暴狂暴而紊乱，存在着大量的空隙可以穿梭甚至借力。\n" +
                "    朱鹏与夏洛特都是最顶级的老牌谍影，两人的驾驭能力在巫师世界众多谍影巫师当中都是最顶尖的，因此，在四周暴虐的宇宙风暴当中，他们两人真的是乘风破浪，借势前行。\n" +
                "    如果是普通的谍影巫师，面对这种情况是根本不敢也绝不会往风暴里面钻的，但朱鹏燃烧命运之瞳中可以隐隐看到一缕金色的纹路在扭曲变幻，他延着此线而行，别说是宇宙风暴，即便是星球爆炸黑洞衍生，朱鹏也会咬牙撞去，他等这个机会已经等得太久太久了。\n" +
                "    而夏洛特是知道朱鹏的命运异能的，她更是心性坚韧之人，这次出手就是做好了分魂泯灭，受重伤返回灵柩的心理准备了，凭借着卓绝出众的驾驭技术，夏洛特于风暴中紧紧跟随着朱鹏，两者一前一后快速的穿梭前行。\n" +
                "    （以这样的能量消耗速率，恐怕还远远未到那个世界，源质能量就已经消耗一空了。）\n" +
                "    “启动源质与灵魂并损模式……”这种把灵魂直接暴露于外宇宙空间的做法，朱鹏只有在自己当年第一次做谍影时才用过一次，是不到万不得已时根本就没人想用的疯狂手段。\n" +
                "    “警告，并损模式会极大损伤您的意识与记忆，虽然以虚拟灵魂投放并不会波及受到源质灵柩保护的本体，但有可能导致您降临后变成一个白痴，甚至导致直接死亡等更加严重后果。”系统惯常的提醒一句，然后被朱鹏确认执行，伴随着谍影的指令，包裹着他恍若暗红色岩浆般的源质更加的收缩，在极大节省消耗的同时，同时将那脆弱不堪的灵魂毫无遮掩的展露在宇宙暴风当中，陨石继续高速飞行。\n" +
                "    看到了朱鹏的做法，下一刻夏洛特也选择了并损模式，半神巫师做谍影有一个好处，那就是在并损模式之下，他们的灵魂厚度与坚韧性是远远超过传奇谍影巫师的，换而言之就是当柴烧的时候也能烧得相对久一些。\n" +
                "    意识海深处，那庞大奢华的记忆宫殿/灵魂图书馆，它在灵魂并损的那一刻四面开始龟裂，一道道裂纹蔓延开来，最后熊熊燃烧的深红色火焰喷涌进来，焚灭一切。\n" +
                "    朱鹏在大型图书馆内奔跑，他不断躲避着龟裂的墙壁与四周所见的火焰，他顺着螺旋回廊奔跑，脚下的楼梯甚至都在飞快的毁灭崩碎，可怕的岩浆涌入进来，就恍若有神明将一座严丝合缝的封闭图书馆扔入了燃烧的活火山里。\n" +
                "    “呼，真是没有想到，真是没有想到，拼命这么多年，今时今日却还是要拼命死斗，不拼就没命。”进入一核心的房间，反手推关上门，朱鹏长长舒了一口气。\n" +
                "    这里是一处立满书柜，摆放满种种藏书的房间密室，在不远处的长桌上有已经倒好的金红色酒浆，处于被翻开状态的命运法典摆放在桌面上，这是朱鹏灵魂最核心的区域，多年以来的记忆与知识形成厚厚的壁垒保护着他。\n" +
                "    当然，反过来说逃到这里后朱鹏也无处可逃了，当这里也被火焰吞噬时，就说明源质能量与灵魂之力同时耗尽，这缕虚拟灵魂将泯灭消亡。\n";
        mchaptertitle = "第一章：我试手，补天之缺！";
        contentPageFactory.setBookName("黑巫师朱鹏");
        contentPageFactory.setChapterName(mchaptertitle);
//        contentPageFactory.setContent(contentString);
    }

    private void initListenter() {

        mPageModeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mPageModeDialog.setPageModeListener(new PageModeDialog.PageModeListener() {
            @Override
            public void changePageMode(int pageMode) {
                contentPageView.setPageMode(pageMode);
            }
        });

        mSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mSettingDialog.setSettingListener(new SettingDialog.SettingListener() {
            @Override
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if (!isSystem) {
                    BrightnessUtil.setBrightness(ContentActivity.this, brightness);
                } else {
                    int bh = BrightnessUtil.getScreenBrightness(ContentActivity.this);
                    BrightnessUtil.setBrightness(ContentActivity.this, bh);
                }
            }

            @Override
            public void changeFontSize(int fontSize) {
                contentPageFactory.changeFontSize(fontSize);
            }

            @Override
            public void changeTypeFace(Typeface typeface) {
                contentPageFactory.changeTypeface(typeface);
            }

            @Override
            public void changeBookBg(int type) {
                contentPageFactory.changeBookBg(type);
            }
        });

        contentPageView.setTouchListener(new ContentPageView.TouchListener() {
            @Override
            public void center() {
                if (isShow) {
                    hideReadSetting();
                } else {
                    showReadSetting();
                }
            }

            @Override
            public Boolean prePage() {
                if (isShow) {
                    return false;
                }
                contentPageFactory.prePage();
                if (contentPageFactory.isfirstPage()) {
                    if (mchapterId != 1) {
                        initcontent(--mchapterId, true);
                    }
//                    else {
//                        Toast.makeText(MyApplication.getContext(), "当前已是第一章", Toast.LENGTH_SHORT).show();
//                    }
                    return false;
                }
                return true;
            }

            @Override
            public Boolean nextPage() {
                Log.e("setTouchListener", "nextPage");
                if (isShow) {
                    return false;
                }
                contentPageFactory.nextPage();
                if (contentPageFactory.islastPage()) {
                    if (next) {
                        initcontent(++mchapterId, false);
                    }
//                    else {
//                        Toast.makeText(MyApplication.getContext(), "当前已是最后一章", Toast.LENGTH_SHORT).show();
//                    }
                    return false;
                }
                return true;
            }

            @Override
            public void cancel() {
                contentPageFactory.cancelPage();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isShow) {
            hideSystemUI();
        }
        initcontent(mchapterId, false);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentPageFactory.clear();
        unregisterReceiver(myReceiver);
        contentPageView = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSettingDialog.isShowing()) {
                mSettingDialog.hide();
                return true;
            }
            if (mPageModeDialog.isShowing()) {
                mPageModeDialog.hide();
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showReadSetting() {
        isShow = true;
        rl_progress.setVisibility(View.GONE);

        showSystemUI();
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
        rl_bottom.startAnimation(topAnim);
        rl_bottom.setVisibility(View.VISIBLE);
    }

    private void hideReadSetting() {
        isShow = false;
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit);
        if (rl_bottom.getVisibility() == View.VISIBLE) {
            rl_bottom.startAnimation(topAnim);
        }
        rl_bottom.setVisibility(View.GONE);
        hideSystemUI();
    }


    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private boolean getDataStatus = false;

    private void initcontent(int chapterId, final boolean islast) {
        if (getDataStatus)
            return;
        getDataStatus = true;
        OkHttpUtils.getInstance().cancelTag(DataConstants.API_BOOK_CONTENT);
        OkHttpUtils.post()
                .url(DataConstants.CONNECT_IP + DataConstants.API_BOOK_CONTENT)
                .addParams("bookId", mBookId + "")
                .addParams("chapterId", chapterId + "")
                .addParams("session", SpfControl.getInstance().getString(DataConstants.SPF_KEY_SESSION))
                .tag(DataConstants.API_BOOK_CONTENT)
                .build()
                .connTimeOut(20 * 1000)
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        Log.i("yc.zhang", jsonObject.opt("next").toString());
                        if (jsonObject.optInt("code", 0) == 200) {
                            contentString = jsonObject.getJSONObject("data").optString("text");
                            mchaptertitle = jsonObject.getJSONObject("data").optString("chapterTitle");
                            next = jsonObject.optBoolean("next", false);
                            contentPageFactory.setChapterName(mchaptertitle);
                            contentPageFactory.setContent(Html.fromHtml(contentString).toString(), islast);
                        }
                        getDataStatus = false;
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        contentPageFactory.setContent("未获取到本章内容，请检查网络后重试", islast);
                        getDataStatus = false;
                    }

                    @Override
                    public void onResponse(Call call, Object o) {
                        getDataStatus = false;
                    }
                });
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                Log.e(DataConstants.TAG, Intent.ACTION_BATTERY_CHANGED);
                int level = intent.getIntExtra("level", 0);
                contentPageFactory.updateBattery(level);
            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                Log.e(DataConstants.TAG, Intent.ACTION_TIME_TICK);
                contentPageFactory.updateTime();
            }
        }
    };

    @OnClick({R.id.tv_progress, R.id.rl_progress, R.id.tv_pre, R.id.sb_progress, R.id.tv_next, R.id.tv_dayornight, R.id.tv_pagemode, R.id.tv_setting, R.id.bookpop_bottom, R.id.rl_bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pre:
                //获取上一章内容
                if (mchapterId != 1) {
                    initcontent(--mchapterId, true);
                }
//                else {
//                    Toast.makeText(this, "当前已是第一章", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.tv_next:
                //获取下一章内容
                if (next) {
                    initcontent(++mchapterId, false);
                }
//                else {
//                    Toast.makeText(this, "当前已是最后一章", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.tv_dayornight:
                changeDayOrNight();
                break;
            case R.id.tv_pagemode:
                hideReadSetting();
                mPageModeDialog.show();
                break;
            case R.id.tv_setting:
                hideReadSetting();
                mSettingDialog.show();
                break;
            case R.id.bookpop_bottom:
                break;
            case R.id.rl_bottom:
                break;
        }
    }

    //改变显示模式
    public void changeDayOrNight() {
        if (mDayOrNight) {
            mDayOrNight = false;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        } else {
            mDayOrNight = true;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        }
        spfControl.setDayOrNight(mDayOrNight);
        contentPageFactory.setDayOrNight(mDayOrNight);
    }


    public void initDayOrNight() {
        mDayOrNight = spfControl.getDayOrNight();
        if (mDayOrNight) {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        } else {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        }
    }

}
