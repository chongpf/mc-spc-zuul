package com.chong.mcspczuul.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="value:Profile实体",description = "ProfileEntity放profileName")
public class ProfileEntity {

    @ApiModelProperty(value = "value:profile名字",name = "name:profile名字",notes = "notes:profile名字")
    private String profileName;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
