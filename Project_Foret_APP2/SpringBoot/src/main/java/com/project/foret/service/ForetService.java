package com.project.foret.service;

import com.project.foret.entity.*;
import com.project.foret.model.*;
import com.project.foret.repository.*;
import com.project.foret.response.CreateResponse;
import com.project.foret.response.ForetResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class ForetService {

    @Autowired
    ServletContext servletContext;

    private ForetRepository foretRepository;
    private ForetPhotoRepository foretPhotoRepository;
    private TagRepository tagRepository;
    private RegionRepository regionRepository;
    private MemberRepository memberRepository;

    private MemberService memberService;

//    public ResponseEntity<Object> photo(MultipartFile[] files, Foret foret) throws Exception{
//        CreateResponse response = new CreateResponse();
//        Foret newForet = new Foret();
//        if(foret != null){
//            newForet.setName(foret.getName());
//            newForet.setIntroduce(foret.getIntroduce());
//            newForet.setMax_member(foret.getMax_member());
//        }
//        if (files != null) {
//            for (MultipartFile photo : files) {
//                newForet.addPhoto(uploadPhoto(photo));
//            }
//        }
//        Foret savedForet = foretRepository.save(newForet);
//        if (foretRepository.findById(savedForet.getId()).isPresent()) {
//            response.setMessage("포레 생성 성공");
//            response.setId(savedForet.getId());
//            return ResponseEntity.ok(response);
//        } else {
//            response.setMessage("포레 생성 실패");
//            return ResponseEntity.unprocessableEntity().body(response);
//        }
//    }

    public CreateResponse signUpForet(Long foret_id, Long member_id) {
        CreateResponse response = new CreateResponse();
        if (!memberRepository.findById(member_id).isPresent()) {
            response.setMessage("존재하지 않는 회원입니다.");
            return response;
        }
        if (!foretRepository.findById(foret_id).isPresent()) {
            response.setMessage("존재하지 않는 포레입니다.");
            return response;
        }
        if (foretRepository.findByIdAndMembersId(foret_id, member_id).isPresent()) {
            response.setMessage("이미 가입한 회원입니다.");
            return response;
        }
        Foret foret = foretRepository.findById(foret_id).get();
        if (foret.getMembers().size() >= foret.getMax_member()) {
            response.setMessage("정원이 초과되었습니다.");
            return response;
        }
        foret.addMember(memberRepository.findById(member_id).get());
        Foret savedForet = foretRepository.save(foret);
        if (foretRepository.findById(savedForet.getId()).isPresent()) {
            response.setMessage("포레 가입 성공");
            return response;
        } else {
            response.setMessage("포레 가입 성공");
            return response;
        }
    }

    public ResponseEntity<Object> createForet(
            Foret model,
            MultipartFile[] files) throws Exception {
        CreateResponse response = new CreateResponse();
        Long member_id = model.getLeader().getId();
        if (memberRepository.findById(member_id).isPresent()) {
            Foret newForet = new Foret();
            newForet.setName(model.getName());
            newForet.setIntroduce(model.getIntroduce());
            newForet.setMax_member(model.getMax_member());
            newForet.setLeader(memberRepository.findById(member_id).get());
            if (model.getTags() != null) {
                for (Tag tag : model.getTags()) {
                    newForet.addTag(tagRepository.findByTagName(tag.getTagName()).get());
                }
            }
            if (model.getRegions() != null) {
                for (Region region : model.getRegions()) {
                    newForet.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()).get());
                }
            }
            if (files != null) {
                for (MultipartFile photo : files) {
                    newForet.addPhoto(uploadPhoto(photo));
                }
            }
            newForet.addMember(memberRepository.findById(member_id).get());
            Foret savedForet = foretRepository.save(newForet);
            if (foretRepository.findById(savedForet.getId()).isPresent()) {
                response.setMessage("포레 생성 성공");
                response.setId(savedForet.getId());
                return ResponseEntity.ok(response);
            } else {
                response.setMessage("포레 생성 실패");
                return ResponseEntity.unprocessableEntity().body(response);
            }
        } else {
            response.setMessage("존재하지 않는 회원입니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional
    public ResponseEntity<Object> updateForet(Long id, Long member_id, Foret model, MultipartFile[] files) throws Exception {
        if (foretRepository.findById(id).isPresent()) {
            Foret updateForet = foretRepository.findById(id).get();
            foretPhotoRepository.deleteByForetId(id);
            if (updateForet.getLeader().getId().equals(member_id)) {
                updateForet.setName(model.getName());
                updateForet.setIntroduce(model.getIntroduce());
                updateForet.setMax_member(model.getMax_member());
                updateForet.setTags(null);
                updateForet.setRegions(null);
                updateForet.setPhotos(null);
                if (model.getTags() != null) {
                    for (Tag tag : model.getTags()) {
                        updateForet.addTag(tagRepository.findByTagName(tag.getTagName()).get());
                    }
                }
                if (model.getRegions() != null) {
                    for (Region region : model.getRegions()) {
                        updateForet.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()).get());
                    }
                }
                if (files != null) {
                    for (MultipartFile photo : files) {
                        updateForet.addPhoto(uploadPhoto(photo));
                    }
                }
                Foret savedForet = foretRepository.save(updateForet);
                if (foretRepository.findById(savedForet.getId()).isPresent())
                    return ResponseEntity.ok("포레수정 성공");
                else return ResponseEntity.unprocessableEntity().body("포레수정 실패");
            } else return ResponseEntity.unprocessableEntity().body("포레 수정권한이 없습니다.");
        } else return ResponseEntity.unprocessableEntity().body("포레를 찾을 수 없습니다.");
    }

    public ResponseEntity<Object> deleteForet(Long id) {
        if (foretRepository.findById(id).isPresent()) {
            foretRepository.deleteById(id);
            if (foretRepository.findById(id).isPresent())
                return ResponseEntity.unprocessableEntity().body("포레삭제 실패");
            else return ResponseEntity.ok().body("포레삭제 성공");
        } else return ResponseEntity.badRequest().body("포레를 찾을 수 없습니다.");
    }

    public ForetModel getForet(Long id) {
        if (foretRepository.findById(id).isPresent()) {
            Foret foret = foretRepository.findById(id).get();
            ForetModel foretModel = new ForetModel();
            foretModel.setId(foret.getId());
            foretModel.setName(foret.getName());
            foretModel.setIntroduce(foret.getIntroduce());
            foretModel.setMax_member(foret.getMax_member());
            foretModel.setCurrent_member(foret.getMembers().size());
            foretModel.setReg_date(foret.getReg_date());
            foretModel.setTags(getTagList(foret));
            foretModel.setRegions(getRegionList(foret));
            foretModel.setPhotos(getPhotoList(foret));
            foretModel.setMembers(getMemberList(foret));
            foretModel.setLeader(memberService.getMember(foret.getLeader().getId()));
            return foretModel;
        } else return null;
    }

    public ForetResponse searchForet(String name){
        if(name.equals("")) return new ForetResponse();
        List<Foret> foretList = foretRepository.findByContainingName(name);
        if (foretList.size() > 0) {
            List<ForetModel> foretModels = new ArrayList<>();
            for (Foret foret : foretList) {
                ForetModel foretModel = new ForetModel();
                foretModel.setId(foret.getId());
                foretModel.setName(foret.getName());
                foretModel.setIntroduce(foret.getIntroduce());
                foretModel.setMax_member(foret.getMax_member());
                foretModel.setReg_date(foret.getReg_date());
                foretModel.setTags(getTagList(foret));
                foretModel.setRegions(getRegionList(foret));
                foretModel.setPhotos(getPhotoList(foret));
                foretModel.setMembers(getMemberList(foret));
                foretModels.add(foretModel);
            }
            return new ForetResponse(foretModels);
        } else return new ForetResponse();
    }

    public ForetResponse getForetsByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        List<Foret> foretList = foretRepository.findAll(pageRequest).getContent();

        if (foretList.size() > 0) {
            List<ForetModel> foretModels = new ArrayList<>();
            for (Foret foret : foretList) {
                ForetModel foretModel = new ForetModel();
                foretModel.setId(foret.getId());
                foretModel.setName(foret.getName());
                foretModel.setIntroduce(foret.getIntroduce());
                foretModel.setMax_member(foret.getMax_member());
                foretModel.setReg_date(foret.getReg_date());
                foretModel.setTags(getTagList(foret));
                foretModel.setRegions(getRegionList(foret));
                foretModel.setPhotos(getPhotoList(foret));
                foretModel.setMembers(getMemberList(foret));
                foretModels.add(foretModel);
            }
            return new ForetResponse(foretModels);
        } else return new ForetResponse();
    }

    public ForetResponse getForets() {
        List<Foret> foretList = foretRepository.findAll();
        if (foretList.size() > 0) {
            List<ForetModel> foretModels = new ArrayList<>();
            for (Foret foret : foretList) {
                ForetModel foretModel = new ForetModel();
                foretModel.setId(foret.getId());
                foretModel.setName(foret.getName());
                foretModel.setIntroduce(foret.getIntroduce());
                foretModel.setMax_member(foret.getMax_member());
                foretModel.setReg_date(foret.getReg_date());
                foretModel.setTags(getTagList(foret));
                foretModel.setRegions(getRegionList(foret));
                foretModel.setPhotos(getPhotoList(foret));
                foretModel.setMembers(getMemberList(foret));
                foretModels.add(foretModel);
            }
            return new ForetResponse(foretModels);
        } else return new ForetResponse();
    }

    public ForetResponse getMyForets(Long member_id) {
        List<Foret> foretList = foretRepository.findByMembersId(member_id);
        if (foretList.size() > 0) {
            List<ForetModel> foretModels = new ArrayList<>();
            for (Foret foret : foretList) {
                ForetModel foretModel = new ForetModel();
                foretModel.setId(foret.getId());
                foretModel.setName(foret.getName());
                foretModel.setIntroduce(foret.getIntroduce());
                foretModel.setMax_member(foret.getMax_member());
                foretModel.setReg_date(foret.getReg_date());
                foretModel.setTags(getTagList(foret));
                foretModel.setRegions(getRegionList(foret));
                foretModel.setPhotos(getPhotoList(foret));
                foretModel.setMembers(getMemberList(foret));
                foretModels.add(foretModel);
            }
            return new ForetResponse(foretModels);
        } else return new ForetResponse();
    }

    private List<TagModel> getTagList(Foret foret) {
        List<TagModel> tagList = new ArrayList<>();
        if (foret.getTags() != null && foret.getTags().size() != 0) {
            for (Tag tag : foret.getTags()) {
                TagModel tagModel = new TagModel();
                tagModel.setTagName(tag.getTagName());
                tagList.add(tagModel);
            }
            return tagList;
        } else return null;
    }

    private List<RegionModel> getRegionList(Foret foret) {
        List<RegionModel> regionList = new ArrayList<>();
        if (foret.getRegions() != null && foret.getRegions().size() != 0) {
            for (Region region : foret.getRegions()) {
                RegionModel regionModel = new RegionModel();
                regionModel.setRegionSi(region.getRegionSi());
                regionModel.setRegionGu(region.getRegionGu());
                regionList.add(regionModel);
            }
            return regionList;
        } else return null;
    }

    private List<PhotoModel> getPhotoList(Foret foret) {
        List<PhotoModel> photoList = new ArrayList<>();
        if (foret.getPhotos() != null && foret.getPhotos().size() != 0) {
            for (ForetPhoto foretPhoto : foret.getPhotos()) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setId(foretPhoto.getId());
                photoModel.setDir(foretPhoto.getDir());
                photoModel.setFilename(foretPhoto.getFilename());
                photoModel.setOriginname(foretPhoto.getOriginname());
                photoModel.setFilesize(foretPhoto.getFilesize());
                photoModel.setFiletype(foretPhoto.getFiletype());
                photoModel.setReg_date(foretPhoto.getReg_date());
                photoList.add(photoModel);
            }
            return photoList;
        } else return null;
    }

    private List<MemberModel> getMemberList(Foret foret) {
        List<MemberModel> memberList = new ArrayList<>();
        if (foret.getMembers() != null && foret.getMembers().size() != 0) {
            for (Member member : foret.getMembers()) {
                memberList.add(memberService.getMember(member.getId()));
            }
            return memberList;
        } else return null;
    }

    private ForetPhoto uploadPhoto(MultipartFile photo) throws Exception {
        String realPath = servletContext.getRealPath("/storage/foret");
        String dir = "/storage/foret";
        String originname = photo.getOriginalFilename();
        String filename = photo.getOriginalFilename();
        int lastIndex = originname.lastIndexOf(".");
        String filetype = originname.substring(lastIndex + 1);
        int filesize = (int) photo.getSize();
        if (!new File(realPath).exists()) {
            new File(realPath).mkdirs();
        }
        File file = new File(realPath, filename);
        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));
        ForetPhoto foretPhoto = new ForetPhoto();
        foretPhoto.setDir(dir);
        foretPhoto.setOriginname(originname);
        foretPhoto.setFilename(filename);
        foretPhoto.setFiletype(filetype);
        foretPhoto.setFilesize(filesize);
        return foretPhoto;
    }
}
