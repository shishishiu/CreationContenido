/**
 * Inicio
 */
$(function(){
	var parentSubmit = document.getElementById("hiddenParentSubmit").value;
	if(parentSubmit == 1){
		parent.window.opener.document.forms[0].submit();
	}
	document.getElementById("hiddenParentSubmit").value = null;

	funcResizeMain();

});
/**
 * Dar permiso de contenido a usuarios
 */
function funcAgregar(){
	if($('#tableUsuario').find('input:checked').length <= 0){
		alert("Ponga una marca a usuario que va a dar un permiso");
		return;
	}
	
	if(confirm("Seguro que agregar permiso?")){
		
		document.getElementById("hiddenActionTipo").value = "2";
		document.forms["form1"].action = "AgregaPermisoContenido";
	    document.forms["form1"].submit();
	}
}
/**
 * Buscar usuario
 */
function funcBuscar(){
	document.getElementById("hiddenActionTipo").value = "1";
	document.forms["form1"].action = "AgregaPermisoContenido";
    document.forms["form1"].submit();
}

function funcCheckAll(elem){
	$('#tableUsuario').find("input:radio[value='"+ elem.value +"']").prop('checked', true);
	
}
function funcCerrar(){
	window.close();
}