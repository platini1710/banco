 1.- Para bajar el proyecto, copie el siguiente comando en la linea de comando:
 
      git clone https://github.com/platini1710/bci.git
	  
2.-	Para probar los test unitario y de integracion debe colocar el siguiente comando

      gradlew check run
	  
	  los reporte html  estan en la carpeta 
	  
	  
	test de integracion ==>  \build\reports\tests\integrationTest\index.html
	test unitarios ==>  \build\reports\tests\test\index.html
	
	 
	 
3.-	Para Levanar   proyecto el :
	  
      gradlew run               ==> ojo, el proyecto se levantara en un tomcat emebedido en el puerto 8081
	   
	   
4.-	para probar con datos en el Postman
      - ingresar un registro de un usuario, se debe ingresar en formato Json
			url   :  http://localhost:8081/registro/usuario/sign-up
			Metodo:  Post
			
			{
			"id" :"1-8",    //rut chileno sin validar
			"name": "augusto Espinoza",
			"password": "A23aaaaa#@",
			"email": "aerrspinoza3010@gmail.com",
			"phone": {
						"number":12212221,
                        "cityCode":569,
                        "countryCode":"CL"
					}
			}
			

			
	

      - consultar por el registro  ingresado
			url   :  http://localhost:8081/consult/usuario/login/1-8
			Metodo:  Get	
			el 1-8 es el rut que gurdamos anteriormente  que forma parte de la url
		       En el  pestaña Header y agregar la key Authorization y el valor del token es el  que se devuelve al crear  un registro 
	
				Authorization:  Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoiYXVndXN0byBFc3Bpbm96YSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2OTYyNjM3MjIsImV4cCI6MTY5NjI2NDMyMn0.LN8HpPOXw_OuwJ0pTflZWcj6XKHKnFpU4LUFpXbxvClKo5wLGEefHVPZmLToE02rgaXPOJEyIJljgidkkANrZQ
			 
		
		
		
	  - consultar por todos los registros  ingresados
	  	    url   :  http://localhost:8081/consult/usuario/allUsuarios
		    Metodo:  Get	
			En el  pestaña Header y agregar la key Authorization y el valor del token es el  que se devuelve al crear  un registro 
	
				Authorization:  Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoiYXVndXN0byBFc3Bpbm96YSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2OTYyNjM3MjIsImV4cCI6MTY5NjI2NDMyMn0.LN8HpPOXw_OuwJ0pTflZWcj6XKHKnFpU4LUFpXbxvClKo5wLGEefHVPZmLToE02rgaXPOJEyIJljgidkkANrZQ
			 
			 
			 
			 
			 
			 
			 DIAGRAMA DE SECUENCIA y de componete dentro de LA CARPETA diagramas
			 
			 documentacion Swagger dentro de carpeta Swagger