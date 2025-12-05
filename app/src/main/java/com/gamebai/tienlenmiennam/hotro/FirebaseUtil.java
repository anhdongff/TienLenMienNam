package com.gamebai.tienlenmiennam.hotro;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.gamebai.tienlenmiennam.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** Lớp này chức các tiện ích sử dụng cho firebase */
public class FirebaseUtil {
    public static boolean isFacebookLinked() {
        // 1. Lấy người dùng hiện tại
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return false; // Không có người dùng, chắc chắn là chưa liên kết
        }

        // 2. Lặp qua danh sách các nhà cung cấp đã liên kết
        for (UserInfo profile : user.getProviderData()) {
            // 3. Kiểm tra Provider ID
            if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                return true;
            }
        }

        // 4. Nếu lặp hết mà không tìm thấy, trả về false
        return false;
    }

}
