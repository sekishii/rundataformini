package com.leadingsoft.rundata.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "run_data")
public class RunDataEntity {

  /**
   * id
   */
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "wechat_id")
  private String wechatID;

  @Id
  @Column(name = "user_name")
  private String userName;

  @Column(name = "status")
  private String status;

  @Column(name = "run_date")
  private Date runDate;

  @Column(name = "run_steps")
  private int runSteps;



}
