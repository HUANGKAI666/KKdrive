package test.hk.com.KkDriving.ui.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;

import test.hk.com.KkDriving.R;
import test.hk.com.KkDriving.been.Mistake;
import test.hk.com.KkDriving.been.Right;
import test.hk.com.KkDriving.db.DbHelper;
import test.hk.com.KkDriving.ui.TestActivity;
import test.hk.com.KkDriving.ui.adapter.myfragmentAdapter;

import static test.hk.com.KkDriving.ui.SelectActivity.flag;
import static test.hk.com.KkDriving.ui.TestActivity.mViewPager;


public class MyFragment extends Fragment implements View.OnClickListener {
    public ScrollView mScrollview;
    private View rootView;
    private TextView mQuestion, mItme1, mItme2, mItme3, mItme4, mReslut, mExplains,mdialogText;
    String imgurl, itme1, itme2, itme3, itme4, question, explains;
    private ImageView mQuestion_img;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private LinearLayout mL_itme1, mL_itme2, mL_itme3, mL_itme4;
    private Context context;
    private ImageView mItme1_img, mItme2_img, mItme3_img, mItme4_img;
    private int answer, number;
    private myfragmentAdapter mAdapter;
    public boolean select;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SQLiteDatabase db;
    private String TAG = "kkk";
    private List<Right> mRightList = new ArrayList<>();
    private List<Mistake> mMistakeList = new ArrayList<>();
    private boolean frist = true;
    private LocalBroadcastManager localBroadcastManager;
private  BroadcastReceiver br;
    private TextView mBeiTi;
    private int datasize;

    public MyFragment(Context context, String question, String imgurl, String itme1, String itme2, String itme3, String itme4, int answer, String explains, int i, myfragmentAdapter mAdapter,int datasize) {
        number = i;
        this.mAdapter = mAdapter;
        this.context = context;
        this.question = question;
        this.imgurl = imgurl;
        this.itme1 = itme1;
        this.itme2 = itme2;
        this.itme3 = itme3;
        this.itme4 = itme4;
        this.answer = answer;
        this.explains = explains;
        this.datasize = datasize;
    }

    public MyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("fragment","onCreateView");
        if (rootView == null) {
            if (itme3.equals("") && imgurl.equals("")) {
                rootView = inflater.inflate(R.layout.fragment_judgment_noimg, container, false);
            } else if (itme3.equals("") && !imgurl.equals("")) {
                rootView = inflater.inflate(R.layout.fragment_judgment, container, false);
            } else if (!itme3.equals("") && imgurl.equals("")) {
                rootView = inflater.inflate(R.layout.fragment_select_noimg, container, false);
            } else {
                rootView = inflater.inflate(R.layout.fragment_select, container, false);
            }
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        Log.d("fragment","onActivityCreated");
        initView();
//        setBroadcastReceiver();
        DisplayImageOptions();
        initData();

    }


////viewpager预加载的解决
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
////可见时执行的操作
//
//            Log.d("isVisibleToUser","true");
//
//
//        } else {
////不可见时执行的操作
////            localBroadcastManager.unregisterReceiver(br);
//            Log.d("isVisibleToUser","false");
//        }
//
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fragment","onCreate");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("fragment","onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("fragment","onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("fragment","onStart");
    }

    private void initData() {
        mQuestion.setText(question);
        mItme1.setText(itme1);
        Log.d(TAG, itme2);
        mItme2.setText(itme2);
        if (!itme3.equals("") && !itme4.equals("")) {
            mItme3.setText(itme3);
            mItme4.setText(itme4);
        }
        if (imgurl.length() != 0) {
            imageLoader.displayImage(imgurl, mQuestion_img, displayImageOptions);
        }
        if (answer == 1) {
            mReslut.setText("答案：A");
        } else if (answer == 2) {
            mReslut.setText("答案：B");
        } else if (answer == 3) {
            mReslut.setText("答案：C");
        } else {
            mReslut.setText("答案：D");
        }
        mExplains.setText(explains);
    }

    private void initView() {
        mQuestion = (TextView) rootView.findViewById(R.id.id_question);
        mItme1 = (TextView) rootView.findViewById(R.id.id_itme1_text);
        mItme2 = (TextView) rootView.findViewById(R.id.id_itme2_text);
        mItme3 = (TextView) rootView.findViewById(R.id.id_itme3_text);
        mItme4 = (TextView) rootView.findViewById(R.id.id_itme4_text);
        mQuestion_img = (ImageView) rootView.findViewById(R.id.id_question_img);
        mExplains = (TextView) rootView.findViewById(R.id.id_explains);
        mReslut = (TextView) rootView.findViewById(R.id.id_result);
        mL_itme1 = (LinearLayout) rootView.findViewById(R.id.id_itme1);
        mL_itme2 = (LinearLayout) rootView.findViewById(R.id.id_itme2);
        mL_itme3 = (LinearLayout) rootView.findViewById(R.id.id_itme3);
        mL_itme4 = (LinearLayout) rootView.findViewById(R.id.id_itme4);
        mItme1_img = (ImageView) rootView.findViewById(R.id.id_itme1_img1);
        mItme2_img = (ImageView) rootView.findViewById(R.id.id_itme1_img2);
        mItme3_img = (ImageView) rootView.findViewById(R.id.id_itme1_img3);
        mItme4_img = (ImageView) rootView.findViewById(R.id.id_itme1_img4);
        mScrollview = (ScrollView) rootView.findViewById(R.id.id_scroll);
        mBeiTi = (TextView) rootView.findViewById(R.id.id_BT);
        mBeiTi.setOnClickListener(this);

        mL_itme1.setOnClickListener(this);
        mL_itme2.setOnClickListener(this);
        if (!itme3.equals("")) {
            mL_itme3.setOnClickListener(this);
            mL_itme4.setOnClickListener(this);
        }




    }

    private void DisplayImageOptions() {
        imageLoader = ImageLoader.getInstance();
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.erro)
                .showImageForEmptyUri(R.drawable.erro)
                .showImageOnFail(R.drawable.erro)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }


    @Override
    public void onClick(View v) {
        DbHelper dbHelper = new DbHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        switch (v.getId()) {

            case R.id.id_itme1:
                if (frist) {
                    mItme1_img.setBackgroundResource(R.drawable.select_wrong);
                    if (answer == 1) {
                        rightDb();
                        mViewPager.setCurrentItem(number + 1);
                    } else {
                        mScrollview.setVisibility(View.VISIBLE);
                        mistakeDb();
                    }
                    setResultTv();
                    frist = false;
                }
                break;
            case R.id.id_itme2:
                if (frist) {
                    mItme2_img.setBackgroundResource(R.drawable.select_wrong);
                    if (answer == 2) {
                        rightDb();
                        mViewPager.setCurrentItem(number + 1);
                    } else {

                        mistakeDb();
                        mScrollview.setVisibility(View.VISIBLE);
                    }
                    setResultTv();
                    frist = false;
                }
                break;
            case R.id.id_itme3:

                if (frist) {
                    mItme3_img.setBackgroundResource(R.drawable.select_wrong);
                    if (answer == 3) {
                        rightDb();
                        mViewPager.setCurrentItem(number + 1);
                    } else {

                        mistakeDb();
                        mScrollview.setVisibility(View.VISIBLE);
                    }
                    setResultTv();
                    frist = false;
                }

                break;
            case R.id.id_itme4:
                            if (frist) {
                                mItme4_img.setBackgroundResource(R.drawable.select_wrong);
                                if (answer == 4) {
                                    rightDb();
                                    mViewPager.setCurrentItem(number + 1);
                                } else {

                        mistakeDb();
                        mScrollview.setVisibility(View.VISIBLE);
                    }
                    setResultTv();

                    frist = false;
                }
                break;
            case R.id.id_BT:
                mScrollview.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void setResultTv() {
        getDbvalues();
        showResult();
        Log.d("right+mistake", mRightList.size() + "" + mMistakeList.size());
        TestActivity.mRight_tv.setText(mRightList.size() + "");
        TestActivity.mMistake_tv.setText(mMistakeList.size() + "");
    }

    private void rightDb() {
        ContentValues Values = new ContentValues();
        Values.put("right", number);
        db.insert("right_table", null, Values);
    }

    private void mistakeDb() {
        ContentValues values = new ContentValues();
        values.put("mistake", number);
        db.insert("mistake_table", null, values);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("fragment","Destrouy");

    }

    public void getDbvalues() {
        DbHelper db = new DbHelper(getActivity());
        SQLiteDatabase dbread = db.getReadableDatabase();
        Cursor cursor = dbread.query("right_table", null, null, null, null,
                null, null);
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int right = cursor.getInt(cursor.getColumnIndex("right"));
                Right r = new Right();
                r.setRight(right);
                r.setId(id);
                mRightList.add(r);
            }
        }
        Cursor cursor2 = dbread.query("mistake_table", null, null, null, null,
                null, null);
        if (cursor2 != null && cursor2.getCount() != 0) {
            while (cursor2.moveToNext()) {
                int id = cursor2.getInt(cursor2.getColumnIndex("_id"));
                int mistake = cursor2.getInt(cursor2.getColumnIndex("mistake"));
                Mistake m = new Mistake();
                m.setMistake(mistake);
                m.setId(id);
                mMistakeList.add(m);
            }
        }
    }

    private void showResult() {
        Log.d("dialog", mRightList.size() + mMistakeList.size() +"");
        Log.d("data","datasize:"+datasize);
        if (datasize==100){
            if (mRightList.size() + mMistakeList.size() == 100) {
                showResultDialog();
            }
        }else {
            if (mRightList.size() + mMistakeList.size() == datasize) {
                showResultDialog();
            }
        }

    }

    private void showResultDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog, null);
        mdialogText= (TextView) view.findViewById(R.id.dialog_tv);
        mdialogText.setText(mRightList.size()+"分");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.reslut);
        builder.setTitle("测试结束：");
        builder.setMessage("亲，你的测试成绩为");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                getActivity().finish();
            }
        });
        builder.setNeutralButton("再看看",null);
        builder.setCancelable(false);
        builder.show();
    }

    private void setBroadcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("Broadcast_select");
        if (br==null){
            br = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    select = intent.getBooleanExtra("select",true);
                    Log.d("select_res",select+"");
                    if (select) {
                        mScrollview.setVisibility(View.GONE);

                    }else {
                        mScrollview.setVisibility(View.VISIBLE);
                    }
                }
            };
        }
        localBroadcastManager.registerReceiver(br,intentfilter);
    }




}

