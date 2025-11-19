INSERT INTO categoria (nome)
VALUES ('Bebidas');

INSERT INTO categoria (nome)
VALUES ('Comidas');

INSERT INTO produto (nome, descricao, quantidade, preco_unitario, id_categoria, status)
VALUES ('Cerveja', 'Schin', 50, 10.50, 1, 'ATIVO');

INSERT INTO produto (nome, descricao, quantidade, preco_unitario, id_categoria, status)
VALUES ('Arroz', 'Branco', 50, 8.50, 2, 'ATIVO');

INSERT INTO usuario (nome, email, senha, perfil)
VALUES ('Yarlei', 'email@email.com', '$2a$10$JRsrDplQ8Cs1PxKFuI.qG.2x1T3nm17Y0mhf3roWtKLNeuoxEJD1S', 'GERENTE');

INSERT INTO usuario (nome, email, senha, perfil)
VALUES ('Isabella', 'gmail@email.com', '$2a$10$DQWsasFk8RKB6xmzZzraO.MxuWnx7rSsI5Tylpn7D9Fg7bx/cZLq2', 'VENDEDOR');

INSERT INTO movimentacao_estoque (tipo, quantidade, produto_id, usuario_id)
VALUES ('ENTRADA', 20, 1, 1);

INSERT INTO movimentacao_estoque (tipo, quantidade, produto_id, usuario_id)
VALUES ('ENTRADA', 20, 2, 2);
