/**
 * Inicio
 */
$(function(){

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

function funcAbrirModificar(nivel, nivgrado, tieneMat){
	
	var strsegundos = Date.now();
    $("#dialog").load('ModificaNivelGrado?query=' + strsegundos + "&tipo=2&nivel=" + nivel + "&nivgrado=" + nivgrado + "&tieneMat=" + tieneMat).dialog({
    	modal: true,
    	title: "Modificar",
    	width: "510px"
    });

}

function funcAbrirAgregar(nivel){

	var strsegundos = Date.now();
    $("#dialog").load('ModificaNivelGrado?query=' + strsegundos + "&tipo=1&nivel=" + nivel).dialog({
    	modal: true,
    	title: "Agregar",
    	width: "510px"
    });
}

function funcModificar(){
	var nomNivGrado = $("#nomNivGrado").val();
	var tipo = $("#hiddenDialogoTipo").val();
	var nivel = $("#hiddenNivel").val();
	var nivelgrado = $("#hiddenDialogoNivelGrado").val();
	var nomGrado = $("#nomGrado").val();
	var tieneMat = $("#hiddenTieneMat").val();
	
	if(nomNivGrado.length <= 0){
		alert("Ingresa nombre del nivel grado, por favor");
		document.getElementById("nomNivGrado").focus();
		return;
	}
	if(nomNivGrado.length > 255){
		alert("Ingresa menos de 255 letras en el nombre del nivel grado, por favor");
		document.getElementById("nomNivGrado").focus();
		return;
	}
	if(nomGrado.length <= 0){
		alert("Ingresa nombre del nombre de directorio, por favor");
		document.getElementById("nomGrado").focus();
		return;
	}
	if(nomNivGrado.length > 50){
		alert("Ingresa menos de 50 letras en el nombre dedirectorio, por favor");
		document.getElementById("nomGrado").focus();
		return;
	}
	
	if(tipo == 1){
		if(!confirm("Seguro que agregar el nivel grado?")){
			return;
		}
		
	}else if(tipo == 2){
		if(!confirm("Seguro que modificar el nivel grado?")){
			return;
		}
		
	}else{
		return;
	}
	
	
	
	$.ajax({
		  method: "POST",
		  url: "ModificaNivelGrado",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: {tipo:tipo, nivel: nivel, nivgrado:nivelgrado, nomNivGrado:nomNivGrado, nomGrado:nomGrado, tieneMat:tieneMat},
		  async: true,
		  success: function(data){
			  if(data['status'] == "error"){
				  alert(data['message']);
				  return false;
			  } else{
				  location.href="VerNivel";
			  }			  
		  }
	})		
	
}

function funcBajar(nivelgrado){
	
	if(!confirm("Seguro que bajar el nivel grado?")){
		return;
	}
	
	$("#hiddenTipo").val(1);
	$("#hiddenNivelGrado").val(nivelgrado);
	
	document.forms["form1"].action = "VerNivel";
    document.forms["form1"].submit();

	
}
