package com.qiqia.duosheng.custom;

import android.content.Context;
import android.graphics.Typeface;
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
import cn.com.smallcake_utils.DpPxUtils;
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

        List<String> years = getYearNum(yearNum);
        List<String> months = getMonthNum(monthNum);
        wheelYear.setData(years);
        wheelMonth.setData(months);
        //设置默认选中位置
        wheelYear.setSelectedItemPosition(yearNum);
        wheelMonth.setSelectedItemPosition(monthNum);

        initWheelSet(wheelYear);
        initWheelSet(wheelMonth);
        wheelYear.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                String year = (String) data;
                L.e(year);
                selectYear  = Integer.parseInt(year.substring(0,year.length()-1));
            }
        });
        wheelMonth.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                String month = (String) data;
                L.e(month);
                selectMonth = Integer.parseInt(month.substring(0,month.length()-1));
            }
        });
    }
    private List<String> getYearNum( int yearNum){
        List<String> data = new ArrayList<>();
        for (int i = yearNum-10; i <= yearNum; i++) {
            data.add(i+"年");
        }
        return data;
    }
    private List<String> getMonthNum(int monthNum){
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            data.add(i+"月");
        }
        return data;
    }
    private void initWheelSet(WheelPicker wheel){
//        wheel.setCurtainColor(Color.RED);//蒙版层颜色
//        wheel.setCurtain(true);//是否加入蒙版层:覆盖在选中的文字上层
        wheel.setAtmospheric(true);//未选中项进行高斯模糊效果
        wheel.setCurved(true);//圆弧滚轮效果
        wheel.setItemSpace(DpPxUtils.dp2px(28));
        wheel.setItemTextColor(ContextCompat.getColor(this.getContext(),R.color.text_light_gray));
        wheel.setSelectedItemTextColor(ContextCompat.getColor(this.getContext(),R.color.text_black));
        wheel.setItemTextSize(DpPxUtils.dp2px(16));
        wheel.setTypeface(Typeface.DEFAULT_BOLD);
    }
    @OnClick(R.id.btn_confirm)
    public void doClick(){
        if (listener!=null)listener.selectYearMonth(selectYear,selectMonth);
        this.dismiss();
    }
}
