package com.longyan.distribution.response;


public class CustomerShortView {

    private Integer id;
    private String phone;
    private String parentPhone;
    private String superParentPhone;
    private String realName;
    private String parentRealName;
    private String superParentRealName;
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getSuperParentPhone() {
        return superParentPhone;
    }

    public void setSuperParentPhone(String superParentPhone) {
        this.superParentPhone = superParentPhone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getParentRealName() {
        return parentRealName;
    }

    public void setParentRealName(String parentRealName) {
        this.parentRealName = parentRealName;
    }

    public String getSuperParentRealName() {
        return superParentRealName;
    }

    public void setSuperParentRealName(String superParentRealName) {
        this.superParentRealName = superParentRealName;
    }
}
