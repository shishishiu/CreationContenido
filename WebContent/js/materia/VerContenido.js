$(window).resize(function () {
	funcChangeHeight();
});
$(function() {
	iniciaMessageDialog();
	funcResizeMain();
	funcChangeHeight();
});
function funcChangeHeight() { 
	document.getElementById("IframeCont").height = $("#main").height() - $("#contAbove").height()-30;

}

/**
 * Regresar
 */
function funcRegreso(){
	document.forms["form1"].action = "BusContenido";
    document.forms["form1"].submit();
}
/**
 * Dar baja el contenido
 */
function funcBajar(){
	document.getElementById("hiddenTipo").value = "1";
	document.forms["form1"].action = "VerContenido";
    document.forms["form1"].submit();
}
/**
 * Pasar a la página de modificar el contenido
 */
function funcModificar(){
	var cveCont = document.getElementById("hiddenCveCont").value;
	location.href="ModificaContenido?cveCont="+cveCont;
}