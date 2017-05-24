$(function() {

	funcResizeMain();
	
    funcSortable();

});


function funcModificar(cveCont){
	
	location.href="ModificaContenido?cveCont="+cveCont;
}
function funcBajar(cveCont){
	if(!confirm("Seguro que dar baja del contenido?")){
		return;
	}			

	document.getElementById("hiddenTipo").value=2;
	document.getElementById("hiddenCveCont").value = cveCont;

	document.forms["form1"].action = "BusContenido";
	document.forms["form1"].submit();	
}
function funcVer(cveCont){
	document.getElementById("hiddenCveCont").value = cveCont;

	document.forms["form1"].action = "VerContenido";
	document.forms["form1"].submit();
}
function funcRegreso(){
    location.href = "BusMat"
}
function funcAgregar(padreId){
	var strsegundos = Date.now();
    $("#dialog").load('AgregaContenido?padreId='+padreId + '&query=' + strsegundos).dialog({
    	modal: true,
    	title: "Agregar Contenido",
    	width: "500px"
    });
}

function funcSortable(){
    $( ".sortable" ).sortable({
        axis: 'y',
        opacity: 0.5,
        update: function(event, ui) {
        	
        	if (!confirm("Va a mover?")) {
                $(ui.sender).sortable('cancel');
                return false;
            }
        	
        	var sorted = $(this).sortable("toArray");
        	
        	funcModificarOrden(sorted);
        	
        }
    });
    
    $( ".sortable" ).disableSelection();
}

function funcModificarOrden(ordens){
	$.ajax({
		  method: "POST",
		  url: "BusContenido",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { menuOrdens: ordens,
			  	  hiddenTipo: 1
			  	},
		  async: true,
		  success: function(data){
			  if(data['status'] == "error"){
				  alert(data['message']);
	
			  }else if(data['status'] == "success"){
				  
				  if(data['message'].length > 0){
					  if(!confirm(data['message'])){
						  return false;
					  }
				  }

		  	  }		  
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No se puede continuar.");
		  }
		})
}

function funcOrdenar(padreId){
	
	var cveMat = $("#hiddenCveMat").val();
	var strsegundos = Date.now();
    $("#dialog").load('OrdenarContenido?padreId='+padreId + '&cveMat='+cveMat + '&query=' + strsegundos).dialog({
    	modal: true,
    	title: "Ordenar Contenido",
    	width: "500px"
    });
}