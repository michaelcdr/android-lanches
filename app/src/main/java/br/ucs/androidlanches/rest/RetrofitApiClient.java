package br.ucs.androidlanches.rest;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import br.ucs.androidlanches.rest.services.IMesaApiService;
import br.ucs.androidlanches.rest.services.IPedidoService;
import br.ucs.androidlanches.rest.services.IProdutoApiService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


public class RetrofitApiClient
{
    private static final String BASE_URL = "https://10.0.2.2:5051/";
    private static Retrofit retrofit = null;
    private static final Gson gson = new Gson();
    public static Retrofit getClient()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                                   .baseUrl(BASE_URL)
                                   .addConverterFactory(GsonConverterFactory.create())
                                   .client(getUnsafeOkHttpClient().build())
                                   .build();
        }
        return retrofit;
    }

    public static IPedidoService getPedidoService() {
        return getClient().create(IPedidoService.class);
    }

    public static IProdutoApiService getProdutoService(){
        return getClient().create(IProdutoApiService.class);
    }

    public static IMesaApiService getMesaService(){

        return getClient().create(IMesaApiService.class);
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient()
    {
        try
        {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}