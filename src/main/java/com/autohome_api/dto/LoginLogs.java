package com.autohome_api.dto;

import java.io.Serializable;
import java.util.Date;

public class LoginLogs implements Serializable {
	
	private Long loginLogId;
	private String loginName;
	private String loginIp;
	private Date loginTime;

	@Override
	public String toString() {
		return "LoginLogs{" +
				"loginLogId=" + loginLogId +
				", loginName='" + loginName + '\'' +
				", loginIp='" + loginIp + '\'' +
				", loginTime=" + loginTime +
				'}';
	}

	public Long getLoginLogId() {
		return loginLogId;
	}

	public void setLoginLogId(Long loginLogId) {
		this.loginLogId = loginLogId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
}
