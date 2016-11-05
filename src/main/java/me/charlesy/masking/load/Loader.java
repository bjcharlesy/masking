/**
 * 
 */
package me.charlesy.masking.load;

/**
 * @author Charles Young
 *
 */
public interface Loader extends Loadable {

	/**
	 * 设置每次读取数量
	 */
	public void setLimit();

	/**
	 * 获取每次读取数量
	 */
	public void getLimit();

}
