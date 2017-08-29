<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Iniciar Sesión" /> 
	<jsp:param name="mostrarMenu" value="false" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
	    <script src="js/general.js"></script>	
		<section style="clear: both">
	       <form action="Autentifica" method="post" name="form1" id="form1" 
	       onsubmit="return funcAutentifica()">
	           <div class="bastidor">  
	            <p><strong>Usuario</strong>:
	                <input name="IdUsu" type="text" id="IdUsu" size="12" maxlength="20" autofocus="true" />
	            </p>
	            <p><strong>Contraseña</strong>
	              <input name="PasswordUsu" type="password" id="PasswordUsu" size="12" maxlength=" " />
	            </p>
	           </div>
	            <input type="submit" name="Submit" value="Iniciar sesión" class="botonimagenchico"  />
	            	            
	        </form>
		</section>
		</jsp:attribute>
	</jsp:param>
</jsp:include>
