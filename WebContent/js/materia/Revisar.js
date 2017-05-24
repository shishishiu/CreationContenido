$(function() {
	iniciaMessageDialog();
	funcResizeMain();

});

function funcRegreso(){
    location.href = "BusMat"
}

function funcRevisar(){
	
	document.getElementById("hiddenTipo").value = 1;
	
	var resultados = $(".resultado");
	var flag = false;
	if(resultados.length) {
		var i;
		for(i = 0; i < resultados.length; i ++){

			if(resultados[i].checked){

				flag = true; break;

			}

		}

	}
	
	if(!flag){
		alert("Elige el resultado, pof favor");
		resultados[0].focus();
		return false;
	}
	
	if(!confirm("Seguro que revisa la materia")){
		return false;
	}
	
	document.forms["form1"].action = "Revisar";
    document.forms["form1"].submit();

}
function funcImprimir(){
	document.getElementById("hiddenTipo").value = 2;

	document.forms["form1"].action = "Revisar";
    document.forms["form1"].submit();

}