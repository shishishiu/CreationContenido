<%-- 
    Document   : BusContenido2
    Created on : 9/03/2016, 10:50:58 AM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Buscar Contenido" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/materia/BusContenido.js"></script>		
			<script src="js/materia/AgregaContenido.js"></script>
    		<script src="js/materia/OrdenarContenido.js"></script>		
			<section style="clear: both">

<style type="text/css">
li{
	list-style-type: none;
	margin: 10px 0px 10px 0px;
}
</style>

			<div id="dialog"></div>
			
			<form action="VerContenido" method="post" name="form1" id="form1">
            <div style="text-align: center;">
                <h3>Buscar Contenido</h3>
                
                <c:if test="${materia.cveMat == ''}" >
                	<p style="text-align:center">No hay información</p>
		            <div style="margin-top: 10px">
			            <input type="button" value="Regresar" class="botonimagenchico" onclick="funcRegreso()"/>
					</div>
                </c:if>
                <c:if test="${materia.cveMat != ''}" >		                
                
		        	<table class="list1 green" style="text-align: center; width:50%25">   
	                	<tr>
	           	            <th width="15%25" class="right">Clave</th>
							<td width="35%25" class="left">${materia.cveMat}</td>
							<th width="15%25" class="right">Modulo</th>
	                        <td width="35%25" class="left">${materia.modulo}</td>	
	                    </tr>
	                    <tr>
	                        <th class="right">Nombre</th>
	                        <td class="left">${materia.nomMat}</td>
	                        <th class="right">Unidad</th>
	                        <td class="left">${materia.unidad}</td>
	                    </tr>
	                    <tr>
	                       	<th class="right">Nivel</th>
	                        <td class="left" colspan="3">${materia.nomNivel} ${materia.nomNivelGrado}</td>
	                    </tr>
		            </table>
		            <div style="margin-top: 10px">
			            <input type="button" value="Regresar" class="botonimagenchico" onclick="funcRegreso()"/>
					</div>
                </c:if>
            </div>
            <c:if test="${materia.cveMat != ''}" >
            
            <div style="overflow:scroll; height:350px">


            ${menuHtml}
            </div>
            
           </c:if>
            <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value="${materia.cveMat}"/>
            <input type="hidden" id="hiddenCveCont" name="hiddenCveCont" value=""/>
           	<input type="hidden" id="hiddenTipo" name="hiddenTipo" value=""/>
           	<input type="hidden" id="hiddenUnidad" name="hiddenUnidad" value=""/>
           	<input type="hidden" id="hiddenApartado" name="hiddenApartado" value=""/>
		    </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
                