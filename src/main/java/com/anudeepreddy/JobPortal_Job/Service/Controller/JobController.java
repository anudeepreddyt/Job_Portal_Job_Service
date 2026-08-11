package com.anudeepreddy.JobPortal_Job.Service.Controller;

import com.anudeepreddy.JobPortal_Job.Service.DTO.DeleteJobDTO;
import com.anudeepreddy.JobPortal_Job.Service.DTO.FeignJobDTO;
import com.anudeepreddy.JobPortal_Job.Service.DTO.SearchJobDTO;
import com.anudeepreddy.JobPortal_Job.Service.DTO.UpdateJobDTO;
import com.anudeepreddy.JobPortal_Job.Service.Model.EmployementType;
import com.anudeepreddy.JobPortal_Job.Service.Model.JobDetails;
import com.anudeepreddy.JobPortal_Job.Service.Service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;

@RestController
@RequestMapping("/job")
@Tag(name="Job Controller API's")
@CrossOrigin(origins = "https://job-portal-frontend-5hsn.onrender.com")
public class JobController {

    @Autowired
    private JobService jobService;



    @Operation(summary="Add job")
    @PostMapping("/addjob")
    public ResponseEntity<String> addJob(@Valid @RequestBody JobDetails jobDetails)
    {
        jobService.addJob(jobDetails);
        return ResponseEntity.ok("Job Added Successfully...");
    }

    @Operation(summary = "Get all job (Only for admin)")
    @GetMapping("/getjobs")
    public ResponseEntity<Page<JobDetails>> getJob(@RequestParam (defaultValue = "0")int page, @RequestParam(defaultValue = "4")int size){
        Pageable pageable= PageRequest.of(page,size);
        return new ResponseEntity<>(jobService.getJob(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Update Job by Job ID")
    @PutMapping("/updatejob/{jobId}")
    public ResponseEntity<String> updateJob(@PathVariable String jobId, @RequestBody UpdateJobDTO updateJobDTO){
        jobService.updateJob(jobId,updateJobDTO);
        return ResponseEntity.ok("Job updated Successfully...");
    }

    @Operation(summary = "Delete Job by Id")
    @DeleteMapping("/deletejob/{jobId}")
    public ResponseEntity<String> deletJob(@PathVariable String jobId, @RequestBody DeleteJobDTO deleteJobDTO){
        jobService.deleteJob(jobId,deleteJobDTO);
        return ResponseEntity.ok("Job deleted...");
    }

    @Operation(summary = "Search job")
    @GetMapping("/searchjob")
    public ResponseEntity<Page<SearchJobDTO>> searchJob(@RequestParam (defaultValue = "0") int page,
                                                        @RequestParam (defaultValue = "5") int size,
                                                        @RequestParam (required = false) String location,
                                                        @RequestParam (required = false) String jobTitle,
                                                        @RequestParam (required = false)EmployementType employementType,
                                                        @RequestParam (required = false) String companyName,
                                                        @RequestParam(required = false) Integer minSalary,
                                                        @RequestParam(required = false) Integer maxSalary){
        Pageable pageable=PageRequest.of(page,size);
        return new ResponseEntity<>(jobService.searchJob(pageable,location,jobTitle,employementType,companyName,minSalary,maxSalary),HttpStatus.OK);
    }

    @Operation(summary = "Get all active jobs")
    @GetMapping("/getallactivejobs")
    public ResponseEntity<Page<SearchJobDTO>> getAllActiveJobs(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "4")int size){
        Pageable pageable=PageRequest.of(page,size);
        return new ResponseEntity<>(jobService.getAllActiveJobs(pageable),HttpStatus.OK);
    }

    @Operation(summary = "Get Latest Jobs")
    @GetMapping("/latestjobs")
    public ResponseEntity<Page<SearchJobDTO>> latestJobs(@RequestParam (defaultValue = "0")int page,@RequestParam(defaultValue = "4") int size){
        Pageable pageable=PageRequest.of(page,size);
        return new ResponseEntity<>(jobService.latestJobs(pageable),HttpStatus.OK);
    }

    @Operation(summary = "Patch Update")
    @PatchMapping("/patchUpdateJob/{jobId}")
    public ResponseEntity<JobDetails> patchUpdateJob(
            @PathVariable String jobId,
            @RequestBody UpdateJobDTO updateJobDTO){

        return ResponseEntity.ok(jobService.patchUpdateJob(jobId, updateJobDTO));
    }

    @Operation(summary = "Get job Status by jobId for feign")
    @GetMapping("/internally/{jobId}")
    public ResponseEntity<FeignJobDTO> getJobStatusInternally(@PathVariable("jobId") String jobId){
        return new ResponseEntity<>(jobService.getJobStatusInternally(jobId),HttpStatus.OK);

    }




}
