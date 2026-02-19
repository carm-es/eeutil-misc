package es.mpt.dsic.inside.ws.service.model;

import java.util.List;

import es.mpt.dsic.inside.model.peticion.PeticionObject;

public class PeticionesResponse {

  List<PeticionObject> data;

  public List<PeticionObject> getData() {
    return data;
  }

  public void setData(List<PeticionObject> data) {
    this.data = data;
  }


}
