package com.selvaraj.buyerapp.Interface;

import com.selvaraj.buyerapp.model.UserMessage;

public interface ChatListener {
    void onNewMessage(UserMessage message);
}
