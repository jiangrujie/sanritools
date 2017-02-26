package ${controllerPackage};

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wifi.commons.exception.RunanException;
import com.wifi.commons.util.page.PageBound;
import com.wifi.commons.util.page.PageContext;
import com.wifi.commons.util.web.HandlerResult;
import com.wifi.commons.ActionOpeMessage;
import com.wifi.commons.Constants;
import com.wifi.commons.LogAction;
import com.wifi.commons.SessionManager;
import com.wifi.commons.annotation.RecordLog;
import com.wifi.service.base.StaticResService;

import ${voPackage}.${entity};
import ${servicePackage}.I${entity}Service;

@Controller
@RequestMapping(value = "/info/${lowEntity}.do")
public class ${entity}Controller {

	@Autowired
	private I${entity}Service ${lowEntity}Service;

	@RequestMapping(params = ("add"))
	@RecordLog(value = "新增${chineseEntity}",action= LogAction.ADD)
	public ModelAndView add(${entity} ${lowEntity}, HttpServletRequest request,
			HttpServletResponse response) throws RunanException {
		${lowEntity}Service.add${entity}(${lowEntity});
		return this.toAdd(request, response);
	}

	@RequestMapping(params = ("toAdd"))
	public ModelAndView toAdd(HttpServletRequest request,
			HttpServletResponse response) throws RunanException {
		ModelAndView mv = new ModelAndView("/info/${lowEntity}/${lowEntity}AddUI");
		return mv;
	}

	@RequestMapping(params = ("list"))
	public ModelAndView list(${entity} ${lowEntity}, HttpServletRequest request,
			HttpServletResponse response) throws RunanException {
		ModelAndView mv = new ModelAndView("/info/${lowEntity}/${lowEntity}ListUI");
		PageContext page = PageBound.ProcessPageParam(request);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("${lowEntity}", ${lowEntity});
		HandlerResult rs = ${lowEntity}Service.get${entity}Page(params);
		mv.addObject("list", rs.getResultObj());
		mv.addObject("page", page);
		page.setPagination(false);
		return mv;
	}

	@RequestMapping(params = ("edit"))
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws RunanException {
		String id = ObjectUtils.toString(request.getParameter("id"),"0");
		ModelAndView mv = new ModelAndView("/info/${lowEntity}/${lowEntity}EditUI");
		mv.addObject("${lowEntity}", ${lowEntity}Service.query${entity}ById(Integer.parseInt(id)));
		return mv;
	}

	@RequestMapping(params = ("update"))
	@RecordLog(value = "修改${chineseEntity}",action= LogAction.UPDATE)
	public ModelAndView update(${entity} ${lowEntity}, HttpServletRequest request,
			HttpServletResponse response) throws RunanException {
		${lowEntity}Service.update(${lowEntity});
		ModelAndView mv = new ModelAndView("/info/${lowEntity}/${lowEntity}EditUI");
		return mv;
	}

	@ResponseBody
	@RequestMapping(params = ("delete"))
	@RecordLog(value = "删除${chineseEntity}",action= LogAction.DELETE)
	public void delete(HttpServletRequest request,
			HttpServletResponse response) throws RunanException {
		String[] ids = request.getParameterValues("ids");
		${lowEntity}Service.delete(ids);
	}
}
