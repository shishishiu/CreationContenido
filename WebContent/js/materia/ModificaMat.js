/**
 * Inicio
 */
$(function(){
	
	var elemClvMat = document.getElementById("clave");
	var elemNomMat = document.getElementById("nomMat");
	if(elemClvMat){
		elemClvMat.focus();
	}else if(elemNomMat){
		elemNomMat.focus();
	}
	
	var nivel = document.getElementById("nivel").value;
	funcChangeNivelGrado(nivel);

	iniciaMessageDialog();
	funcResizeMain();
});

/**
 * Regresar
 */
function funcModMatRegreso(){
    var hidBack = document.getElementById("hidBack").value;
    
    if(hidBack=="1"){
        location.href = "BusMat";
    }
    else if(hidBack=="2"){
        location.href = "BusContenido";
    } else{
    	location.href = "BusMat";
    }
    
}

/**
 * Verificación de entrada
 */
function funcModMatVerificEntrada(cveMat){
	
	if(cveMat != ''){	
		if(!isValidado()){
			return;
		}

		if(!confirm("Seguro que modificar la materia de " + cveMat + "?")){
			return;
		}			
	} else{
		var elemClave = document.getElementById("clave");
		if(elemClave.value == ""){
			alert("Ingresa clave de la materia, por favor.");
			elemClave.focus();
			return;
		}
		if(elemClave.value.length > 20){
			alert("Ingresa menos de 20 letras en clave de la materia, por favor.");
			elemClave.focus();
			return;
		}
		
		
		if(elemClave.value.match(/[ñáéíóú]/gi) != null){
			alert("No puede ingresar ñ, á, é, í, ó, ú para la clave.");
			elemClave.focus();
			return;
		}
		
		
		if(!isValidado()){
			return;
		}
		
		if(!confirm("Segura que agregar la materia?")){
			return;
		}					
	}
	document.forms["form1"].action = "ModificaMat";
	document.forms["form1"].submit();

}

function isValidado(){
	var elemNomMat = document.getElementById("nomMat");
	if(elemNomMat.value == ""){
		alert("Ingresa nombre de la materia, por favor.");
		elemNomMat.focus();
		return false;
	}
	if(elemNomMat.value.length > 50){
		alert("Ingresa menos de 50 letras en nombre de la materia, por favor.");
		elemNomMat.focus();
		return false;
	}
	var elemNivel = document.getElementById("nivel");
	var nivel = elemNivel.value;
	if(nivel == ""){
		alert("Ingresa nivel, por favor.");
		elemNivel.focus();
		return false;
	}
	if(isNaN(nivel) || nivel <= 0){
		alert("Ingresa numero que más de 1 en el nivel, por favor.");
		elemNivel.focus();
		return false;
	}
	
	var elemNivelGrado = document.getElementById("nivelGrado"+nivel);
	var nivelgrado = elemNivelGrado.value;
	if(nivelgrado == ""){
		alert("Ingresa nivel grado, por favor.");
		elemNivelGrado.focus();
		return false;
	}
	if(isNaN(nivelgrado) || nivelgrado <= 0){
		alert("Ingresa numero que más de 1 en el nivel grado, por favor.");
		elemNivelGrado.focus();
		return false;
	}
	
	var elemModulo = document.getElementById("semestre");
	var modulo = elemModulo.value;
	if(modulo == ""){
		alert("Ingresa modulo, por favor.");
		elemModulo.focus();
		return false;
	}
	if(isNaN(modulo) || modulo <= 0 || modulo > 999){
		alert("Ingresa numero que más de 0 a 999 en el modulo grado, por favor.");
		elemModulo.focus();
		return false;
	}

	var elemUnidad = document.getElementById("unidad");
	var unidad = elemUnidad.value;
	if(unidad == ""){
		alert("Ingresa unidad, por favor.");
		elemUnidad.focus();
		return false;
	}
	if(isNaN(unidad) || unidad <= 0 || unidad > 999){
		alert("Ingresa numero que más de 0 a 999 en la unidad, por favor.");
		elemUnidad.focus();
		return false;
	}
	return true;
	
}


function funcModificarPermiso(){
	var cveMat = document.getElementById("hidCveMat").value;
    location.href = "ModificaPermisoContenido?cveMat=" + cveMat;
}