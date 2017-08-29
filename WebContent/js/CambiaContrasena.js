/**
 * Inicio
 */
$(function(){
	iniciaMessageDialog();
	funcResizeMain();

	var focusElem = document.getElementById("pwsUsu");
	if(focusElem){
		focusElem.focus();
	}

});


function funcValidacion(){
	
	var elemPwsUsu = document.getElementById("pwsUsu");
	if(elemPwsUsu){
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
		
		document.forms["form1"].action = "CambiaContrasena";
		document.forms["form1"].submit();

	}

	
}