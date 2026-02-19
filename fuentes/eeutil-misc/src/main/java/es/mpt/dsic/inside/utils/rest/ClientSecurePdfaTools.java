package es.mpt.dsic.inside.utils.rest;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.stereotype.Service;

import es.mpt.dsic.inside.aop.AuditExternalServiceAnnotation;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.utils.string.StringUtil;

/**
 * Clase del cliente para acceso a los servicios de pdfatools. xxx = entorno. POST
 * https://[ENTORNO]/eeutilpdfatools/api/convertirPDFA
 * https://[ENTORNO]/eeutilpdfatools/api//eeutilpdfatools/api/validarPDFA
 * 
 * @author mamoralf
 *
 */

@Service
public class ClientSecurePdfaTools {

  private org.apache.http.ssl.SSLContextBuilder sslContextBuilder;
  private SSLContext sslContext;
  private org.apache.http.conn.ssl.SSLConnectionSocketFactory sslSocketFactory;
  private HttpClientBuilder httpClientBuilder;

  private final static String HTTP_HEADER_PDFTOOLS_LENGTH = "fileconv-length";
  private final static String HTTP_HEADER_IDAPP = "h-idapp-client";

  @AuditExternalServiceAnnotation(nombreModulo = "eeutil-pdfatools")
  public CloseableHttpResponse getResponseClientPdfatools(CloseableHttpClient httpClient,
      String url, String tokenNamePdfaTools, String tokenValuePdfaTools, String jsonPeticion,
      Integer tamBytes, String uuidTraza) throws EeutilException, IOException,
      NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

    try {

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

      // escribimos la peticion
      StringEntity entity = new StringEntity(jsonPeticion);

      // chunked a false
      entity.setChunked(false);
      httpPost.setEntity(entity);

      httpPost.setHeader("Accept", "application/json");
      httpPost.setHeader("Content-type", "application/json");
      httpPost.addHeader(tokenNamePdfaTools, tokenValuePdfaTools);
      httpPost.addHeader("uUId_traza", uuidTraza);
      // anadimos cabeceras con metainfo usuario, mime, longitud fichero
      httpPost.addHeader(HTTP_HEADER_PDFTOOLS_LENGTH,
          tamBytes != null ? String.valueOf(tamBytes) : "0");
      httpPost.addHeader(HTTP_HEADER_IDAPP, (String) org.apache.log4j.MDC.get("idApli"));

      return httpClient.execute(httpPost);

    } finally {
    }

  }

  public String toJsonValidarPdfaCompilanceLevel(byte[] bytesEntrada, Integer level,
      String compilance, String password) {
    if (StringUtil.esVacioOrNull(password)) {
      return "{\"level\":" + level + ",\"compilance\":\"" + compilance + "\",\"bytesEntrada\":\""
          + Base64.encodeBase64String(bytesEntrada) + "\"}";
    } else {
      return "{\"level\":" + level + ",\"compilance\":\"" + compilance + "\",\"password\":\""
          + password + "\",\"bytesEntrada\":\"" + Base64.encodeBase64String(bytesEntrada) + "\"}";
    }
  }

  public String toJsonValidarPdfa(byte[] bytesEntrada, String password) {
    if (StringUtil.esVacioOrNull(password)) {
      return "{\"bytesEntrada\":\"" + Base64.encodeBase64String(bytesEntrada) + "\"}";
    } else {
      return "{\"password\":\"" + password + "\",\"bytesEntrada\":\""
          + Base64.encodeBase64String(bytesEntrada) + "\"}";
    }
  }

  public String toJsonConvertirPDFA(Integer level, byte[] contenido, String password) {
    if (StringUtil.esVacioOrNull(password)) {
      return "{\"level\":" + level + ",\"contenido\":\"" + Base64.encodeBase64String(contenido)
          + "\"}";
    } else {
      return "{\"level\":" + level + ",\"password\":\"" + password + "\",\"contenido\":\""
          + Base64.encodeBase64String(contenido) + "\"}";
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

  /*
   * public static void main(String args[]) {
   * 
   * byte[] bytesPrueba = "fdfse".getBytes();
   * System.out.println(ClientSecurePdfaTools.toJsonValidarPdfaCompilanceLevel( bytesPrueba, 1, "b",
   * null)); System.out.println(ClientSecurePdfaTools.toJsonValidarPdfaCompilanceLevel( bytesPrueba,
   * 1, "b", "pass")); System.out.println(ClientSecurePdfaTools.toJsonValidarPdfa(bytesPrueba,
   * null)); System.out.println(ClientSecurePdfaTools.toJsonValidarPdfa(bytesPrueba, "pass"));
   * System.out.println(ClientSecurePdfaTools.toJsonConvertirPDFA(1, bytesPrueba, null));
   * System.out.println(ClientSecurePdfaTools.toJsonConvertirPDFA(1, bytesPrueba, "pass")); }
   */
}
