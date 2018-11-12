package entity;

import java.io.Serializable;

/**
 * 返回结果
 * 
 * @ClassName: Result
 * 
 * @Description: TODO
 * 
 * @author: zmz
 * 
 * @date: 2018年11月11日 上午10:14:39
 */
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean success;// 是否成功
	private String message;// 返回信息
	
	

	public Result(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
