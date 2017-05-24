/**
 * Inicio
 */
$(function(){

	iniciaMessageDialog();
	funcResizeMain();
	
});

function funcDownload(tipo){

	$("#innerMessage").html("");
	$("#message").hide();

	var nivel = $("#nivel").val();
	if(nivel == null || nivel <= 0){
		alert("Elige Nivel, por favor");
		$("#nivel").focus();
		return;
	}
	
	document.getElementById("hiddenTipo").value = 1;
	document.getElementById("hiddenTipoArchivo").value = tipo;
	document.forms["form1"].action = "VerPlantilla";
    document.forms["form1"].submit();

}

function funcOpenModificar(tipo){

	$("#innerMessage").html("");
	$("#message").hide();

	var nivel = $("#nivel").val();
	if(nivel == null || nivel <= 0){
		alert("Elige Nivel, por favor");
		$("#nivel").focus();
		return;
	}

	var strsegundos = Date.now();
    $("#dialog").load('ModificaPlantilla?query=' + strsegundos + "&nivel=" + nivel + "&tipo=" + tipo).dialog({
    	modal: true,
    	title: "Modificar Plantilla",
    	width: "500px"
    });

}

function funcModificar(tipo, nivel){
	
	$("#innerMessageChild").html("");
	$("#messageChild").hide();

	
	var files = document.getElementById("file");
	if(files.value == ""){
		alert("Elige archivo, por favor.");
		files.focus();
		return false;
	}
	
	document.getElementById("modificar").disabled = true

	var form = $('#form2').get(0);
    var formData = new FormData( form );
    
	$.ajax({
		  method: "POST",
		  url: "ModificaPlantilla",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
//		  data: { hiddenTipo: 2, hiddenTipoArchivo: tipo, hiddenNivel: nivel},
		  data: formData,
		  processData: false,
		  contentType: false,
		  async: true,
		  success: function(data){
				$("#innerMessageChild").html(data['message']);
				$("#messageChild").show();
				document.getElementById("modificar").disabled = false
		  }
		})
	
}