/* Copyright (C) 2012-13 MINHAP, Gobierno de EspaÃ±a
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.mpt.dsic.inside.ws.service.codigosRespuestaValidacion;

//Código_Respuesta - Detalle respuesta
public class DocumentoENICodigosRespuestaValidacion {
	
		//Documento Estructura	Se comprobará que se responde al XSD	
		public static final String D_E_000_CODIGO  = "[D.E.000]";
		public static final String D_E_000_DETALLE = "Estructura Correcta";
		
		public static final String D_E_999_CODIGO  = "[D.E.999]";
		public static final String D_E_999_DETALLE = "Estructura Incorrecta";
		
		
		//Se comprobará la validación que devuelva @firma. Si este servicio 
		//estuviera saturado o no contestara, se contestará en este sentido, si no, 
		//se devolverá el resultado de la validación.
		public static final String D_F_000_CODIGO  = "[D.F.000]";
		public static final String D_F_000_DETALLE = "Firma Válida";
		
		public static final String D_F_001_CODIGO  = "[D.F.001]";
		public static final String D_F_001_DETALLE = "Firma Válida pero Caducada";
		
		public static final String D_F_002_CODIGO  = "[D.F.002]";
		public static final String D_F_002_DETALLE = " Firma Válida sin Timestamp";
		
		public static final String D_F_003_CODIGO  = "[D.F.003]";
		public static final String D_F_003_DETALLE = "Firma Válida no Longeva";
		
		public static final String D_F_998_CODIGO  = "[D.F.998]";
		public static final String D_F_998_DETALLE = "Servicio indisponible";
		
		public static final String D_F_999_CODIGO  = "[D.F.999]";
		public static final String D_F_999_DETALLE = "Firma Incorrecta";
		
		
		//Se comprobará la existencia de código DIR3 en una descarga que se 
		//realizará diariamente. Si no existiera, se expondrá este extremo.
		public static final String D_D_000_CODIGO  = "[D.D.000]";
		public static final String D_D_000_DETALLE = "Código DIR3 Existente";
		public static final String D_D_999_CODIGO  = "[D.D.999]";
		public static final String D_D_999_DETALLE = "Código DIR3 NO existente";
		

}
