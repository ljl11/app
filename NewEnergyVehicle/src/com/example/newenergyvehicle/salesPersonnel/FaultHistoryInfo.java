package com.example.newenergyvehicle.salesPersonnel;

import java.io.Serializable;

public class FaultHistoryInfo implements Serializable {
    private String hour;
    private String year;
    private String distribution;
    private String distributionId;
    private String reception;
    private String receptionId;
    private String type;
    private String faultDistributionId;
    private String disDescription;
    
	public String getDisDescription() {
		return disDescription;
	}
	public void setDisDescription(String disDescription) {
		this.disDescription = disDescription;
	}
	public String getFaultDistributionId() {
		return faultDistributionId;
	}
	public void setFaultDistributionId(String faultDistributionId) {
		this.faultDistributionId = faultDistributionId;
	}
	public String getReceptionId() {
		return receptionId;
	}
	public void setReceptionId(String receptionId) {
		this.receptionId = receptionId;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDistribution() {
		return distribution;
	}
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	public String getReception() {
		return reception;
	}
	public void setReception(String reception) {
		this.reception = reception;
	}
	public String getDistributionId() {
		return distributionId;
	}
	public void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
