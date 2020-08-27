package org.chang.springboot.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity {
  @Temporal(TemporalType.TIMESTAMP)
  @Column(updatable = false)
  private Date created;

  @Temporal(TemporalType.TIMESTAMP)
  private Date updated;

  @PrePersist
  protected void onCreate() {
    updated = created = new Date();
  }

  @PreUpdate
  protected void onUpdate() {
    updated = new Date();
  }

  public Date getCreated() {
    return created;
  }
  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }
}
