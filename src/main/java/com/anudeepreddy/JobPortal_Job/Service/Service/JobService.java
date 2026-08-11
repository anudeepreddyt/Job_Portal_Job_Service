package com.anudeepreddy.JobPortal_Job.Service.Service;

import com.anudeepreddy.JobPortal_Job.Service.Client.UserClient;
import com.anudeepreddy.JobPortal_Job.Service.DTO.*;
import com.anudeepreddy.JobPortal_Job.Service.Exception.JobNotFoundException;
import com.anudeepreddy.JobPortal_Job.Service.Exception.OnlyAdminException;
import com.anudeepreddy.JobPortal_Job.Service.Model.EmployementType;
import com.anudeepreddy.JobPortal_Job.Service.Model.JobDetails;
import com.anudeepreddy.JobPortal_Job.Service.Model.Role;
import com.anudeepreddy.JobPortal_Job.Service.Repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class JobService {

    @Autowired
    private JobRepo jobRepo;
    @Autowired
    private UserClient userClient;

    private SearchJobDTO convertToDTO(JobDetails job){
        return new SearchJobDTO(
                job.getJobTitle(),
                job.getJobDes(),
                job.getJobPostedOn(),
                job.getCompanyName(),
                job.getExperienceReq(),
                job.getSalary(),
                job.getJobpostedBy(),
                job.getActive(),
                job.getLocation(),
                job.getSkillReq(),
                job.getEmployementType()
        );
    }

    public JobDetails addJob( JobDetails jobDetails) {
        FeignUserDTO user=userClient.getInternalUserRole(jobDetails.getUserId());
        if(user.getRole()!= Role.ADMIN)
            throw new OnlyAdminException("Only admin can job!!!");
        jobDetails.setPostedById(user.getUserId());
        jobDetails.setJobPostedOn(LocalDateTime.now());
        jobDetails.setJobId("JOB"+ UUID.randomUUID().toString().substring(0,5));
       return jobRepo.save(jobDetails);
    }

    public void updateJob(String jobId, UpdateJobDTO updateJobDTO) {
        FeignUserDTO user=userClient.getInternalUserRole(updateJobDTO.getUserId());
        if(user.getRole()!=Role.ADMIN)
            throw new OnlyAdminException("Only Admin can update Job");
        JobDetails jobDetails=jobRepo.findByJobId(jobId).orElseThrow(()->new JobNotFoundException("Unable fetch job by this job id"));
        if(!jobDetails.getJobpostedBy().equals(user.getUserId()))
            throw new OnlyAdminException("Only same admin can updated job!!!");
        if(updateJobDTO.getJobDes()!=null && !updateJobDTO.getJobDes().isBlank()){
            jobDetails.setJobDes(updateJobDTO.getJobDes());
        }

        if(updateJobDTO.getLocation() != null && !updateJobDTO.getLocation().isBlank()){
            jobDetails.setLocation(updateJobDTO.getLocation());
        }

        if(updateJobDTO.getJobTitle()!=null && !updateJobDTO.getJobTitle().isBlank()){
            jobDetails.setJobTitle(updateJobDTO.getJobTitle());
        }

        if(updateJobDTO.getSalary()>0){
            jobDetails.setSalary(updateJobDTO.getSalary());
        }

        if(updateJobDTO.getExperience()>0){
            jobDetails.setExperienceReq(updateJobDTO.getExperience());
        }

        if(!updateJobDTO.getPostedBy().isBlank() && updateJobDTO.getPostedBy() != null){
            jobDetails.setJobpostedBy(updateJobDTO.getPostedBy());
        }

        jobDetails.setJobPostedOn(LocalDateTime.now());

        if(updateJobDTO.getSkillReq() != null && !updateJobDTO.getSkillReq().isBlank()){
            jobDetails.setSkillReq(updateJobDTO.getSkillReq());
        }

        if(updateJobDTO.getActive()!=null){
            jobDetails.setActive(updateJobDTO.getActive());
        }

        if(updateJobDTO.getEmployementType() != null){
            jobDetails.setEmployementType(updateJobDTO.getEmployementType());
        }

        jobRepo.save(jobDetails);
    }

    public Page<JobDetails> getJob(Pageable pageable) {
        return jobRepo.findAll(pageable);
    }

    public void deleteJob(String jobId, DeleteJobDTO deleteJobDTO) {
        FeignUserDTO user=userClient.getInternalUserRole(deleteJobDTO.getUserId());
        if(user.getRole()!=Role.ADMIN)
            throw new OnlyAdminException("Only admin can delete this job");
        JobDetails jobDetails=jobRepo.findByJobId(jobId).orElseThrow(()-> new JobNotFoundException("No such job found by this id"));
        if(!jobDetails.getPostedById().equals(user.getUserId()))
            throw new OnlyAdminException("Only same admin can delete posted job!!!");
        jobRepo.delete(jobDetails);
    }

    public Page<SearchJobDTO> searchJob(Pageable pageable, String location, String jobTitle, EmployementType employementType, String companyName,Integer minSalary,Integer maxSalary) {
        boolean hasLocation=location !=null && !location.isBlank();
        boolean hasJobTitle=jobTitle !=null && !jobTitle.isBlank();
        boolean hasEmployementType= employementType!=null;
        boolean hasCompanyName= companyName !=null && companyName.isBlank();
        boolean hasMinSalary= minSalary!=null;
        boolean hasMaxSalary = maxSalary!=null;



        if(hasMinSalary && hasMaxSalary){
            Page<JobDetails> jobs=jobRepo.findBySalaryBetween(minSalary,maxSalary,pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No jobs found in such salary range");
            return jobs.map(job->convertToDTO(job));
        }
        if (hasLocation && hasJobTitle && hasEmployementType && hasCompanyName) {

            Page<JobDetails> jobs= jobRepo.findByLocationAndJobTitleAndEmployementTypeAndCompanyName(
                    location, jobTitle, employementType, companyName, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
           return jobs.map(job->convertToDTO(job));
        }

        if (hasLocation && hasJobTitle && hasEmployementType) {

            Page<JobDetails> jobs= jobRepo.findByLocationAndJobTitleAndEmployementType(
                    location, jobTitle, employementType, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasLocation && hasJobTitle && hasCompanyName) {

            Page<JobDetails> jobs= jobRepo.findByLocationAndJobTitleAndCompanyName(
                    location, jobTitle, companyName, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasLocation && hasEmployementType && hasCompanyName) {

            Page<JobDetails> jobs= jobRepo.findByLocationAndEmployementTypeAndCompanyName(
                    location, employementType, companyName, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasJobTitle && hasEmployementType && hasCompanyName) {

            Page<JobDetails> jobs= jobRepo.findByJobTitleAndEmployementTypeAndCompanyName(
                    jobTitle, employementType, companyName, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasLocation && hasJobTitle) {
            Page<JobDetails> jobs= jobRepo.findByLocationAndJobTitle(
                    location, jobTitle, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasLocation && hasEmployementType) {

            Page<JobDetails> jobs= jobRepo.findByLocationAndEmployementType(
                    location, employementType, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasLocation && hasCompanyName) {

            Page<JobDetails> jobs =jobRepo.findByLocationAndCompanyName(
                    location, companyName, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasJobTitle && hasEmployementType) {

            Page<JobDetails> jobs= jobRepo.findByJobTitleAndEmployementType(
                    jobTitle, employementType, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasJobTitle && hasCompanyName) {

            Page<JobDetails> jobs= jobRepo.findByJobTitleAndCompanyName(
                    jobTitle, companyName, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasEmployementType && hasCompanyName) {

            Page<JobDetails> jobs= jobRepo.findByEmployementTypeAndCompanyName(
                    employementType, companyName, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }


        if (hasLocation) {

            Page<JobDetails> jobs= jobRepo.findByLocation(location, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasJobTitle) {

            Page<JobDetails> jobs= jobRepo.findByJobTitle(jobTitle, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasEmployementType) {

            Page<JobDetails> jobs= jobRepo.findByEmployementType(employementType, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        if (hasCompanyName) {

            Page<JobDetails> jobs= jobRepo.findByCompanyName(companyName, pageable);
            if(jobs.isEmpty())
                throw new JobNotFoundException("No such job Found");
            return jobs.map(job->convertToDTO(job));
        }

        throw new JobNotFoundException("Please provide at least one search filter.");

    }

    public Page<SearchJobDTO> getAllActiveJobs(Pageable pageable) {
        Page<JobDetails> jobs=jobRepo.findByActiveTrue(pageable);
        if(jobs.isEmpty())
            throw new JobNotFoundException("No active jobs found!!!");
        return jobs.map(job->convertToDTO(job));
    }

    public Page<SearchJobDTO> latestJobs(Pageable pageable) {
        Page<JobDetails> jobs=jobRepo.findAllByOrderByJobPostedOnDesc(pageable);
        if(jobs.isEmpty())
            throw new JobNotFoundException("No latest jobs found!!!");
        return jobs.map(job-> convertToDTO(job));
    }

    public JobDetails patchUpdateJob(String jobId, UpdateJobDTO updateJobDTO){

        JobDetails job = jobRepo.findByJobId(jobId)
                .orElseThrow(() ->
                        new JobNotFoundException("Job Not Found"));

        if(updateJobDTO.getJobTitle()!=null &&
                !updateJobDTO.getJobTitle().isBlank()){
            job.setJobTitle(updateJobDTO.getJobTitle());
        }

        if(updateJobDTO.getJobDes()!=null &&
                !updateJobDTO.getJobDes().isBlank()){
            job.setJobDes(updateJobDTO.getJobDes());
        }



        if(updateJobDTO.getExperience()!=null &&
                updateJobDTO.getExperience()>=0){
            job.setExperienceReq(updateJobDTO.getExperience());
        }

        if(updateJobDTO.getSalary()!=null &&
                updateJobDTO.getSalary()>0){
            job.setSalary(updateJobDTO.getSalary());
        }

        if(updateJobDTO.getPostedBy()!=null &&
                !updateJobDTO.getPostedBy().isBlank()){
            job.setJobpostedBy(updateJobDTO.getPostedBy());
        }

        if(updateJobDTO.getLocation()!=null &&
                !updateJobDTO.getLocation().isBlank()){
            job.setLocation(updateJobDTO.getLocation());
        }

        if(updateJobDTO.getSkillReq()!=null &&
                !updateJobDTO.getSkillReq().isBlank()){
            job.setSkillReq(updateJobDTO.getSkillReq());
        }

        if(updateJobDTO.getEmployementType()!=null){
            job.setEmployementType(updateJobDTO.getEmployementType());
        }

        if(updateJobDTO.getActive()!=null){
            job.setActive(updateJobDTO.getActive());
        }

        return jobRepo.save(job);
    }

    public FeignJobDTO getJobStatusInternally(String jobId) {
        JobDetails jobDetails=jobRepo.findByJobId(jobId).orElseThrow(()->new JobNotFoundException("No job found by this id"));
        return new FeignJobDTO(
                jobDetails.getJobId(),
                jobDetails.getActive()
        );
    }
}
