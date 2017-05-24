/**
 * Inicio
 */
$(function(){
	document.getElementById("nivel").focus();
	var nivel = document.getElementById("nivel").value;
	funcChangeModulo(nivel);

	iniciaMessageDialog();
	funcResizeMain();
	
	$( "tr.datarow" ).hover(
		function() {
		  $(this).addClass("highlight");
		}, 
		function(){
			$(this).removeClass("highlight");
		});
});


/**
 * Pasa a la página de agregar la materia
 */
function funcAgregar(){
    location.href = "ModificaMat?tipo=2&back=1";
}
/**
 * Dar de baja la materia
 * @param cveMat
 */
function funcDarBaja(cveMat, banmat){
	if(banmat == 1){
		if(!confirm("Seguro que dar baja la materia de " + cveMat + "?")){
			return false;
		}
	} else{
		if(!confirm("Segura que dar alta la materia de " + cveMat + "?")){
			return false;
		}
	}
	
	document.getElementById("hiddenTipo").value = 1;
	document.forms["form1"].action = "BusMat?cveMat=" + cveMat;
    document.forms["form1"].submit();

	
}
/**
 * Pasa a la página de modificar la materia
 * @param cveMat
 */
function funcModificar(cveMat){
	location.href = "ModificaMat?tipo=1&back=1&cveMat=" + cveMat;
}

/**
 * Pasa a la página de solicitar la materia
 * @param cveMat
 */
function funcSolicitar(cveMat, estadoSolicitud){
	var url = "";
	if(estadoSolicitud == 0){
		url = "Solicitar";
	}
	else if(estadoSolicitud == 1){
		url = "Revisar";
	}
	else if(estadoSolicitud == 2){
		url = "Validar";
	}
	else if(estadoSolicitud == 3){
		url = "Liberar";
	}
	else if(estadoSolicitud == 4){
		document.getElementById("hiddenTipo").value = 6;
		document.forms["form1"].action = "BusMat?cveMat=" + cveMat;
	    document.forms["form1"].submit();
	    return;
	}
	
	location.href = url + "?cveMat="+cveMat;
}
/**
 * Pasa a la página de buscar el contenido
 * @param cveMat
 */
function funcBusContenido(cveMat){
	location.href = "BusContenido?cveMat=" + cveMat;
}
/**
 * Cambiar combo box de modulo
 */
function funcChangeModulo(selectedIdx){
	var x = document.getElementById("nivel").length -1;
	if(selectedIdx == 0){
		for(i=1; i<=x; i++){
			document.getElementById("modulo" + i).style.display = "none";		
		}
		return;
	} else{
		for(i=1; i<=x; i++){
			document.getElementById("modulo" + i).style.display = "none";		
		}
		if(document.getElementById("modulo" + selectedIdx).length <= 0){
			document.getElementById("modulo" + selectedIdx).style.display = "none";
		}else{
			document.getElementById("modulo" + selectedIdx).style.display = "inline";		
		}		
	}	
}

function funcCrearJsp(cveMat){
	document.getElementById("hiddenTipo").value = 2;
	document.forms["form1"].action = "BusMat?cveMat=" + cveMat;
    document.forms["form1"].submit();	
}

function funcExportar(cveMat){
	$("#innerMessage").html("");
	$("#message").hide();
	
	if(!confirm("Seguro que exportar la materia?")){
		return false;
	}			
	//Confirmar si no hay misma materia
	$.ajax({
		  method: "POST",
		  url: "BusMat",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { hiddenCveMat: cveMat,
			  	  hiddenTipo: 	3
			  	},
		  async: true,
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
			  alert("Error. No pudo exportar.");
		  }
		})
}

function Exportar(cveMat){
	document.getElementById("hiddenTipo").value = 4;
	document.getElementById("hiddenCveMat").value = cveMat;
	document.forms["form1"].action = "BusMat";
    document.forms["form1"].submit();
}


function funcBuscar(){
	document.getElementById("hiddenCurrentPagina").value = 1;
	document.getElementById("hiddenTipo").value = 5;
}

function funcMovePagina(numpagina){
	document.getElementById("hiddenCurrentPagina").value = numpagina;
	document.forms["form1"].action = "BusMat";
    document.forms["form1"].submit();
}