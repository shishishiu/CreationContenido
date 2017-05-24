<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Solicitar Revisión" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    	<script src="js/materia/Solicitar.js"></script>		
	        <section style="clear: both">
            <form action="Solicitar" method="post" name="form1" id="form1">
                <div style="text-align: center; margin-bottom: 30px">
            		<div class="ui-widget" id="message">
						<div class="ui-state-highlight ui-corner-all msg" >
							<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
							<strong id="innerMessage">${message}</strong></p>
						</div>
					</div>
					
					<c:if test="${tieneAutoridad == true}">
             
                    <h3>Solicitar Revisión</h3>
                    
					<div id="dialog"></div>

                    <table class="list1 green" style="width:50%25">
                         <tr>
                            <th width="30%25" class="right">Fecha de Solicitar</th>
                            <td width="70%25" class="left">${fechaSolicitud}</td>
                        </tr>
                         <tr>
                            <th width="30%25" class="right">Persona de Solicitar</th>
                            <td width="70%25" class="left">${usuario.cveUsu} ${usuario.nomUsu}</td>
                        </tr>
                       <tr> 
                           <th width="30%25" class="right">Nota</th>
                           <td width="70%25" class="left">
                                <textarea cols="50" rows="5" id="nota" name="nota">${nota}</textarea>
                           </td>
                       </tr>
                       <tr>
                           <th class="right" colspan="2">
                               La materia
                           </th>
                       </tr>
                        <tr>
                            <th width="30%25" class="right">Clave</th>
                            <td width="70%25" class="left">${materia.cveMat}</td>
                        </tr>
                        <tr>
                            <th width="30%25" class="right">Materia</th>
                            <td width="70%25" class="left">${materia.nomMat}</td>
                        </tr>
                        <tr>
                            <th width="30%25" class="right">Nivel</th>
                            <td width="70%25" class="left">${materia.nomNivel}</td>
                        </tr>
                        <tr>
                            <th width="30%25" class="right">Modulo</th>
                            <td width="70%25" class="left">${materia.modulo}</td>
                        </tr>
                    </table>                   
 
                    <div class="bastidor">
                        <input type="button" value="Solicitar" class="botonimagenchico" onclick="funcSolicitar()"/>
                    	<input type="button" value="Ver Historia" onclick="funcMostrarSolicitarHistoria()" class="botonimagenchico"/>
                        <input type="button" name="regresar" value="Regresar" class="botonimagenchico" onclick="funcRegreso()" />
                    </div>
                </c:if>
                    
                </div>
            	<input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value="${materia.cveMat}"/>
            </form>
        </section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
