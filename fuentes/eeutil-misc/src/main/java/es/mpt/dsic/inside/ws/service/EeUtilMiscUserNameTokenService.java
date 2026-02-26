package es.mpt.dsic.inside.ws.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import es.mpt.dsic.inside.ws.service.exception.InSideException;
import es.mpt.dsic.inside.ws.service.model.ContenidoInfo;
import es.mpt.dsic.inside.ws.service.model.SalidaVisualizacion;
import es.mpt.dsic.inside.ws.service.model.TCNInfo;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntrada;
import es.mpt.dsic.inside.ws.service.model.pdf.PdfSalida;

@WebService
public interface EeUtilMiscUserNameTokenService {

  @WebMethod(operationName = "postProcesarFirma", action = "urn:postProcesarFirma")
  @WebResult(name = "FirmaProcesada", partName = "FirmaProcesada")
  public byte[] postProcesarFirma(
      @WebParam(name = "Firma") @XmlElement(required = true, name = "Firma") byte[] firma)
      throws InSideException;

  @WebMethod(operationName = "convertirTCNAPdf", action = "urn:convertirTCNAPdf")
  @WebResult(name = "contenidoInfo", partName = "contenidoInfo")
  public ContenidoInfo convertirTCNAPdf(
      @WebParam(name = "TCNInfo") @XmlElement(required = true, name = "TCNInfo") TCNInfo tcnInfo)
      throws InSideException;

  @WebMethod(operationName = "visualizarFacturae", action = "urn:visualizarFacturae")
  @WebResult(name = "FacturaProcesada", partName = "FacturaProcesada")
  public SalidaVisualizacion visualizarFacturae(
      @WebParam(name = "Factura") @XmlElement(required = true, name = "Factura") byte[] factura,
      @XmlElement(required = false, name = "Version") String version) throws InSideException;


  @WebMethod(operationName = "visualizarFacturaePDF", action = "urn:visualizarFacturaePDF")
  @WebResult(name = "FacturaProcesada", partName = "FacturaProcesada")
  public SalidaVisualizacion visualizarFacturaePDF(
      @WebParam(name = "Factura") @XmlElement(required = true, name = "Factura") byte[] factura,
      @XmlElement(required = false, name = "Version") String version) throws InSideException;


  @WebMethod(operationName = "comprobarPDFA", action = "urn:comprobarPDFA")
  @WebResult(name = "esPdfA", partName = "esPdfA")
  public Boolean comprobarPDFA(
      @WebParam(name = "DocumentoEntrada")
      @XmlElement(required = true, name = "DocumentoEntrada") DocumentoEntrada docEntrada,
      @XmlElement(required = false, name = "nivelCompilacion") Integer nivelCompilacion)
      throws InSideException;

  @WebMethod(operationName = "convertirPDFA", action = "urn:convertirPDFA")
  @WebResult(name = "PdfSalida", partName = "PdfSalida")
  public PdfSalida convertirPDFA(
      @WebParam(name = "DocumentoEntrada")
      @XmlElement(required = true, name = "DocumentoEntrada") DocumentoEntrada docEntrada,
      @XmlElement(required = false, name = "nivelCompilacion") Integer nivelCompilacion)
      throws InSideException;

}
