package alonedroid.com.bustimetable.io.timetable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class TimeTableConverterFactory extends Converter.Factory {

    public static TimeTableConverterFactory create() {
        return new TimeTableConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new TimeTableResponseBodyConverter();
    }
}
