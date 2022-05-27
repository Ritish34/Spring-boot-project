
$(document).ready(function() {
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
		
	var table = $('#table_id').DataTable({
		"ajax": {
			"url": "ShowAllUser",
			"type": "POST",
			"datatype": "json"
		},
		"columns": [
			{ "data": "id" },
			{ "data": "first_name" },
			{ "data": "last_name" },
			{ "data": "date" },
			{ "data": "phone" },
			{ "data": "gender" },
			{ "data": "checkbox" },
			{ "data": "email" },
			{ "defaultContent": "<a class='btn btn-danger' id='delete-btn' role='button' >Delete</a>" },
			{ "defaultContent": "<a class='btn btn-info' id='update-btn' role='button'>Update</a>" },
		]
	});

	$('#table_id').on('click', '#delete-btn', function(event) {
		if (!(confirm('Are you sure you want to delete this User?'))) return false
		event.preventDefault();
		var data = table.row($(this).parents('tr')).data();
		var UserId = data["id"];
		console.log(UserId);
		
		$.ajax({
			type: "post",
			url: "DeleteUser",
			data: { "UserId": UserId },
			success: function() {
				Toast.fire({
					icon: 'success',
					title: 'User Deleted Sucessfully'
			  })
				table.ajax.reload();
			},
			error: function() {
				Toast.fire({
					icon: 'error',
					title: 'Oops,Something Wrong'
			  })
			},
		})

	});
	
	$('#table_id').on('click', '#update-btn', function(event) {
		event.preventDefault();
		var data = table.row($(this).parents('tr')).data();
		var UserId = data["id"];
		console.log(UserId);

		var url = "update-"+UserId;
		window.location = url;
	});
});