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
    	<script src="js/CambiaContrasena.js"></script>		

        <section style="clear: both">
            <form action="CambiaContrasena" method="post" name="form1" id="form1">
                <div style="text-align: center; margin-bottom: 30px;">    

					<div class="ui-widget" id="message">
						<div class="ui-state-highlight ui-corner-all msg" >
							<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
							<strong id="innerMessage">${message}</strong></p>
						</div>
					</div>

                    <h3>Cambiar Contraseña</h3>

	                     <table style="width:40%25; text-align: center" class="list1 green">
                            <tr>
                                <th width="30%25" class="right">Clave</th>
								<td width="60%25" class="left">${usu.cveUsu}</td>    	
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Nombre</th>
                                <td width="60%25" class="left">${usu.nomUsu}</td>
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Apellido Padrino</th>
                                <td width="60%25" class="left">${usu.nomPatUsu}</td>
                            </tr>
                            <tr>
                                <th width="30%25" class="right">Apellido Madrino</th>
                                <td width="60%25" class="left">${usu.nomMatUsu}</td>
                            </tr>
                           <tr>
                                <th width="30%25" class="right">Contraseña</th>
                                <td width="60%25" class="left">
                                	<input type="text" size="20" value="" name="pwsUsu" id="pwsUsu"/>
                                </td>
                            </tr>
                    </table>
	                <div style="text-align: center; margin-top: 10px">
                        <input type="button" value="Modificar" name="Modificar" class="botonimagenchico" onclick="funcValidacion()"/>
	                </div> 
                </div>
                <input type="hidden" name="hidCveUsu" id="hidCveUsu" value="${usu.cveUsu}"/>
            </form>
        </section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
