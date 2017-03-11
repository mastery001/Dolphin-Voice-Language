package dv.toJava;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dv.interpreter.JavaFunction;
import dv.utils.TypeUtils;

public class RuntimeJavaFunction extends JavaFunction{

	private final Map<String , Method> methodCache = new HashMap<String , Method>();
	
	private final Object obj;
	
	private final String methodName;
	
	private final boolean generic;
	
	public RuntimeJavaFunction(String name , Object obj , String methodName) {
		super(name);  
		this.obj = obj;
		this.methodName = methodName;
		generic = TypeUtils.getParameterizedTypes(obj) == null ? false : true;
	}

	@Override
	protected Object invoke0(Object[] params) throws Exception {
		Class<?>[] parameterTypes = new Class<?>[params.length];
		StringBuilder sb = new StringBuilder(methodName);
		for(int i =0 ; i < params.length ; i++) {
			if(generic) {
				parameterTypes[i] = Object.class;
			}else {
				parameterTypes[i] = params[i].getClass();
			}
			sb.append("_").append(parameterTypes[i].getSimpleName());
		}
		String cacheKey = sb.toString();
		Method method = methodCache.get(cacheKey);
		if(method == null) {
			method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
			methodCache.put(cacheKey, method);
		}
		return method.invoke(obj, params);
	}

}
