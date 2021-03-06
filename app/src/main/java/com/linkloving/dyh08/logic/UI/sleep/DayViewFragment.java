package com.linkloving.dyh08.logic.UI.sleep;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linkloving.dyh08.R;
import com.linkloving.dyh08.ViewUtils.calendar.MonthDateView;
import com.linkloving.dyh08.utils.logUtils.MyLog;

import butterknife.ButterKnife;

/**
 * Created by leo.wang on 2016/7/28.
 */
public class DayViewFragment extends Fragment {
    private static final String TAG = DayViewFragment.class.getSimpleName();

    public ImageView left_btn;
    public ImageView right_btn;
    public MonthDateView monthDateView;

    private IDataChangeListener dataChangeListener;
    private View view;
    public String date;
    private String checkDate;
    private int type = 0;

    public void setDataChangeListener(IDataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tw_dayview_fragment, container, false);
        ButterKnife.inject(this, view);
        left_btn = (ImageView) view.findViewById(R.id.step_calendar_iv_left);
        right_btn = (ImageView) view.findViewById(R.id.step_calendar_iv_right);
        monthDateView = (MonthDateView) view.findViewById(R.id.monthDateView);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClickOnDate() {
                MyLog.i(TAG, "monthDateView.setDateClick:");
                //拼接日期字符串（可以自己定义）
                if (monthDateView.getmSelDay() == 0) {
                    return;
                }
                checkDate = monthDateView.getmSelYear() + "-" + monthDateView.getmSelMonth() + "-" + monthDateView.getmSelDay();
                dataChangeListener.onDataChange(checkDate);
//                if (type == 0) {
//                    //加载下面的buttom
//                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                    SleepDayButtomFragment sleepDayButtomFragment = new SleepDayButtomFragment();
//                    fragmentTransaction.replace(R.id.tw_sleep_buttomfragment, sleepDayButtomFragment).commit();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    DaychartviewFragment daychartviewFragment = new DaychartviewFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("date", checkDate);
//                    daychartviewFragment.setArguments(bundle);
//                    transaction.replace(R.id.step_middle, daychartviewFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    SleepActivity activity = (SleepActivity) getActivity();
//                    activity.videGone();
//                }
            }
        });
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                SleepActivity activity = (SleepActivity) getActivity();
                activity.setDayButtomTag(0);
                monthDateView.onLeftClick();
                activity.setDayButtomTag(1);
                type = 0;

            }
        });
        right_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                type = 1;
                SleepActivity activity = (SleepActivity) getActivity();
                activity.setDayButtomTag(0);
                monthDateView.onRightClick();
                activity.setDayButtomTag(1);
                type = 0;
            }
        });

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
