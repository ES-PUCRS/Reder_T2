# Redes/L TF
.
## Definição do Trabalho Final

O objetivo do trabalho visa implementar um programa que faça a transferência de
mensagens e arquivos através de uma rede simulada. Para a transferência funcionar, será
preciso implementar uma aplicação, que utilize sockets, rode com UDP e faça o roteamento dos
pacotes entre aplicações, sendo que cada aplicação simula um roteador. Além de poder
transmitir mensagens e arquivos, a aplicação deverá também realizar a troca e atualização de
tabelas de roteamento. Sendo assim, a aplicação deverá ser dividida em duas partes, uma que
faz o roteamento das mensagens ou arquivos e outra que faz a troca e atualização das tabelas
de roteamento, como mostrado da Figura 1.

```
Aplicação
Transf. Msg/Arq
Roteamento
```
```
Aplicação
Transf. Msg^ /Arq
Roteamento
Pilha
TCP/IP do
sistema
```
```
socket
```
```
Figura 1: Exemplo de Aplicações.
```
```
Troca e Atualização de Tabelas de Roteamento
```
A aplicação deverá implementar a troca de tabelas de roteamento, semelhante a troca
que ocorre no protocolo de roteamento RIP. A aplicação deve implementar o protocolo descrito
a seguir.

Inicialmente, deverá ser informado, na aplicação, o número das portas (até 2 portas) dos
roteadores vizinhos para que várias topologias diferentes possam ser simuladas. Cada roteador
vizinho será uma instância do roteador implementado executando na mesma máquina física. A
porta será usada como identificação dos roteadores. Elas deverão ser cadastradas em uma
tabela de roteamento com métrica 1 e saída com a identificação da própria porta de destino.
Além disso, as portas locais também precisam ser cadastradas nas tabelas com métrica 0. Três
campos deverão estar presentes nas tabelas de roteamento: Porta de Destino, Métrica e Porta
de Saída. Um exemplo dessa configuração está apresentado na Figura 2.

As tabelas de roteamento (apenas os campos Porta de Destino e Métrica) deverão ser
trocadas entre os roteadores vizinhos a cada 10 segundos. Ao receber a tabela de roteamento
de seus vizinhos, a aplicação deverá verificar as rotas recebidas e fazer as atualizações
necessárias na tabela de roteamento local. Uma atualização deverá ser feita sempre que:


- for recebido uma Porta de Destino não presente na tabela local. Neste caso a rota
    deve ser adicionada, a Métrica deve ser incrementada em 1 e a Porta de Saída deve
    ser a identificação da porta do roteador que ensinou esta informação;
- for recebida uma Métrica menor para uma Porta de Destino presente na tabela local.
    Neste caso, a Métrica e a Porta de Saída devem ser atualizadas;
- uma Porta de Destino deixar de ser divulgada. Neste caso, a rota deve ser retirada
    da tabela de roteamento.

Um roteador pode sair da rede a qualquer momento. Isso significa que seus vizinhos não
receberão mais anúncios de rotas. Assim, depois de 30 segundos sem receber mensagens do
roteador vizinho em questão, as rotas que passam por ele devem ser esquecidas.

Periodicamente, a tabela de roteamento local deverá ser apresentada para o usuário.
Além disso, alterações na tabela de roteamento deverão ser informadas para os usuários
(através de prints na saída padrão).

Não existirá um número fixo de aplicações (roteadores), elas poderão ser incluídas ou
retiradas a qualquer momento e as tabelas de roteamento devem ser o reflexo da topologia da
rede simulada.

```
Envio de mensagens ou Arquivos (Roteamento)
```
O usuário poderá enviar mensagens ou arquivos (dados) para outra aplicação. Para
realizar essa função, o roteador (aplicação) deverá consultar sua tabela de roteamento e verificar
qual a Porta de Destino deve receber os dados.

```
O usuário deve informar para a aplicação:
```
- o número da Porta de Destino;
- a mensagem ou o caminho do arquivo a ser enviado.

Os pacotes que circulam na rede devem possuir um cabeçalho com as informações
necessárias para a transferência. Essas informações são: o número da porta de origem, o
número da porta destino e os dados.

Todo o processo de recepção e roteamento dos pacotes deve estar impresso na tela
por cada aplicação. Dessa forma, será possível visualizar todo o caminho por onde o pacote
passou. Além disso, quando o destino receber um arquivo, o mesmo deve ser sinalizado com
uma mensagem na tela, além de ser armazenado na própria máquina.


```
Exemplo de topologia com 4 roteadores
```
A Figura 2 abaixo ilustra 4 roteadores e suas respectivas tabelas de roteamento antes da
primeira troca de suas tabelas.

```
Figura 2 : Momento inicial.
```
```
Após algumas trocas, as tabelas de roteamento ficam como apresentado da Figura 3.
```
```
Figura 3 : Após algumas trocas de tabelas de roteamento.
```

Regras Gerais

Grupos: Até 4 componentes

Data de entrega: 2 8/ 06
Obs.: Todos os participantes devem estar presentes na apresentação via Zoom

Entrega final:

- Código fonte comentado.
- Descrição do formato do pacote utilizado

IMPORTANTE: Não serão aceitos trabalhos entregues fora do prazo. Trabalhos que não
compilam ou que não executam não serão avaliados. Todos os trabalhos serão analisados e
comparados. Caso seja identificada cópia de trabalhos, todos os trabalhos envolvidos receberão
nota ZERO.


