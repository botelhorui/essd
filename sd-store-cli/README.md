# Projecto de Sistemas Distribuidos #

## Primeira entrega ##

Grupo de SD 50.

-Rui Botelho, 67077, botelhorui@gmail.com
-Nuno Sousa, 73216 nuno_guilherme_scp@hotmail.com


Repositório
[tecnico-softeng-distsys-2015/A_10_08_50-project](https://github.com/tecnico-softeng-distsys-2015/C_XX_YY_ZZ-project/)

-------------------------------------------------------------------------------

## Serviço SD-Store

### Instruções de instalação 

[0] Iniciar sistema operativo

Windows

[1] Iniciar servidores de apoio

JUDDI:
> %CATALINA_HOME%\bin\startup

[2] Criar pasta temporária

> cd \

> mkdir temp

[3] Obter versão entregue

> cd \temp

> git clone -b SD-ID_R_1 https://github.com/tecnico-softeng-distsys-2015/A_10_08_50-project/


[4] Construir e executar **servidor**

> cd \temp\A_10_08_50-project\sd-store

> mvn clean package 

> mvn exec:java

[5] Construir **cliente**

> cd \temp\A_10_08_50-project\sd-store-cli

> mvn clean package

-------------------------------------------------------------------------------

### Instruções de teste: ###
*(Como verificar que todas as funcionalidades estão a funcionar correctamente)*


[1] Executar **testes de implementação** ...

> cd \temp\A_10_08_50-project\sd-store

> mvn test

[2] Executar **cliente de testes** ...

> cd \temp\A_10_08_50-project\sd-store-cli

> mvn test

-------------------------------------------------------------------------------
**FIM**
