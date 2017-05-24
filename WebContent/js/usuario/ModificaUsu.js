$(function(){
	
	var elemClv = document.getElementById("clave");
	var elemNom = document.getElementById("nomUsu");
	if(elemNom){
		elemNom.focus();
	}
	if(elemClv){
		elemClv.focus();
	}
	
	iniciaMessageDialog();
	funcResizeMain();
});

/**
 * Regresar
 */
function funcRegreso(){
	location.href = "BusUsu";
    
}

function funcValidacion(cveUsu){
	
	var hidTipo = document.getElementById("hidTipo").value;
	
	if(hidTipo == 1){	
		if(!isValidado()){
			return;
		}

		if(!confirm("Seguro que modificar el usuario de " + cveUsu + "?")){
			return false;
		}			
	} else{
		if(!isValidado()){
			return false;
		}
		if(!confirm("Segura que agregar el usuario?")){
			return false;
		}					

	}
	
	document.forms["form1"].action = "ModificaUsu";
	document.forms["form1"].submit();

	
}

function isValidado(){
	var elemClave = document.getElementById("clave");
	if(elemClave){
		if(elemClave.value == ""){
			alert("Ingresa clave, por favor.");
			elemClave.focus();
			return false;
		}
		if(elemClave.value.length > 21){
			alert("Ingresa menos de 21 letras en clave, por favor.");
			elemClave.focus();
			return false;
		}
	}

	var elemNomUsu = document.getElementById("nomUsu");
	if(elemNomUsu.value == ""){
		alert("Ingresa nombre del usuario, por favor.");
		elemNomUsu.focus();
		return false;
	}
	if(elemNomUsu.value.length > 50){
		alert("Ingresa menos de 50 letras en nombre del usuario, por favor.");
		elemNomUsu.focus();
		return false;
	}

	var elemNomPatUsu = document.getElementById("nomPatUsu");
	if(elemNomPatUsu.value == ""){
		alert("Ingresa nombre padrino del usuario, por favor.");
		elemNomPatUsu.focus();
		return false;
	}
	if(elemNomPatUsu.value.length > 50){
		alert("Ingresa menos de 50 letras en nombre padrino del usuario, por favor.");
		elemNomPatUsu.focus();
		return false;
	}

	var elemNomMatUsu = document.getElementById("nomMatUsu");
	if(elemNomMatUsu.value == ""){
		alert("Ingresa nombre madrino del usuario, por favor.");
		elemNomMatUsu.focus();
		return false;
	}
	if(elemNomMatUsu.value.length > 50){
		alert("Ingresa menos de 50 letras en nombre madrino del usuario, por favor.");
		elemNomMatUsu.focus();
		return false;
	}

	var elemPermiso = document.getElementById("permiso");
	if(elemPermiso.value == "" || isNaN(elemPermiso.value) || elemPermiso.value <= 0){
		alert("Ingresa permiso, por favor.");
		elemPermiso.focus();
		return false;
	}
	
	var elemPwsUsu = document.getElementById("pwsUsu");
	if(elemPwsUsu.value == ""){
		alert("Ingresa contraseña, por favor.");
		elemPwsUsu.focus();
		return false;
	}
	if(elemPwsUsu.value.length > 21){
		alert("Ingresa menos de 21 letras en contraseña, por favor.");
		elemPwsUsu.focus();
		return false;
	}

	return true;
	
}
