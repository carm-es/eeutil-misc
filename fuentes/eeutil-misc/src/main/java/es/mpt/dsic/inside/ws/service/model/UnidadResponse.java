package es.mpt.dsic.inside.ws.service.model;

import java.util.List;

import es.mpt.dsic.inside.model.unidad.UnidadObject;

public class UnidadResponse {

  List<UnidadObject> data;

  public List<UnidadObject> getData() {
    return data;
  }

  public void setData(List<UnidadObject> data) {
    this.data = data;
  }


}
