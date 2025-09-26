package com.example.cgufinalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import com.example.cgufinalproject.adapters.UsersAdapter;
import com.example.cgufinalproject.databinding.ActivityUsersBinding;
import com.example.cgufinalproject.listeners.UserListener;
import com.example.cgufinalproject.models.User;
import com.example.cgufinalproject.utilities.Constants;
import com.example.cgufinalproject.utilities.PreferenceManager;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding; // 設立UsersBinding可使用UsersActivity所有XML檔的資源
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    // 從雲端中擷取其他使用者資訊
    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance(); // 與雲端資料庫建立連結
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>(); // 新生成一個集合來放使用者資訊，<User>來自於我所定義的models檔案夾當中
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) { // 跳過自己帳號本身(只顯示其他使用者資訊)
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME); // 擷取其使用者名稱
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL); // 擷取其使用者電子郵件
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE); // 擷取其使用者圖片
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN); // 擷取其使用者註冊令牌
                            user.id = queryDocumentSnapshot.getId(); // 擷取其使用者ID
                            users.add(user); // 將其使用者新增至users集合中
                        }
                        if (users.size() > 0) {
                            UsersAdapter usersAdapter = new UsersAdapter(users, this); // 將UserAdapter指定到此users參數
                            binding.userRecyclerView.setAdapter(usersAdapter); // 將其他使用者資訊顯示於layout中的RecyclerView
                            binding.userRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage(); // 無其他使用者時顯示出錯
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    // 顯示出錯函式
    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "沒有可顯示的使用者"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}