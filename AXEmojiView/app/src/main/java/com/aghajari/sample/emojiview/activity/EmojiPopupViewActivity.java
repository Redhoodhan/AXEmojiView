package com.aghajari.sample.emojiview.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.listener.SimplePopupAdapter;
import com.aghajari.emojiview.search.AXEmojiSearchView;
import com.aghajari.emojiview.view.AXEmojiEditText;
import com.aghajari.emojiview.view.AXEmojiPager;
import com.aghajari.emojiview.view.AXEmojiPopupLayout;
import com.aghajari.emojiview.view.AXEmojiTextView;
import com.aghajari.sample.emojiview.R;
import com.aghajari.sample.emojiview.UI;

public class EmojiPopupViewActivity extends AppCompatActivity {
    AXEmojiPopupLayout layout;

    FrameLayout edtParent;
    AXEmojiEditText edt;
    AppCompatImageView emojiImg;
    AXEmojiTextView textView;

    private boolean isShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("load")) {
            setContentView(R.layout.emoji_activity_layout_view);
            init(Color.BLACK);
        }
    }

    protected void init(final int color){

        getSupportActionBar().setTitle(AXEmojiUtils.replaceWithEmojis(this,
                "AXEmojiView "+AXEmojiUtils.getEmojiUnicode(0x1f60d),20));

        layout = findViewById(R.id.layout);

        // get emoji edit text
        edtParent = findViewById(R.id.edt_parent);
        edt = findViewById(R.id.edt);
        emojiImg = findViewById(R.id.imageView);
        textView = findViewById(R.id.textview);

        AXEmojiPager emojiPager = UI.loadView(this,edt);

        // create emoji popup
        layout.initPopupView(emojiPager);

        // SearchView
        if (AXEmojiManager.isAXEmojiView(emojiPager.getPage(0))) {
            layout.setSearchView(new AXEmojiSearchView(this, emojiPager.getPage(0)));
            emojiPager.setOnFooterItemClicked(new AXEmojiPager.OnFooterItemClicked() {
                @Override
                public void onClick(View view, boolean leftIcon) {
                    if (leftIcon) layout.showSearchView();
                }
            });
        }

        layout.hideAndOpenKeyboard();
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.openKeyboard();
            }
        });

        emojiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing){
                    layout.openKeyboard();
                }else{
                    layout.show();
                }
            }
        });

        findViewById(R.id.send_emoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt.getText().length()>0){
                    textView.setText(edt.getText().toString());
                    edt.setText("");
                }
            }
        });

        layout.setPopupListener(new SimplePopupAdapter() {
            @Override
            public void onShow() {
                updateButton(true);
            }

            @Override
            public void onDismiss() {
                updateButton(false);
            }

            @Override
            public void onKeyboardOpened(int height) {
                updateButton(false);
            }

            @Override
            public void onKeyboardClosed() {
                updateButton(layout.isShowing());
            }

            private void updateButton(boolean emoji){
                if (isShowing==emoji) return;
                isShowing = emoji;
                if (emoji){
                    Drawable dr = AppCompatResources.getDrawable(EmojiPopupViewActivity.this, R.drawable.ic_msg_panel_kb);
                    DrawableCompat.setTint(DrawableCompat.wrap(dr), color);
                    emojiImg.setImageDrawable(dr);
                }else {
                    Drawable dr = AppCompatResources.getDrawable(EmojiPopupViewActivity.this, R.drawable.ic_msg_panel_smiles);
                    DrawableCompat.setTint(DrawableCompat.wrap(dr), color);
                    emojiImg.setImageDrawable(dr);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!layout.onBackPressed())
            super.onBackPressed();
    }
}
