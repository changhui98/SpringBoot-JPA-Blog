let index = {
	init: function() {
		$("#btn-save").on("click", () => { // function(){} , ()=>{} this를 바인딩 하기 위해서 ! 
			this.save();
		});

		$("#btn-delete").on("click", () => {
			this.deleteById();
		})

		$("#btn-update").on("click", () => {
			this.update();
		})
	},

	save: function() {
		// alert('user의 save함수 호출됨');
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};

		// console.log(data);

		// ajax 호출시 default가 비동기 호출 
		// ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청 !!
		$.ajax({
			// 회원가입 수행 요청 
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data), // http body데이터 
			contentType: "application/json; charset=utf-8", // body데이터가 어떤 타입인지(MIME)
			dataType: "json" // 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면 )=> javascript오브젝트로 변경 

		}).done(function(resp) {
			alert("글쓰기가 완료되었습니다.");
			// console.log(resp);
			location.href = "/";

		}).fail(function(error) {
			alert(JSON.stringify(error));

		});

	},

	deleteById: function() {

		let id = $("#id").text();

		$.ajax({
			// 회원가입 수행 요청 
			type: "DELETE",
			url: "/api/board/" + id,
			dataType: "json" // 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면 )=> javascript오브젝트로 변경 

		}).done(function(resp) {
			alert("삭제가 완료되었습니다.");
			// console.log(resp);
			location.href = "/";

		}).fail(function(error) {
			alert(JSON.stringify(error));

		});

	},

	update: function() {

		let id = $("#id").val();

		// alert('user의 save함수 호출됨');
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};

		// console.log(data);

		// ajax 호출시 default가 비동기 호출 
		// ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청 !!
		$.ajax({
			// 회원가입 수행 요청 
			type: "PUT",
			url: "/api/board/"+id,
			data: JSON.stringify(data), // http body데이터 
			contentType: "application/json; charset=utf-8", // body데이터가 어떤 타입인지(MIME)
			dataType: "json" // 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면 )=> javascript오브젝트로 변경 

		}).done(function(resp) {
			alert("글 수정이 완료되었습니다.");
			// console.log(resp);
			location.href = "/";

		}).fail(function(error) {
			alert(JSON.stringify(error));

		});

	},
}

index.init();