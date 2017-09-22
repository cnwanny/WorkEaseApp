package com.wanny.workease.system.framework_uikite.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import java.util.ArrayList;

public class IOSDialogView extends Dialog {

	private Context context;
	private ArrayList<String> dataList;
	private String titleName;
	private ListView listView;
	private TextView cancel;
	private TextView title;
	private IosDialogAdapter adapter;
	public IOSDialogView(Context context, ArrayList<String> dataList, String titleName) {
		super(context);
		this.context = context;
		this.dataList = dataList;
		this.titleName = titleName;
	}

	public IOSDialogView(Context context, int theme, ArrayList<String> dataList, String titleName) {
		super(context, theme);
		this.context = context;
		this.dataList = dataList;
		this.titleName = titleName;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void setWindows(){
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		params.width =(int)(AppUtils.getScreenHeight(context) * 0.7);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		this.getWindow().setAttributes(params);
		Window window = this.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.AnimBottom);
	}

	private void init() {
		setWindows();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.ios_dialog_view, null);
		setContentView(view);
		adapter = new IosDialogAdapter(dataList,context);
		title = (TextView) view.findViewById(R.id.ios_dialog_title);
		listView = (ListView) view.findViewById(R.id.ios_dialog_listview);
		cancel = (TextView) view.findViewById(R.id.ios_dialog_cancel_tv);
		if(!TextUtils.isEmpty(titleName)){
			title.setText(titleName);
		}
		if(adapter != null){
			listView.setAdapter(adapter);
		}
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(iosDialogSelectListener != null){
					iosDialogSelectListener.cancel();
				}
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(iosDialogSelectListener != null){
					iosDialogSelectListener.onItemClick(position);
				}
			}
		});

	}

	private IosDialogSelectListener iosDialogSelectListener;

	public void setIosDialogSelectListener(IosDialogSelectListener iosDialogSelectListener) {
		this.iosDialogSelectListener = iosDialogSelectListener;
	}

	public interface IosDialogSelectListener {
		 void onItemClick(int position);
		 void cancel();
	}

}
