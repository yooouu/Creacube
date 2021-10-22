package kr.co.creacube.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import kr.co.creacube.R;

public class MessagePopup extends Dialog {

    private TextView tvMessage;
    private Button btnPopupClose;

    public MessagePopup(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_message);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);

        tvMessage = findViewById(R.id.tv_message);
        btnPopupClose = findViewById(R.id.btn_popup_close);

        btnPopupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitle(String title) {
        tvMessage.setText(title);
    }

    public void setTitle(int titleID) {
        tvMessage.setText(titleID);
    }

    public void setCloseListener(View.OnClickListener listener) {
        btnPopupClose.setOnClickListener(listener);
    }
}
