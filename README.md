# Sistema de Pedidos para Restaurantes

Esse projeto é uma implementação de um aplicativo móvel para Android apenas para fins de estudos usando a linguagem JAVA, o mesmo teria o objetivo de ser usado por restaurantes para a realização de pedidos na mesa. 

O App permiti a seleção de mesas, pratos e bebidas, de forma que o pedido possa ser montado atravez do App.
#### Requisitos do App:
- O app deverá permitir adicionar ou remover itens de um pedido
enquanto o mesmo não estiver pago.
- Pedidos pagos deixam de ser exibidos na lista.
- Mesas ocupadas não devem ser exibidas na lista de seleção de mesas.
- As quantidades dos itens deverão ser alteradas usando os botões + (mais) e –
(menos) ao lado do ítem na listagem do pedido.
- Possibilidade dos produtos conterem imagens.

#### Observações
- O pedido consome dados de um API, porem apos consumir os mesmos trabalha com eles de forma local utilizando **SQLite**, não é a forma ideal de se trabalhar mas para fins didaticos serve muito bem. A API usada tambem foi desenvolvida por mim, a mesma foi feita usando **.NET** e esta disponível no repositório https://github.com/michaelcdr/dotnet-android-lanches 