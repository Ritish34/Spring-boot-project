//Start JQuery
$(function() {
	//make validation method to validate regex using regex pattern
	$.validator.addMethod(
		"regex",
		function(value, element, regexp) {
			return this.optional(element) || regexp.test(value);
		},
		jQuery.validator.format("Formate Mismatched")
	);
	
	//set lazzy to eager validation
	$("form[name='reg_form']").validate({
		onfocusout: function(element) {
			// "eager" validation
			this.element(element);
		},
		// Specify validation rules
		rules: {
			first_name: {
				required: true,
				minlength: 3,
				regex: /^([a-zA-Z])+(\s)*$/
			},
			last_name: {
				required: true,
				minlength: 3,
				regex: /^([a-zA-Z])+(\s)*$/
			},
			email: {
				required: true,
				email: true,
				regex: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\.[a-zA-Z.]{2,5}$/
			},
			password: {
				required: true,
				minlength: 8,
				maxlength: 20,
				regex: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/
			},
			conpass : {
				required: true,
				minlength: 8,
				maxlength: 20,
				equalTo : "#pass"
			},
			phone : {
				required : true,
				rangelength : [10, 10],
				number : true
			},
			date : {
				required : true,
			},
			checkbox : {
				required : true,
			},
		},
		// Specify validation error messages
		messages: {
			firstname: {
				required: "Please enter your firstname",
			},
			lastname: {
				required: "Please enter your lasttname",
			},
			password: {
				required: "Please provide a password",
				minlength: "Your password must be at least 8 characters long",
				regex: "Password must contain Minimum eight characters, at least one letter, one number and one special characte"
			},
			email: "Please enter a valid email address",
			conpass : {
				required: "Please provide a Confirm password",
				minlength: "Your password must be at least 8 characters long",
				equalTo : "PassWord Mismatched!!",
			},
			phone : {
				required: "Phone Number Can't Be Empty'",
				rangelength : "Length Should be 10",
				number : "only Numbers Are Allowed",
			},
			date : {
				required: "Enter Date Of Birth",
			},
		},
		submitHandler: function(form,event) {
			var $empty = $('input[type != "hidden" && input[type != "file"]]').filter(function() {
				return this.value == ''
			});
			if ($empty.length == 0) {
				form.submit();
			}
			else{
				event.preventDefault();
			}
		}
	});

	$('[id*="zip"]').each(function() {
        $(this).rules('add', {
            required : true,
			number : true,
			rangelength : [5, 6],
			messages: { 
				number : "only Numbers Are Allowed",
				rangelength : "Zip length should be 5 or 6 Digit only"
			}
        });
    });
	
	$('[id*="city"]').each(function() {
        $(this).rules('add', {
            required: true,
			regex: /^([a-zA-Z])+(\s)*$/,
			messages: { 
				regex : "Only Letters Allowed!!",
			}
        });
    });

	$('[id*="country"]').each(function() {
        $(this).rules('add', {
            required: true,
			regex: /^([a-zA-Z])+(\s)*$/,
			messages: { 
				regex : "Only Letters Allowed!!",
			}
        });
    });

	$('[id*="state"]').each(function() {
        $(this).rules('add', {
            required: true,
			regex: /^([a-zA-Z])+(\s)*$/,
			messages: { 
				regex : "Only Letters Allowed!!",
			}
        });
    });
	
	let hiddenvalue = $("#status").val();
	if(hiddenvalue == "add"){
		$("div.loader-wrapper").fadeOut("slow");
	}
	
	if(hiddenvalue == 'update'){
		$("#passdiv").addClass("invisible");
		$("#conpassdiv").addClass("invisible");
	
		let userid = $("#userid").val();
		$.ajax({
				type: "post",
				url: "GetOneUserData",
				data : { "UserId": userid },
				datatype: "json",
				success: function(r) {	
					console.log(r);
					if(r.data == "User Does Not Exist For Given Id"){
						Toast.fire({
		  				icon: 'error',
		  				title: r.data
					})
					window.location = "/EmployeeManagmentSystem/Error";
					}
					else{
						$("#fname").val(r.data[0].first_name);
					$("#lname").val(r.data[0].last_name);
					$("#email").val(r.data[0].email);
					$("#email").prop('readonly',true);
					$("#phone").val(r.data[0].phone);
					$("#phone").prop('readonly',true);
					$("#pass").val(r.data[0].password);
					$("#dob").val(r.data[0].date);

					let lang = r.data[0].checkbox;
					if(lang.includes($("#chk1").val() )){
						$("#chk1").prop('checked', 'checked')
					}
					if(lang.includes($("#chk2").val() )){
						$("#chk2").prop('checked', 'checked')
					} 
					if(lang.includes($("#chk3").val() )){
						$("#chk3").prop('checked', 'checked')
					}
            		
            		let gen = r.data[0].gender;
					if(gen == "male"){
						$("#male").prop('checked','checked');
					}
					else{
						$("#female").prop('checked','checked');
					}
					$("img#show_image").attr("src","data:image/jpg;base64,"+r.data[0].base64Image);
					
					$.each(r.Address, function(i) {
						$("#form fieldset:last .addressid").val(r.Address[i].addressid);
						$("#form fieldset:last .address").val(r.Address[i].address);
						$("#form fieldset:last .zip").val(r.Address[i].zip);
						$("#form fieldset:last .city").val(r.Address[i].city);
						$("#form fieldset:last .state").val(r.Address[i].state);
						$("#form fieldset:last .country").val(r.Address[i].contry);
						
						if(i+1 < r.Address.length){
							$('#add').click();
						}
				});
				$("div.loader-wrapper").fadeOut("slow");
					Toast.fire({
		  				icon: 'success',
		  				title: 'User Data Fetched SuccessFully'
					})

					}
									},
				error: function() {
					Toast.fire({
		  				icon: 'error',
		  				title: 'Oops,Something Wrong...'
					})
				},
		}); 
		
		const Toast = Swal.mixin({
		  toast: true,
		  position: 'top-end',
		  showConfirmButton: false,
		  timer: 3000,
		  timerProgressBar: true,
		  didOpen: (toast) => {
		    toast.addEventListener('mouseenter', Swal.stopTimer)
		    toast.addEventListener('mouseleave', Swal.resumeTimer)
		  }
		});
		} 
});

//create function to check entered eamail is already present into database or not 
function checkEmail() {
	//get email value
	var emailInput = document.querySelector('#email').value;
	
	var regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\.[a-zA-Z.]{2,5}$/;
	debugger
	if(!regex.test(emailInput)){
		$('#emailStatus').html(" ");
	}else{
	//make ajax call to fetch data from db
	$.ajax({
		method: "POST",
		url: "checkemail.ajax",
		async: false,
		data: { email: emailInput },
		success: function(data) {
			if(data === "Duplicate"){
				$('#emailStatus').html("Email is Already Registered");
				$('#email').focus();	// rediret focus to email input tag
				$('#submit').attr('disabled',true);
				$('#update').attr('disabled',true);
			}
			else{
				$('#emailStatus').html(" ");
				$('#submit').attr('disabled',false);
				$('#update').attr('disabled',false);
			}
		}
	});
	}
}

function readURL(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    
    reader.onload = function (e) {
      $('#show_image').attr('src', e.target.result);
    }
    reader.readAsDataURL(input.files[0]);
  }
}
function clearFile(){
  const file =document.querySelector('#new_image');
      file.value = '';
}
$("#new_image").change(function(){
    //value of file
    var imgName = $(this)[0].value;

    //find extatntion of file
    var extn = imgName.substring(imgName.lastIndexOf('.') + 1).toLowerCase();
    //vallidation of image
    if (extn == "gif" || extn == "png" || extn == "jpg" || extn == "jpeg"){
      readURL(this);
    }
    else{
      clearFile();
    } 
});

let counter = 1;

function changeAddId(){
	$("#addbtn").click();
	++counter;
	$("#form fieldset:last #addressid").attr("id","id"+(counter));
	
	$("#form fieldset:last #address").attr("id","address"+(counter));
	
	$("#form fieldset:last #zip").attr("id","zip"+(counter));
	
	$("#form fieldset:last #city").attr("id","city"+(counter));
	$("#form fieldset:last #state").attr("id","state"+(counter));
	$("#form fieldset:last #country").attr("id","country"+(counter));
}

dob.max = new Date().toISOString().split("T")[0];