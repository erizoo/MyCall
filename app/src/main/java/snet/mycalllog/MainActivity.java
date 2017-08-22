package snet.mycalllog;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;

import static snet.mycalllog.R.layout.string;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private static final String TAG = "MyActivity";
    List<snet.mycalllog.Call> callsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CALL_LOG},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_CALL_LOG},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        textView = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCallDetails();
                appendLog("RAM");
            }
        });
    }

    private List<snet.mycalllog.Call> getCallDetails() {
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy");
            String time = sdf.format(callDayTime);
            String dateCall = dateFormat.format(callDayTime);
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            callsList.add(new snet.mycalllog.Call(phNumber, dir, dateCall, time, callDuration));
        }
        cursor.close();
        return callsList;
    }

    public void appendLog(String text) {
        File logDir = Environment.getExternalStorageDirectory();
        File logFile = new File(logDir.getAbsolutePath() + "/", "hi.xls");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet firstSheet = workbook.createSheet("Sheet No 1");
            for (int i = 0; i <= callsList.size()-1; i++){
                HSSFRow rowA = firstSheet.createRow(i);
                HSSFCell cellNumberPhone = rowA.createCell(0);
                HSSFCell cellIncome = rowA.createCell(1);
                HSSFCell cellDate = rowA.createCell(2);
                HSSFCell cellTime = rowA.createCell(3);
                HSSFCell cellDuration = rowA.createCell(4);
                cellNumberPhone.setCellValue(new HSSFRichTextString(callsList.get(i).getNumberPhone()));
                cellIncome.setCellValue(String.valueOf(callsList.get(i).getStatus()));
                cellDate.setCellValue(callsList.get(i).getDate());
                cellTime.setCellValue(callsList.get(i).getDateTime());
                cellDuration.setCellValue(callsList.get(i).getDuration());
            }

            workbook.write(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

