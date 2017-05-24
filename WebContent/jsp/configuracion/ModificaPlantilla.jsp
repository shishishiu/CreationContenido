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
			document.getElementById("file").focus();
		});

		</script>  
        <form action="ModificaPlantilla" method="post" name="form2" id="form2" enctype="multipart/form-data">
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
						<c:if test="${tipoArchivo == 1}">Archivo del encabezado</c:if>
						<c:if test="${tipoArchivo == 2}">Archivo del pie</c:if>
					</div>
	
	          		<input type="file" name="file" id="file">
					
	                <div style="margin-top:10px">
	                    <input type="button" name="modificar" id="modificar" value="Modificar" class="botonimagenchico" onclick="funcModificar(${tipoArchivo},${nivel})"/>
	                </div>
				
				</c:if>
			</div>
			<input type="hidden" name="hiddenNivel" id="hiddenNivel" value="${nivel}"/>
			<input type="hidden" name="hiddenTipoArchivo" id="hiddenTipoArchivo" value="${tipoArchivo}"/>
		</form>
    </body>
</html>
