package com.project.foret.service;

import com.project.foret.entity.*;
import com.project.foret.model.*;
import com.project.foret.repository.MemberPhotoRepository;
import com.project.foret.repository.MemberRepository;
import com.project.foret.repository.RegionRepository;
import com.project.foret.repository.TagRepository;
import com.project.foret.response.CreateResponse;
import com.project.foret.response.Response;
import com.project.foret.response.SignInResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    @Autowired
    ServletContext servletContext;

    private MemberRepository memberRepository;
    private MemberPhotoRepository memberPhotoRepository;
    private TagRepository tagRepository;
    private RegionRepository regionRepository;

    public ResponseEntity<Object> checkEmail(String email) {
        Response response = new Response();
        if(memberRepository.findByEmail(email).isPresent()){
            response.setResult("FAIL");
            response.setMessage("이미 존재하는 이메일 입니다.");
            return ResponseEntity.ok().body(response);
        } else {
            response.setResult("OK");
            response.setMessage("사용 가능한 이메일 입니다.");
            return ResponseEntity.ok().body(response);
        }
    }

    public ResponseEntity<Object> signIn(String email, String password) {
        SignInResponse response = new SignInResponse();
        if(memberRepository.findByEmail(email).isPresent()){
            if(memberRepository.findByEmailAndPassword(email, password).isPresent()){
                Long id = memberRepository.findByEmailAndPassword(email, password).get().getId();
                response.setId(id);
                response.setMessage("로그인 성공");
                return ResponseEntity.ok().body(response);
            } else {
                response.setMessage("비밀번호가 다릅니다.");
                return ResponseEntity.ok().body(response);
            }
        } else {
            response.setMessage("존재하지 않는 이메일입니다.");
            return ResponseEntity.ok().body(response);
        }
    }

    public ResponseEntity<Object> createMember(Member model, MultipartFile[] files) throws Exception {
        CreateResponse response = new CreateResponse();
        Member newMember = new Member();
        if (memberRepository.findByEmail(model.getEmail()).isPresent()) {
            response.setMessage("이미 사용중인 이메일 입니다. 회원가입 실패");
            return ResponseEntity.badRequest().body(response);
        } else {
            newMember.setName(model.getName());
            newMember.setEmail(model.getEmail());
            newMember.setPassword(model.getPassword());
            newMember.setNickname(model.getNickname());
            newMember.setBirth(model.getBirth());
            // newMember.setDeviceToken(model.getDeviceToken());
            newMember.setDeviceToken("token");
            if (model.getTags() != null) {
                for (Tag tag : model.getTags()) {
                    newMember.addTag(tagRepository.findByTagName(tag.getTagName()).get());
                }
            }
            if (model.getRegions() != null) {
                for (Region region : model.getRegions()) {
                    newMember.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()).get());
                }
            }
            if (files != null) {
                for (MultipartFile photo : files) {
                    newMember.addPhoto(uploadPhoto(photo));
                }
            }
            // 포레
            Member savedMember = memberRepository.save(newMember);
            if (memberRepository.findById(savedMember.getId()).isPresent()){
                response.setMessage("회원가입 성공");
                response.setId(savedMember.getId());
                return ResponseEntity.ok(response);
            }
            else {
                response.setMessage("회원가입 실패");
                return ResponseEntity.unprocessableEntity().body(response);
            }
        }
    }

    @Transactional
    public ResponseEntity<Object> updateMember(Long id, Member model, MultipartFile[] files) throws Exception {
        if (memberRepository.findById(id).isPresent()) {
            Member updateMember = memberRepository.findById(id).get();
            memberPhotoRepository.deleteByMemberId(id);

            updateMember.setName(model.getName());
            updateMember.setEmail(model.getEmail());
            updateMember.setPassword(model.getPassword());
            updateMember.setNickname(model.getNickname());
            updateMember.setBirth(model.getBirth());
            updateMember.setDeviceToken(model.getDeviceToken());
            // 기존에 있던거 초기화
            updateMember.setTags(null);
            updateMember.setRegions(null);
            updateMember.setPhotos(null);
            if (model.getTags() != null) {
                for (Tag tag : model.getTags()) {
                    updateMember.addTag(tagRepository.findByTagName(tag.getTagName()).get());
                }
            }
            if (model.getRegions() != null) {
                for (Region region : model.getRegions()) {
                    updateMember.addRegion(regionRepository.findByRegionSiAndRegionGu(region.getRegionSi(), region.getRegionGu()).get());
                }
            }
            if (files != null) {
                for (MultipartFile photo : files) {
                    updateMember.addPhoto(uploadPhoto(photo));
                }
            }
            // 포레
            Member savedMember = memberRepository.save(updateMember);
            if (memberRepository.findById(savedMember.getId()).isPresent())
                return ResponseEntity.ok("회원수정 성공");
            else return ResponseEntity.unprocessableEntity().body("회원수정 실패");
        } else return ResponseEntity.unprocessableEntity().body("회원을 찾을 수 없습니다.");
    }

    public ResponseEntity<Object> deleteMember(Long id) {
        if (memberRepository.findById(id).isPresent()) {
            memberRepository.deleteById(id);
            if (memberRepository.findById(id).isPresent())
                return ResponseEntity.unprocessableEntity().body("회원삭제 실패");
            else return ResponseEntity.ok().body("회원삭제 성공");
        } else return ResponseEntity.badRequest().body("회원을 찾을 수 없습니다.");
    }

    public MemberModel getMember(Long id) {
        if (memberRepository.findById(id).isPresent()) {
            Member member = memberRepository.findById(id).get();
            MemberModel memberModel = new MemberModel();
            memberModel.setId(member.getId());
            memberModel.setName(member.getName());
            memberModel.setEmail(member.getEmail());
            memberModel.setNickname(member.getNickname());
            memberModel.setBirth(member.getBirth());
            memberModel.setReg_date(member.getReg_date());
            memberModel.setTags(getTagList(member));
            memberModel.setRegions(getRegionList(member));
            memberModel.setPhotos(getPhotoList(member));
            return memberModel;
        } else return null;
    }

    public List<MemberModel> getMembers() {
        List<Member> memberList = memberRepository.findAll();
        if (memberList.size() > 0) {
            List<MemberModel> memberModels = new ArrayList<>();
            for (Member member : memberList) {
                MemberModel memberModel = new MemberModel();
                memberModel.setName(member.getName());
                memberModel.setEmail(member.getEmail());
                memberModel.setNickname(member.getNickname());
                memberModel.setBirth(member.getBirth());
                memberModel.setReg_date(member.getReg_date());
                memberModel.setTags(getTagList(member));
                memberModel.setRegions(getRegionList(member));
                memberModel.setPhotos(getPhotoList(member));
                memberModels.add(memberModel);
            }
            return memberModels;
        } else return new ArrayList<>();
    }

    // 이걸로 안하면 태그안의 멤버타고 재귀 순회
    private List<TagModel> getTagList(Member member) {
        List<TagModel> tagList = new ArrayList<>();
        if (member.getTags() != null && member.getTags().size() != 0) {
            for (Tag tag : member.getTags()) {
                TagModel tagModel = new TagModel();
                tagModel.setTagName(tag.getTagName());
                tagList.add(tagModel);
            }
            return tagList;
        } else return null;
    }

    private List<RegionModel> getRegionList(Member member) {
        List<RegionModel> regionList = new ArrayList<>();
        if (member.getRegions() != null && member.getRegions().size() != 0) {
            for (Region region : member.getRegions()) {
                RegionModel regionModel = new RegionModel();
                regionModel.setRegionSi(region.getRegionSi());
                regionModel.setRegionGu(region.getRegionGu());
                regionList.add(regionModel);
            }
            return regionList;
        } else return null;
    }

    private List<PhotoModel> getPhotoList(Member member){
        List<PhotoModel> photoList = new ArrayList<>();
        if (member.getPhotos() != null && member.getPhotos().size() != 0) {
            for (MemberPhoto memberPhoto : member.getPhotos()) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setId(memberPhoto.getId());
                photoModel.setDir(memberPhoto.getDir());
                photoModel.setFilename(memberPhoto.getFilename());
                photoModel.setOriginname(memberPhoto.getOriginname());
                photoModel.setFilesize(memberPhoto.getFilesize());
                photoModel.setFiletype(memberPhoto.getFiletype());
                photoModel.setReg_date(memberPhoto.getReg_date());
                photoList.add(photoModel);
            }
            return photoList;
        } else return null;
    }

    private MemberPhoto uploadPhoto(MultipartFile photo) throws Exception {
        String realPath = servletContext.getRealPath("/storage/member");
        String dir = "/storage/member";
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
        MemberPhoto memberPhoto = new MemberPhoto();
        memberPhoto.setDir(dir);
        memberPhoto.setOriginname(originname);
        memberPhoto.setFilename(filename);
        memberPhoto.setFiletype(filetype);
        memberPhoto.setFilesize(filesize);
        return memberPhoto;
    }
}
