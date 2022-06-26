insert into cozinha (nome) values ('Tailandesa');
insert into cozinha (nome) values ('Indiana');

insert into restaurante (nome, taxa_frete, cozinha_id) values ('Thai Gourmet', 10, 1);
insert into restaurante (nome, taxa_frete, cozinha_id) values ('Thai Delivery', 9.50, 1);
insert into restaurante (nome, taxa_frete, cozinha_id) values ('Tuk Tuk Comida Indiana', 15, 2);

insert into forma_pagamento (descricao) values ('Dinheiro');
insert into forma_pagamento (descricao) values ('Débito');
insert into forma_pagamento (descricao) values ('Crédito');
insert into forma_pagamento (descricao) values ('Pix');

insert into cidade (nome) values ('Recife');
insert into cidade (nome) values ('São Paulo');
insert into cidade (nome) values ('Rio de Janeiro');

insert into estado (nome) values ('Pernambuco');
insert into estado (nome) values ('Minas Gerais');
insert into estado (nome) values ('São Paulo');