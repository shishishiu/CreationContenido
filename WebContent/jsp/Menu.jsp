<%@page import="util.common.Common"%>
<link rel="stylesheet" type="text/css" href="css/menu_component.css" />
<script src="js/jquery.dlmenu.js"></script>
<jsp:useBean id="menuBean" class="beans.Menu"></jsp:useBean>
<%beans.Usuario u = new beans.Usuario(request, response);%>

<style>
ul.dl-menu,.dl-menuwrapper{
	width:150px;
}
ul.dl-menu,.dl-menuwrapper li a{
	line-height:5px;
}

</style>
<div class="demo-4" id="heder_menu">	
   <div class="main1 clearfix1">
       <div class="column">
           <div id="dl-menu" class="dl-menuwrapper">
               <button>Open Menu</button>
               <ul class="dl-menu">
				<%
					out.print(menuBean.BuscarMenu(0,u.getPerUsu()));
				%>
               </ul>
           </div><!-- /dl-menuwrapper -->
        </div>
    </div>
</div><!-- /container -->
<script>
	$(function() {
		$( '#dl-menu' ).dlmenu({
			animationClasses : { in : 'dl-animate-in-2', out : 'dl-animate-out-2' }
		});
	});
</script>
<p style="text-align: right; margin-right: 5px">
Usuario: <%out.print(u.getCveUsu() + " " + u.getNomCompletoUsu() + " " + u.getPerUsuNombre()); %>
</p>
