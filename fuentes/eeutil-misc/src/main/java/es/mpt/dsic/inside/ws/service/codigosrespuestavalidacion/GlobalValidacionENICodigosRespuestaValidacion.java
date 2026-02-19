package es.mpt.dsic.inside.ws.service.codigosrespuestavalidacion;

// C�digo_Respuesta - Detalle respuesta
public class GlobalValidacionENICodigosRespuestaValidacion {

  // Hide public constructor
  private GlobalValidacionENICodigosRespuestaValidacion() {

  }

  // C�digo de la validaci�n global
  public static final String G_000_CODIGO = "[G.000]";
  public static final String G_001_CODIGO = "[G.001]";
  public static final String G_999_CODIGO = "[G.999]";

  public static final String G_000_DETALLE = "Sin problemas";
  public static final String G_001_DETALLE = "Sin problemas con advertencias";
  public static final String G_999_DETALLE = "Existe alg�n problema";

  public static final String G_EXX_CODIGO = "[G.EXX]";
  public static final String G_DXX_CODIGO = "[G.DXX]";
  public static final String G_SXX_CODIGO = "[G.SXX]";
  public static final String G_FXX_CODIGO = "[G.FXX]";
  public static final String G_XXX_CODIGO = "[G.XXX]";
  public static final String G_XXX_DETALLE = "Error interno. Contacte con el administrador";

  public static final String G_ZZZ_ID_OBJETO_ERROR_VALIDACIONES =
      "NO SE HA SOLICITADO HACER ALGUNA VALIDACION";
}
