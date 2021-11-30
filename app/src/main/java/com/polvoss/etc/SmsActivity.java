package com.polvoss.etc;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.polvoss.R;

import java.util.ArrayList;
import java.util.List;

public class SmsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri mSmsQueryUri = Uri.parse("content://sms");
        List<String> messages = new ArrayList<String>();

        Cursor cursor = null;
        try {

            cursor = getContentResolver().query(mSmsQueryUri, null, null, null, null);
            if (cursor == null) {
                Log.d("message_error", "null error");
                // Log.i(TAG, "cursor is null. uri: " + mSmsQueryUri);
            }
            for(int i = 0; i < cursor.getColumnCount(); i++){
                Log.d("column", cursor.getColumnName(i));
            }
            for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {
                final String body = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                final String sender_no= cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
                final String date= cursor.getString(cursor.getColumnIndexOrThrow("date"));
                final String type =cursor.getString(cursor.getColumnIndexOrThrow("type"));
                messages.add(body);
                messages.add(sender_no);
                messages.add(date);
                messages.add(type);

                String[] mSelectionArgs = new String[3];
                mSelectionArgs[0] = body;
                mSelectionArgs[1] = sender_no;




                try{
                    String qry = "body = ? AND address = ?";

                    int rowsDeleted = 0;
                    rowsDeleted = getContentResolver().delete(
                            Uri.parse("content://sms"),   // the sms content URI
                            qry,                    // the column to select on
                            mSelectionArgs                       // the value to compare to
                    );
                    if(rowsDeleted > 0){
                        Log.d("message_d", "delete success");

                    }else{
                        Log.d("message_d", "delete failed");
                    }
                } catch (Exception e){
                    Log.d("message_d", "delete fail");
                }

                Log.d("message", body+"");
                Log.d("message_sender", sender_no+"");
                Log.d("message_date", date+"");
                Log.d("message_type", type+"");
            }
        } catch (Exception e) {
            Log.d("message_error", "error");
        } finally {
            cursor.close();
        }
    }

}