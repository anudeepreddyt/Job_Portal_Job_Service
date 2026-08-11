package com.anudeepreddy.JobPortal_Job.Service.DTO;

import com.anudeepreddy.JobPortal_Job.Service.Model.EmployementType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobDTO {

    private String jobDes;
    private Integer salary;
    private String location;
    private Integer experience;
    private String postedBy;
    private String skillReq;
    private Boolean active;
    private String jobTitle;
    private EmployementType employementType;
    private String userId;

}
