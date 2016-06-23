package dv.utils;

import java.util.Collection;
import java.util.Iterator;

public class Printer {
	
	public static final void print(Object obj) {
		if(obj instanceof Collection) {
			int index = 1;
			StringBuffer sb = new StringBuffer();
			Collection<?> col = (Collection<?>) obj;
			for(Iterator<?> it = col.iterator(); it.hasNext();) {
				sb.append(index++).append(". ").append(it.next()).append("\n");
			}
			System.out.println(sb);
		}else {
			System.out.println(obj);
		}
	}
}
