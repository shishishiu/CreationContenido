<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <body>
		      
		<script>
		$(function() {
			document.getElementById("caption").focus();
			document.getElementById("agregar").disabled = false
		});

		</script>  
		<form action="AgregaContenido" method="post" name="form1" id="form1">
		    <div style="text-align: center; margin-bottom: 10px">
		    
	            <table style="width: 40%25; border: 0;  text-align: center" class="list1 green">
	            	<c:if test="${padreCaption == contenidoMateria}" >
	            	<tr>
	            		<th width="30%25" class="right">¿Cuál agrega?</th>
	            		<td>
	            			<input type="radio" id="radioUnidad" name="tipoAgregar" onclick="funcChangeTipoAgregar(this,'${captionUnidad}','${countUnidad+1}')"/><label for="radioUnidad">Nueva Unidad</label>
	            			<input type="radio" id="radioOtro" name="tipoAgregar" onclick="funcChangeTipoAgregar(this,'','')"/><label for="radioOtro">Otro</label>
	            		</td>
	            	</tr>
	            	</c:if>
	            	
	            	<c:if test="${padreCaption == contenidoMateria}" >
	                	<tr style="display:none" id="trCaption">
	                </c:if>
	                <c:if test="${padreCaption != contenidoMateria}">
	                	<tr>
	                </c:if>
	                
                        <th width="30%25" class="right">Caption</th>
						<td width="50%25" class="left">
							<input type="text" id="caption" name="caption" />
							<c:if test="${padreCaption == contenidoMateria}" >
		                		<input type="hidden" id="unidad" name="unidad" value=""/>
		                	</c:if>
						</td>    	
                    </tr>


                    <c:if test="${unidad > 0}" >
                    
	                    <tr>
	                        <th width="30%25" class="right">Unidad </th>
							<td width="50%25" class="left">
								<select id="unidad" name="unidad">
									<option value="">--------</option>
										<c:forEach var="i" begin="1" end="${materiaUnidad}">
											
											<c:choose>
											 <c:when test="${i == unidad}">
											     <c:set var="selected" value="selected"/>
											 </c:when>
											 <c:otherwise>
										        <c:set var="selected" value=""/>
	  									     </c:otherwise>
											</c:choose>
											<option value="${i}" ${selected }>${i}</option>
										</c:forEach>
								</select>
							</td>    	
	                    <tr>
		                <tr>
	                        <th width="30%25" class="right">Apartado</th>
							<td width="50%25" class="left">
								<input type="text" id="apartado" name="apartado" value="${apartado }" />
							</td>    	
	                    </tr>
                   	</c:if>
                </table>
                <div style="margin-top:10px">
		    		<input type="button" id="agregar" value="Agregar" onclick="funcAgregaContenido('${cveMat}','${padreId}')"/>
				</div>
			</div>
									
		</form>
    </body>
</html>
