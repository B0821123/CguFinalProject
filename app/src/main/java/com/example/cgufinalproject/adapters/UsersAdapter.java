package com.example.cgufinalproject.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Base64;
import java.util.List;

import com.example.cgufinalproject.databinding.ItemContainerUserBinding;
import com.example.cgufinalproject.listeners.UserListener;
import com.example.cgufinalproject.models.User;

// 擴充、繼承自父類別(RecyclerView.Adapter<UsersAdapter.UserViewHolder>)
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // 擴充、繼承自父類別(RecyclerView.ViewHolder)
    class UserViewHolder extends RecyclerView.ViewHolder {

        ItemContainerUserBinding binding;

        // 當viewBinding在project中被使用時，相對應每個XML layout的Binding class將會自動生成
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        void setUserData(User user) {
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }

    private Bitmap getUserImage(String encodeImage) {
        byte[] bytes = Base64.getMimeDecoder().decode(encodeImage);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}

