package com.cse403chorecenter.chorecenterapp.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cse403chorecenter.chorecenterapp.MainActivity;
import com.cse403chorecenter.chorecenterapp.R;
import com.cse403chorecenter.chorecenterapp.ServiceHandler;
import com.cse403chorecenter.chorecenterapp.UserLogin;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class GalleryFragment extends Fragment {
    private static final String TAG = "ParentCode";

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        getParentCode(textView);
        return root;
    }

    /** Look up the parent code for the parent account */
    public static boolean getParentCode(TextView tv) {
        try {
            // checking account status on the server
            ServiceHandler sh = new ServiceHandler();
            String[] params = new String[2];
            params[0] = MainActivity.DNS + "api/parents/info";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("GoogleTokenId", UserLogin.ACCOUNT_ID_TOKEN);
            jsonObj.put("GoogleAccountId", UserLogin.ACCOUNT_ID);
            params[1] = jsonObj.toString();
            sh = (ServiceHandler) sh.execute(params);

            // output response
            try {
                String response = sh.get();
                if(response != null && !response.equals("")) {
                    Log.i(TAG, response);
                    if (!response.equals("404") && !response.equals("500") && !response.equals("400")) {
                        JSONObject jsonObject = new JSONObject(response);
                        tv.setText(jsonObject.getString("ParentCode"));
                        return true;
                    }
                    tv.setText("Account not available. Try again later.");
                    return false;
                }
                return false;
            } catch (ExecutionException e) {
                Log.e(TAG, "Async execution error: " + e.getMessage());
            } catch (InterruptedException e) {
                Log.e(TAG, "Async interrupted error: " + e.getMessage());
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return false;
    }
}