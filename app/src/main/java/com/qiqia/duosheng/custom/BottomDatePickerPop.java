package com.qiqia.duosheng.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.lxj.xpopup.core.BottomPopupView;
import com.qiqia.duosheng.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.smallcake_utils.L;

/**
 * 底部年月时间选择弹出窗
 */
public class BottomDatePickerPop extends BottomPopupView {

    @BindView(R.id.wheel_year)
    WheelPicker wheelYear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wheel_month)
    WheelPicker wheelMonth;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    int selectYear;
    int selectMonth;

    public BottomDatePickerPop(@NonNull Context context) {
        super(context);
    }
    private onSelectYearMonth listener;

    public void setListener(onSelectYearMonth listener) {
        this.listener = listener;
    }

    public interface  onSelectYearMonth{
        void selectYearMonth(int year,int month);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_bottom_date_picker;
    }

    @Override
    protected void onCreate() {
        ButterKnife.bind(this);
        super.onCreate();
        initYearMonth();
    }

    private void initYearMonth() {
        Calendar now = Calendar.getInstance();
        int yearNum = now.get(Calendar.YEAR);
        int monthNum = now.get(Calendar.MONTH);//默认月份会少1
        selectYear = yearNum;
        selectMonth = monthNum+1;

        List<Integer> years = getYearNum(yearNum);
        List<Integer> months = getMonthNum(monthNum);
        wheelYear.setData(years);
        wheelMonth.setData(months);
        wheelYear.setSelectedItemPosition(years.size()-1);
        wheelMonth.setSelectedItemPosition(months.size()-1);

        initWheelSet(wheelYear);
        initWheelSet(wheelMonth);
        wheelYear.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                int year = (int) data;
                L.e(year+"年");
                selectYear  =year;
            }
        });
        wheelMonth.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                int month = (int) data;
                L.e(month+"月");
                selectMonth = month;
            }
        });
    }
    private List<Integer> getYearNum( int yearNum){
        List<Integer> data = new ArrayList<>();
        for (int i = yearNum-3; i <= yearNum; i++) {
            data.add(i);
        }
        return data;
    }
    private List<Integer> getMonthNum(int monthNum){
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= monthNum+1; i++) {
            data.add(i);
        }
        return data;
    }
    private void initWheelSet(WheelPicker wheel){
//        wheel.setCurtain(true);//是否加入蒙版层，颜色深度降低
        wheel.setCurved(true);//圆弧滚轮效果
        wheel.setItemTextColor(ContextCompat.getColor(this.getContext(),R.color.text_light_gray));
        wheel.setSelectedItemTextColor(ContextCompat.getColor(this.getContext(),R.color.text_black));

    }
    @OnClick(R.id.btn_confirm)
    public void doClick(){
        if (listener!=null)listener.selectYearMonth(selectYear,selectMonth);
        this.dismiss();

    }
}
