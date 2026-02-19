package es.mpt.dsic.eeutil.operFirma.consumer.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.stereotype.Service;

import es.mpt.dsic.eeutil.operFirma.consumer.model.DatosFirmados;
import es.mpt.dsic.eeutil.operFirma.consumer.model.EeUtilService;
import es.mpt.dsic.eeutil.operFirma.consumer.model.EeUtilServiceImplService;
import es.mpt.dsic.eeutil.operFirma.consumer.model.InSideException;
import es.mpt.dsic.eeutil.operFirma.consumer.model.ResultadoValidacionFirmaInfo;
import es.mpt.dsic.eeutil.operFirma.consumer.model.ResultadoValidacionInfo;
import es.mpt.dsic.eeutil.operFirma.consumer.model.ResultadoValidarCertificado;
import es.mpt.dsic.inside.aop.AuditExternalServiceAnnotation;
import es.mpt.dsic.inside.utils.exception.EeutilException;

@Service
public class ConsumerEeutilOperFirmaImpl {

  protected static final Log logger = LogFactory.getLog(ConsumerEeutilOperFirmaImpl.class);

  private EeUtilService operFirmaWs;

  private Properties properties;

  private String truststore;
  private String passTruststore;

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public String getTruststore() {
    return truststore;
  }

  public void setTruststore(String truststore) {
    this.truststore = truststore;
  }

  public String getPassTruststore() {
    return passTruststore;
  }

  public void setPassTruststore(String passTruststore) {
    this.passTruststore = passTruststore;
  }

  public void configure() throws EeutilException {
    if (operFirmaWs == null) {
      URL urlOperFirma = null;
      String urlOper = null;
      try {
        urlOper = properties.getProperty("eeutil.operfirma.ws.url");
        logger.debug(String.format("El WS se encuentra en %s", urlOper));
        urlOperFirma = new URL(urlOper);

        System.setProperty("javax.net.ssl.trustStore", this.truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", this.passTruststore);



        EeUtilServiceImplService ssOperFirma = new EeUtilServiceImplService(urlOperFirma);
        operFirmaWs = ssOperFirma.getEeUtilServiceImplPort();

        setupTimeouts(ClientProxy.getClient(operFirmaWs));
        disableChunking(ClientProxy.getClient(operFirmaWs));

      } catch (MalformedURLException e) {
        // logger.error(
        // "No se puede crear la URL del servicio Eeutil OperFirma "
        // + urlOper, e);
        throw new EeutilException(
            "No se puede crear la URL del servicio Eeutil OperFirma " + e.getMessage(), e);
      } catch (Exception e) {
        // logger.error(
        // "No se puede crear la URL del servicio Eeutil OperFirma "
        // + urlOper, e);
        throw new EeutilException(
            "No se puede crear la URL del servicio Eeutil OperFirma " + e.getMessage(), e);
      }
    }
  }

  @AuditExternalServiceAnnotation(nombreModulo = "eeutil-misc")
  public byte[] postProcesarFirma(String idApp, String passw, byte[] firma) throws EeutilException {

    try {
      configure();
      es.mpt.dsic.eeutil.operFirma.consumer.model.ApplicationLogin credential =
          new es.mpt.dsic.eeutil.operFirma.consumer.model.ApplicationLogin();
      credential.setIdaplicacion(idApp);
      credential.setPassword(passw);
      return operFirmaWs.postProcesarFirma(credential, firma);
    } catch (EeutilException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (InSideException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }
  }

  @AuditExternalServiceAnnotation(nombreModulo = "eeutil-misc")
  public ResultadoValidacionInfo validacionFirma(String idApp, String password, byte[] firma,
      String tipoFirma, DatosFirmados datosFirmados) throws EeutilException {
    try {
      configure();
      es.mpt.dsic.eeutil.operFirma.consumer.model.ApplicationLogin credential =
          new es.mpt.dsic.eeutil.operFirma.consumer.model.ApplicationLogin();
      credential.setIdaplicacion(idApp);
      credential.setPassword(password);
      return operFirmaWs.validacionFirma(credential, firma, tipoFirma, datosFirmados);
    } catch (EeutilException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (InSideException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }
  }


  @AuditExternalServiceAnnotation(nombreModulo = "eeutil-misc")
  public ResultadoValidacionFirmaInfo validacionFirmaInfo(String idApp, String password,
      byte[] firma, String tipoFirma, DatosFirmados datosFirmados, boolean infoCertificados)
      throws EeutilException {

    try {
      configure();
      es.mpt.dsic.eeutil.operFirma.consumer.model.ApplicationLogin credential =
          new es.mpt.dsic.eeutil.operFirma.consumer.model.ApplicationLogin();
      credential.setIdaplicacion(idApp);
      credential.setPassword(password);
      return operFirmaWs.validacionFirmaInfo(credential, firma, tipoFirma, datosFirmados,
          infoCertificados);
    } catch (EeutilException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (InSideException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }
  }


  private void disableChunking(Client client) {
    HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
    HTTPClientPolicy policy = httpConduit.getClient();
    policy.setAllowChunking(false);
    policy.setChunkingThreshold(0);
    logger.debug("AllowChunking:" + policy.isAllowChunking());
    logger.debug("ChunkingThreshold:" + policy.getChunkingThreshold());
  }


  @AuditExternalServiceAnnotation(nombreModulo = "eeutil-misc")
  public ResultadoValidarCertificado validacionCertificado(String idApp, String password,
      String certificate) throws EeutilException {
    try {
      configure();
      es.mpt.dsic.eeutil.operFirma.consumer.model.ApplicationLogin credential =
          new es.mpt.dsic.eeutil.operFirma.consumer.model.ApplicationLogin();
      credential.setIdaplicacion(idApp);
      credential.setPassword(password);
      return operFirmaWs.validarCertificado(credential, certificate);
    } catch (EeutilException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (InSideException e) {
      throw new EeutilException(e.getMessage(), e);
    } catch (Exception e) {
      throw new EeutilException(e.getMessage(), e);
    }
  }


  /**
   * Timeout de 120 sg para llamada entre modulos a misc
   * 
   * @param client
   */
  private void setupTimeouts(Client client) {

    HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
    HTTPClientPolicy policy = httpConduit.getClient();

    // set time to wait for response in milliseconds. zero means unlimited

    policy.setReceiveTimeout(120000L);
    policy.setConnectionTimeout(120000L);

  }

}
