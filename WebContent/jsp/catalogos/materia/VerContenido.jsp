<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Ver Contenido" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/materia/VerContenido.js"></script>		
			<section style="clear: both">
            <form action="VerContenido" method="post" name="form1" id="formVerContenido">
                <div style="text-align: center; margin-bottom: 10px;" id="contAbove"> 
  
                    <table style="border: none; text-align: center;width:98%25">
                        <tr>
                            <td style="border: none; text-align: right">
                                <h3>${title}</h3>                                
                            </td>
                        </tr>
                    </table>
             		<div class="ui-widget" id="message">
						<div class="ui-state-highlight ui-corner-all msg" >
							<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
							<strong id="innerMessage">${message}</strong></p>
						</div>
					</div>
                     <table style="border:none;width:100%25">
                     	<tr>
                     		<td style="width: 50%25; border: none;vertical-align: bottom">
	                     		<c:if test="${permisoModificar == true }">
									<input type="button" value="Modificar" class="botonimagenchico" onclick="funcModificar()"/>
									<input type="button" value="Baja" class="botonimagenchico" onclick="funcBajar()"/>
								</c:if>
								<input type="button" value="Regresar" class="botonimagenchico" onclick="funcRegreso()"/>
                     		</td>
                     		<td style="width: 50%25;border: none">
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
				                        	${contenido.parentContName} ${contenido.caption}
				                        </td>
				                    </tr>
					            </table>
                     		</td>
                     	</tr>
                     </table>        
                </div>
            	<iframe src="Contenido?cveCont=${cveContenido}" id="IframeCont" style="width:99%25; overflow:scroll;"></iframe>
	            <script>$('#IframeCont').on('load', function() {funcChangeHeight()})</script>
			    <input type="hidden" id="hiddenCveMat" name="hiddenCveMat" value="${materia.cveMat}"/>
			    <input type="hidden" id="hiddenCveCont" name="hiddenCveCont" value="${cveContenido}"/>
			    <input type="hidden" id="hiddenTipo" name="hiddenTipo" value=""/>
           		<input type="hidden" id="hiddenBack" name="hiddenBack" value=""/>
            </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
           