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
			iniciaMessageDialogChild();
			document.getElementById("nomNivGrado").focus();
		});

		</script>  
        <form action="ModificaNivelGrado" method="post" name="form2" id="form2" enctype="multipart/form-data">
              <div style="text-align: center; margin-bottom: 30px">

				<div class="ui-widget" id="messageChild">
					<div class="ui-state-highlight ui-corner-all msg" >
						<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
						<strong id="innerMessageChild">${message}</strong></p>
					</div>
				</div>
				
                <c:if test="${puedeMostrar}">

					<div style="margin-bottom: 10px">
						Nivel:<c:out value="${nombreNivel}"></c:out><br>
					</div>

					<table>
						<tr>
							<td style="width:20%;text-align: left">Nivel grado</td>
							<td style="width:80%;text-align: left"><input type="text" size="50" id="nomNivGrado" value="${nombreNivelGrado}"/> (menos de 255 letras)</td>
						</tr>
						<tr>
							<td style="text-align: left">Nombre de directorio</td>
							<td style="text-align: left"><input type="text" size="30" id="nomGrado" value="${nombreGrado}"/> (menos de 50 letras)</td>
						</tr>
					</table>

					
	                <div style="margin-top:10px">
	                
	                	<c:if test="${tipo==1}">
							<input type="button" name="agregar" id="agregar" value="Agregar" class="botonimagenchico" onclick="funcModificar()"/>
	                	</c:if>
	                	<c:if test="${tipo==2}">
	                    	<input type="button" name="modificar" id="modificar" value="Modificar" class="botonimagenchico" onclick="funcModificar()"/>
	                    </c:if>
	                </div>
				
				</c:if>
			</div>
				<input type="hidden" name="hiddenNivel" id="hiddenNivel" value="${cveNivel}"/>
				<input type="hidden" name="hiddenDialogoNivelGrado" id="hiddenDialogoNivelGrado" value="${cveNivelGrado}"/>
				<input type="hidden" name="hiddenDialogoTipo" id="hiddenDialogoTipo" value="${tipo}"/>
				<input type="hidden" name="hiddenTieneMat" id="hiddenTieneMat" value="${tieneMat}"/>
		</form>
    </body>
</html>
