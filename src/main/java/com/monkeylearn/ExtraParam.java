package com.monkeylearn;


public class ExtraParam {

  private String paramName;
  private String paramValue;

  public ExtraParam(String paramName, String paramValue) {
      this.paramName = paramName;
      this.paramValue = paramValue;
  }

  public String getParamName() {
      return this.paramName;
  }

  public String getParamValue() {
      return this.paramValue;
  }
}
