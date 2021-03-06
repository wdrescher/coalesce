package com.incadence.coalesce.framework.persistor.performance;

import java.io.Serializable;

public class TimeTrack implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _appStartTime;
	private String _appStopTime;
	private String _entityID;
	private String _iterationInterval;
	private String _iterationVal;
	private String _startTime;
	private String _threadID;
	private String _stopTime;
	private String _startMSTime;
	private String _stopMSTime;

	public String getStartMSTime() {
		return _startMSTime;
	}

	public void setStartMSTime(String _startMSTime) {
		this._startMSTime = _startMSTime;
	}

	public String getStopMSTime() {
		return _stopMSTime;
	}

	public void setStopMSTime(String _stopMSTime) {
		this._stopMSTime = _stopMSTime;
	}

	public String getAppStartTime() {
		return _appStartTime;
	}

	public String getAppStopTime() {
		return _appStopTime;
	}

	public String getEntityID() {
		return _entityID;
	}

	public String getIterationInterval() {
		return _iterationInterval;
	}

	public String getIterationVal() {
		return _iterationVal;
	}

	public String getStartTime() {
		return _startTime;
	}

	public String getStopTime() {
		return _stopTime;
	}

	public String getThread() {
		return _threadID;
	}

	public void setAppStartTime(String _appStartTime) {
		this._appStartTime = _appStartTime;
	}

	public void setAppStopTime(String _appStopTime) {
		this._appStopTime = _appStopTime;
	}

	public void setEntityID(String _entityID) {
		this._entityID = _entityID;
	}

	public void setIterationInterval(String _iterationInterval) {
		this._iterationInterval = _iterationInterval;
	}

	public void setIterationVal(String _iterationVal) {
		this._iterationVal = _iterationVal;
	}

	public void setStartTime(String _startTime) {
		this._startTime = _startTime;
	}

	public void setStopTime(String _stopTime) {
		this._stopTime = _stopTime;
	}

	public void setThread(String _threadID) {
		this._threadID = _threadID;
	}
}
