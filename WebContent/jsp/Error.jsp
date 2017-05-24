<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Error" /> 
	<jsp:param name="mostrarMenu" value="false" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    		<script src="js/Error.js"></script>		
			<section style="clear: both">
				<form action="Error" method="post" name="form1" id="form1">
						<div class="ui-widget" id="message">
							<div class="ui-state-highlight ui-corner-all msg" >
								<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
								<strong id="innerMessage">Error....</strong></p>
							</div>
						</div>
				</form>
			</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
