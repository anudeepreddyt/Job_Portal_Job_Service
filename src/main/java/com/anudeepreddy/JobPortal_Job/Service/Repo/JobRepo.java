package com.anudeepreddy.JobPortal_Job.Service.Repo;

import com.anudeepreddy.JobPortal_Job.Service.Model.EmployementType;
import com.anudeepreddy.JobPortal_Job.Service.Model.JobDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepo extends JpaRepository<JobDetails,Integer> {

    Optional<JobDetails> findByJobId(String jobId);


    Page<JobDetails> findByLocationAndJobTitleAndEmployementTypeAndCompanyName(String location, String jobTitle, EmployementType employementType, String companyName, Pageable pageable);

    Page<JobDetails> findByCompanyName(String companyName, Pageable pageable);

    Page<JobDetails> findByEmployementType(EmployementType employementType, Pageable pageable);

    Page<JobDetails> findByJobTitle(String jobTitle, Pageable pageable);

    Page<JobDetails> findByLocationAndJobTitleAndEmployementType(String location, String jobTitle, EmployementType employementType, Pageable pageable);

    Page<JobDetails> findByLocationAndJobTitleAndCompanyName(String location, String jobTitle, String companyName, Pageable pageable);

    Page<JobDetails> findByLocationAndEmployementTypeAndCompanyName(String location, EmployementType employementType, String companyName, Pageable pageable);

    Page<JobDetails> findByJobTitleAndEmployementTypeAndCompanyName(String jobTitle, EmployementType employementType, String companyName, Pageable pageable);

    Page<JobDetails> findByLocationAndJobTitle(String location, String jobTitle, Pageable pageable);

    Page<JobDetails> findByLocationAndEmployementType(String location, EmployementType employementType, Pageable pageable);

    Page<JobDetails> findByLocationAndCompanyName(String location, String companyName, Pageable pageable);

    Page<JobDetails> findByJobTitleAndEmployementType(String jobTitle, EmployementType employementType, Pageable pageable);

    Page<JobDetails> findByJobTitleAndCompanyName(String jobTitle, String companyName, Pageable pageable);

    Page<JobDetails> findByEmployementTypeAndCompanyName(EmployementType employementType, String companyName, Pageable pageable);

    Page<JobDetails> findByLocation(String location, Pageable pageable);

    Page<JobDetails> findByActiveTrue(Pageable pageable);


    Page<JobDetails> findAllByOrderByJobPostedOnDesc(Pageable pageable);

    Page<JobDetails> findBySalaryBetween(Integer minSalary, Integer maxSalary, Pageable pageable);
}
