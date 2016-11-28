package alonedroid.com.bustimetable.feature.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.Window;

import lombok.Getter;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

public class BTTWaitDialog {

    private Context mContext;

    @Getter
    BehaviorSubject<Boolean> loading = BehaviorSubject.create(false);

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private Dialog dialog;

    public BTTWaitDialog(Context context) {
        mContext = context;
        initDialog();
        initSubscriber();
    }

    void initDialog() {
        this.dialog = new Dialog(mContext);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.setContentView(new BTTWaitView(mContext));
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.setOnKeyListener((dialog, keyCode, event) -> filterEvent(keyCode));
    }

    void initSubscriber() {
        this.compositeSubscription.add(this.loading
                .filter(loading -> loading)
                .subscribe(loading -> this.dialog.show()));
        this.compositeSubscription.add(this.loading
                .filter(loading -> !loading)
                .subscribe(loading -> this.dialog.dismiss()));
    }

    private boolean filterEvent(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_SEARCH:
                return true;
            default:
                return false;
        }
    }

    public void clear() {
        this.compositeSubscription.clear();
    }
}
