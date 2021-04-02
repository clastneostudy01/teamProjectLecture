package com.example.teamProjectLecture.lectureUser;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.teamProjectLecture.lecture.Lecture;
import com.example.teamProjectLecture.lecture.LectureRepository;

@RestController
public class LectureUserController {
	
	// 유저정보 하드코딩
	final long userId = 1;
	
	private LectureUserRepository lectureUserRepo;
	private LectureRepository lectureRepo;
	private LectureUserService service;
	
	@Autowired
	public LectureUserController(LectureUserRepository lectureUserRepo, LectureRepository lectureRepo, LectureUserService service) {
		this.lectureUserRepo = lectureUserRepo;
		this.lectureRepo = lectureRepo;
		this.service = service;
	}

//	쓸 일 없지?
//	@RequestMapping(value="/lecture-users", method=RequestMethod.GET)
//	public List<LectureUser> getLectureUsers(HttpServletRequest req){
//		
//		List<LectureUser> list = lectureUserRepo.findAll(Sort.by("id").descending());		
//		return list;
//	}
	
	
	@RequestMapping(value="/lecture-users", method=RequestMethod.GET)
	public List<LectureUser> getLectureUserList(HttpServletRequest req){
		List <LectureUser> list = lectureUserRepo.findAll(Sort.by("id"));
		return list;
	}
	
	@RequestMapping(value="/lecture-users/{lectureId}", method=RequestMethod.GET)
	public boolean isSubscribed(@PathVariable("lectureId") long lectureId){
		LectureUser lectureUser = lectureUserRepo.findByLectureIdAndUserId(lectureId, userId);
		
		if(lectureUser != null) {
//			System.out.println("구독중");
			return true;
		}
//		System.out.println("미구독");
		return false;
	}	
	
	@RequestMapping(value="/lecture-users/{lectureId}", method=RequestMethod.POST)
	public LectureUser subscribe(@PathVariable("lectureId") long lectureId, HttpServletResponse res) {
		
		if(lectureUserRepo.findByLectureIdAndUserId(lectureId, userId) != null) {
//			System.out.println("이미 구독한 강의");
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		Lecture lecture = lectureRepo.findById(lectureId);
		
		LectureUser lectureUser = new LectureUser();

		lectureUser.setSubscribedTime(new Date().getTime());
		
		lectureUser.setUserId(userId);
		lectureUser.setLectureId(lectureId);
		lectureUser.setLectureTitle(lecture.getTitle());
		lectureUser.setLectureSummary(lecture.getSummary());
		lectureUser.setLectureImageSRC(lecture.getImageSRC());

		
//		System.out.println(lectureUser);
		lectureUserRepo.save(lectureUser);
		
		
		System.out.println(lectureUser);
		service.sendSubscribe(lectureUser);
		System.out.println("queuing");
		
//		System.out.println("LectureUser Saving");
		return lectureUser;
	}
	
	@RequestMapping(value="/lecture-users/{lectureId}", method=RequestMethod.DELETE)
	public boolean unSubscribe(@PathVariable("lectureId") long lectureId, HttpServletResponse res) {
//		System.out.println(lectureId);
		
		LectureUser lectureUser = lectureUserRepo.findByLectureIdAndUserId(lectureId, userId);
		
		if(lectureUser == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			System.out.println("구독하지 않은 강의");
			return false;
		}
		
		System.out.println(lectureUser);
		service.sendUnSubscribe(lectureUser.getLectureId());
		System.out.println("queuing");
		
		lectureUserRepo.deleteById(lectureUser.getId());
//		System.out.println("구독해제");
		
		return true;
	}
	
	
//  이어서 보기 추가할까 했는데 유튜브라 애매한듯.
//	@RequestMapping(value="lecture-users/{lectureId}",method=RequestMethod.PATCH)
//	public LectureUser stopViewing(@PathVariable("lectureId") long lectureId, HttpServletResponse res) {
//		
//		LectureUser lectureUser = lectureUserRepo.findByLectureIdAndUserId(lectureId, userId);
//
//		
//		
//		
//		return null;
//		
//	}
	
	
}
