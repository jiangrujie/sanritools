package temp.demo;
//package temp;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Collection;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.commons.lang.ObjectUtils;
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.output.Format;
//import org.jdom.output.XMLOutputter;
//
//import com.sanri.Constants;
//import com.sanri.MyClass;
//import com.sanri.file.F;
//import com.sanri.utils.StringUtil;
//import com.sanri.validate.Regex;
//import com.sanri.validate.Validate;
//
//public class XmlWriter {
//	private Document doc;
//	private String myClass;
//	
//	public XmlWriter(String pathname){
//		super(pathname);
//		doc = new Document();
//		myClass = MyClass.getInstance().toString();
//	}
//	public void build(Object object,String rootName){
//		Element root = toXML(object,rootName);
//		doc.setRootElement(root);
//	}
//	
//	private Element toXML(Object object,String rootName){
//		if(object == null){return null;}
//		Class<? extends Object> clazz = object.getClass();
//		Element root = null;
//		if(isPrimitive(clazz)){
//			rootName = Validate.isEmpty(rootName) ? StringUtil.reversalFirstLetter(clazz.getSimpleName()) : rootName;
//			root = new Element(rootName);
//			root.setAttribute("value",ObjectUtils.toString(object));
//			return root;
//		}else if(clazz.isArray()){
//			Object [] objects = (Object []) object;
//			if(Validate.isEmpty(objects)){return root;}
//			rootName = Validate.isEmpty(rootName) ? StringUtil.reversalFirstLetter(objects[0].getClass().getSimpleName()) : rootName;
//			root = new Element(rootName + "s");
//			Element el = null;
//			for (Object obj : objects) {
//				el = toXML(obj,rootName);
//				if(el != null){root.addContent(el);}
//			}
//			return root;
//		}else if(object instanceof Collection){
//			Collection<? extends Object> objects = (Collection<? extends Object>) object;
//			if(Validate.isEmpty(objects)){return root;}
//			//TODO
////			rootName = Validate.isEmpty(rootName) ? StringUtil.reversalFirstLetter(objects.get(0).getClass().getSimpleName()) : rootName;
//			root = new Element(rootName + "s");
//			Element el = null;
//			for (Object obj : objects) {
//				el = toXML(obj,rootName);
//				if(el != null){root.addContent(el);}
//			}
//			return root;
//		}else if(object instanceof Map){
//			Map<? extends Object,? extends Object> map = (Map<? extends Object, ? extends Object>) object;
//			if(Validate.isEmpty(map)){return root;}
//			rootName = Validate.isEmpty(rootName) ? "map":rootName;
//			root = new Element(rootName);
//			Element el = toXML(map.entrySet(), "entry");
//			if(el != null){
//				root.addContent(el);
//			}
//			return root;
//		}else if(object instanceof Entry){
//			//May.Entry  有四种情况
//			Entry<? extends Object, ? extends Object> entry = (Entry<? extends Object, ? extends Object>) object;
//			rootName = Validate.isEmpty(rootName) ? "entry":rootName;
//			root = new Element(rootName);
//			Object key = entry.getKey();
//			Object value = entry.getValue();
//			if(key == null){return root;}
//			if(value == null){root.setAttribute(ObjectUtils.toString(key),"");return root;}
//			//isPrimitive(key.getClass()) && isPrimitive(value.getClass())
//			if(key.getClass() == String.class && value.getClass() == String.class && !Regex.isMatch(ObjectUtils.toString(key), Regex.BEGIN_NUM)){
//				root.setAttribute(ObjectUtils.toString(key),ObjectUtils.toString(value));
//				return root;
//			}
//			if(Regex.isMatch(ObjectUtils.toString(key), Regex.BEGIN_NUM)){
//				//处理键以数字打头
//				Element keyEl = toXML(key, "key");
//				if(keyEl != null){
//					root.addContent(keyEl);
//				}
//				Element valueEl = toXML(value, "value");
//				if(valueEl != null){
//					root.addContent(valueEl);
//				}
//				return root;
//			}
//			if(!isPrimitive(key.getClass())){
//				Element keyEl = toXML(key, "key");
//				if(keyEl != null){
//					root.addContent(keyEl);
//				}
//			}
//			if(!isPrimitive(value.getClass())){
//				Element valueEl = toXML(value, "value");
//				if(valueEl != null){
//					root.addContent(valueEl);
//				}
//			}
//			return root;
//		}else{
//			//对象类型
//			root = new Element(rootName); 
//			try{
//				while(clazz != null){
//					Method[] declaredMethods = clazz.getDeclaredMethods();
//					if(!Validate.isEmpty(declaredMethods)){
//						String methodName = "";
//						Class<?> returnType = null;
//						String key = "";	//元素键
//						Object value = "";	//元素值 
//						for (Method method : declaredMethods) {
//							method.setAccessible(true);//设置方法可操作
//							methodName = method.getName();
//							if(methodName.indexOf("get") != -1){
//								//只操作 get 方法
//								returnType = method.getReturnType();
//								key = StringUtil.reversalFirstLetter(methodName.substring(3));
//								value = method.invoke(object);
//								if(value==null){continue;}//当是类本身里面有递归时,为空跳过
//								if(isPrimitive(returnType)){
//									root.setAttribute(key,ObjectUtils.toString(value));
//								}else{
//									//非原始型即为其它型,自然递归toXML 方法
//									Element el = toXML(value, key);
//									if(el != null){
//										//有可能返回 null
//										root.addContent(el);
//									}
//								}
//							}
//						}
//					}
//					clazz = clazz.getSuperclass();
//					if(clazz == null || myClass.indexOf(clazz.getSimpleName()) == -1){break;}
//				}
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
//		return root;
//	}
//	
//	@Override
//	public boolean out(OutputStream out) {
//		//生成文文件
//		Format format = Format.getCompactFormat(); //xml文件格式化输出
//		format.setEncoding(Constants.DEFAULT_CHARSET);
//		format.setIndent("    ");
//		XMLOutputter outputter=new XMLOutputter(format);
//		try {
//			outputter.output(doc, out);
//			return true;
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	public Document getDoc() {
//		return doc;
//	}
//
//	public void setDoc(Document doc) {
//		this.doc = doc;
//	}
//}
