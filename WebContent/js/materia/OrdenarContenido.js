function funcCrearPostData(padreid, postdata){

	var sortArr = $( ".sortable_"+padreid ).sortable("toArray");
	var sortedData="";
	if(sortArr != ""){
		
		postdata[padreid] = sortArr;

	}
	
	for(var i=0; i<sortArr.length; i++){

		var str = sortArr[i];
		var id = str.substring(str.indexOf("_")+1);

		funcCrearPostData(id,postdata);

	}
	return sortedData;
	
}

function funcOrdenarContenido(padreId){

	var postdata = {};
	
	funcCrearPostData(padreId, postdata);
//	for (var key in postdata) {
//	    alert(key + ":" + postdata[key]);
//	}
	if(!confirm("Segura que ordenar los contenidos?")){
		return false;
	}			

	postdata["menuId"] = padreId;
	
	document.getElementById("ordenar").disabled = true

	$.ajax({
		  method: "POST",
		  url: "OrdenarContenido",
		  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
		  data: postdata,
		  async: true,
		  success: function(data){
			  if(data['status'] == "error"){
				  alert(data['message']);
				  document.getElementById("ordenar").disabled = false
				  return false;
			  } else{
				  
				  var cveMat = document.getElementById("hiddenCveMat").value;
				  location.href="BusContenido?cveMat=" + cveMat;
				  
			  }			  
		  }
	})		
}
