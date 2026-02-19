package es.mpt.dsic.inside.ws.service.model.pdf;

import javax.activation.DataHandler;

public interface IPdfSalida {

  public void setContenido(byte[] contenido);

  public void setMime(String mime);

  public void setContenido(DataHandler contenido);

}
