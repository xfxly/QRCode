package com.qrcode;


import com.google.zxing.WriterException;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.*;
import android.os.Environment;
import android.util.*;

public class MainActivity extends Activity {
	private TextView resultTextView;
	private EditText qrStrEditText;
	private TextView listTextView;;
	private  Button exitBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        qrStrEditText = (EditText) this.findViewById(R.id.tv_input_data);
		listTextView= (TextView) this.findViewById(R.id.textViewdata);
		listTextView.setText("");
		tmFileUtils tmfileutils = new tmFileUtils();
		if (!tmfileutils.isFileExist("QSCode"))
		tmfileutils.creatSDDir("QSCode");
		ImageButton scanBarCodeButton = (ImageButton) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
        
        Button addCodeButtonToList = (Button) this.findViewById(R.id.btn_add_qrcode);
		addCodeButtonToList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String inputString = qrStrEditText.getText().toString();
					String qsCode = resultTextView.getText().toString();
					if (!inputString.equals("")&&!qsCode.equals("")) {
						String vInput=qsCode+','+inputString;
						listTextView.append(vInput + "\n");
					}else {
						Toast.makeText(MainActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Button saveBtn = (Button) this.findViewById(R.id.btn_save_qrcode);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listTextView.getText();
				String filename = "QSCode/QSCode.txt";
				String filecontent = listTextView.getText().toString();
				try {
					//判断SDcard是否存在并且可读写
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
						saveToSDCard(filename,filecontent);
						Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), R.string.sdcarderror, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), R.string.fail,Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});

		Button clearBtn = (Button) this.findViewById(R.id.btn_clear_qrcode);
		clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listTextView.setText("");
			}
		});

		Button exitBtn = (Button) this.findViewById(R.id.btn_exit);
		exitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					finish();
			}
		});
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			resultTextView.setText(scanResult);
		}
	}

	/**
	 * 保存到SD卡
	 * @param filename
	 * @param filecontent
	 * @throws Exception
	 */
	public void saveToSDCard(String filename, String filecontent){
		tmFileUtils tmfileutils = new tmFileUtils();
		File file = null;
		try {
			file = tmfileutils
					.creatSDFile(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			tmfileutils.writeFile(file.getAbsolutePath(),
					filecontent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}