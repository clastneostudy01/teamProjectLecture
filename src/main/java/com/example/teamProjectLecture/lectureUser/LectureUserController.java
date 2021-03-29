package com.example.teamProjectLecture.lectureUser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.teamProjectLecture.lectureUser.LectureUser;
import com.example.teamProjectLecture.lectureUser.LectureUserRepository;

@RestController
public class LectureUserController {
	
	private LectureUserRepository lectureUserRepo;
	
	@Autowired
	public LectureUserController(LectureUserRepository lectureUserRepo) {
		this.lectureUserRepo = lectureUserRepo;
	}


	@RequestMapping(value="/lecture-users", method=RequestMethod.GET)
	public List<LectureUser> getLectureUsers(HttpServletRequest req){
		
		List<LectureUser> list = lectureUserRepo.findAll(Sort.by("id").descending());		
		return list;
	}
		
	@RequestMapping(value="/lecture-users", method=RequestMethod.POST)
	public LectureUser subscribe(@RequestBody LectureUser lectureUser) {

		System.out.println(lectureUser);
		lectureUserRepo.save(lectureUser);
		
		System.out.println("LectureUser Saving");
		return lectureUser;
	}
	
	@RequestMapping(value="/lecture-users/{id}", method=RequestMethod.DELETE)
	public boolean unSubscribing(@PathVariable("id") long id, HttpServletResponse res) {
		System.out.println(id);
		
		LectureUser lectureUser = lectureUserRepo.findById(id).orElse(null);
		
		if(lectureUser == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}
		
		
		lectureUserRepo.deleteById(id);
		return true;
	}
	
	
}
