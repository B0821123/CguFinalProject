package com.example.cgufinalproject.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.cgufinalproject.utilities.Constants;
import com.example.cgufinalproject.utilities.PreferenceManager;

public class BaseActivity extends AppCompatActivity {

    private DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // 鏈接雲端資料庫 -> users集合 -> 使用者ID的內容中
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 若暫停執行程式則將值為0的KEY_AVAILABILITY代入雲端資料庫中
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 若繼續執行程式則將值為1的KEY_AVAILABILITY代入雲端資料庫中
        documentReference.update(Constants.KEY_AVAILABILITY, 1);
    }
}
