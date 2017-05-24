<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="${title}" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/materia/ModificaContenido.js"></script>		
	        <script src="ckeditor/ckeditor.js"></script>
	        <script src="ckfinder/ckfinder.js"></script>
	        <script src="ckeditor/adapters/jquery.js"></script>
			<section style="clear: both">			
            <form action="ModificaContenido" method="post" name="form1" id="form1">
                <div style="text-align: center; margin-bottom: 10px">
                    <table style="border: none; text-align: center;width:98%25">
                        <tr>
                            <td style="border: none; text-align: right">
                                <h3>${title}</h3>                                
                            </td>
                            <td style="border: none; text-align: right">
                                <input type="button" name="Submit" value="Regresar" class="botonimagenchico botonimagenchicoWidth" onclick="funcRegreso()" />
                            </td>
                        </tr>
                    </table>

                    <div class="ui-widget" id="message">
						<div class="ui-state-highlight ui-corner-all msg" >
							<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
							<strong id="innerMessage">${message}</strong></p>
						</div>
					</div>
                    
					<c:if test="${puedeMostrar}">

                    <table style="border:none;width:100%25">
                        <tr>
                            <td style="border:none;width:50%25;text-align: center; vertical-align: bottom">
                                <table style="border:none;width:100%25">
                                    <tr>
                                        <td style="border:none">
                                            <input type="button" name="Submit" value="Guardar" class="botonimagenchico botonimagenchicoWidth" onclick="funcGuardar()" />
                                            <input type="button" name="Submit" value="Ver Servidor" class="botonimagenchico botonimagenchicoWidth" onclick="funcShowCkfinder()" />
                                            <input type="button" name="Submit" value="Agregar Archivos" class="botonimagenchico botonimagenchicoWidth" onclick="funcAgregaImagenes()"  />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td style="border:none;width:50%25;text-align: center">
					        	<table class="green list1">   
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
			                        		<c:if test="${contenido.caption == capIntroduccion}">
			                        			${contenido.caption}
			                        			<input type="hidden" value="${contenido.caption}" name="nomContenido" id="nomContenido"/>
			                        		</c:if>
			                        		<c:if test="${contenido.caption != capIntroduccion}">
			                        			<input type="text" value="${contenido.caption}" name="nomContenido" id="nomContenido"/>
			                        		</c:if>			                        		
				                        </td>
				                    </tr>
					            </table>
                            </td>
                        </tr>
                    </table>
                    <textarea name="contenido" id="contenido">
                        ${contenidoHtml}
                    </textarea>
                    
                   <script lang="javascript">
                        // Replace the <textarea id="editor1"> with a CKEditor
                        // instance, using default configuration.
                        var urlImg = 'ckfinder/ckfinder.html?type=Files%26start=Files:${urlImg}';
                        var urlFlash = 'ckfinder/ckfinder.html?type=Files%26start=Files:${urlFlash}';
                        
                        var editor = CKEDITOR.replace( 'contenido', {

					        height: '400',
					        width: '98%25',
					        extraPlugins: 'autogrow',
					        autoGrow_minHeight: '40%25',
					        autoGrow_maxHeight: '80%25',
					        autoGrow_bottomSpace: '50',
					        filebrowserBrowseUrl: 'ckfinder/ckfinder.html',
					        filebrowserImageBrowseUrl: urlImg,
					        filebrowserFlashBrowseUrl: urlFlash,
					        ${ac}

					        toolbarGroups: [
				                        		{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
				                        		{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
				                        		{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
				                        		{ name: 'forms', groups: [ 'forms' ] },
				                        		'/',
				                        		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
				                        		{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
				                        		{ name: 'links', groups: [ 'links' ] },
				                        		{ name: 'insert', groups: [ 'insert' ] },
				                        		'/',
				                        		{ name: 'styles', groups: [ 'styles' ] },
				                        		{ name: 'colors', groups: [ 'colors' ] },
				                        		{ name: 'tools', groups: [ 'tools' ] },
				                        		{ name: 'others', groups: [ 'others' ] },
				                        		{ name: 'about', groups: [ 'about' ] }
				                        	],
					        
					        removeButtons: 'Save,NewPage,Styles,Format,Font,FontSize,Iframe,Smiley,BGColor ${editorConfigSource}',
					        removeDialogTabs : 'flash:Upload;image:Upload',
					        colorButton_colors : '000000,FF0000,0000FF',
					        colorButton_enableMore : false,
					        fontSize_defaultLabel : '10px'
                  		});
                        
                    </script>

				</c:if>
				                    
                </div>
			    <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value="${materia.cveMat}"/>
			    <input type="hidden" id="hiddenCveCont" name="hiddenCveCont" value="${contenido.menuItem}"/>
            	<input type="hidden" id="hiddenTipo" name="hiddenTipo" value=""/>
    	       	<input type="hidden" id="hiddenBack" name="hiddenBack" value=""/>
            </form>
        </section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>