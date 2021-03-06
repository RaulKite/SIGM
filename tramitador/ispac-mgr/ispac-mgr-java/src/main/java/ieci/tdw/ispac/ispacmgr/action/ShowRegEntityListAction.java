package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.GenericFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class ShowRegEntityListAction extends BaseAction
{

 	public ActionForward executeAction(
 			ActionMapping mapping,
 			ActionForm form,
 			HttpServletRequest request,
 			HttpServletResponse response,
 			SessionAPI session)
 			throws Exception
	{

		ClientContext cct = session.getClientContext();
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

		IManagerAPI managerAPI=ManagerAPIFactory.getInstance().getManagerAPI(cct);

		// Estado de tramitación
    	IState state = managerAPI.currentState(getStateticket(request));

   	 	String entity=request.getParameter("entityId");
   	 	int entityId=0;
   	 	if(entity!=null)
   	 	    entityId=Integer.parseInt(entity);

   	 	if (state.getNumexp()==null)
   	 	{
   	   	 	request.setAttribute("RegEntityListFormatter",new BeanFormatter());
   	   	 	request.setAttribute("RegEntityList",new ArrayList());
   	 	    return mapping.findForward("success");
   	 	}

   	 	IItemCollection itemcol=entitiesAPI.getEntities(entityId,state.getNumexp(),null);
   	 	List regentlist=CollectionBean.getBeanList(itemcol);

   	 	if (regentlist.size()==0)
   	 	{
   	   	 	request.setAttribute("RegEntityListFormatter",new BeanFormatter());
   	   	 	request.setAttribute("RegEntityList",new ArrayList());
   	 	    return mapping.findForward("success");
   	 	}

   	 	GenericFormatterFactory formatterfactory=new GenericFormatterFactory();
   	 	ItemBean bean=(ItemBean)regentlist.get(0);
   	 	BeanFormatter beanformatter=formatterfactory.getBeanFormatter(bean.getItem());

   	 	request.setAttribute("RegEntityListFormatter",beanformatter);
   	 	request.setAttribute("RegEntityList",regentlist);

   	 	return mapping.findForward("success");
	}
}