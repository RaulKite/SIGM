/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
* Solo podr� usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
* V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.map.directorio.ws.manager.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import es.map.directorio.ws.manager.impl.wsexport.ObjectFactory;
import es.map.directorio.ws.manager.impl.wsexport.RespuestaWS;
import es.map.directorio.ws.manager.impl.wsexport.UnNoOrganicasWs;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "SD03NO_DescargaUnNoOrganicas", targetNamespace = "http://impl.manager.ws.directorio.map.es")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SD03NODescargaUnNoOrganicas {


    /**
     * 
     * @param exportarRequest
     * @return
     *     returns es.map.directorio.ws.manager.impl.wsexport.RespuestaWS
     */
    @WebMethod
    @WebResult(name = "exportarReturn", partName = "exportarReturn")
    public RespuestaWS exportar(
        @WebParam(name = "exportarRequest", partName = "exportarRequest")
        UnNoOrganicasWs exportarRequest);

    /**
     * 
     * @param exportarRequest
     * @return
     *     returns es.map.directorio.ws.manager.impl.wsexport.RespuestaWS
     */
    @WebMethod
    @WebResult(name = "exportarReturn", partName = "exportarReturn")
    public RespuestaWS exportarV2(
        @WebParam(name = "exportarRequest", partName = "exportarRequest")
        UnNoOrganicasWs exportarRequest);

}