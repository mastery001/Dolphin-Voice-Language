package dv;

  
/**  
 *@Description:  变量
 *@Author:zouziwen
 *@Since:2016年6月13日
 *@Version:1.1.0  
 */
public interface Variable {
	
	/**  
	 * @return  变量名
	 * @Description:  
	 */
	public String name();

	/**  
	 * @return  变量值
	 * @Description:  
	 */
	public Object value();

	/**  
	 * @return  变量类型
	 * @Description:  
	 */
	public Class<?> type();
}
