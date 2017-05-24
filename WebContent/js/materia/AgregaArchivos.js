$(function(){
	iniciaMessageDialog();
	funcResizeMain();

});

function funcAgregar(){
	
	var files = document.getElementById("file");
	if(files.value == ""){
		alert("Elige archivo, por favor.");
		files.focus();
		return false;
	}
	if(document.getElementById("tipoArchivo").value == 0){
		alert("Elige tipo del archivo, por favor.");
		document.getElementById("tipoArchivo").focus();
		return false;
	}
	
	if ('files' in files) {
	    for (var i = 0; i < files.files.length; i++) {
	        var file = files.files[i];
	        if(file.size > 20000000){
	        	alert("Puedes subir archivos de hasta 20 megabytes (MB). " + file.name + " es mayor que 20MB.")
	        	return false;
	        }
	    }
	} 
	
	if(!confirm("Seguro que agregar archivo?")){
		return;
	}					
	
	document.getElementById("hiddenTipo").value = "1";
	document.forms["form1"].action = "AgregaArchivos";
    document.forms["form1"].submit();
}
function funcCerrar(){
	window.close();
}
function funcEligirTipo(){
	var filename = document.getElementById("file").value;
	var i = filename.lastIndexOf(".");
	var extension = filename.substring(i).toLowerCase();
	var selection;
	if(extension == ".bmp" || extension == ".gif" || extension == ".jpeg" || 
	   extension == ".jpg" || extension == ".png"){
		selection = 1
	}else if(extension == ".mp3" || extension == ".wav" || extension == ".aiff" || 
    		extension == ".wma" || extension == ".m4a"){
		selection = 3
	}
	document.getElementById("tipoArchivo").value = selection
}

function funcAgregarHtml5(){
	var cveMat = $("#hiddenCveMat").val();
	var cveCont = $("#hiddenCveCont").val();
	location.href="VerArchivos?cveMat=" + cveMat + "&cveCont=" + cveCont;
}