$(function() {
	iniciaMessageDialog();
	funcResizeMain();
	
});

function funcExportar(){
	$("#innerMessage").html("");
	$("#message").hide();
	
	if(!confirm("Seguro que exportar la materia?")){
		return false;
	}			

	//Confirmar si no hay misma materia
	$.ajax({
		  method: "POST",
		  url: "ExportarMat",
		  data: { hiddenCveMat: document.getElementById("hiddenCveMat").value,
			  	  hiddenTipo: 	1
			  	},
		  async: true,
		  success: function(data){
			  if(data['status'] == "error"){
				  alert(data['message']);
	
			  }else if(data['status'] == "success"){
				  if(!confirm(data['message'])){
					  return false;
				  }
				  Exportar();

		  	  }		  
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No pudo exportar.");
		  }
		})
}

function Exportar(){
	document.getElementById("hiddenTipo").value = 2;
	document.forms["form1"].action = "ExportarMat";
    document.forms["form1"].submit();
}

function funcRegreso(){
	document.forms["form1"].action = "BusMat";
    document.forms["form1"].submit();
}