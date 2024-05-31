package Utills;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.os.BuildCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

public class MyEditText extends AppCompatEditText {
    private String[] imgTypeString;
    private KeyBoardInputCallbackListener keyBoardInputCallbackListener;


    public MyEditText(@NonNull Context context) {
        super(context);
        initView();
    }

    public MyEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        imgTypeString = new String[]{"image/png",
                "image/gif",
                "image/jpeg",
                "image/webp"};
    }

    @Nullable
    @Override
    public InputConnection onCreateInputConnection(@NonNull EditorInfo outAttrs) {
        final InputConnection ic = super.onCreateInputConnection(outAttrs);
        EditorInfoCompat.setContentMimeTypes(outAttrs,
                imgTypeString);
        return InputConnectionCompat.createWrapper(ic, outAttrs, callback);
    }

    final InputConnectionCompat.OnCommitContentListener callback =
            new InputConnectionCompat.OnCommitContentListener() {
                @Override
                public boolean onCommitContent(@NonNull InputContentInfoCompat inputContentInfo,
                                               int flags, Bundle opts) {

                    // read and display inputContentInfo asynchronously
                    if (BuildCompat.isAtLeastNMR1() && (flags &
                            InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                        try {
                            inputContentInfo.requestPermission();
                        } catch (Exception e) {
                            return false; // return false if failed
                        }
                    }
                    boolean supported = false;
                    for (final String mimeType : imgTypeString) {
                        if (inputContentInfo.getDescription().hasMimeType(mimeType)) {
                            supported = true;
                            break;
                        }
                    }
                    if (!supported) {
                        return false;
                    }
                    inputContentInfo.releasePermission();
                    if (keyBoardInputCallbackListener != null) {
                        keyBoardInputCallbackListener.onCommitContent(inputContentInfo, flags, opts);
                    }
                    Log.e("MyEdittext", "keyboard opts= "+ opts+" |inputContentInfo= "+inputContentInfo);

                    return true;  // return true if succeeded
                }


            };


    public interface KeyBoardInputCallbackListener {
        void onCommitContent(InputContentInfoCompat inputContentInfo,
                             int flags, Bundle opts);
    }

    public void setKeyBoardInputCallbackListener(KeyBoardInputCallbackListener keyBoardInputCallbackListener) {
        this.keyBoardInputCallbackListener = keyBoardInputCallbackListener;
    }

    public String[] getImgTypeString() {
        return imgTypeString;
    }

    public void setImgTypeString(String[] imgTypeString) {
        this.imgTypeString = imgTypeString;
    }
}
