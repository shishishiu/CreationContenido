/**
 * Inicio
 */
$(function(){
	iniciaMessageDialog();

	var focusElem = document.getElementById("nivel");
	if(focusElem){
		focusElem.focus();
	}
	funcChangeNivelGrado(focusElem.value);
	
	funcResizeMain();


});

function funcImportar(){
	$("#innerMessage").html("");
	$("#message").hide();
	BotonImporDisabled("disabled");

	if(!Validacion()){
		BotonImporDisabled("");
		return;
	}

	if(!confirm("Seguro que importar la materia?")){
		BotonImporDisabled("");
		return false;
	}			
	
	var flgImport = false;
	
	//Confirmar si no hay misma materia
	$.ajax({
		  method: "POST",
		  url: "Importar",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { nivel: document.getElementById("nivel").value,
			  	  cveMat: document.getElementById("cveMat").value,
			  	  hiddenTipo: 1
			  	},
		  async: true,
		  success: function(data){
			  if(data['status'] == "error"){
				  $("#innerMessage").html(data['message']);
				  $("#message").show();
					BotonImporDisabled("");
	
			  }else if(data['status'] == "noAutorizado"){
				  location.href = "AutentificaError";
				  
			  }else if(data['status'] == "success"){
				  if(data['message'] != ""){
					  if(!confirm(data['message'])){
						  BotonImporDisabled("");
						  return false;
					  }
				  }
				  flgImport = true;
		  	  }		  
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  $("#innerMessage").html("Error. No pudo exportar." + thrownError);
			  $("#message").show();
			  BotonImporDisabled("");
		  }
	}).done(function(){
			
		if(flgImport){
			  Importar();
		}
			
	});

}

function BotonImporDisabled(isDisabled){
	$("#importar").prop("disabled", isDisabled);	
}

function Importar(){
	document.getElementById("hiddenTipo").value = 2;
	document.forms["form1"].action = "Importar";
    document.forms["form1"].submit();	
	
}

function Validacion(){
	var elemNivel = document.getElementById("nivel");
	if(elemNivel.value <= 0){
		alert("Elige el nivel, por favor.");
		elemNivel.focus();
		return false;
	}
	var elemNivelGrado = document.getElementById("nivelGrado"+elemNivel.value);
	var nivelgrado = elemNivelGrado.value;
	if(isNaN(nivelgrado) || nivelgrado <= 0){
		alert("Elige el nivel grado, por favor.");
		elemNivelGrado.focus();
		return false;
	}

	var elemMat = document.getElementById("cveMat");
	if(elemMat.value == ""){
		alert("Ingresa materia, por favor.");
		elemMat.focus();
		return false;
	}

	return true;
}