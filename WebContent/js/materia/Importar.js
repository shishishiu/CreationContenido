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

	$( "tr.datarow" ).hover(
			function() {
			  $(this).addClass("highlight");
			}, 
			function(){
				$(this).removeClass("highlight");
			});

});

function funcImportar(cveMat, nivel, nivelgrado){
	$("#innerMessage").html("");
	$("#message").hide();


	if(!confirm("Seguro que importar la materia?")){
		return false;
	}			
	
	var flgImport = false;
	
	//Confirmar si no hay misma materia
	$.ajax({
		  method: "POST",
		  url: "Importar",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { nivel: nivel,
			      nivelGrado: nivelgrado,
			  	  cveMat: cveMat,
			  	  hiddenTipo: 1
			  	},
		  async: true,
		  success: function(data){
			  if(data['status'] == "error"){
				  $("#innerMessage").html(data['message']);
				  $("#message").show();
	
			  }else if(data['status'] == "noAutorizado"){
				  location.href = "AutentificaError";
				  
			  }else if(data['status'] == "success"){
				  if(data['message'] != ""){
					  if(!confirm(data['message'])){
						  return false;
					  }
				  }
				  flgImport = true;
		  	  }		  
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  $("#innerMessage").html("Error. No pudo exportar." + thrownError);
			  $("#message").show();
		  }
	}).done(function(){
			
		if(flgImport){
			  Importar(cveMat, nivel);
		}
			
	});

}

function Importar(cveMat,nivel){
	document.getElementById("hiddenTipo").value = 2;
	document.getElementById("hiddenCveMat").value = cveMat;
	document.getElementById("hiddenNivel").value = nivel;
	document.forms["form1"].action = "Importar";
    document.forms["form1"].submit();	
	
}

function funcBuscar(){
	
	document.getElementById("hiddenTipo").value = 3;
	document.forms["form1"].action = "Importar";
    document.forms["form1"].submit();
		
}

function funcMovePagina(numpagina){
	document.getElementById("hiddenCurrentPagina").value = numpagina;
	document.forms["form1"].action = "Importar";
    document.forms["form1"].submit();
}