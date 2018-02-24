package state.identity.blockchain.iosl.de.blockidclientqrscanner;

import feign.Feign;
import feign.Headers;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

public class FeignAdapterFactory {

    public static <T> T createAdapter(Class<T> clazz) {
        return buildBean(clazz, "http://srv01.snet.tu-berlin.de:8100");
    }

    public static <T> T buildBean(Class<T> clazz, String url) {
        return Feign.builder()
                .requestInterceptor(new BasicAuthRequestInterceptor("admin", "password"))
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(clazz, url);
    }
}
