package es.mpt.dsic.inside.ws.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mpt.dsic.inside.aop.AuditEntryPointAnnotation;
import es.mpt.dsic.inside.reflection.MapUtil;
import es.mpt.dsic.inside.reflection.UtilReflection;
import es.mpt.dsic.inside.security.model.ApplicationLogin;
import es.mpt.dsic.inside.utils.xml.XMLUtil;
import es.mpt.dsic.inside.ws.service.EeUtilValidacionENIServiceMtom;
import es.mpt.dsic.inside.ws.service.model.RespuestaValidacionENI;
import es.mpt.dsic.inside.ws.service.model.Validaciones;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntradaMtom;


@Service("eeUtilValidacionENIServiceMtom")
@WebService(endpointInterface = "es.mpt.dsic.inside.ws.service.EeUtilValidacionENIServiceMtom")
public class EeUtilValidacionENIServiceMtomImpl implements EeUtilValidacionENIServiceMtom {

  private static final String ENI_XSD_V_PATTERN = "ENI/XSD/v";

  private static final String EXTRA_PARA_M = "ExtraParaM";

  private static final String VERSION_PARAM = "version";

  private static final String G_999 = "G.999";

  protected static final Log logger = LogFactory.getLog(EeUtilValidacionENIServiceMtomImpl.class);

  /**
   * Etiqueta para sacar la version del ENI
   */
  private static final String VERSION_NTI = "VersionNTI";

  @Autowired
  EeUtilValidacionENIServiceBusiness eeUtilValidacionENIServiceBusiness;



  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarDocumentoENI(ApplicationLogin info,
      DocumentoEntradaMtom documentoENI, String version, Validaciones validaciones) {



    RespuestaValidacionENI resultado = null;
    try {

      resultado = eeUtilValidacionENIServiceBusiness.validarDocumentoENIMtom(
          info.getIdApplicacion(), info.getPassword(), documentoENI, version, validaciones);

      if (resultado.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarDocumentoENIMDC(documentoENI, version, validaciones);
        logger.error(resultado.getDetalle());
      }

    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarDocumentoENIMDC(documentoENI, version, validaciones);
      logger.error(e.getMessage(), e);
    }

    return resultado;



  }



  /**
   * @param documentoENI
   * @param version
   * @param validaciones
   */
  private void validarDocumentoENIMDC(DocumentoEntradaMtom documentoENI, String version,
      Validaciones validaciones) {
    try {
      Object[] objs = new Object[3];
      String[] strP = new String[] {"documentoENI", VERSION_PARAM, "validaciones"};
      objs[0] = documentoENI;
      objs[1] = version;
      objs[2] = validaciones;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }



  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarExpedienteENI(ApplicationLogin info,
      DocumentoEntradaMtom expedienteENI, String version, Validaciones validaciones) {



    RespuestaValidacionENI resultado = null;

    try {

      resultado = eeUtilValidacionENIServiceBusiness.validarExpedienteENIMtom(
          info.getIdApplicacion(), info.getPassword(), expedienteENI, version, validaciones);

      if (resultado.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarExpedienteENIMDC(expedienteENI, version, validaciones);
        logger.error(resultado.getDetalle());
      }


    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarExpedienteENIMDC(expedienteENI, version, validaciones);
      logger.error(e.getMessage(), e);
    }

    return resultado;

  }



  /**
   * @param expedienteENI
   * @param version
   * @param validaciones
   */
  private void validarExpedienteENIMDC(DocumentoEntradaMtom expedienteENI, String version,
      Validaciones validaciones) {
    try {
      Object[] objs = new Object[3];
      String[] strP = new String[] {"expedienteENI", VERSION_PARAM, "validaciones"};
      objs[0] = expedienteENI;
      objs[1] = version;
      objs[2] = validaciones;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }


  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarFirmaExpedienteENI(ApplicationLogin info,
      DocumentoEntradaMtom expedienteENI) {



    RespuestaValidacionENI resultado = null;

    try {
      // obtenemos la version del eni del nodo VersionNTI

      String version = XMLUtil.getNodeValue(
          IOUtils.toByteArray(expedienteENI.getContenido().getInputStream()), VERSION_NTI);

      version = obtenerVersionDesdeNTI(version);

      resultado = eeUtilValidacionENIServiceBusiness.validarFirmaExpedienteENIMtom(
          info.getIdApplicacion(), info.getPassword(), expedienteENI, version);

      if (resultado.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarFirmaExpedienteENIMDC(expedienteENI);
        logger.error(resultado.getDetalle());
      }

    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarFirmaExpedienteENIMDC(expedienteENI);
      logger.error(e.getMessage(), e);
    }

    return resultado;


  }



  /**
   * @param expedienteENI
   */
  private void validarFirmaExpedienteENIMDC(DocumentoEntradaMtom expedienteENI) {
    try {
      Object[] objs = new Object[1];
      String[] strP = new String[] {"expedienteENI"};
      objs[0] = expedienteENI;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }



  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarFirmaDocumentoENI(ApplicationLogin info,
      DocumentoEntradaMtom documentoENI) {



    RespuestaValidacionENI resultado = null;

    try {

      // obtenemos la version del eni del nodo VersionNTI

      String version = XMLUtil.getNodeValue(
          IOUtils.toByteArray(documentoENI.getContenido().getInputStream()), VERSION_NTI);

      version = obtenerVersionDesdeNTI(version);

      resultado = eeUtilValidacionENIServiceBusiness.validarFirmaDocumentoENIMtom(
          info.getIdApplicacion(), info.getPassword(), documentoENI, version);

      if (resultado.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarFirmaDocumentoENIMDC(documentoENI);
        logger.error(resultado.getDetalle());
      }

    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarFirmaDocumentoENIMDC(documentoENI);
      logger.error(e.getMessage(), e);
    }

    return resultado;

  }



  /**
   * @param documentoENI
   */
  private void validarFirmaDocumentoENIMDC(DocumentoEntradaMtom documentoENI) {
    try {
      Object[] objs = new Object[1];
      String[] strP = new String[] {"documentoENI"};
      objs[0] = documentoENI;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }



  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public RespuestaValidacionENI validarExpedienteDocumentosENIZIP(ApplicationLogin info,
      DocumentoEntradaMtom docZipEntrada, String version, Validaciones validacionesExpediente,
      Validaciones validacionesDocumentos) {



    RespuestaValidacionENI resultado = null;

    try {


      resultado = eeUtilValidacionENIServiceBusiness.validarExpedienteDocumentosENIZIPMtom(
          info.getIdApplicacion(), info.getPassword(), docZipEntrada, version,
          validacionesExpediente, validacionesDocumentos);

      if (resultado.getGlobal().contains(G_999)) {
        ingresarMDCAppUUID(info.getIdApplicacion());
        validarExpedienteDocumentosENIZIPMDC(docZipEntrada, version, validacionesExpediente,
            validacionesDocumentos);
        logger.error(resultado.getDetalle());
      }


    } catch (Exception e) {
      ingresarMDCAppUUID(info.getIdApplicacion());
      validarExpedienteDocumentosENIZIPMDC(docZipEntrada, version, validacionesExpediente,
          validacionesDocumentos);
      logger.error(e.getMessage(), e);
    }

    return resultado;


  }



  /**
   * @param docZipEntrada
   * @param version
   * @param validacionesExpediente
   * @param validacionesDocumentos
   */
  private void validarExpedienteDocumentosENIZIPMDC(DocumentoEntradaMtom docZipEntrada,
      String version, Validaciones validacionesExpediente, Validaciones validacionesDocumentos) {
    try {
      Object[] objs = new Object[4];
      String[] strP = new String[] {"docZipEntrada", VERSION_PARAM, "validacionesExpediente",
          "validacionesDocumentos"};
      objs[0] = docZipEntrada;
      objs[1] = version;
      objs[2] = validacionesExpediente;
      objs[3] = validacionesDocumentos;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }


  private void ingresarMDCAppUUID(String idApp) {
    MDC.put("idApli", idApp);
    MDC.put("uUId", UUID.randomUUID().toString());
  }


  /**
   * Obtiene el valor numerico de la version NTI a partir del valor del nodo NTI
   * 
   * @param versionNTI
   * @return
   */
  private String obtenerVersionDesdeNTI(String versionNTI) {
    String version = null;

    try {

      if (versionNTI != null && versionNTI.length() > 0) {
        if (versionNTI.indexOf("documento-e") != -1) {
          version = versionNTI.substring(
              versionNTI.indexOf(ENI_XSD_V_PATTERN) + ENI_XSD_V_PATTERN.length(),
              versionNTI.indexOf("/documento-e"));
        } else if (versionNTI.indexOf("expediente-e") != -1) {
          version = versionNTI.substring(
              versionNTI.indexOf(ENI_XSD_V_PATTERN) + ENI_XSD_V_PATTERN.length(),
              versionNTI.indexOf("/expediente-e"));
        }

      }

    } catch (Exception e) {
      return version;
    }

    return version;
  }


}
