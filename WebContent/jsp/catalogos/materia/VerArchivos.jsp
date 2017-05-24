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
   		<script src="js/materia/VerArchivos.js"></script>
   		
   		<style>
   		#filelist .line{
	   		display: table-cell;
   			vertical-align: middle;
   			width:500px;
   			height:30px;
     		line-height:30px;
   			border-bottom: 1px #e6e6e6 solid;
			}
		#filelist .material-icons{
		 	display: block;
		 	float:left;
		 	margin-right: 5px;
		 }			
		 #filelist{
		 	text-align: left;
		 	margin-left: 10px;
		 	padding: 15px;
		 	width:95%25;
		 	overflow:scroll; 
		 	height:350px;
		 	border: solid #e6e6e6 1px;
		 }
		 #filelist .foldername {
		 	vertical-align: middle;
		 	height:30px
		 }
         a{
         	color:black;
         }
		.no-close .ui-dialog-titlebar {
		  display: none;
		}
		#divPathDir{
			text-align: left; 
			margin: 10px; 
			font-size: 13px
		}
		</style>
   				
		<section style="clear: both" >
	        <form action="VerArchivos" method="post" name="form1" id="form1">
	        
	        	<div id="dialog"></div>
	        	<div id="loadingDialog">
	        		<div style="display:table-cell;width:180px; height:180px;text-align: center; vertical-align: middle">
		        		<img src="images/loading.gif" style="width:50px;height:60px"/>
	        		</div>
	        	</div>
	        
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

					<div style="margin-top:20px;margin-bottom:20px">Tipo del archivo: 
						<select name="tipoArchivo" id="tipoArchivo" onchange="funcOnChangeTipoArchivo()">
					        <!--<option value="2">Animacion</option> -->
					        <option value="9">Html5</option>
					        <option value="5">Actividades Complementarias</option>
						</select>
					</div>

					<div style="text-align: right; width:95%25;">
	
			            <a class='btn' href='javascript:void(0);' onclick="funcAbrirAgregar()" title='nueva carpeta' style="margin-right: 10px">
			            	<i class='material-icons'>create_new_folder</i>
			            </a>
			            <a class='btn' href='javascript:void(0);' onclick="funcAbrirAgregarArchivo()" title='subir archivo' style="margin-right: 10px" id="fileupload">
			            	<i class="material-icons">file_upload</i>
			            </a>
			            <a class='btn' href='javascript:void(0);' onclick="funcDelete('${materia.cveMat}','${cveCont}')" title='delete' style="display:none" id="delete">
			            	<i class='material-icons'>delete</i>
			            </a>
			        
			        </div>
			        <div id="divFile">
			        
				        <div id="divPathDir">
				        	${pathDirectory}
				        	<input type="hidden" id="pathDirectory" value="${pathDirectory}"/>
				        	<input type="hidden" id="rootDir" value="${rootDir}"/>
				        </div>
				        
				        
				        <div id="filelist">
					        <c:forEach var="file" items="${fileList}" varStatus="status">
				        
	
								<c:if test="${file.isFile() == true}"> 
				        		<div class="line">
				        			<div class="filename">${file.getName()}</div>
				        		</div>
				        		</c:if>
					        	<c:if test="${file.isDirectory() == true}">
				        		<div class="line folder">
					        		<a>
						        		<i class="material-icons">folder</i>
						        		<div class="foldername">${file.getName()}</div>
					        		</a>
				        		</div>
					        	</c:if> 
				        		<br>
					        </c:forEach>  
					        <script>
					    	$(".folder").dblclick(function() {
					    		funcAbrirCarpeta(1, $(".foldername", this).html());		  
					    	});
					        </script>
				        </div>
			        
			        </div>
	           	</div>

				<input type="hidden" name="hiddenCveMat" id="hiddenCveMat" value="${materia.cveMat}" />
				<input type="hidden" name="hiddenCveCont" id="hiddenCveCont" value="${contenido.menuItem}" />
			</form>
        </section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
