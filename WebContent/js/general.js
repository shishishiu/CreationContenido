/**
 * Inicio
 */
$(function(){
	funcResizeMain();
});


/**************************************
 * Autentifica.jsp
 **************************************/
/**
 * Validación
 */
function funcAutentifica(){
	if($("#IdUsu").val() == ""){
		alert("Ingresa Usuario, por favor");
		$("#IdUsu").focus();
		return false;
	}
	if($("#PassIdUsu").val() == ""){
		alert("Ingresa Contraseña, por favor");
		$("#PasswordUsu").focus();
		return false;
	}
    return true;
}

/**************************************
 * AutentificaError.jsp
 **************************************/
function funcRegresarAutentifica(){
	location.href = "Autentifica";
}