package com.hy.entity.system;

import com.hy.entity.Page;
import java.io.Serializable;

public class User
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long USER_ID;
  private String USERNAME;
  private String PASSWORD;
  private String NAME;
  private String RIGHTS;
  private String ROLE_ID;
  private String LAST_LOGIN;
  private String IP;
  private String STATUS;
  private Role role;
  private Page page;
  private String SKIN;
  private int ckId;
  
  public int getCkId() {
	return ckId;
}

public void setCkId(int ckId) {
	this.ckId = ckId;
}

public String getSKIN()
  {
    return this.SKIN;
  }
  
  public void setSKIN(String sKIN) { this.SKIN = sKIN; }
  
  public String getUSERNAME() {
    return this.USERNAME;
  }
  
  public void setUSERNAME(String uSERNAME) { this.USERNAME = uSERNAME; }
  
  public String getPASSWORD() {
    return this.PASSWORD;
  }
  
  public void setPASSWORD(String pASSWORD) { this.PASSWORD = pASSWORD; }
  
  public String getNAME() {
    return this.NAME;
  }
  
  public void setNAME(String nAME) { this.NAME = nAME; }
  
  public String getRIGHTS() {
    return this.RIGHTS;
  }
  
  public void setRIGHTS(String rIGHTS) { this.RIGHTS = rIGHTS; }
  
  public String getROLE_ID() {
    return this.ROLE_ID;
  }
  
  public void setROLE_ID(String rOLE_ID) { this.ROLE_ID = rOLE_ID; }
  
  public String getLAST_LOGIN() {
    return this.LAST_LOGIN;
  }
  
  public void setLAST_LOGIN(String lAST_LOGIN) { this.LAST_LOGIN = lAST_LOGIN; }
  
  public String getIP() {
    return this.IP;
  }
  
  public void setIP(String iP) { this.IP = iP; }
  
  public String getSTATUS() {
    return this.STATUS;
  }
  
  public void setSTATUS(String sTATUS) { this.STATUS = sTATUS; }
  
  public Role getRole()
  {
    return this.role;
  }
  
  public void setRole(Role role) { this.role = role; }
  
  public Page getPage() {
    if (this.page == null)
      this.page = new Page();
    return this.page;
  }
  
  public void setPage(Page page) { this.page = page; }
  
  public long getUSER_ID() {
    return this.USER_ID;
  }
  
  public void setUSER_ID(long uSER_ID) { this.USER_ID = uSER_ID; }
}


