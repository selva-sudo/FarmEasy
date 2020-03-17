package com.selvaraj.buyerapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.selvaraj.buyerapp.Interface.DeleteListener;
import com.selvaraj.buyerapp.Interface.RecyclerViewItemClickListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.adapter.ProfileAdapter;
import com.selvaraj.buyerapp.base.BaseActivity;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.model.ProfileItems;
import com.selvaraj.buyerapp.model.SaveUser;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, DeleteListener {

    private RecyclerView rvProfile;
    private TextView tvUserName, tvAboutUs;
    private CircleImageView civInfo;
    private Button btnLogout;
    private AlertDialog dialogFeedBack;
    private List<ProfileItems> profileItemsList = new ArrayList<>();
    private ProfileAdapter profileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setHomeInSupportActionBar(true);
        rvProfile = findViewById(R.id.rv_profile);
        tvUserName = findViewById(R.id.tv_profile_name);
        civInfo = findViewById(R.id.iv_info_alert);
        btnLogout = findViewById(R.id.btn_logout);
        tvAboutUs = findViewById(R.id.tv_about_us);
        tvAboutUs.setVisibility(View.INVISIBLE);
        initListeners();
        initRecyclerView();
        prepareList();
    }

    private void initListeners() {
        civInfo.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void prepareList() {
        profileItemsList.clear();
        SaveUser user = BaseApplication.getInstance().getUserManager().getAuthUser();
        String date = "";
        if (user != null) {
            date = user.getDateOfJoining();
        }
        tvUserName.setText(BaseApplication.getInstance().getUserManager().getUserName());
        profileItemsList.add(new ProfileItems(R.drawable.ic_account, "My Account"));
        profileItemsList.add(new ProfileItems(R.drawable.ic_date_icon, "From " + date));
        profileItemsList.add(new ProfileItems(R.drawable.ic_feedback_icon, "Leave a FeedBack"));
        profileItemsList.add(new ProfileItems(R.drawable.ic_share_icon, "Invite a friend"));
        profileItemsList.add(new ProfileItems(R.drawable.ic_lang_icon, "App language"));
        profileItemsList.add(new ProfileItems(R.drawable.ic_delete_icon, "Delete my account"));
        profileAdapter.updateDetails(profileItemsList);
    }

    //scroll to position
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvProfile.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvProfile.addItemDecoration(itemDecoration);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        rvProfile.setItemAnimator(itemAnimator);
        profileAdapter = new ProfileAdapter(profileItemsList, new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 2:
                        getFeedBack();
                        break;
                    case 3:
                        createShareAlert();
                        break;
                    case 4:
                        changeLanguageAlert();
                        break;
                    case 5:
                        showDeleteAlert();
                        break;
                }

            }
        });
        rvProfile.setAdapter(profileAdapter);
    }

    public void changeLanguageAlert() {

    }

    private void createShareAlert() {

    }

    private void showDeleteAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.MyDialog);
        builder1.setIcon(android.R.drawable.ic_dialog_alert);
        builder1.setTitle("Delete Account?");
        builder1.setMessage("Are you sure to delete your Account?");
        builder1.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseApplication.getInstance().getFireBaseUtils().deleteMyAccount(ProfileActivity.this);
            }
        }).setNegativeButton("Leave FeedBack", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getFeedBack();
            }
        });
        builder1.show();
    }

    private void getFeedBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        View view = getLayoutInflater().inflate(R.layout.feed_back_alert, null);
        builder.setIcon(R.drawable.ic_feedback_icon);
        builder.setView(view);
        Button btnSend, btnCancel;
        btnSend = view.findViewById(R.id.btn_send_feed_back);
        btnCancel = view.findViewById(R.id.btn_cancel_feed_back);
        btnSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        dialogFeedBack = builder.create();
        dialogFeedBack.setCanceledOnTouchOutside(false);
        dialogFeedBack.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_info_alert:
                if (tvAboutUs.getVisibility() == View.INVISIBLE) {
                    tvAboutUs.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_about_us:
                showAboutUsAlert();
                break;
            case R.id.btn_logout:
                showLogoutAlert();
                break;
            case R.id.btn_send_feed_back:
                dialogFeedBack.dismiss();
                break;
            case R.id.btn_cancel_feed_back:
                dialogFeedBack.dismiss();
                break;
        }
    }

    private void showAboutUsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        builder.setIcon(R.drawable.ic_info_icon);
        builder.setTitle("About Farmer-Go");
        View view = getLayoutInflater().inflate(R.layout.about_us, null);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (tvAboutUs.getVisibility() == View.VISIBLE) {
                    tvAboutUs.setVisibility(View.INVISIBLE);
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        builder.setTitle("Logout?");
        builder.setTitle("Are you sure to perform logout?!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create();
        builder.show();

    }

    @Override
    public void onDeleteSuccess(boolean state) {
        if (state) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show();
        }
    }
}
