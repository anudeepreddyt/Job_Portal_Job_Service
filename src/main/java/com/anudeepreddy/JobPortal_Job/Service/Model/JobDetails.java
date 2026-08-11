package com.anudeepreddy.JobPortal_Job.Service.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String jobId;
    @NotBlank(message = "Job title should be posted")
    private String jobTitle;
    @NotBlank(message = "Job des should be posted")
    private String jobDes;
    private LocalDateTime jobPostedOn;
    @NotBlank(message = "Company Name shouldn't be blank")
    private String companyName;
    @PositiveOrZero(message = "experience shouldn't be negative")
    private Integer experienceReq;
    @Positive(message = "Salary shouldn't be negative")
    private Integer salary;
    @NotBlank(message = "Posted by shouldn't be negative")
    private String jobpostedBy;
    private Boolean active;
    @NotBlank(message = "Location shouldn't be null")
    private String location;
    @NotBlank(message = "Skills should be posted")
    private String skillReq;
    @NotNull(message = "Employement Type should be posted")
    private EmployementType employementType;
    private String postedById;
    private String userId;


}
