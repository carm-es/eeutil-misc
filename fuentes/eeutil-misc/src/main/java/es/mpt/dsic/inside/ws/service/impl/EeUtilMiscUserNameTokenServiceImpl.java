package es.mpt.dsic.inside.ws.service.impl;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mpt.dsic.inside.aop.AuditEntryPointAnnotation;
import es.mpt.dsic.inside.reflection.MapUtil;
import es.mpt.dsic.inside.reflection.UtilReflection;
import es.mpt.dsic.inside.security.context.AplicacionContext;
import es.mpt.dsic.inside.security.model.AppInfo;
import es.mpt.dsic.inside.security.wss4j.CredentialUtil;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.ws.service.EeUtilMiscUserNameTokenService;
import es.mpt.dsic.inside.ws.service.exception.InSideException;
import es.mpt.dsic.inside.ws.service.model.ContenidoInfo;
import es.mpt.dsic.inside.ws.service.model.EstadoInfo;
import es.mpt.dsic.inside.ws.service.model.SalidaVisualizacion;
import es.mpt.dsic.inside.ws.service.model.TCNInfo;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntrada;
import es.mpt.dsic.inside.ws.service.model.pdf.PdfSalida;

@Service("eeUtilMiscUserNameTokenService")
@WebService(endpointInterface = "es.mpt.dsic.inside.ws.service.EeUtilMiscUserNameTokenService")
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.BARE, use = Use.LITERAL)
public class EeUtilMiscUserNameTokenServiceImpl implements EeUtilMiscUserNameTokenService {

  private static final String EXTRA_PARA_M = "ExtraParaM";

  private static final String ERROR = "ERROR";

  protected static final Log logger = LogFactory.getLog(EeUtilMiscUserNameTokenServiceImpl.class);

  @Autowired
  private AplicacionContext aplicacionContext;

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  CredentialUtil credentialUtil;

  @Autowired
  EeUtilMiscServiceBusiness eeUtilMiscServiceBusiness;

  @Override
  @AuditEntryPointAnnotation(nombreApp = "EEUTIL-MISC")
  public byte[] postProcesarFirma(byte[] firma) throws InSideException {



    try {
      // activa que se introduzca el idapp en el MDC
      credentialUtil.getCredentialEeutilUserToken(wsContext).getIdApplicacion();
      return eeUtilMiscServiceBusiness.postProcesarFirma(
          credentialUtil.getCredentialEeutilUserToken(wsContext).getIdApplicacion(),
          credentialUtil.getCredentialEeutilUserToken(wsContext).getPassword(), firma);
    } catch (EeutilException e) {

      postProcesarFirmaMDC(firma);
      logger.error(e.getMessage(), e);
      throw new InSideException(e.getMessage(), e);
    } catch (Exception e) {
      postProcesarFirmaMDC(firma);
      logger.error(e.getMessage(), e);
      EstadoInfo info = new EstadoInfo(ERROR, ERROR, e.getMessage());
      throw new InSideException(e.getMessage(), info, e);
    }

  }

  /**
   * @param firma
   */
  private void postProcesarFirmaMDC(byte[] firma) {
    try {

      Object[] objs = new Object[1];
      String[] strP = new String[] {"firma"};
      objs[0] = firma;

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
  public ContenidoInfo convertirTCNAPdf(TCNInfo tcnInfo) throws InSideException {



    try {
      // activa que se introduzca el idapp en el MDC
      credentialUtil.getCredentialEeutilUserToken(wsContext).getIdApplicacion();
      return eeUtilMiscServiceBusiness.convertirTCNAPdf(tcnInfo);
    } catch (EeutilException e) {
      convertirTCNAPdfMDC(tcnInfo);
      logger.error(e.getMessage(), e);
      throw new InSideException(e.getMessage(), e);
    } catch (Exception e) {
      convertirTCNAPdfMDC(tcnInfo);
      logger.error(e.getMessage(), e);
      EstadoInfo info = new EstadoInfo(ERROR, ERROR, e.getMessage());
      throw new InSideException(e.getMessage(), info, e);
    }
  }

  /**
   * @param tcnInfo
   */
  private void convertirTCNAPdfMDC(TCNInfo tcnInfo) {
    try {
      Object[] objs = new Object[1];
      String[] strP = new String[] {"tcnInfo"};
      objs[0] = tcnInfo;

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
  public SalidaVisualizacion visualizarFacturae(byte[] factura, String version)
      throws InSideException {



    try {
      // activa que se introduzca el idapp en el MDC
      credentialUtil.getCredentialEeutilUserToken(wsContext).getIdApplicacion();
      return eeUtilMiscServiceBusiness.visualizarFacturae(factura, version);
    } catch (EeutilException e) {
      visualizarFacturaeMDC(factura, version);
      logger.error(e.getMessage(), e);
      throw new InSideException(e.getMessage(), e);
    } catch (Exception e) {
      visualizarFacturaeMDC(factura, version);
      logger.error(e.getMessage(), e);
      EstadoInfo info = new EstadoInfo(ERROR, ERROR, e.getMessage());
      throw new InSideException(e.getMessage(), info, e);
    }
  }

  /**
   * @param factura
   * @param version
   */
  private void visualizarFacturaeMDC(byte[] factura, String version) {
    try {
      Object[] objs = new Object[2];
      String[] strP = new String[] {"factura", "version"};
      objs[0] = factura;
      objs[1] = version;

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
  public SalidaVisualizacion visualizarFacturaePDF(byte[] factura, String version)
      throws InSideException {
    try {

      // activa que se introduzca el idapp en el MDC
      credentialUtil.getCredentialEeutilUserToken(wsContext).getIdApplicacion();


      return eeUtilMiscServiceBusiness.visualizarFacturaePDF(factura, version);
    } catch (EeutilException e) {
      visualizarFacturaePDFMDC(factura, version);
      logger.error(e.getMessage(), e);
      throw new InSideException(e.getMessage(), e);
    } catch (Exception e) {
      visualizarFacturaePDFMDC(factura, version);
      logger.error(e.getMessage(), e);
      EstadoInfo info = new EstadoInfo(ERROR, ERROR, e.getMessage());
      throw new InSideException(e.getMessage(), info, e);
    }
  }

  /**
   * @param factura
   * @param version
   */
  private void visualizarFacturaePDFMDC(byte[] factura, String version) {
    try {
      Object[] objs = new Object[2];
      String[] strP = new String[] {"factura", "version"};
      objs[0] = factura;
      objs[1] = version;

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
  public PdfSalida convertirPDFA(DocumentoEntrada docEntrada, Integer nivelCompilacion)
      throws InSideException {


    // activa que se introduzca el idapp en el MDC
    credentialUtil.getCredentialEeutilUserToken(wsContext).getIdApplicacion();

    try {
      AppInfo appInfo = aplicacionContext.getAplicacionInfo();

      return eeUtilMiscServiceBusiness.convertirPDFA(appInfo.getIdaplicacion(),
          appInfo.getPropiedades().get("ip.openoffice"),
          appInfo.getPropiedades().get("port.openoffice"), docEntrada, nivelCompilacion,
          docEntrada.getPassword());
    } catch (EeutilException e) {
      convertirPDFAMDC(docEntrada, nivelCompilacion);
      logger.error(e.getMessage(), e);
      throw new InSideException(e.getMessage(), e);
    } catch (Exception e) {
      convertirPDFAMDC(docEntrada, nivelCompilacion);
      logger.error(e.getMessage(), e);
      EstadoInfo info = new EstadoInfo(ERROR, ERROR, e.getMessage());
      throw new InSideException(e.getMessage(), info, e);
    }

  }

  /**
   * @param docEntrada
   * @param nivelCompilacion
   */
  private void convertirPDFAMDC(DocumentoEntrada docEntrada, Integer nivelCompilacion) {
    try {
      Object[] objs = new Object[2];
      String[] strP = new String[] {"docEntrada", "nivelCompilacion"};
      objs[0] = docEntrada;
      objs[1] = nivelCompilacion;

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
  public Boolean comprobarPDFA(DocumentoEntrada docEntrada, Integer nivelCompilacion)
      throws InSideException {


    // activa que se introduzca el idapp en el MDC
    credentialUtil.getCredentialEeutilUserToken(wsContext).getIdApplicacion();


    try {
      return eeUtilMiscServiceBusiness.comprobarPDFA(docEntrada, nivelCompilacion);
    } catch (EeutilException e) {
      comprobarPDFAMDC(docEntrada, nivelCompilacion);
      logger.error(e.getMessage(), e);
      throw new InSideException(e.getMessage(), e);
    } catch (Exception e) {
      comprobarPDFAMDC(docEntrada, nivelCompilacion);
      logger.error(e.getMessage(), e);
      EstadoInfo info = new EstadoInfo(ERROR, ERROR, e.getMessage());
      throw new InSideException(e.getMessage(), info, e);
    }
  }

  /**
   * @param docEntrada
   * @param nivelCompilacion
   */
  private void comprobarPDFAMDC(DocumentoEntrada docEntrada, Integer nivelCompilacion) {
    try {
      Object[] objs = new Object[2];
      String[] strP = new String[] {"docEntrada", "nivelCompilacion"};
      objs[0] = docEntrada;
      objs[1] = nivelCompilacion;

      Map<String, String> mParametros =
          UtilReflection.getInstance().extractMultipleDataPermitted(null, objs, strP);
      String resultado = MapUtil.mapToString(mParametros);
      MDC.put(EXTRA_PARA_M, resultado);

    } catch (IOException e1) {

      // si falla palante

    }
  }
}
