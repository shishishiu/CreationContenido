<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<jsp:include page="/jsp/Master.jsp">
	<jsp:param name="title" value="Iniciar Sesión Error" /> 
	<jsp:param name="mostrarMenu" value="false" /> 
	<jsp:param name="content">
		<jsp:attribute name="value">
    <script src="js/general.js"></script>	
		<section style="clear: both">
        <form action="Autentifica" method="post" name="form1" id="form1" onsubmit="return funcAutentifica()">
           <div class="bastidor">  
			No puede entrar...
			Inicia sesión por favor.
           </div>
            <input type="button" name="Regresar" value="Regresar" class="botonimagenchico" onclick="funcRegresarAutentifica()"  />
        </form>
		</section>
 		</jsp:attribute>
	</jsp:param>
</jsp:include>
