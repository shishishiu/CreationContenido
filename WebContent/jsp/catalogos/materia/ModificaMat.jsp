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
    	<script src="js/materia/ModificaMat.js"></script>		

        <section style="clear: both">
            <form action="ModificaMat" method="post" name="form1" id="form1">
           		<div class="ui-widget" id="message">
					<div class="ui-state-highlight ui-corner-all msg" >
						<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
						<strong id="innerMessage">${message}</strong></p>
					</div>
				</div>

                <div style="text-align: center; margin-bottom: 30px;">    
                    <h3>${title}</h3>
                    
		        	<c:if test="${materia.cveMat == ''}">
		        		<p>No se encontró la materia.</p>
		        		<input type="button" value="Regresar" class="botonimagenchico" onclick="funcModMatRegreso()"/>
		        	</c:if>
		        	<c:if test="${materia.cveMat != ''}">

	                     <table style="width:40%25; text-align: center" class="list1 green">
                            <tr>
                                <th width="30%25" class="right">Clave de la materia</th>
								<td width="60%25" class="left">
									<c:choose>
					    				<c:when test="${hidTipo == 1}">${materia.cveMat}</c:when>
										<c:otherwise><input type="text" size="50%25" name="clave" id="clave" /></c:otherwise>
							    	</c:choose>	                            
						    	</td>    	
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Nombre de la materia</th>
                                <td width="60%25" class="left">
                                	<input type="text" size="50" value="${materia.nomMat}" name="nomMat" id="nomMat"/>
                                </td>
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Nivel</th>
                                <td width="60%25" class="left">
									<select id="nivel" name="nivel" onchange="funcChangeNivelGrado(this.options[this.options.selectedIndex].value)">
				                        <c:forEach var="nivel" items="${niveles}">
				       	                    <option value="${nivel.cveNivel}" ${nivel.selected}>${nivel.nomNivel}</option>
				                        </c:forEach>
				                    </select>
									
									<c:forEach var="nivelGrado" items="${nivelGrados}" varStatus="status">
										<select id="nivelGrado${status.index+1}" name="nivelGrado${status.index+1}">
											<option value="0" >------------</option>
											<c:forEach var="grado" items="${nivelGrado}">
				       	                    	<option value="${grado.cveNivelGrado}" ${grado.selected}>${grado.nomNivelGrado}</option>
				                        	</c:forEach>
										</select>
									</c:forEach>
				                </td>
                            </tr>
                           <tr>
                                <th width="30%25" class="right">Modulo</th>
                                <td width="60%25" class="left">
                                	<input type="text" size="20" value="${materia.modulo}" name="semestre" id="semestre"/>
                                </td>
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Unidad</th>
                                <td width="60%25" class="left">

									<c:choose>
					    				<c:when test="${hidTipo == 1}"><input type="text" size="20" value="${materia.unidad}" name="unidad" id="unidad" disabled="disabled"/></c:when>
										<c:otherwise><input type="text" size="20" name="unidad" id="unidad" /></c:otherwise>
							    	</c:choose>	                            



                                	
                                </td>
                            </tr>
                            <c:if test="${hidTipo == 1}">
	                            <tr>
	                            	<td colspan="2"><a href="javascript:void(0)" onclick="funcModificarPermiso()">Modificar el permiso del contenido de la materia</a></td>
	                            </tr>
                            </c:if>
                    </table>
	                <div style="text-align: center; margin-top: 10px">
                        <input type="button" value="${boton}" name="${boton}" class="botonimagenchico" onclick="funcModMatVerificEntrada('${materia.cveMat}')"/>
                        <input type="button" value="Regresar" class="botonimagenchico" onclick="funcModMatRegreso()"/>
	                </div> 
	        	</c:if>
                </div>
                <input type="hidden" name="hidBack" id="hidBack" value="${hidBack}" />
                <input type="hidden" name="hidTipo" id="hidTipo" value="${hidTipo}" />
                <input type="hidden" name="hidCveMat" id="hidCveMat" value="${materia.cveMat}"/>
                <input type="hidden" name="hidFechaModf" id="hidFechaModf" value="${materia.fechaModf}"/>
            </form>
        </section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
