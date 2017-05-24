<%@page contentType="text/html" pageEncoding="ISO-8859-1" session="false" %>
<%@page import="util.conf.Configuracion"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <title></title>
    <meta name="description" content="Responsive Multi-Level Menu: Space-saving drop-down menu with subtle effects" />
    <meta name="keywords" content="multi-level menu, mobile menu, responsive, space-saving, drop-down menu, css, jquery" />
    <meta name="author" content="Codrops" />
    <link rel="shortcut icon" href="../favicon.ico"> 
    <link rel="stylesheet" type="text/css" href="css/style.css" />
	<link rel="stylesheet" href="css/jquery-ui.min.css">
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<script src="js/jquery-1.12.0.min.js"></script>
	<script src="js/jquery-ui.min.js"></script>

    <script src="js/modernizr.custom.js"></script>
    <script src="js/common.js"></script>

<style>
.fondoverde
{
    background:url(images/cabezadoPie/Fondo_Verde2.jpg);
}
picture {display: block; width: 80%; text-align: center;}
#titulo{font-size: 23px; padding:10px; color:gray;float: left; vertical-align: middle;}
</style>
<div id="header">
<table width="100%" border="0" cellpadding="0" cellspacing="0"  style="background:url(images/cabezadoPie/Fondo_Ban.jpg);  ">
    <tr> 
      <td width="30%"   align="center" >  
        <img src="images/cabezadoPie/Gobierno.png" alt="Gobierno del Estado de México" height="50">
        
        </td>
      <td></td>
      <td width="30%" align="center">
        
        <img src="images/cabezadoPie/LogoUDEMex.png" alt="Plataforma Educativa y de Control Escolar de Licenciaturas" height="50">
        
       </td>
    </tr>
    <tr>
      <td align="center">   
      </td>
      </tr>
  </table>  
  <div class="fondoverde">
  		<div id="titulo">
		<c:if test="${param.mostrarMenu != false}">
  			<a href="/CreationContenido/BusMat" style="text-decoration: none;color:gray">
		</c:if>
	  			Creación
		  		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;de
		  		<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Contenidos
		<c:if test="${param.mostrarMenu != false}">
	  		</a>
		</c:if>
	  	</div>
        <picture>
          <source media="(min-width: 800px)"  srcset="images/cabezadoPie/Baner.png">
          <source media="(min-width: 600px)"  srcset="images/cabezadoPie/Baner2.png">
          <source media="(min-width: 400px)"  srcset="images/cabezadoPie/Baner3.png">
          <source media="(min-width: 4px)"  srcset="images/cabezadoPie/Baner4.png">
          <!-- img tag for browsers that do not support picture element -->
          <img src="images/cabezadoPie/Baner.png" alt="Plataforma Educativa y de Control Escolar de la UDEMEX" height="120">
        </picture>
 </div>      
 </div> 
