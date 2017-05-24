<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Agregar Archivo" /> 
	<jsp:param name="mostrarMenu" value="false" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
   		<script src="js/materia/AgregaArchivos.js"></script>		
		<section style="clear: both" >
	        <form action="AgregaFile" method="post" name="form1" id="form1" enctype="multipart/form-data">
		            <div style="text-align: center; margin-bottom: 30px;">    
	
						<div class="ui-widget" id="message">
							<div class="ui-state-highlight ui-corner-all msg" >
								<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
								<strong id="innerMessage">${message}</strong></p>
							</div>
						</div>
	
	
		                <h3>Agregar Archivos</h3>
		                <table class="list1 green" style="width:60%25">   
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
		                        <td class="left">${materia.nomNivel} ${materia.nomNivelGrado}</td>
		                       	<th class="right">Contenido</th>
		                        <td class="left">
	                        		${contenido.parentContName}
	                        		${contenido.caption}
		                        </td>
		                    </tr>
			            </table>
		            </div>
		             <div style="text-align: center">
		                 <div style="margin-bottom:20px">
		                 	<input type="file" name="file" id="file" multiple="multiple" onchange="funcEligirTipo()">
		                 </div>
		          		 <div style="margin-bottom:20px">Tipo del archivo: 
			          		<select name="tipoArchivo" id="tipoArchivo" onchange="">
			                    <option value="0">----------</option>
		         	            <option value="1">Imagen</option>
		         	            <option value="2">Animacion</option>
		         	            <option value="9">Html5</option>
		         	            <option value="3">Sonido</option>
		         	            <option value="4">Actividades</option>
		         	            <option value="5">Actividades Complementarias</option>
		         	            <option value="6">Contenido Imprimible</option>
		         	            <option value="7">Programa Imprimible</option>
		         	            <option value="8">Formulario</option>
			                </select>
						 </div>
	
						<div style="margin-bottom: 10px">Nota: El tamaño máximo del archivo es 600KB</div> 
	
						<div style="margin-bottom:10px">
			                <input type="button" value="Agregar" class="botonimagenchico" onclick="funcAgregar()" />
			                <input type="button" value="Cerrar" class="botonimagenchico" onclick="funcCerrar()"/>
							<input type="button" value="Agregar Html5" class="botonimagenchico" onclick="funcAgregarHtml5()" />
		                </div>
		            </div>
		            <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value="${materia.cveMat}"/>
		            <input type="hidden" id="hiddenCveCont" name="hiddenCveCont" value="${contenido.menuItem}"/>
					<input type="hidden" id="hiddenTipo" name="hiddenTipo" value=""/>	        
			</form>
        </section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
