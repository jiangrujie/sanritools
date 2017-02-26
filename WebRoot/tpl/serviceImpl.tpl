package ${serviceImplPackage};

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wifi.commons.exception.RunanException;
import com.wifi.commons.util.web.HandlerResult;

import ${daoPackage}.${entity}Mapper;
import ${servicePackage}.I${entity}Service;

import ${voPackage}.${entity};

@Service("${lowEntity}Service")
public class ${entity}ServiceImpl implements I${entity}Service {

	@Autowired
	private ${entity}Mapper ${lowEntity}Mapper;
	
	@Override
	public void add${entity}(${entity} entity) {
        ${lowEntity}Mapper.add${entity}(entity);
	}

	@Override
	public HandlerResult get${entity}Page(Map<String, Object> params) throws RunanException {
		HandlerResult hr = new HandlerResult();
		hr.setResultObj(${lowEntity}Mapper.get${entity}Page(params));
		return hr;
	}

	@Override
	public void update(${entity} ${lowEntity}){
		${lowEntity}Mapper.update(${lowEntity});
	}

	@Override
	public ${entity} query${entity}ById(Integer id) {
		return ${lowEntity}Mapper.query${entity}ById(id);
	}

	@Override
	public void delete(String[] ids){
		${lowEntity}Mapper.delete(ids);
	}
	
	@Override
	public List<${entity}> listAll() {
		return ${lowEntity}Mapper.listAll();
	}
}