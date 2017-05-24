$(function() {
	iniciaMessageDialog();
	funcResizeMain();

});

$("textarea").resizable({
    resize: function() {
    	funcResizeMain();
    }
});

function funcRegreso(){
    location.href = "BusMat"
}

function funcLiberar(){
	$("#innerMessage").html("");
	$("#message").hide();

	document.getElementById("hiddenTipo").value = 1;
	
	
	if(!confirm("Segura que libera la materia")){
		return false;
	}
	
	var cveMat = document.getElementById("hiddenCveMat").value;
	$.ajax({
		  method: "POST",
		  url: "Liberar",
		  data: { hiddenCveMat: cveMat,
			  	  hiddenTipo: 	1,
			  	  nota2:document.getElementById("nota2").value
			  	},
		  async: true,
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  beforeSend:function( xhr ) {
		    	$(".botonimagenchico").prop("disabled", true);
		    	$("#loading").show();
	  		},
		  success: function(data){
			  if(data['status'] == "error"){
					$("#innerMessage").html(data['message']);
					$("#message").show();
	
			  }else if(data['status'] == "success"){
				  
				  if(data['message'].length > 0){
					  if(!confirm(data['message'])){
						  return false;
					  }
				  }
				  
				  funcImprimir();
				  
 			  }else if(data['status'] == "noAutorizado"){
 				 location.href = "AutentificaError";
 			  }

		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No pudo continuar.");
		  },
		  complete: function(xhr){
		    	$(".botonimagenchico").prop("disabled", false);
		    	$("#loading").hide();
		  }
	})
}

function funcImprimir(){
	document.getElementById("hiddenTipo").value = 2;
	document.forms["form1"].action = "Liberar";
    document.forms["form1"].submit();
}
//function funcPreviewNota1(){
//	var nota = document.getElementById("nota").value;
//	PreviewNota(3, nota);
//}
function funcPreviewNota2(){
	var nota = document.getElementById("nota2").value;
	PreviewNota(4, nota);
}
function PreviewNota(tipo, nota){

	document.getElementById("hiddenTipo").value = tipo;
	document.forms["form1"].action = "Liberar";
    document.forms["form1"].submit();

}
