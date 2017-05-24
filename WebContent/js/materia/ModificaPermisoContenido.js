/**
 * Inicio
 */
$(function(){
	iniciaMessageDialog();
	funcResizeMain();
});
/**
 * Dar permiso de contenido a usuarios
 */
function funcConfirmar(){
	if(confirm("Seguro que modificar permiso a los usuarios que tiene una marca de verificación?")){
		
		document.getElementById("hiddenActionTipo").value = "1";
		document.forms["form1"].action = "ModificaPermisoContenido";
	    document.forms["form1"].submit();
	}
}

function funcCheckAll(elem){
	$('#tableUsuario').find("input:radio[value='"+ elem.value +"']").prop('checked', true);
	
}

function funcAnadir(){
	var cveMat = document.getElementById("hiddenCveMat").value;
	
	window.open("AgregaPermisoContenido?cveMat=" + cveMat,"Agregar Permiso", "scrollbars=yes,resizable=yes,width=1200,height=600");
		
}
/**
 * Regresar
 */
function funcRegreso(){
	var cveMat = document.getElementById("hiddenCveMat").value;
	location.href = "ModificaMat?tipo=1&back=1&cveMat=" + cveMat;
}
function funcMovePagina(numpagina){
	document.getElementById("hiddenCurrentPagina").value = numpagina;
	document.forms["form1"].action = "ModificaPermisoContenido";
    document.forms["form1"].submit();
}