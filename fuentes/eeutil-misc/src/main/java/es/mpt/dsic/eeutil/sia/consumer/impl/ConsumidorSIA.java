package es.mpt.dsic.eeutil.sia.consumer.impl;


import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import es.mpt.dsic.eeutil.service.sia.model.ConstantesSIA;
import es.mpt.dsic.eeutil.service.sia.model.EnviaSIA2;
import es.mpt.dsic.eeutil.service.sia.model.ParamSIA3;
import es.mpt.dsic.eeutil.service.sia.model.WsSIAConsultarActuacionesIdentificacion;
import es.mpt.dsic.eeutil.service.sia.model.WsSIAConsultarActuacionesIdentificacion_Service;
import es.mpt.dsic.inside.aop.AuditExternalServiceAnnotation;



public class ConsumidorSIA {

  private WsSIAConsultarActuacionesIdentificacion sc;
  private String user;
  private String password;
  private String url;

  private boolean configured = false;

  protected static final Log logger = LogFactory.getLog(ConsumidorSIA.class);

  public boolean configure() {
    if (!configured) {
      try {
        WsSIAConsultarActuacionesIdentificacion_Service service1 =
            new WsSIAConsultarActuacionesIdentificacion_Service();
        sc = service1.getPort(WsSIAConsultarActuacionesIdentificacion.class);

        setupTimeouts(ClientProxy.getClient(sc));
        disableChunking(ClientProxy.getClient(sc));

        String endpointURL = url;
        BindingProvider bp = (BindingProvider) sc;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
        configured = true;
      } catch (RuntimeException e) {
        logger.error("No se ha podido conectar al servicio:" + url);
      }
    }
    return configured;
  }

  @AuditExternalServiceAnnotation(nombreModulo = "eeutil-misc")
  public boolean existClasificacion(String clasificacion, String anio) {
    logger.debug("Inicio existClasificacion: " + clasificacion + "," + anio);
    boolean retorno = false;
    if (configure()) {
      ParamSIA3 parameters = new ParamSIA3();
      parameters.setUser(user);
      parameters.setPassword(password);
      es.mpt.dsic.eeutil.service.sia.model.ParamSIA3.Filtro value =
          new es.mpt.dsic.eeutil.service.sia.model.ParamSIA3.Filtro();
      value.setAnioVolTramitaciones(anio);
      value.setTipoActuacion(ConstantesSIA.TIPO_ACTUACION_PROCEDIMIENTO);
      value.setCodigoActuacion(clasificacion);
      parameters.setFiltro(value);
      EnviaSIA2 respuestaSIA = sc.consultarSIAV30(parameters);

      if (respuestaSIA != null && respuestaSIA.getACTUACIONES() != null
          && respuestaSIA.getACTUACIONES().getACTUACION() != null
          && respuestaSIA.getACTUACIONES().getACTUACION().size() > 0) {
        if (clasificacion
            .equals(respuestaSIA.getACTUACIONES().getACTUACION().get(0).getCODIGOACTUACION())) {
          retorno = true;
        }
      }

      logger.debug("Fin existClasificacion:" + retorno);
    }
    return retorno;
  }

  public WsSIAConsultarActuacionesIdentificacion getSc() {
    return sc;
  }

  public void setSc(WsSIAConsultarActuacionesIdentificacion sc) {
    this.sc = sc;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  /** 2 minutos de timeout ***/
  private void setupTimeouts(Client client) {

    HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
    HTTPClientPolicy policy = httpConduit.getClient();

    // set time to wait for response in milliseconds. zero means unlimited

    policy.setReceiveTimeout(120000);
    policy.setConnectionTimeout(120000);

  }

  private void disableChunking(Client client) {
    HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
    HTTPClientPolicy policy = httpConduit.getClient();
    policy.setAllowChunking(false);
    policy.setChunkingThreshold(0);
    logger.debug("AllowChunking:" + policy.isAllowChunking());
    logger.debug("ChunkingThreshold:" + policy.getChunkingThreshold());
  }

}
