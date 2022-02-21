package uz.pdp.apponlinetestserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.pdp.apponlinetestserver.entity.Subject;
import uz.pdp.apponlinetestserver.exception.ResourceNotFoundException;
import uz.pdp.apponlinetestserver.payload.*;
import uz.pdp.apponlinetestserver.repository.SubjectRepositrory;
import uz.pdp.apponlinetestserver.utils.CommonUtils;

@Service
public class SubjectService {

    @Autowired
    SubjectRepositrory subjectRepositrory;

    public ApiResponse saveOrEdit(ReqSubject reqSubject) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setMessage("Saved");
            apiResponse.setSuccess(true);
            Subject subject = new Subject();
            if (reqSubject.getId()!=null){
                subject = subjectRepositrory.findById(reqSubject.getId()).orElseThrow(() -> new ResourceNotFoundException("Subject", "id", reqSubject.getId()));
                apiResponse.setMessage("Edited");
            }
            subject.setNameUz(reqSubject.getNameUZ());
            subject.setNameRu(reqSubject.getNameRU());
            subjectRepositrory.save(subject);
        }catch (Exception e){
            apiResponse.setMessage("Error");
            apiResponse.setSuccess(false);
        }
        return apiResponse;
    }


    public ApiResponse deleteSubject(Integer id) {
        try {
            subjectRepositrory.deleteById(id);
            return new ApiResponse("Deleted", true);
        }catch (Exception e){
            return new ApiResponse("Conflict", false);
        }
    }

    public ResPageable getSubjectByPageable(Integer page, Integer size) {
        Page<Subject> subjectPage=subjectRepositrory.findAll(CommonUtils.getPageableById(page,size));
        return new ResPageable(subjectPage.getContent(),subjectPage.getTotalElements(),page);
    }


    public ApiResponseModel getAll() {
        return new ApiResponseModel(true,"Ok",subjectRepositrory.findAll());
    }

    public ResSubject getResSubject(Subject subject){
        ResSubject resSubject = new ResSubject();
        resSubject.setId(subject.getId());
        resSubject.setNameUz(subject.getNameUz());
        resSubject.setNameRu(subject.getNameRu());
        return resSubject;
    }
}
