$(function(){
	iniciaMessageDialog();
//	funcResizeMain();

});
var myVar = setInterval(funcGuardar, 600000);
var myMesVar;

function funcGuardar() {

	if(!funcValidacion()){
		return;
	}
	
	$.ajax({
		  method: "POST",
		  url: "ModificaContenido",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  beforeSend: function( xhr ) {
			    $("#message").show();
			    $("#innerMessage").html("<img src='images/loading.gif' style='vertical-align:middle;width:50px;height:60px'> Está guardando");
			  },
		  data: { hiddenCveMat: 	document.getElementById("hiddenCveMat").value,
			  	  hiddenCveCont: 	document.getElementById("hiddenCveCont").value, 
			   	  nomContenido: 	document.getElementById("nomContenido").value, 
			   	  contenido: 		CKEDITOR.instances.contenido.getData(),
			  	  hiddenTipo: 		1},
		  async: true,
		  error: function(jqXHR, textStatus, errorThrown ){
			  $("#message").show();
			  $("#innerMessage").html("Error. No se guardó el contenido" + errorThrown);
			  
		  },
		  success: function(data){
			  if(data['status'] == "error"){
				  $("#message").show();
				  $("#innerMessage").html(data['message']);
				  myMesVar = setInterval(BorrarMessageSave, 300000);
			  } else if(data['status'] == "noAutorizado"){
				  location.href = "AutentificaError";
			  } else{
				  $("#message").show();
				  $("#innerMessage").html("Contenido se guardó");
				  myMesVar = setInterval(BorrarMessageSave, 300000);
			  }			  
		  }

		})
}

function BorrarMessageSave(){
	$("#message").hide();
	$("#innerMessage").html("");
	clearInterval(myMesVar);
}

function funcRegreso(){
	document.forms["form1"].action = "BusContenido";
    document.forms["form1"].submit();
}
function funcShowCkfinder(){
	
	var finder = new CKFinder();
	
	finder.basePath = '/ckfinder/';
	finder.rememberLastFolder = false;
	finder.popup({
      height: 600
	    });

}
//function funcGuardar(){
//	
//	if(!funcValidacion()){
//		return;
//	}
//	document.getElementById("hiddenTipo").value="1";
//	document.forms["form1"].action = "ModificaContenido";
//    document.forms["form1"].submit();
//}

function funcValidacion(){
	
	var elemNomCont = document.getElementById("nomContenido");
	if(elemNomCont){
		if(elemNomCont.value == ""){
			alert("Ingresa nombre del contenido, por favor.");
			elemNomCont.focus();
			return false;
		}
		if(elemNomCont.value.length > 200){
			alert("Ingresa menos de 200 letras en nombre del contenido, por favor.");
			elemNomCont.focus();
			return false;
		}
	}
	return true;
	
}
function funcVistaPrevista(){
    var hsize = $(window).height() * 0.7;
    var wsize = $(window).width() * 0.7;

    funcWinCenter("VerVistaPrevista.jsp", "VistaPrevista", wsize, hsize);
}
function funcAgregaImagenes(){
    var hsize = $(window).height() * 0.7;
    var wsize = $(window).width() * 0.7;
    var cveMat = document.getElementById("hiddenCveMat").value;
    var cveCont = document.getElementById("hiddenCveCont").value;

    funcWinCenter("AgregaArchivos?cveMat="+cveMat+"&cveCont="+cveCont, "AgregaArchivos", wsize, hsize);
    
}
function funcWinCenter(url, name, width, height){
  
  var w = ( screen.width-width ) / 2;
  var h = ( screen.height-height ) / 2;

  window.open(url,name,"scrollbars=yes,resizable=yes,width=" + width + " ,height=" + height + " ,left=" + w + ",top=" + h);
}

