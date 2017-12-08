package test.hk.com.KkDriving.ui;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.hk.com.KkDriving.R;

import test.hk.com.KkDriving.been.TestData;
import test.hk.com.KkDriving.been.Url;

import test.hk.com.KkDriving.db.DbHelper;
import test.hk.com.KkDriving.http.Requstes;
import test.hk.com.KkDriving.tool.ImageLoaderApplication;
import test.hk.com.KkDriving.ui.adapter.myfragmentAdapter;
import test.hk.com.KkDriving.ui.fragment.MyFragment;


public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    //上次按下返回键的系统时间
    private long lastBackTime = 0;
    //当前按下返回键的系统时间
    private long currentBackTime = 0;
    public static ViewPager mViewPager;
    private Toolbar mToolbar;
    public static TextView mcount2_tv, mcount1_tv, mRight_tv, mMistake_tv;
    public static List<TestData> mlistdata = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private myfragmentAdapter mAdapter;
    private ImageLoaderApplication application = (ImageLoaderApplication) getApplication();
    Url mUrl;
    String imgurl, itme1, itme2, itme3, itme4, question, explains;
    int id;
   private  MyFragment fragment;
    private int answer;

    private ImageView mBack;
    private SQLiteDatabase dbread;
    private TextView error_tv;
    private ImageView error_iv;
    private RotateAnimation animation;
    private LinearLayout errorView;
//    private  Listener mListenter;
//    public interface Listener
//    {
//        void send(String s);
//    }
//    public void setFlag(Listener listtenter){
//        this.mListenter = listtenter;
//    }

    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mlistdata = (List<TestData>) msg.obj;
            setfragment(mlistdata);
            if (mlistdata.size() > 0) {
                loading(false);
            }
        }
    };

    private void setfragment(List<TestData> listdata) {
        for (int i = 0; i < listdata.size(); i++) {
            question = mlistdata.get(i).getQuestion();
            imgurl = mlistdata.get(i).getImgurl();
            itme1 = mlistdata.get(i).getItem1();
            itme2 = mlistdata.get(i).getItem2();
            itme3 = mlistdata.get(i).getItem3();
            itme4 = mlistdata.get(i).getItem4();
            answer = mlistdata.get(i).getAnswer();
            explains = mlistdata.get(i).getExplains();
            fragment = new MyFragment(this, question, imgurl, itme1, itme2, itme3, itme4, answer, explains, i, mAdapter,listdata.size());
            mFragments.add(fragment);
        }


        mAdapter = new myfragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mcount2_tv.setText(mFragments.size() + "");
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mcount1_tv.setText(position + 1 + "");
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//       mViewPager.setOffscreenPageLimit(1);
//        设置预加载页数


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        DbHelper dbHelper = new DbHelper(this);
        dbHelper.getWritableDatabase().delete("right_table", null, null);
        dbHelper.getWritableDatabase().delete("mistake_table", null, null);
        mUrl = new Url(this);
        Requstes requstes = new Requstes();
        requstes.getJson(mUrl.url, this, mHandler);
//        loading(true);
    }

    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mcount2_tv = (TextView) findViewById(R.id.id_count2_tv);
        mcount1_tv = (TextView) findViewById(R.id.id_count1_tv);

        mBack = (ImageView) findViewById(R.id.id_back);
        mBack.setOnClickListener(this);
        errorView = (LinearLayout) findViewById(R.id.errorView);
        error_iv = (ImageView) findViewById(R.id.error_iv);
        error_tv = (TextView) findViewById(R.id.error_tv);

//        mLianTi = (TextView) findViewById(R.id.id_LT);
        mRight_tv = (TextView) findViewById(R.id.id_right_tv);
        mMistake_tv = (TextView) findViewById(R.id.id_wrong_tv);

//        mLianTi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                showExitDialog();
//                startActivity(new Intent(TestActivity.this, SelectActivity.class));
//                finish();
                break;
//            case R.id.id_LT:
//                Log.d("select","点击id_lT");
//                Intent intent = new Intent();
//                intent.setAction("Broadcast_select");
//                intent.putExtra("select",true);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//                break;
//            case R.id.id_BT:
//                Log.d("select","点击id_BT");
//                Intent intent2 = new Intent();
//                intent2.setAction("Broadcast_select");
//                intent2.putExtra("select",false);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
//                break;
            default:
                break;
        }
    }

    private void loading(final boolean isRefresh) {
        if (isRefresh) {
            showLoadingPage("加载中……", R.drawable.ic_loading);
        } else {
            errorView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示加载页面
     *
     * @param tip
     * @param resId
     */
    public void showLoadingPage(String tip, int resId) {
        errorView.setVisibility(View.VISIBLE);
        error_tv.setText(tip);
        error_iv.setImageResource(resId);
        /** 设置旋转动画 */
        if (animation == null) {
            animation = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1000);//设置动画持续时间
            /** 常用方法 */
            animation.setRepeatCount(Integer.MAX_VALUE);//设置重复次数
            animation.startNow();
        }
        error_iv.setAnimation(animation);

    }
    private void showExitDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.exit);
        builder.setTitle("提示");
        builder.setMessage("亲，确定退出测试？" );
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            finish();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获返回键按下的事件
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
//            //获取当前系统时间的毫秒数
//            currentBackTime = System.currentTimeMillis();
//            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
//            if (currentBackTime - lastBackTime > 2 * 1000) {
//                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
//                lastBackTime = currentBackTime;
//            } else { //如果两次按下的时间差小于2秒，则退出程序
//                finish();
//            }


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
