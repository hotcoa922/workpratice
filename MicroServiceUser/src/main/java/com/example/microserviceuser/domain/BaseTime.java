package com.example.microserviceuser.domain;

import java.time.LocalDateTime;

public abstract class BaseTime {

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}