package ${daoPackage};

import java.util.List;
import java.util.Map;

import ${voPackage}.${entity};

public interface ${entity}Mapper {
	//增删改查
	void add${entity}(${entity} entity);
	List<${entity}> get${entity}Page(Map<String, Object> params);
	void update(${entity} entity);
	void delete(String[] ids);
	
	//列出所有
	List<${entity}> listAll();
	//通过 id 获取
	${entity} query${entity}ById(Integer id);
}
