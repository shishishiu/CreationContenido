/**
 * Inicio
 */
$(function(){

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

function funcModificar(key, idx){
	
	if(!confirm("Se va a modificar '" + key + "'")){
		return;
	}
	
	var val = $("#val" + idx).val();
			
	document.getElementById("modificar" + idx).disabled = true
	$("#wait" + idx).html("<img src='images/loading.gif' style='vertical-align:middle'>");

	try{
				
		$.ajax({
			  method: "POST",
			  url: "ModificaConfiguracion",
			  headers: {"HTTP_X_REQUESTED_WITH": "xmlhttprequest"},
			  data: { key: key, val: val },
			  async: true,
			  success: function(data){
					document.getElementById("modificar" + idx).disabled = false
	 			    $("#wait" + idx).html(data['message']);
	
					if(data['status'] == "error"){
					  return false;
				  } else{
					  
				  }			  
			  },
			  error: function(jqXHR, textStatus, errorThrown ){
				  alert(textStatus);
				  
			  }
		})
	}catch(e){
		alert(e)
	}
	
}