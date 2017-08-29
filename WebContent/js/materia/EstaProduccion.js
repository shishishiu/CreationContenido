$(function() {
	iniciaMessageDialog();
	funcResizeMain();

});

function funcRegreso(){
    location.href = "BusMat"
}

function funcEnviar(){
	
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
	
	if(!confirm("Seguro que envía el resultado?")){
		return false;
	}
	
	document.forms["form1"].action = "EstaProduccion";
    document.forms["form1"].submit();

}
