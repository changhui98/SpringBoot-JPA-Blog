package com.cos.blog.test;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성 주입 (DI)
	private UserRepository userRepositroy;
	
	// {id} 주소로 파라미터를 전달 받을 수 있음.
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// user/4 을 찾으면 내가 데이터베이스에서 못찾아오게 되면 user 가 null 이 될 것 아니냐 ?
		// 그럼 return null 이 리턴이 된다. 그럼 프로그램에 문제가 있지 않겠니 ?
		// Optional 로 너의 user 객체를 감싸서 가져올테니 null 인지 아닌지 판단해서 return 해 
		
		// 람다식 
		/*
		 * User user = userRepositroy.findById(id).orElseThrow(() -> { return new
		 * IllegalArgumentException("해당 사용자는 없습니다."); });
		 */
			
		User user = userRepositroy.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
			}
		});
		
		return user;
	}
	
	// http://localhost:8000/blog/dummy/join(요청)
	// http의 body에 username, password, email 데이터를 가지고 (요청)
	@PostMapping("/dummy/join")
	public String join(User user) {
		// key=value (약속된 규칙)
		
		System.out.println("id : " + user.getId());
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		System.out.println("role : " + user.getRole());
		System.out.println("createDate : " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepositroy.save(user);
		
		return "회원가입이 완료 되었습니다.";
	}
	

}
