$(function() {
	iniciaMessageDialog();
	funcResizeMain();
  
});

function funcRegreso(){
    location.href = "BusMat"
}

function funcSolicitar(){
	
	if(!confirm("Seguro que solicita la materia")){
		return false;
	}
	
	document.forms["form1"].action = "Solicitar";
    document.forms["form1"].submit();

}
function funcVerContenidos(){
	
}