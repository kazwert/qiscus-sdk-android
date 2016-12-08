/*
 * Copyright (c) 2016 Qiscus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qiscus.sdk.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.qiscus.sdk.R;
import com.qiscus.sdk.data.model.QiscusChatRoom;
import com.qiscus.sdk.ui.fragment.QiscusBaseChatFragment;
import com.qiscus.sdk.ui.fragment.QiscusChatFragment;

import java.util.Date;

public class QiscusChatActivity extends QiscusBaseChatActivity {
    protected Toolbar toolbar;
    protected TextView tvTitle;
    protected TextView tvSubtitle;

    public static Intent generateIntent(Context context, QiscusChatRoom qiscusChatRoom) {
        Intent intent = new Intent(context, QiscusChatActivity.class);
        intent.putExtra(CHAT_ROOM_DATA, qiscusChatRoom);
        return intent;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_qiscus_chat;
    }

    @Override
    protected void onLoadView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void applyChatConfig() {
        toolbar.setBackgroundResource(chatConfig.getAppBarColor());
        tvTitle.setTextColor(ContextCompat.getColor(this, chatConfig.getTitleColor()));
        tvSubtitle.setTextColor(ContextCompat.getColor(this, chatConfig.getSubtitleColor()));
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        tvTitle.setText(qiscusChatRoom.getName());
        tvSubtitle.setText(qiscusChatRoom.getSubtitle());
        tvSubtitle.setVisibility(qiscusChatRoom.getSubtitle().isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected QiscusBaseChatFragment onCreateChatFragment() {
        return QiscusChatFragment.newInstance(qiscusChatRoom);
    }

    @Override
    public void onUserTyping(String user, boolean typing) {
        if (qiscusChatRoom.getSubtitle().isEmpty()) {
            tvSubtitle.setText(typing ? "Typing..." : "Online");
            tvSubtitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUserStatusChanged(String user, boolean online, Date lastActive) {
        if (qiscusChatRoom.getSubtitle().isEmpty()) {
            String last = DateUtils.getRelativeTimeSpanString(lastActive.getTime(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            tvSubtitle.setText(online ? "Online" : "Last seen " + last);
            tvSubtitle.setVisibility(View.VISIBLE);
        }
    }
}
