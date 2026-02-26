package es.mpt.dsic.inside.ws.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import es.mpt.dsic.inside.security.model.ApplicationLogin;
import es.mpt.dsic.inside.ws.service.exception.InSideException;
import es.mpt.dsic.inside.ws.service.model.pdf.DocumentoEntradaMtom;
import es.mpt.dsic.inside.ws.service.model.pdf.PdfSalidaMtom;

@WebService
@BindingType(SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public interface EeUtilServiceMtom {

  @WebMethod(operationName = "comprobarPDFA", action = "urn:comprobarPDFA")
  @WebResult(name = "esPdfA", partName = "esPdfA")
  public Boolean comprobarPDFA(
      @WebParam(name = "aplicacionInfo")
      @XmlElement(required = true, name = "aplicacionInfo") ApplicationLogin info,
      @WebParam(name = "DocumentoEntradaMtom")
      @XmlElement(required = true, name = "DocumentoEntradaMtom") DocumentoEntradaMtom docEntrada,
      @XmlElement(required = false, name = "nivelCompilacion") Integer nivelCompilacion)
      throws InSideException;

  @WebMethod(operationName = "convertirPDFA", action = "urn:convertirPDFA")
  @WebResult(name = "PdfSalidaMtom", partName = "PdfSalidaMtom")
  public PdfSalidaMtom convertirPDFA(
      @WebParam(name = "aplicacionInfo")
      @XmlElement(required = true, name = "aplicacionInfo") ApplicationLogin info,
      @WebParam(name = "DocumentoEntradaMtom")
      @XmlElement(required = true, name = "DocumentoEntradaMtom") DocumentoEntradaMtom docEntrada,
      @XmlElement(required = false, name = "nivelCompilacion") Integer nivelCompilacion)
      throws InSideException;

}
