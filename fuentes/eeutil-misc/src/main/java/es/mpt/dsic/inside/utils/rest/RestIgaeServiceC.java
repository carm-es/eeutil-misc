package es.mpt.dsic.inside.utils.rest;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import es.mpt.dsic.inside.aop.AuditExternalServiceAnnotation;
import es.mpt.dsic.inside.utils.exception.EeutilException;

@Service
public class RestIgaeServiceC {

  private org.apache.http.ssl.SSLContextBuilder sslContextBuilder;
  private SSLContext sslContext;
  private org.apache.http.conn.ssl.SSLConnectionSocketFactory sslSocketFactory;
  private HttpClientBuilder httpClientBuilder;

  @AuditExternalServiceAnnotation(nombreModulo = "eeutil-misc")
  public CloseableHttpResponse getResponseClientIgae(CloseableHttpClient httpClient, String url,
      Object objectConvertJson, String tokenNameIgae, String tokenValueIgae) throws EeutilException,
      IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

    String json = null;
    Gson gson = null;

    try {

      if (objectConvertJson instanceof String) {
        json = ((String) objectConvertJson);
      }

      if (sslContextBuilder == null) {
        {
          sslContextBuilder = SSLContextBuilder.create();
          sslContextBuilder
              .loadTrustMaterial(new org.apache.http.conn.ssl.TrustSelfSignedStrategy());
        }
        if (sslContext == null) {
          sslContext = sslContextBuilder.build();
        }
        if (sslSocketFactory == null) {
          sslSocketFactory =
              new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1.1", "TLSv1.2"}, null,
                  new org.apache.http.conn.ssl.DefaultHostnameVerifier());
        }
        if (httpClientBuilder == null) {
          httpClientBuilder = HttpClients.custom().setSSLSocketFactory(sslSocketFactory);
        }

      }
      httpClient = httpClientBuilder.build();

      // httpClient = HttpClients.createDefault();

      HttpPost httpPost = new HttpPost(url);

      if (json == null) {
        gson = new Gson();
        json = gson.toJson(objectConvertJson);
      }
      // StringEntity entity = new StringEntity(json,StandardCharsets.UTF_8);
      StringEntity entity = new StringEntity(json);
      // chunked a false
      entity.setChunked(false);
      httpPost.setEntity(entity);

      httpPost.setHeader("Accept", "application/json");
      httpPost.setHeader("Content-type", "application/json");
      httpPost.addHeader(tokenNameIgae, tokenValueIgae);

      return httpClient.execute(httpPost);

    } finally {
    }

  }

  public void cerrarRequestResponse(CloseableHttpClient httpClient, CloseableHttpResponse response)
      throws IOException {
    if (response != null) {
      response.close();
    }
    if (httpClient != null) {
      httpClient.close();
    }
  }

}
