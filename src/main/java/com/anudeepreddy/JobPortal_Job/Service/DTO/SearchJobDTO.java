package com.anudeepreddy.JobPortal_Job.Service.DTO;

import com.anudeepreddy.JobPortal_Job.Service.Model.EmployementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchJobDTO {


    private String jobTitle;
    private String jobDes;
    private LocalDateTime jobPostedOn;
    private String companyName;
    private Integer experienceReq;
    private Integer salary;
    private String jobPostedBy;
    private Boolean active;
    private String location;
    private String skillReq;
    private EmployementType employementType;

}
