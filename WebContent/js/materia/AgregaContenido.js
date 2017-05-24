
function funcAgregaContenido(cveMat, padreId){	
	try{
	var elemCaption = document.getElementById("caption");
	if(elemCaption.value == ""){
		alert("Ingresa caption, por favor.");
		elemCaption.focus();
		return false;
	}
	if(elemCaption.value.length > 200){
		alert("Ingresa menos de 200 letras en nombre del contenido, por favor.");
		elemCaption.focus();
		return false;
	}
	
	var unidad = "";
	var elemUnidad = document.getElementById("unidad");
	if(elemUnidad){
		unidad = elemUnidad.value
		if(unidad == "" || isNaN(unidad)){
			alert("Ingresa unidad, por favor.");
			elemUnidad.focus();
			return false;
		}
	}
	
	var apartado = "";
	var elemApartado = document.getElementById("apartado");
	if(elemApartado){
		apartado = elemApartado.value
		if(apartado == "" || isNaN(apartado) || apartado.value <= 0){
			alert("Ingresa apartado, por favor.");
			elemApartado.focus();
			return false;
		}
	}	

	if(!confirm("Segura que agregar el contenido?")){
		return false;
	}			

	document.getElementById("agregar").disabled = true

	$.ajax({
		  method: "POST",
		  url: "AgregaContenido",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { cveMat: cveMat, padreId: padreId, caption:elemCaption.value, unidad: unidad, apartado:apartado },
		  async: true,
		  success: function(data){
			  if(data['status'] == "error"){
				  alert(data['message']);
				  document.getElementById("agregar").disabled = false
				  return false;
			  } else{
				  location.reload();
			  }			  
		  }
		})
		
	}catch(e){
		alert(e);
	}
}

function funcChangeTipoAgregar(radio,captionUnidad, unidad){
	$("#trCaption").show();
	if(radio.id == "radioUnidad"){
		$("#caption").val(captionUnidad + unidad);
		$("#caption").prop('disabled', true);
		$("#divUnidad").show();
		$("#unidad").val(unidad);
	}else{
		$("#caption").val("");
		$("#caption").prop('disabled', false);
		$("#caption").focus();
		$("#unidad").val("-1");
	}
	
}