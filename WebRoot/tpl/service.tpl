package ${servicePackage};

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.wifi.commons.exception.RunanException;
import com.wifi.commons.util.web.HandlerResult;

import ${voPackage}.${entity};

public interface I${entity}Service {
	//增删改查
	@Transactional
	void add${entity}(${entity} entity);
	@Transactional(readOnly = true)
	HandlerResult get${entity}Page(Map<String, Object> params) throws RunanException;
	@Transactional
	void update(${entity} entity);
	@Transactional
	void delete(String[] ids);
	
	//列出所有
	@Transactional(readOnly = true)
	List<${entity}> listAll();
	//通过 id 获取
	@Transactional(readOnly = true)
	${entity} query${entity}ById(Integer id);
}
