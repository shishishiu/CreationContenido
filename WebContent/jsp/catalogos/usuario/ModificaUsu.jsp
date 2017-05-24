<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="${title}" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    	<script src="js/usuario/ModificaUsu.js"></script>		

        <section style="clear: both">
            <form action="ModificaUsu" method="post" name="form1" id="form1">
                <div style="text-align: center; margin-bottom: 30px;">    

					<div class="ui-widget" id="message">
						<div class="ui-state-highlight ui-corner-all msg" >
							<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
							<strong id="innerMessage">${message}</strong></p>
						</div>
					</div>

                    <h3>${title}</h3>

                   <c:if test="${puedeMostrar}">

                    
		        	<c:if test="${hidTipo == 1 && usu.cveUsu == ''}">
		        		<p>No se encontró.</p>
		        		<input type="button" value="Regresar" class="botonimagenchico" onclick="funcRegreso()"/>
		        	</c:if>
		        	
		        	<c:if test="${!(hidTipo == 1 && usu.cveUsu == '')}">

	                     <table style="width:40%25; text-align: center" class="list1 green">
                            <tr>
                                <th width="30%25" class="right">Clave</th>
								<td width="60%25" class="left">
									<c:choose>
					    				<c:when test="${hidTipo==1}">${usu.cveUsu}</c:when>
										<c:otherwise>
											<input type="text" size="50%25" name="clave" id="clave" value="${usu.cveUsu}" />
										</c:otherwise>
							    	</c:choose>	                            
						    	</td>    	
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Nombre</th>
                                <td width="60%25" class="left">
                                	<input type="text" size="50" value="${usu.nomUsu}" name="nomUsu" id="nomUsu"/>
                                </td>
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Nombre Padrino</th>
                                <td width="60%25" class="left">
                                	<input type="text" size="50" value="${usu.nomPatUsu}" name="nomPatUsu" id="nomPatUsu"/>
                                </td>
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Nombre Madrino</th>
                                <td width="60%25" class="left">
                                	<input type="text" size="50" value="${usu.nomMatUsu}" name="nomMatUsu" id="nomMatUsu"/>
                                </td>
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Permiso</th>
                                <td width="60%25" class="left">
									<select id="permiso" name="permiso">
										<option value="-1">--------</option>
										<c:forEach var="permiso" items="${permisos}">
			       	                    	<option value="${permiso.code}" ${permiso.selected}>${permiso.nomCode}</option>
			                        	</c:forEach>
									</select>
				                </td>
                            </tr>
                           <tr>
                                <th width="30%25" class="right">Contraseña</th>
                                <td width="60%25" class="left">
                                	<input type="text" size="20" value="${usu.pwsUsu}" name="pwsUsu" id="pwsUsu"/>
                                </td>
                            </tr>
                    </table>
	                <div style="text-align: center; margin-top: 10px">
                        <input type="button" value="${boton}" name="${boton}" class="botonimagenchico" onclick="funcValidacion('${usu.cveUsu}')"/>
                        <input type="button" value="Regresar" class="botonimagenchico" onclick="funcRegreso()"/>
	                </div> 
	        	</c:if>
	        	</c:if>
                </div>
                <input type="hidden" name="hidTipo" id="hidTipo" value="${hidTipo}" />
                <input type="hidden" name="hidCveUsu" id="hidCveUsu" value="${usu.cveUsu}"/>
            </form>
        </section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
