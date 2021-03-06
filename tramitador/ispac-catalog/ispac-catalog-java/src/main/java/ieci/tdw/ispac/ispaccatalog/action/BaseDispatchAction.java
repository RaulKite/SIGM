package ieci.tdw.ispac.ispaccatalog.action;


import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * Clase abstracta que a�ade la funcionalidad de la clase DispatchAction de
 * Struts.
 *
 */
public abstract class BaseDispatchAction extends BaseAction {

    protected Class clazz;
    protected static Logger log = Logger.getLogger(BaseDispatchAction.class);
    protected static MessageResources messages = 
    	MessageResources.getMessageResources("org.apache.struts.actions.LocalStrings");
    protected HashMap methods;
    protected Class types[];

    
    /**
     * Constructor.
     *
     */
    public BaseDispatchAction() {
        this.clazz = getClass();
        this.methods = new HashMap();
        this.types = (new Class[] {
            ActionMapping.class, 
            ActionForm.class, 
            HttpServletRequest.class, 
            HttpServletResponse.class,
            SessionAPI.class
        });
    }


	/**
	 * Acci�n principal de la acci�n
	 * @param mapping Objeto ActionMapping.
	 * @param form Objeto ActionForm.
	 * @param request Petici�n HTTP que se est� procesando.
	 * @param response Respuesta HTTP que se est� procesando.
	 * @param session Informaci�n de la sesi�n.
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, 
			SessionAPI session) throws Exception {

        if (isCancelled(request)) {
			ActionForward af = cancelled(mapping, form, request, response, 
					session);
			if (af != null) {
				return af;
			}
		}
        
		String parameter = mapping.getParameter();
		if (parameter == null) {
			String message = messages.getMessage("dispatch.handler", 
					mapping.getPath());
			log.error(message);
			throw new ServletException(message);
		}
		
		String name = getMethodName(mapping, form, request, response, parameter);
		if ("execute".equals(name) || "perform".equals(name)) {
			String message = messages.getMessage("dispatch.recursive", 
					mapping.getPath());
			log.error(message);
			throw new ServletException(message);
		} else {
			return dispatchMethod(mapping, form, request, response, session, 
					name);
		}
    }

    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, 
    			HttpServletRequest request, HttpServletResponse response,
    			SessionAPI session)
    		throws Exception {
        String message = messages.getMessage("dispatch.parameter", 
        		mapping.getPath(), mapping.getParameter());
        log.error(message);
        throw new ServletException(message);
    }

    protected ActionForward cancelled(ActionMapping mapping, ActionForm form, 
    			HttpServletRequest request, HttpServletResponse response,
    			SessionAPI session)
        	throws Exception {
        return null;
    }

    protected ActionForward dispatchMethod(ActionMapping mapping, 
    			ActionForm form, HttpServletRequest request, 
    			HttpServletResponse response, SessionAPI session, String name) 
    		throws Exception {
    	
        if(name == null) {
            return unspecified(mapping, form, request, response, session);
        }
        
        Method method = null;
        try {
			method = getMethod(name);
		} catch (NoSuchMethodException e) {
			
			String message = messages.getMessage("dispatch.method", 
					mapping.getPath(), name);
			log.error(message, e);
			throw e;
		}
		
		ActionForward forward = null;
		try {
			Object args[] = { mapping, form, request, response, session };
			forward = (ActionForward) method.invoke(this, args);
		} catch (ClassCastException e) {
			String message = messages.getMessage("dispatch.return", 
					mapping.getPath(), name);
			log.error(message, e);
			throw e;
		} catch (IllegalAccessException e) {
			String message = messages.getMessage("dispatch.error", 
					mapping.getPath(), name);
			log.error(message, e);
			throw e;
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof Exception) {
				throw (Exception) t;
			} else {
				String message = messages.getMessage("dispatch.error", 
						mapping.getPath(), name);
				log.error(message, e);
				throw new ServletException(t);
			}
		}
        return forward;
    }

    protected Method getMethod(String name) throws NoSuchMethodException {
    	
        Method method = (Method) methods.get(name);
        if(method == null) {
            method = clazz.getMethod(name, types);
            methods.put(name, method);
        }
        return method;
    }

    protected String getMethodName(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response, 
    		String parameter) throws Exception {
        return request.getParameter(parameter);
    }
    
    public  class ParIdPos {
    	public String id;
    	public int pos;

    	public ParIdPos(String id, int pos){
    		this.id = id;
    		this.pos = pos;
    	}
    	public String toString(){
    		return "[ " +this.id + " , " + this.pos + " ]";
    	}
    }
    
    public List moveUp(List originalPars, int initPos, int lastPos, int numPosAmover){
		List parsToUpdate = new ArrayList();
		if (originalPars!=null){
			int numPosADesplazar = Math.abs(numPosAmover);
			int numElementosADesplazar = Math.abs(lastPos - initPos)+1;
			for (int i = (initPos - numPosADesplazar)-1; i < initPos-1; i++) {
				ParIdPos par = (ParIdPos)originalPars.get(i);
				parsToUpdate.add(new ParIdPos(par.id, par.pos + numElementosADesplazar));
		
			}
			for (int i = initPos -1 ; i < lastPos; i++) {
				ParIdPos par = (ParIdPos)originalPars.get(i);
				parsToUpdate.add(new ParIdPos(par.id, par.pos - numPosADesplazar));
			}
		}
		return parsToUpdate;
	}
	
	public List moveElements(List originalPars, int initPos, int lastPos, int numPosAmover)
			throws ISPACException {
		boolean desplazamientoHaciaArriba = (numPosAmover < 0);

		if (desplazamientoHaciaArriba ){
			if (initPos == 1)
				throw new ISPACInfo("exception.info.impossibleOperation");
		}else{
			if (lastPos == originalPars.size())
				throw new ISPACInfo("exception.info.impossibleOperation");
		}
		//

		List parsToUpdate = new ArrayList();
		if (originalPars != null) {

			if (desplazamientoHaciaArriba) {
				parsToUpdate = moveUp(originalPars, initPos, lastPos, numPosAmover);
			} else {
				parsToUpdate = moveUp(originalPars, lastPos + 1,
						(lastPos + Math.abs(numPosAmover)), -1 * (lastPos + 1 - initPos));
			}
		}
		return parsToUpdate;
	}


}