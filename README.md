Padrões de código:
Nome de classe - PadraoDoJava
Nome de variavel - camelCase - manter o que está vindo do banco para facilitar o banco <-> back, nomes em português, não abreviar (mvto → movimento, freq → frequência) 
Nome controler - Classe + Controller
Nome do Service - Classe + Service

Rotas criadas, está organizado em Nome da tabela do banco / Nome da entidade no nosso código / Rota testad no postman 

TB_CALIBRACAO_EQUIPAMENTO - CalibracaoEquipamento  - GET http://localhost:8080/calibracoes
TB_EMPRESA - Empresa - GET http://localhost:8080/empresas
TB_INSPECAO_PIEZOMETRO - InspecaoPiezometro - GET http://localhost:8080/inspecoes-piezometros
TB_INSPECAO_PIEZOMETRO_FREQ - InspecaoPiezometroFrequencia - GET http://localhost:8080/inspecoes-piezometros-frequencias
TB_INSPECAO_PIEZOMETRO_MVTO - InspecaoPiezometroMovimento - GET http://localhost:8080/inspecoes-piezometros-movimentos (limitei para 100 pq tem muito dado nessa)
TB_METEOROLOGIA - Meteorologia - http://localhost:8080/meteorologias              
TB_METEOROLOGIA_ITEM - MeterologiaItem - http://localhost:8080/meteorologia-itens  
TB_NIVEL_AGUA - NivelAgua - http://localhost:8080/niveis-agua               
TB_NIVEL_AGUA_ITEM - NivelAguaItem - http://localhost:8080/nivel-agua-itens     
TB_PESSOA - Pessoa - GET  http://localhost:8080/pessoas                     
TB_PIEZOMETRO - Piezometro - GET http://localhost:8080/piezometros                  
TB_RECURSOS_HIDRICOS - RecursosHidricos - http://localhost:8080/recursos-hidricos         
TB_RECURSOS_HIDRICOS_ITEM - RecursosHidricosItem - http://localhost:8080/recursos-hidricos-itens   
