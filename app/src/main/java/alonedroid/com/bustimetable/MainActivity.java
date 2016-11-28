package alonedroid.com.bustimetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import alonedroid.com.bustimetable.feature.BTTSearchFragment;
import rx.Subscription;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    private static Subscription sSubscription;
    public static PublishSubject<Fragment> revelation = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("itinoue", "onCreate");
        setContentView(R.layout.activity_main);
        sSubscription = revelation.subscribe(this::showFragment);
        initViews();
    }

    private void initViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_contents, BTTSearchFragment.instantiate(this))
                .commit();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_contents, fragment)
                .addToBackStack("")
                .commitAllowingStateLoss();
    }

    @Override
    protected void onStop() {
        Log.d("itinoue", "stop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d("itinoue", "pause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("itinoue", "resume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sSubscription != null) {
            sSubscription.unsubscribe();
            sSubscription = null;
        }
        Log.d("itinoue", "destroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_contents);
            if (currentFragment instanceof BTTBackableFragment) {
                return ((BTTBackableFragment) currentFragment).onBackPress();
            }
        }
        return false;
    }
}
