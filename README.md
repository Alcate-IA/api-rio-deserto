Padrões de código:
Nome de classe - PadraoDoJava
Nome de variavel - camelCase - manter o que está vindo do banco para facilitar o banco <-> back, nomes em português, não abreviar (mvto → movimento, freq → frequência) 
Nome controler - Classe + Controller
Nome do Service - Classe + Service

Rotas criadas, está organizado em Nome da tabela do banco / Nome da entidade no nosso código / Rota testad no postman 

TB_CALIBRACAO_EQUIPAMENTO - CalibracaoEquipamento  - GET http://localhost:8080/calibracoes
Retorna todos os dados da tabela

TB_EMPRESA - Empresa - GET http://localhost:8080/empresas
Retorna todos os dados da tabela

TB_INSPECAO_PIEZOMETRO - InspecaoPiezometro - GET http://localhost:8080/inspecoes-piezometros
Retorna todos os dados da tabela

TB_INSPECAO_PIEZOMETRO_FREQ - InspecaoPiezometroFrequencia - GET http://localhost:8080/inspecoes-piezometros-frequencias
Retorna todos os dados da tabela

TB_INSPECAO_PIEZOMETRO_MVTO - InspecaoPiezometroMovimento - GET http://localhost:8080/inspecoes-piezometros-movimentos (limitei para 100 pq tem muito dado nessa)
Retorna todos os dados da tabela

TB_METEOROLOGIA - Meteorologia - http://localhost:8080/meteorologias              
Retorna todos os dados da tabela

TB_METEOROLOGIA_ITEM - MeterologiaItem - http://localhost:8080/meteorologia-itens  
Retorna todos os dados da tabela

TB_NIVEL_AGUA - NivelAgua - http://localhost:8080/niveis-agua               
Retorna todos os dados da tabela

TB_NIVEL_AGUA_ITEM - NivelAguaItem - http://localhost:8080/nivel-agua-itens     
Retorna todos os dados da tabela

TB_PESSOA - Pessoa - GET  http://localhost:8080/pessoas                     
Retorna todos os dados da tabela

TB_PIEZOMETRO - Piezometro - GET http://localhost:8080/piezometros                  
Retorna todos os dados da tabela

TB_RECURSOS_HIDRICOS - RecursosHidricos - http://localhost:8080/recursos-hidricos         
Retorna todos os dados da tabela

TB_RECURSOS_HIDRICOS_ITEM - RecursosHidricosItem - http://localhost:8080/recursos-hidricos-itens   
Retorna todos os dados da tabela

-----------------------------------------------------------

GET http://localhost:8080/relatorios/medias-mensais-todos
Trás as médias mensais de nível (piezometros e peços e réguas, precipitação e futuramente trará a vazão)
Lembrando que no momento esses valores podem estar errados


GET /relatorios/medias-mensais?mesAnoInicio=01/2023&mesAnoFim=12/2024
Busca todos os dados entre o período que tá na url (prestar atenção em como passar os dados: mesAnoInicio=01/2023 e mesAnoFim=12/2024, outras formas vão dar erro)
Trás as médias mensais de nível (piezometros e peços e réguas, precipitação e futuramente trará a vazão) POR PERIODO DESCRITO NA URL
Lembrando que no momento esses valores podem estar errados


POST http://localhost:8080/relatorios/medias-mensais
modo mais limpo de passar os dados, caso prefira, passar com a barra separando e lembra de passar em string
Trás as médias mensais de nível (piezometros e peços e réguas, precipitação e futuramente trará a vazão) POR PERIODO DESCRITO NA URL
Lembrando que no momento esses valores podem estar errados

Body raw:

{
"mesAnoInicio": "01/2023",
"mesAnoFim": "12/2024"
}

GET  http://localhost:8080/piezometros/ativos  
Lucas pediu essa API para ter acessos alguns dados no aplicativo, retorna alguns dados dos poços / piezometros, segue o retorno esperado:

    {
        "idPiezometro": "PZ 44",
        "nomePiezometro": "Furo de sonda CM-181",
        "situacaoPiezometro": "A"
    },

GET http://localhost:8080/relatorios/piezometro/572/filtro?mesAnoInicio=01/2023&mesAnoFim=12/2023     <----- trocar o 572 pelo cd_pieozometro de sua escolha e as datas também

Essa com filtro de data

http://localhost:8080/relatorios/piezometro/572   <----- trocar o 572 pelo cd_pieozometro de sua escolha

E essa sem filtro de data, trazendo todos os dados do sistema

ambas retornam os mesmos dados

    {
        "cota_superficie": 214.1700,
        "cota_base": 184.9500,
        "mes_ano": "2025-11-01",
        "precipitacao": 247.00,
        "vazao_bombeamento": null,
        "nivel_estatico": null
    },