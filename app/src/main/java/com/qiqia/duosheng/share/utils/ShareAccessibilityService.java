package com.qiqia.duosheng.share.utils;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.List;

import cn.com.smallcake_utils.L;

import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

/**
 * 辅助功能只在帮助残障人士使用Android设备和app的时候使用。
 * 服务进程被杀掉后，下次启动，需再次申请权限
 * TYPE_VIEW_CLICKED==1                 //视图被点击事件
 * TYPE_VIEW_FOCUSED==8                 //视图获取焦点事件
 * TYPE_WINDOW_STATE_CHANGED ==32       //窗口页面改变
 * TYPE_WINDOW_CONTENT_CHANGED==2048    //页面内容视图改变
 * TYPE_VIEW_SCROLLED=4096              //视图滚动事件
 * <p>
 * 来到微信主页面LauncherUI
 * 1.查找底部【发现】按钮，并模拟点击
 * 2.模拟点击第一项【朋友圈】
 * 来到微信朋友圈界面SnsTimeLineUI
 * 3.模拟点击右上角拍照分享【相机】图标
 * 弹出了微信选择界面ui.base.k,由于混淆了，后面的k不定，可能是a,f,z...
 * 4.模拟点击【从相册获取】
 * 来到照片选择界面AlbumPreviewUI
 * 5.【选中】要分享的几张图片
 * 6.选中完成后，并点击【完成】按钮
 * 来到发表页面SnsUploadUI
 * 7.自动输入文字,并关闭执行辅助服务
 */
public class ShareAccessibilityService extends AccessibilityService {
    public static boolean IS_SHARE;//控制是否执行辅助服务
    public static int SELECT_PIC_NUM =1;//选择照片数量
    public static String SHARE_CONTENT="分享";//要分享的文字
    final static String TAG = ShareAccessibilityService.class.getSimpleName();
    final static String WECHAT_SPLASH_ACTIVITY = "com.tencent.mm.app.WeChatSplashActivity";//启动页面
    final static String SNS_UPLOAD_UI = "com.tencent.mm.plugin.sns.ui.SnsUploadUI";//发表页面
    final static String ALBUM_PREVIEW_UI = "com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI";//照片选择界面
    final static String SNS_TIMELINE_UI = "com.tencent.mm.plugin.sns.ui.SnsTimeLineUI";//朋友圈主界面
    final static String LAUNCHER_UI = "com.tencent.mm.ui.LauncherUI";//微信主界面
    final static String DIALOG_UI = "com.tencent.mm.ui.base";//弹出窗口界面

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        L.e("辅助服务链接成功onServiceConnected");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!IS_SHARE)return;
        int eventType = event.getEventType();//事件类型
        AccessibilityNodeInfo source = event.getSource();
        CharSequence className = event.getClassName();
        String classNameStr = className.toString();
        L.e("来到页面ClassName==" + className + "   eventType==" + eventType);
        //再启动首页&&视图内容进行了变换
        if (eventType == TYPE_WINDOW_STATE_CHANGED && className.equals(LAUNCHER_UI)) clickFindButton(source);//点击发现
        if (eventType == TYPE_WINDOW_STATE_CHANGED&& className.equals(LAUNCHER_UI)) openCircleFriends();//打开朋友圈
        if (className.equals(SNS_TIMELINE_UI)) openShareDialog(source);//打开拍照分享的弹出窗口
        if (classNameStr.startsWith(DIALOG_UI)) selectByPhoto(source);//弹出框选择从相册获取图片
        if (eventType == TYPE_WINDOW_STATE_CHANGED&&className.equals(ALBUM_PREVIEW_UI)) findGridViewByWindows();//查找相册布局控件
        if (className.equals(SNS_UPLOAD_UI)) inputShareTxt(source);//自动输入文字

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void findGridViewByWindows() {
        List<AccessibilityWindowInfo> windows = getWindows();
        L.e("窗口改变，来到照片选择界面 windows.size=="+windows.size());
        //3个Fragment,第一个系统级：com.android.systemui，第二个微信：com.tencent.mm 第三个 null
        for (int i = 0; i < windows.size(); i++) {
            AccessibilityWindowInfo accessibilityWindowInfo = windows.get(i);
            AccessibilityNodeInfo root = accessibilityWindowInfo.getRoot();
            L.e("root=="+(root==null?"0":root.toString()));
            if (root!=null&&root.getPackageName().equals("com.tencent.mm"))for (int j = 0; j < root.getChildCount(); j++) {

                AccessibilityNodeInfo child = root.getChild(j);
                if (child.getClassName().equals("android.widget.LinearLayout")){
                    AccessibilityNodeInfo childGridView = child//LinearLayout(1)
                            .getChild(0)// FrameLayout(1)
                            .getChild(0)// ViewGroup(2)
                            .getChild(1)// FrameLayout(1)
                            .getChild(0)//FrameLayout(1)
                            .getChild(0)//FrameLayout(1)
                            .getChild(0);//GridView(24)
                    L.e("查找GridView为=="+childGridView);
                    selectPic(childGridView,SELECT_PIC_NUM);
                }

            }
        }
    }

    /**
     * 1.微信主页 - 点击发现按钮
     */
    private void clickFindButton(AccessibilityNodeInfo source) {
        L.e("1. 点击发现按钮......");
        List<AccessibilityNodeInfo> shareCircleNodes = source.findAccessibilityNodeInfosByText("发现");
        if (shareCircleNodes != null && shareCircleNodes.size() > 0){
            clickParent(shareCircleNodes.get(0));
        }

    }

    /**
     *  2.微信主页 - 打开朋友圈
     *  注意：如果已经打开了主页面LauncherUI的情况下，可能获取不到朋友圈的节点，
     *  可以在clickFindButton执行后，执行此方法，保证打开朋友圈更稳定。
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCircleFriends() {
        L.e("2.打开朋友圈......");
        List<AccessibilityWindowInfo> windows = getWindows();
        for (int i = 0; i < windows.size(); i++) {
            AccessibilityWindowInfo accessibilityWindowInfo = windows.get(i);
            AccessibilityNodeInfo root = accessibilityWindowInfo.getRoot();
            L.e("root=="+root);
            if (root!=null&&root.getPackageName().equals("com.tencent.mm")){
                List<AccessibilityNodeInfo> shareCircleNodes = root.findAccessibilityNodeInfosByText("朋友圈");
                L.e("朋友圈按钮个数=="+(shareCircleNodes==null?"0":shareCircleNodes.size()));
                if (shareCircleNodes != null && shareCircleNodes.size() > 0)
                clickParent(shareCircleNodes.get(0));

            }
        }
    }
    /**
     *  3.朋友圈 - 打开拍照分享的弹出窗口
     */

    private void openShareDialog(AccessibilityNodeInfo rootInfo) {
        if (rootInfo == null || rootInfo.getClassName() == null) return;
        L.e("3.打开拍照分享的弹出窗口......");
        if ("android.widget.ImageButton".equals(rootInfo.getClassName())) {
            String contentDesc = rootInfo.getContentDescription().toString();
            if (!TextUtils.isEmpty(contentDesc) && contentDesc.equals("拍照分享"))
                rootInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            for (int i = 0; i < rootInfo.getChildCount(); i++) {
                openShareDialog(rootInfo.getChild(i));
            }
        }
    }

    /**
     *  4.拍照分享的弹出窗口 -选择从相册选择
     */

    private void selectByPhoto(AccessibilityNodeInfo source) {
        L.e("4.点击从相册选择......");
        if (source == null || TextUtils.isEmpty(source.getClassName())) return;
        List<AccessibilityNodeInfo> selectByPhotoInfo = source.findAccessibilityNodeInfosByText("从相册选择");
        if (selectByPhotoInfo != null && selectByPhotoInfo.size() > 0)
            clickParent(selectByPhotoInfo.get(0));
    }

    /**
     * 5.开始选中相册照片
     * @param source
     * @param picNum
     */
    private void selectPic(AccessibilityNodeInfo source, int picNum) {
        L.e("5.开始选中相册照片......");
        if (source == null || source.getClassName() == null) return;
        //source 5个子项都为null
        //getRootInActiveWindow()也为null
        if ("android.widget.GridView".equals(source.getClassName())) {
            int childCount = source.getChildCount();
            L.e("当前页面包含GridView==" + childCount + "  picNum==" + picNum);
            int selectNum = childCount < picNum ? childCount : picNum;
            for (int i = 0; i < selectNum; i++) {
                AccessibilityNodeInfo child = source.getChild(i);//android.widget.RelativeLayout
                int checkBoxIndex = getCheckBoxIndex(child);
                if (checkBoxIndex>0){
                    AccessibilityNodeInfo view = child.getChild(checkBoxIndex-1);
                    AccessibilityNodeInfo checkBoxNode = child.getChild(checkBoxIndex);
                    if (checkBoxNode.isEnabled()&&!checkBoxNode.isChecked()) view.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                if (i == (selectNum - 1)) completeSelect();

            }
        }else {
            for (int i = 0; i < source.getChildCount(); i++) {
                AccessibilityNodeInfo child = source.getChild(i);
                if (child==null)continue;
                selectPic(child,picNum);
            }
        }
    }
    /**
     * 6.点击完成
     */
    private void completeSelect() {
        List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText("完成");
        L.e("完成按钮个数===" + nodes.size());
        if (nodes != null && nodes.size() > 0) {
            L.e("6.点击完成----");
            AccessibilityNodeInfo btnComplete = nodes.get(0);
            if (btnComplete.isEnabled() && btnComplete.isClickable())
                btnComplete.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    /**
     * 7.自动输入文字,并关闭执行辅助服务
     * 注意：这里并不是关闭了此辅助服务进程，而是通过设置IS_SHARE=false，来屏蔽服务的执行
     * 避免用户分享完成后，再次选中相册分享
     *
     * @param source
     */
    private void inputShareTxt(AccessibilityNodeInfo source) {
        L.e("7.自动输入文字......");
        if (source == null || source.getClassName() == null) return;
        if (!"android.widget.EditText".equals(source.getClassName())) {
            for (int i = 0; i < source.getChildCount(); i++) inputShareTxt(source.getChild(i));
        } else {
            final AccessibilityNodeInfo etMsg = source;
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,SHARE_CONTENT);
            etMsg.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            etMsg.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            IS_SHARE = false;
        }
    }

    /**
     * 通过该节点，查找为CheckBox项
     * 获取它同级的上一项View,通过点击实现
     * @param source
     * return CheckBox所在位置
     */
    private int getCheckBoxIndex(AccessibilityNodeInfo source){
        for (int i = 0; i <source.getChildCount() ; i++) {
            AccessibilityNodeInfo child = source.getChild(i);
            if (child!=null&&child.getClassName().equals("android.widget.CheckBox")){
                return i;//CheckBox 的位置
            }
        }
        return -1;
    }

    private void logAllInfoStr(AccessibilityNodeInfo info) {
        if (info != null && info.getChildCount() > 0)
            for (int i = 0; i < info.getChildCount(); i++) {
                AccessibilityNodeInfo child = info.getChild(i);
                L.e(i + "====" + (child == null ? "" : child.getClassName()));
            }
    }

    /**
     * 无限向上级父类控件查找，开启了使用（enabled=true），且可以点击的控件(clicked=true)，并点击
     *
     * @param info
     */
    private void clickParent(AccessibilityNodeInfo info) {
        AccessibilityNodeInfo parent = info.getParent();
        if (parent == null) return;
        if (parent.isEnabled() && parent.isClickable()) {
            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            clickParent(parent);
        }
    }

    @Override
    public void onInterrupt() {
        //服务中断
        L.e("ShareAccessibilityService服务中断...");
    }
}
