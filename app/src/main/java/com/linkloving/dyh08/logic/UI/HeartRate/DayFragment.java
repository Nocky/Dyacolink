package com.linkloving.dyh08.logic.UI.HeartRate;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkloving.band.ui.BRDetailData;
import com.linkloving.dyh08.R;
import com.linkloving.dyh08.logic.UI.HeartRate.DayView.BarChartView;
import com.linkloving.dyh08.logic.UI.HeartRate.DayView.DetailChartControl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Trace.GreenDao.DaoMaster;
import Trace.GreenDao.heartrate;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Daniel.Xu on 2016/10/15.
 */

public class DayFragment extends Fragment {
    private final static String TAG = DayFragment.class.getSimpleName();

    @InjectView(R.id.Heartrate_day_barchartview)
    BarChartView barchartview;
    private View view;
    private DetailChartControl detailChartControl;
    private ProgressDialog progressDialog;

    /** 当前正在运行中的数据加载异步线程(放全局的目的是尽量控制当前只有一个在运行，防止用户恶意切换导致OOM) */
    private AsyncTask<Object, Object, List<BRDetailData>> dayDataAsync = null;
    private DaoMaster.DevOpenHelper heartrateHelper;
    private GreendaoUtils greendaoUtils;
    private Date date;

    private RestingBpm restingBpm ;
    public  void setRestingBpmListener(RestingBpm restingBpm){
            this.restingBpm = restingBpm ;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tw_heartrate_day, container, false);
        ButterKnife.inject(this, view);
        heartrateHelper = new DaoMaster.DevOpenHelper(getActivity(), "heartrate", null);
        SQLiteDatabase readableDatabase = heartrateHelper.getReadableDatabase();
        greendaoUtils = new GreendaoUtils(getActivity(), readableDatabase);
        List<BarChartView.BarChartItemBean> list = getHeartPointoneDay();
        barchartview.setItems(list);
//   调滑动的线
        detailChartControl = (DetailChartControl)view.findViewById(R.id.activity_detailChartView1);
        detailChartControl.initDayIndex(date);
        return view;
    }
    private List<BarChartView.BarChartItemBean>  getHeartPointoneDay(){
         date=new Date();//取时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR,-1);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long dayStart = calendar.getTime().getTime();
        System.out.println("开始时间："+calendar.getTime());
        calendar.set(Calendar.HOUR,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        long dayEnd = calendar.getTime().getTime();
        System.out.println("结束时间："+calendar.getTime());
        List<heartrate> heartrates = greendaoUtils.searchOneDay(dayStart, dayEnd);
        ArrayList<BarChartView.BarChartItemBean> list = new ArrayList<>();
        int rest = 0 ;
        int avg = 0 ;
        for (heartrate record : heartrates){
            BarChartView.BarChartItemBean barChartItemBean = new BarChartView.BarChartItemBean
                    (record.getStartTime(), record.getMax(), record.getAvg());
            list.add(barChartItemBean);
            rest = rest+record.getMax();
            avg = avg+record.getAvg();
        }
        int resting,avging ;
        if (list.size()==0){
             resting = 0 ;
            avging = 0 ;
        }else{
             resting = rest / list.size();
             avging = avg/list.size();
        }

        restingBpm.restAndAvg(resting,avging,date);
        return list ;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}