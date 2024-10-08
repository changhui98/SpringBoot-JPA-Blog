package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

import jakarta.transaction.Transactional;

// html 파일이 아니라 data 를 리턴해주는 controller = RestController 
@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성 주입 (DI)
	private UserRepository userRepositroy;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepositroy.deleteById(id);	
		} catch (EmptyResultDataAccessException e) {
			
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		
		return "삭제 되었습니다. : " + id;
	}
	
	// save 함수는 id를 전달하지 않으면 insert 를 해주고 
	// save 함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해주고 
	// save 함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 한다.  
	// email, password
	@Transactional // 함수 종료시에 자동 commit 됨. 
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		// JSON 데이터를 요청 => 스프링이 Java Object 로 변환해서 받아줍니다. 
		// (MessageConverter 의 Jackson 라이브러리가 변환해서 받아준다) 
		// @RequestBody 가 필요한 시점. 
		
		System.out.println("id : " + id);
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		
		User user = userRepositroy.findById(id).orElseThrow(()-> {
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
//		userRepositroy.save(user);
		
		// 더티 체킹 
		return user;
		
	}
	
	// http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list(){
		
		return userRepositroy.findAll();
	}
	
	// 한 페이지당 2건의 데이터를 리턴받아 볼 예정 
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		
		Page<User> pagingUser = userRepositroy.findAll(pageable);
		
		List<User> users = pagingUser.getContent();
		
		return users;
	}
	
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
		
		// 요청 : 웹브라우저 
		// user 객체 = 자바 오브젝트 
		// 변환 (웹브라우저가 이해할 수 있는 데이터) -> JSON (Gson 라이브러리) 
		// 스프링부트 = MessageConverter 라는 애가 응답시에 자동 작동 
		// 만약에 자바 오즈젝트를 리턴하게 되면 MessageConverter가 Jackson 이라는 라이브러리를 호출해서
		// user 오브제특를 json으로 변환해서 브라우저에게 던져줍니다. 
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
