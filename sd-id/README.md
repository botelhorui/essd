# Projecto de Sistemas Distribuidos #

## Primeira entrega ##

Grupo de SD 08

Pedro Miguel Caeiro 69775 pedro.miguel14@gmail.com

Maria Costa e Silva 69682 maria.costa.e.silva@ist.utl.pt

Jo�o Afonso Baptista 69364 joao.a.baptista@ist.utl.pt

Reposit�rio:
[tecnico-softeng-distsys-2015/A_10_08_50-project](https://github.com/tecnico-softeng-distsys-2015/A_10_08_50-project/)

-------------------------------------------------------------------------------

## Servi�o SD-ID

### Instru��es de instala��o 


[0] Iniciar sistema operativo

Windows

[1] Iniciar servidores de apoio

JUDDI:
> %CATALINA_HOME%\bin\startup

[2] Criar pasta tempor�ria

> cd \

> mkdir temp

[3] Obter vers�o entregue

> cd \temp

> git clone -b SD-ID_R_1 https://github.com/tecnico-softeng-distsys-2015/A_10_08_50-project/


[4] Construir e executar **servidor**

> cd \temp\A_10_08_50-project\sd-id

> mvn clean package 

> mvn exec:java -Dexec.args="http://localhost:8081 SD-ID http://localhost:8080/id-ws/endpoint"


[5] Construir **cliente**

> cd \temp\A_10_08_50-project\sd-id-cli

> mvn clean package

...


-------------------------------------------------------------------------------

### Instru��es de teste: ###


[1] Executar **testes de implementa��o do servidor**

> cd \temp\A_10_08_50-project\sd-id

> mvn test

[2] Executar **cliente de testes**

> cd \temp\A_10_08_50-project\sd-id-cli

> mvn test

...


-------------------------------------------------------------------------------
**FIM**