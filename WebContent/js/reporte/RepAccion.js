/**
 * Inicio
 */
$(function(){
	var focusElem = document.getElementById("fechaInicial");
	if(focusElem){
		focusElem.focus();
	}
	
	iniciaMessageDialog();
	funcResizeMain();
	
	$( "tr.datarow" ).hover(
		function() {
		  $(this).addClass("highlight");
		}, 
		function(){
			$(this).removeClass("highlight");
		});
	
	$.datepicker.setDefaults({
	    monthNames: ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dec'],
    	dayNamesMin: [ "Do", "Lu", "Mar", "Mie", "Jue", "Vie", "Sab" ],
        numberOfMonths: 3,
        showCurrentAtPos: 2,
        stepMonths: 3,
        maxDate: '0',
        dateFormat:'dd/mm/yy',
        buttonImage: "images/calendar.png",
        buttonImageOnly: true,
        buttonText: "calendar",
        showOn: "button"
	})
	
	$('#fechaInicial').datepicker({
		onSelect: function(dateStr) {
			var min = $(this).datepicker('getDate') || new Date();
	        $('#fechaTerminal').datepicker('option', {minDate: min});
	    	if(dateStr != null){
	    		$("#fechaTerminal").focus();
	    	}

	    }
	});
	$('#fechaTerminal').datepicker({
		onSelect: function(dateStr) {
			var max = $(this).datepicker('getDate') || new Date();
	        $('#fechaInicial').datepicker('option', {maxDate: max});

	    }
	});
	
	$('#fechaInicial').change(function() {
		var txt = $("#fechaInicial").val();
		if(txt == ""){
			$('#fechaTerminal').datepicker('option', {minDate: null});	
		}
	})
	$('#fechaTerminal').change(function() {
		var txt = $("#fechaTerminal").val();
		if(txt == ""){
			$('#fechaInicial').datepicker('option', {maxDate: '0'});	
		}
	})

	
});

function funcBuscar(){
	if($('#fechaInicial').val() == "" && $('#fechaTerminal').val() == "" && 
			$('#usuario').val() == "" && $('#accion').val() == ""){
		alert("Ingresa algun texto");
		document.getElementById("fechaInicial").focus();
		return false;
	}
}


