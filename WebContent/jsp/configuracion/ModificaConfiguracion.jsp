<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Modificar Configración" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/configracion/ModificaConfigracion.js"></script>		
			<section style="clear: both">

			<div id="dialog"></div>

	           <form action="ModificaConfigracion" method="post" name="form1" id="form1">
	               <div style="text-align: center; margin-bottom: 30px">
						<div class="ui-widget" id="message">
							<div class="ui-state-highlight ui-corner-all msg" >
								<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
								<strong id="innerMessage">${message}</strong></p>
							</div>
						</div>
						
		               <c:if test="${puedeMostrar}">

	               		   <h3>Modificar Configración</h3>
	
							
								<c:forEach var="prop" items="${propList}" varStatus="status">
								
									<c:if test="${fn:contains(prop[0],'comment')}">
										<c:if test="${status.index >0 }">
											</table>
										</c:if>					
										
										<table class="list1 green" style="width:50%25;margin-top: 20px">
											<caption>${prop[1]}</caption>
											<tr><th>Key</th><th>Value</th><th></th></tr>
									</c:if>
									<c:if test="${!fn:contains(prop[0],'comment')}">
	
										<tr class="datarow">
											<td style="width:20%25"><c:out value="${prop[0]}"></c:out></td>
											<td style="width:70%25"><input type="text" value="${prop[1]}" size="50" id="val${status.index+1}"></td>
											<td style="width:10%25">
												
												<a id="modificar${status.index+1}" class='btn' href='javascript:void(0);' onclick="funcModificar('${prop[0]}', ${status.index+1})" title='guardar'>
													<i class='material-icons'>save</i>
												</a>
												<div><span id="wait${status.index+1}"></span></div>
											</td>
										</tr>
	
									</c:if>
								
							
							</c:forEach>
							</table>
						</c:if>		
					</div>

	           </form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
