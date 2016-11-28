package alonedroid.com.bustimetable;

public interface BTTBackableFragment {

    /**
     * @return true:後続処理のキャンセル, false:後続処理の続行
     */
    boolean onBackPress();
}
