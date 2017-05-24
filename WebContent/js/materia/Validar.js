$(function() {
	iniciaMessageDialog();
	funcResizeMain();
	
	if($("#hiddenFlgOpenWindow").val()==1){
		window.open($("#hiddenUrl").val());
	}

});

function funcRegreso(){
    location.href = "BusMat"
}

function funcValidar(){
	$("#innerMessage").html("");
	$("#message").hide();
	
	var resultados = $(".resultado");
	var resultado = "";
	var flag = false;
	if(resultados.length) {
		var i;
		for(i = 0; i < resultados.length; i ++){

			if(resultados[i].checked){

				flag = true;
				resultado = resultados[i].value;
				break;

			}

		}

	}
	
	if(!flag){
		alert("Elige el resultado, pof favor");
		resultados[0].focus();
		return false;
	}
	
	if(!confirm("Segura que revisa la materia")){
		return false;
	}
	
	var cveMat = document.getElementById("hiddenCveMat").value;
	$.ajax({
		  method: "POST",
		  url: "Validar",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { hiddenCveMat: cveMat,
			  	  hiddenTipo: 	1,
			  	  nota:document.getElementById("nota").value,
			  	  resultado:resultado
			  	},
		  async: true,
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
						$("#innerMessage").html(data['message']);
						$("#message").show();
				  }
				  
					if(resultado == 3){
						funcExportar(cveMat);
					}

 			  }else if(data['status'] == "noAutorizado"){
  				 location.href = "AutentificaError";
  			  }
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No pudo exportar.");
		  },
		  complete: function(xhr){
		    	$(".botonimagenchico").prop("disabled", false);
		    	$("#loading").hide();
		  }
	})

}

function funcExportar(cveMat){
	$("#innerMessage").html("");
	$("#message").hide();
	
	if(!confirm("El contenido está validado. Se va a exportar el contenido a la plataforma de prueba para su revisión. Está bien?")){
		return false;
	}			

	//Confirmar si no hay misma materia
	$.ajax({
		  method: "POST",
		  url: "Validar",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { hiddenCveMat: cveMat,
			  	  hiddenTipo: 	2
			  	},
		  async: true,
		  beforeSend:function( xhr ) {
			    	$(".botonimagenchico").prop("disabled", true);
			    	$("#loading").show();
		  		},
		  success: function(data){
			  if(data['status'] == "error"){
				  alert(data['message']);
	
			  }else if(data['status'] == "success"){
				  
				  if(data['message'].length > 0){
					  if(!confirm(data['message'])){
						  return false;
					  }
				  }
				  
				  Exportar(cveMat);

		  	  }		  
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No pudo exportar." + thrownError.message);
		  },
		  complete: function(xhr){
		    	$(".botonimagenchico").prop("disabled", false);
		    	$("#loading").hide();
		  }
	})
}

function Exportar(cveMat){
	document.getElementById("hiddenTipo").value = 3;
	document.forms["form1"].action = "Validar";
    document.forms["form1"].submit();
}
