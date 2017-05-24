/**
 * Inicio
 */
$(function(){
	var focusElem = document.getElementById("cveUsu");
	if(focusElem){
		focusElem.focus();
	}

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
 * Pasa a la p�gina de agregar la materia
 */
function funcAgregar(){
	location.href = "ModificaUsu?tipo=2";
}

/**
 * Pasa a la p�gina de modificar la materia
 * @param cveMat
 */
function funcModificar(cveUsu){
	location.href = "ModificaUsu?tipo=1&cveUsu=" + cveUsu;
}

function funcBuscar(){
	document.getElementById("hiddenTipo").value = 0;
	document.forms["form1"].action = "BusUsu";
    document.forms["form1"].submit();
}

/**
 * Dar de baja la materia
 * @param cveMat
 */
function funcDarBaja(cveUsu, banmat){
	if(banmat == 1){
		if(!confirm("Seguro que dar baja el usuario de " + cveUsu + "?")){
			return false;
		}
	} else{
		if(!confirm("Segura que dar alta el usuario de " + cveUsu + "?")){
			return false;
		}
	}
	
	document.getElementById("hiddenTipo").value = 1;
	document.forms["form1"].action = "BusUsu?paramCveUsu=" + cveUsu;
    document.forms["form1"].submit();
}

function funcMovePagina(numpagina){
	document.getElementById("hiddenCurrentPagina").value = numpagina;
	document.getElementById("hiddenTipo").value = 2;
	document.forms["form1"].action = "BusUsu";
    document.forms["form1"].submit();
}