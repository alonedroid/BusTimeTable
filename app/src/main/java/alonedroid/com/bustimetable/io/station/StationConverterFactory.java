package alonedroid.com.bustimetable.io.station;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class StationConverterFactory extends Converter.Factory {

    static StationConverterFactory create() {
        return new StationConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new StationResponseBodyConverter();
    }
}
