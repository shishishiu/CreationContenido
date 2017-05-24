/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(window).resize(function () {
	funcResizeMain();
	
});

/**
 * Menu de contenidos
 */
function funcMostrarMenu(id){
	var elem = $("#" + id);
	elem.toggle();

}
function iniciaMessageDialog(){
	$("#message").hide();
	if($("#innerMessage").html().length > 0){
		$("#message").show();
	}

	if($(".message").length > 0){
		$(".message").hide();
		if($(".innerMessage").html().length > 0){
			$(".message").show();
		}
	}

	
}
function iniciaMessageDialogChild(){
	$("#messageChild").hide();
	if($("#innerMessageChild").html().length > 0){
		$("#messageChild").show();
	}
	
}
/**
 * Cambiar combo box de nivel grado
 */
function funcChangeNivelGrado(selectedIdx){
	var x = document.getElementById("nivel").length;
	for(i=1; i<=x; i++){
		document.getElementById("nivelGrado" + i).style.display = "none";		
	}
	if(document.getElementById("nivelGrado" + selectedIdx).length <= 0){
		document.getElementById("nivelGrado" + selectedIdx).style.display = "none";
	}else{
		document.getElementById("nivelGrado" + selectedIdx).style.display = "inline";		
	}
	
}

function funcResizeMain(){
	var hfooter = $("#footer").height();
	var hheader = $("#header").height();
	var hpant = window.innerHeight;
	var hdiv = $("#main").height();
	var hmain = hpant-hfooter-hheader;

	if(document.getElementById("heder_menu") == null){
		hmain = hmain - 10;
	} else{
		hmain = hmain - 33;
	}
	
	if(hdiv < hmain){
//		if(hpant > 400){
			document.getElementById("main").style.height = hmain + "px";
//		}
	}
	
	
}

function funcMostrarSolicitarHistoria(){
	var cveMat = document.getElementById("hiddenCveMat").value;
    var hsize = $(window).height() * 0.7;
    var wsize = $(window).width() * 0.7;

    $("#dialog").load('SolicitarHistoria?cveMat='+cveMat).dialog({
    	modal: true,
    	title: "Historia",
    	width: wsize,
    	height: hsize
    });
}