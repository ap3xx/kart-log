# kart-log

## Descrição

Este projeto é o desafio para o processo seletivo da Gympass. O problema, resultados esperados e bônus estão todos descritos [aqui](doc/ASSIGNMENT_DESCRIPTION.md)

## Pré-requisitos

* JDK 8
* Maven

## Guia de Instalação

Clone esse repositório:

`git clone https://github.com/ap3xx/kart-log.git`

Execute o seguinte comando para construir o pacote (na raiz do projeto):

`mvn package`

## Como executar os testes

Execute o seguinte comando (na raiz do projeto):

`mvn test`

_Obs: o comando para construir o pacote ja passa pelos testes por padrão._

## Como rodar o programa

Com o pacote construído, execute o seguinte comando (na raiz do projeto):

` java -jar target/kartlog-1.0.jar path/to/log/file.log` 

## Resultados entregues:

* **Resultado da corrida**
* **Melhor volta de cada piloto**
* **Melhor volta da corrida**
* **Velocidade Média de cada piloto**
* **Quanto tempo cada piloto chegou após o vencedor**

## Observações

Fiquei com muita dúvida em relação ao fim da corrida. 
Na descrição do desafio estava escrito que a corrida terminava assim que o primeiro piloto finalizasse a quarta volta, então eu não sabia se deveria considerar o tempo do restante das voltas dos pilotos na soma e preferi incluir, permitindo que os pilotos finalizassem as voltas incompletas até o momento.
Para calcular as melhores voltas, considerei apenas as voltas válidas pela corrida.

Para os testes, deixei o acesso aos métodos `protected` para poder entregar uma quantidade e qualidade de testes mais refinada. 

## Autor

* **Flávio Teixeira** - (ap3xx)[https://github.com/ap3xx]