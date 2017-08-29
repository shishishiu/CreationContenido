$(function(){
	iniciaMessageDialog();
	funcResizeMain();
	$("#loadingDialog").hide();
	
	if(IsPrimerDir()){
		$("#fileupload").hide();
	}else{
		$("#fileupload").show();
	}
	
	
	// add and delete highlight
	$(document).click(function(event) {
		  if(!$(event.target).closest('#filelist .line').length && !$(event.target).closest('#delete').length) {
			  $("#delete").hide();
			  $("#filelist > .line").removeClass("highlight");
		  } else {
			  
			  if(IsPrimerDir()){
				  $("#delete").hide();
			  }else {			  
				  $("#delete").show();
			  }
			  $("#filelist > .line").removeClass("highlight");
			  $(event.target).closest('#filelist .line').addClass("highlight");

		  }
		});
	

});

function IsPrimerDir(){
	
	var pathDirectory = $("#pathDirectory").val();
	var arrDir = pathDirectory.split("/");
	if(arrDir.length <=1){
		return true;
	}
	return false;

	
}


function funcAbrirCarpeta(idxCarpeta, foldername){
	
	$("#delete").hide();
	
	var cveMat = $("#hiddenCveMat").val();
	var cveCont = $("#hiddenCveCont").val();
	var pathDirectory = $("#pathDirectory").val();
	
	var arrDir = pathDirectory.split("/");
	
	
	if(arrDir.length-1 < idxCarpeta){
		
		$("#pathDirectory").val(pathDirectory + "/" + foldername);
		
	}else{
		
		var str = "";
		for (i = 0; i <= idxCarpeta; i++) {
			if(str.length > 0) {
				str += "/";
			}
		    str += arrDir[i];
		}
		$("#pathDirectory").val(str);
		
	}
	
	funcLoadList(cveMat, cveCont)
	
}

function funcAbrirAgregar(){

	var cveMat = $("#hiddenCveMat").val();
	var cveCont = $("#hiddenCveCont").val();
	var tipoAgregar
	var tipoArchivo
	if(IsPrimerDir()){
		tipoAgregar = 1;
		tipoArchivo = $("#tipoArchivo").val();
	} else {
		tipoAgregar = 2;
	}
	
	var strsegundos = Date.now();
    $("#dialog").load('AgregaCarpeta?query=' + strsegundos + '&tipoAgregar=' + tipoAgregar + '&tipoArchivo=' + tipoArchivo + '&cveMat=' + cveMat + '&cveCont=' + cveCont).dialog({
    	modal: true,
    	title: "Crear Nueva Carpeta",
    	width: 400,
    	height: 170,
    	buttons: [
    		    {
    		      text: "Agregar",
    		      click: function() {
    		    	  funcAgregar();
    		      }
    		    }
    		  ]
    });

	
}
function funcAbrirAgregarArchivo(){

	var strsegundos = Date.now();
    $("#dialog").load('AgregaArchivo?query=' + strsegundos).dialog({
    	modal: true,
    	title: "Subir Archivos",
    	width: 400,
    	height: 170,
    	buttons: [
    		    {
    		      text: "Subir",
    		      click: function() {
    		    	  funcAgregarArchivo();
    		      }
    		    }
    		  ]
    });

	
}

function funcAgregar(){
	var nombreCarpeta = $("#nombreCarpeta").val();
	$("#innerMessageChild").html("");
	$("#messageChild").hide();
	
	var cveMat = $("#form1 #hiddenCveMat").val();
	var cveCont = $("#form1 #hiddenCveCont").val();
	var pathDirectory = $("#form1 #pathDirectory").val();
	
	$.ajax({
		  method: "POST",
		  url: "AgregaCarpeta",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { nombreCarpeta: nombreCarpeta,
			  	  cveMat: cveMat,
			  	  cveCont: cveCont,
			  	  pathDirectory: pathDirectory
			  	},
		  async: true,
		  success: function(data){

				if(data['status'] == "error"){
					$("#innerMessageChild").html(data['message']);
					$("#messageChild").show();
				}
				else if(data['status'] == "success"){
				  $( "#dialog" ).dialog( "close" );
				  
				  
				  //load list
				  funcLoadList(cveMat, cveCont);
				  

		  	  }		  
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No se puede continuar.");
		  }
		})
}

function funcAgregarArchivo(){

	$("#innerMessageChild").html("");
	$("#messageChild").hide();

	var cveMat = $("#form1 #hiddenCveMat").val();
	var cveCont = $("#form1 #hiddenCveCont").val();

	var form = $('#formAgregaArchivo').get(0);
    var formData = new FormData( form );

	$.ajax({
		  method: "POST",
		  url: "AgregaArchivo",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: formData,
		  processData: false,
		  contentType: false,
		  async: true,
		  success: function(data){

				if(data['status'] == "error"){
					$("#innerMessageChild").html(data['message']);
					$("#messageChild").show();
				}
				else if(data['status'] == "success"){
				  $( "#dialog" ).dialog( "close" );
				  
				  
				  //load list
				  funcLoadList(cveMat, cveCont);
				  

		  	  }		  
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No se puede continuar.");
		  }
		})
}

function funcLoadList(cveMat, cveCont){
	
	var pathDirectory = $("#pathDirectory").val();
	var rootDir = $("#rootDir").val();
	
	$("#loadingDialog").dialog({
		dialogClass: "no-close",
		modal: true,
		width: 200,
		height: 200
	});
	$("#loadingDialog").dialog("open");
	
	
	$("#innerMessage").html("");
	$("#message").hide();

	
	$.ajax({
		  method: "POST",
		  url: "VerArchivos",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { cveMat: cveMat,
			  	  cveCont: cveCont,
			  	  tipo:1,
			  	  pathDirectory: pathDirectory,
			  	  rootDir: rootDir
			  	},
		  async: true,
		  success: function(data){

				$("#loadingDialog").dialog( "close" );

			  if(data['status'] == "error"){
					
					$("#innerMessage").html(data["message"]);
					$("#message").show();
					
					
				}
				else if(data['status'] == "success"){
					
					$("#divFile").html(data["message"]);

				}
			  	funcResizeMain();
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No se puede continuar.");
		  }
		})	
		
		if(IsPrimerDir()){
			$("#fileupload").hide();
		}else{
			$("#fileupload").show();
		}

}

function funcDelete(cveMat, cveCont){
	
	var selectedName = $("#filelist .highlight .foldername").html();
	if(selectedName == "" || selectedName == null){
		selectedName = $("#filelist .highlight .filename").html();
	}

	if(!confirm("Una vez borra, no se puede corregir. Quiere borrar?")){
		return;
	}
	

	var pathDirectory = $("#pathDirectory").val();
	
	$("#innerMessage").html("");
	$("#message").hide();

	$.ajax({
		  method: "POST",
		  url: "VerArchivos",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { cveMat: cveMat,
			  	  cveCont: cveCont,
			  	  selectedName: selectedName,
			  	  tipo: 2,
			  	  pathDirectory: pathDirectory
			  	},
		  async: true,
		  success: function(data){

				if(data['status'] == "error"){
					$("#innerMessage").html(data['message']);
					$("#message").show();
				}
				else if(data['status'] == "success"){
				  
				  //load list
				  funcLoadList(cveMat, cveCont);

		  	  }		  
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No se puede continuar.");
		  }
		})
	
	
}

function funcOnChangeTipoArchivo(){
	var cveMat = $("#hiddenCveMat").val();
	var cveCont = $("#hiddenCveCont").val();
	var tipoArchivo = $("#tipoArchivo").val();

	$("#pathDirectory").val("");
	$("#rootDir").val("");
	
	$("#loadingDialog").dialog({
		dialogClass: "no-close",
		modal: true,
		width: 200,
		height: 200
	});
	$("#loadingDialog").dialog("open");
	
	
	$("#innerMessage").html("");
	$("#message").hide();

	
	$.ajax({
		  method: "POST",
		  url: "VerArchivos",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: { cveMat: cveMat,
			  	  cveCont: cveCont,
			  	  tipo:3,
			  	  tipoArchivo: tipoArchivo
			  	},
		  async: true,
		  success: function(data){

				$("#loadingDialog").dialog( "close" );

			  if(data['status'] == "error"){
					
					$("#innerMessage").html(data["message"]);
					$("#message").show();
					
					
				}
				else if(data['status'] == "success"){
					
					$("#divFile").html(data["message"]);

				}
			  	funcResizeMain();
		  },
		  error : function(xhr, ajaxOptions, thrownError) {
			  alert("Error. No se puede continuar.");
		  }
		})	

}

function funcCerrar(){
	window.close();
}
