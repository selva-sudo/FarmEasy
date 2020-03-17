package com.selvaraj.buyerapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.FirebaseDatabase;
import com.selvaraj.buyerapp.Interface.ChatListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.activity.CartListActivity;
import com.selvaraj.buyerapp.activity.MainActivity;
import com.selvaraj.buyerapp.activity.ProfileActivity;
import com.selvaraj.buyerapp.adapter.MessageAdapter;
import com.selvaraj.buyerapp.base.BaseApplication;
import com.selvaraj.buyerapp.model.UserMessage;
import com.selvaraj.buyerapp.utils.FireBaseUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QueryFragment extends Fragment implements View.OnClickListener, ChatListener {
    private RecyclerView rvChat;
    private List<UserMessage> messageList = new ArrayList<>();
    private EditText etNewMessage;
    private MessageAdapter adapter;
    private Context context;

    public QueryFragment() {
        // Required empty public constructor
    }


    public static QueryFragment newInstance() {
        return new QueryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_query, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btnSend = view.findViewById(R.id.btn_chatbox_send);
        rvChat = view.findViewById(R.id.rv_chat);
        etNewMessage = view.findViewById(R.id.et_chatbox);
        btnSend.setOnClickListener(this);
        initRecyclerView();
        getMessages();
    }

    private void getMessages() {
        BaseApplication.getInstance().getFireBaseUtils().getMessage(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvChat.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(messageList);
        rvChat.setAdapter(adapter);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        item.setVisible(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chatbox_send:
                String message = etNewMessage.getText().toString();
                if (!message.isEmpty()) {
                    ((MainActivity) context).hideKey();
                    String messageuser = new FireBaseUtils().firebaseUser.getDisplayName();
                    long msgTime = new Date().getTime();
                    FirebaseDatabase.getInstance().getReference("Users" + "/" + "Chats").push().setValue(new UserMessage(message, true, messageuser, msgTime));
                    messageList.add(new UserMessage(message, true, messageuser, msgTime));
                    adapter.updateDetails(messageList);
                    rvChat.getLayoutManager().scrollToPosition(messageList.size() - 1);
                    etNewMessage.setText("");
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                Intent intent = new Intent(context, CartListActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_profile:
                intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                ((MainActivity) context).showLogoutAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onNewMessage(UserMessage message) {
        messageList.add(message);
        adapter.updateDetails(messageList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
